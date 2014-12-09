/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.business.integration.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.core.conflictdetection.ConflicDetectionConstants;
import com.qualogy.qafe.core.conflictdetection.UpdateConflictException;

public class ConflictDetectionUtil {

	public static Long removeChecksum(Map<String, AdaptedToService> paramsIn) {
		AdaptedToService checksumContainer = paramsIn.remove(ConflicDetectionConstants.QAFE_CHECKSUM);
		if(checksumContainer == null || checksumContainer.getValue().toString().isEmpty()) {
			return null;
		}
		return Long.valueOf(checksumContainer.getValue().toString());
	}

	public static void addChecksums(List<Map<String, Object>> recordList, String sql) {
		if (recordList == null) {
			return;
		}
		if (hasColumnAliases(sql)) {
			return;
		}
		for (Map<String, Object> record : recordList) {
			record.put(ConflicDetectionConstants.QAFE_CHECKSUM, calculateChecksum(record));
		}
	}
	
	public static void validateChecksum(SimpleJdbcTemplate template, String updateQuery,
			Map<String, Object> localRecord, Long oldChecksum) {
		
		if (oldChecksum != null) {
			Map<String, Object> localRecordContent = getContent(localRecord);
			String sqlSelectByPk = createSelectByPkQuery(localRecordContent.keySet(), updateQuery);
			Map<String, Object> remoteRecord = template.queryForMap(sqlSelectByPk, localRecord);
			if (hasChanged(remoteRecord, oldChecksum)) {
				throw new UpdateConflictException(localRecordContent);
			}
		}
	}
	
	private static boolean hasColumnAliases(String sql) {
		String normalizedSql = sql.trim().toUpperCase().replaceAll("\\s+AS\\s+", " ");
		return normalizedSql.matches("^SELECT\\s.*?\\w \\w.*?\\sFROM\\s.+");
	}

	private static Map<String, Object> getContent(Map<String, Object> localRecord) {
		Map<String, Object> shallowClone = new HashMap<String, Object>(localRecord);
		removeMetaData(shallowClone);
		return shallowClone;
	}

	private static boolean hasChanged(Map<String, Object> newContent, long oldChecksum) {
		return calculateChecksum(newContent) != oldChecksum;
	}

	private static long calculateChecksum(Map<String, Object> record) {
		long checksum = 0;
		for (Map.Entry<String, Object> field : record.entrySet()) {
			if (!isMetaData(field.getKey())) {
				checksum += calculateChecksum(field.getKey(), field.getValue());	
			}
		}
		return checksum;
	}
	
	/**
	 * IMPORTANT: this method must be an exact copy of the one used in the qafe-web-gwt project
	 */
	private static long calculateChecksum(String key, Object value) {
		long checksum = key.hashCode();
		if (value instanceof Number) {
			checksum += value.toString().hashCode();
		} else if (value != null) {
			checksum += value.hashCode();
		}
		return checksum;
	}

	private static String createSelectByPkQuery(Set<String> columnNames, String updateQuery) {
		String updateSql = updateQuery.trim().toUpperCase();
		updateSql = updateSql.replaceAll("\\r|\\n", " ");
		String find = "^UPDATE\\s+(\\w+).*?(\\sWHERE\\s.*)";
		String fromAndWhereClauseTemplate = " FROM $1$2";
		String fromAndWhereClause = updateSql.replaceFirst(find, fromAndWhereClauseTemplate);
		StringBuilder sql = new StringBuilder("SELECT ");
		for (String columnName : columnNames) {
			sql.append(columnName).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(fromAndWhereClause);
		return sql.toString();
	}

	private static boolean isMetaData(String key) {
		if (key == null) {
			return false;
		}
		return ConflicDetectionConstants.CONSTANTS.contains(key.toUpperCase());
	}
	
	private static void removeMetaData(Map<String,Object> record) {
		if (record == null) {
			return;
		}
		List<String> keys = new ArrayList<String>();
		Iterator<String> itrKey = record.keySet().iterator();
		while (itrKey.hasNext()) {
			String key = itrKey.next();
			if (isMetaData(key)) {
				keys.add(key);
			}
		}
		for (String key : keys) {
			record.remove(key);
		}
	}
}