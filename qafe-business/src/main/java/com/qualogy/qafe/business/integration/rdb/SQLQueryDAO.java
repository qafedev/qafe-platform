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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Batch;
import com.qualogy.qafe.bind.resource.query.Insert;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.SQLOnly;
import com.qualogy.qafe.bind.resource.query.SQLQuery;
import com.qualogy.qafe.bind.resource.query.Select;
import com.qualogy.qafe.bind.resource.query.Update;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.integration.filter.page.Page;
import com.qualogy.qafe.business.integration.filter.page.ResultSetDataExtractor;
import com.qualogy.qafe.business.integration.filter.sort.Sort;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.resource.rdb.query.QueryToStringCreator;
import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.util.ExceptionHelper;

/**
 * Generic Data access class
 *
 * @author mvanderwurff
 */
public class SQLQueryDAO extends DAO {

	public final static Logger logger = Logger.getLogger(SQLQueryDAO.class.getName());
	private Dialect dialect;
	private Map<String, Set<String>> tableColumnSet = new HashMap<String, Set<String>>();
	private String[] operators = { "=>", "!=", "<=", "<>", ">", "<", "%", "=" };
	private boolean isCountOnly;

	/**
	 * general method
	 *
	 * @param ds
	 * @param query
	 * @param paramsIn
	 * @param paramsOut
	 * @param filters
	 * @return
	 */
	public Object execute(RDBDatasource ds, Query query, Method method, Map<String, AdaptedToService> paramsIn, Set paramsOut, Filters filters, DataIdentifier dataId) {
		Object result = null;
		if (query instanceof Batch) {
			List<Object> tmp = new ArrayList<Object>();
			for (Iterator<Query> iter = ((Batch) query).getQueries().iterator(); iter.hasNext();) {
				tmp.add(execute(ds, (SQLQuery) iter.next(), paramsIn, paramsOut, filters, dataId));
			}
			if (tmp.size() > 0) {
				result = tmp;
			}
		} else {
			result = execute(ds, (SQLQuery) query, paramsIn, paramsOut, filters, dataId);
		}
		return result;
	}

	/**
	 * @param ds
	 * @param tableName
	 * @throws SQLException
	 */
	private void populateTableColumnSet(DataSource ds, String tableName) throws SQLException {
		Connection conn = ds.getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rsc = dbmd.getColumns(conn.getCatalog(), null, tableName, "%");
		Set<String> foundColumnSet = new HashSet<String>();
		while (rsc.next()) {
			String columnName = rsc.getString("COLUMN_NAME");
			foundColumnSet.add(columnName);
		}
		tableColumnSet.put(tableName, foundColumnSet);
		DataSourceUtils.releaseConnection(conn, ds);
	}

	/**
	 * check if given input param has a equal column name in the given table
	 *
	 * @param tableColumns
	 * @param columnName
	 * @return
	 */
	private boolean inputParamIsAColumnInGiveTable(Set<String> tableColumns, String columnName) {
		String colName = "";
		Iterator<String> iterator = tableColumns.iterator();
		while (iterator.hasNext()) {
			colName = iterator.next();
			if (StringUtils.equalsIgnoreCase(colName, columnName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param statement
	 * @param sqlQuery
	 * @return
	 */
	private String completeSqlStatement(String statement, SQLQuery sqlQuery) {
		String finalStatement = "";
		if (sqlQuery instanceof Select) {
			if (statement.startsWith("from")) {
				finalStatement = "select * " + statement;
			}
		} else if (sqlQuery instanceof Insert) {
			if (statement.trim().startsWith("into")) {
				finalStatement = "insert " + statement;
			} else {
				finalStatement = statement;
			}
		} else if (sqlQuery instanceof Update) {
			if (!statement.toLowerCase().startsWith("update")) {
				finalStatement = "update " + statement;
			} else {
				finalStatement = statement;
			}
		} else if (sqlQuery instanceof Select) {
			if (!statement.toLowerCase().startsWith("select")) {
				finalStatement = "select *" + statement;
			}
		}
		finalStatement = finalStatement.replace(":", "");
		return finalStatement;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private Object execute(RDBDatasource ds, SQLQuery stmt, Map<String, AdaptedToService> paramsIn, Set outputMapping, Filters filters, DataIdentifier dataId) {
		Long oldChecksum = null;
		if (isConcurrentModificationEnabled()) {
			oldChecksum = ConflictDetectionUtil.removeChecksum(paramsIn);
		}

		String[] inputKeys = null;
		Map<String, Object> paramIns = new HashMap<String, Object>();
		if (paramsIn != null) {
			paramIns = narrow(paramsIn);
			inputKeys = (String[]) paramIns.keySet().toArray(new String[paramIns.size()]);
		}
		MapSqlParameterSource namedParameters = new MapSqlParameterSource(paramIns);
		Object result = null;
		isCountOnly = DataStore.findValue(dataId, DataStore.KEY_WORD_COUNT) != null;
		String sql = QueryToStringCreator.toString(stmt, namedParameters, inputKeys, outputMapping);
		Map values = namedParameters.getValues();
		if ((values != null) && (values.size() > 0)) {
			Map replacementMap = new HashMap<String, Object>();
			for (String key : inputKeys) {
				if (values.containsKey(key.toUpperCase())) {
					replacementMap.put(key, values.get(key.toUpperCase()));
				}
				if (values.containsKey(key.toLowerCase())) {
					replacementMap.put(key, values.get(key.toLowerCase()));
				}
			}
			namedParameters.addValues(replacementMap);
		}
		logger.info("Executing SQL: " + sql);
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(ds.getDataSource());
		try {
			Connection conn = ds.getDataSource().getConnection();
			dialect = getDatabaseDialect(conn);
			DataSourceUtils.releaseConnection(conn, ds.getDataSource());
		} catch (SQLException e) {
			ExceptionHelper.printStackTrace(e);
		}
		if (stmt instanceof Select) {
			result = handleSelect(sql, namedParameters, (Select) stmt, template, filters);
			if (!isCountOnly && isConcurrentModificationEnabled()) {
				ConflictDetectionUtil.addChecksums((List<Map<String, Object>>) result, sql);
			}
		} else if (stmt instanceof Insert) {
			result = handleInsert(sql, namedParameters, (Insert) stmt, template);
			if (isConcurrentModificationEnabled()) {
				DataStore.store(dataId, DataStore.KEY_SERVICE_MODIFY);
			}
		} else if (stmt instanceof SQLOnly) {
			result = handleQueryTag(sql, namedParameters, template);
		} else {
			if (isConcurrentModificationEnabled()) {
				ConflictDetectionUtil.validateChecksum(template, sql, namedParameters.getValues(), oldChecksum);
			}
			template.update(sql, namedParameters);
			if (isConcurrentModificationEnabled()) {
				DataStore.store(dataId, DataStore.KEY_SERVICE_MODIFY);
			}
		}
		return result;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	/**
	 * Return database dialect.
	 *
	 * @param conn
	 * @return
	 */
	private Dialect getDatabaseDialect(Connection conn) {
		Dialect dialect = null;
		try {
			String database = conn.getMetaData().getDatabaseProductName();
			if ((database != null) && database.equalsIgnoreCase(Dialect.ORACLE_DIALECT_KEY)) {
				dialect = Dialect.ORACLE_DIALECT;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dialect;
	}

	/**
	 * @param sql
	 * @param namedParameters
	 * @param stmt
	 * @param template
	 * @param filters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object handleSelect(String sql, MapSqlParameterSource namedParameters, Select stmt, SimpleJdbcTemplate template, Filters filters) {
		Object result = null;
		if(isCountOnly) { // Means $COUNT is set by QAML developer
			sql = "select count(*) " + sql.substring(sql.indexOf("from"));
			result = template.queryForList(sql, namedParameters);
		} else {
			if ((filters != null) && (filters.getSort() != null)) {
				Sort sort = filters.getSort();
				String column = sort.getColumn();
				String sortOrder = sort.getSortOrder();
				if ((column != null) && (sortOrder.equalsIgnoreCase(Sort.ASCENDING) || sortOrder .equalsIgnoreCase(Sort.DESCENDING))) {
					sql = "select * from (" + sql + ") dummy order by " + column + " " + sortOrder;
				}
			}
			if ((filters != null) && (filters.getPage() != null)) {
				Page page = filters.getPage();
				String startRow;
				String endRow;
				int rowCount = page.getMaxRows();
				int offSet = page.getOffset();
				if ((dialect != null) && dialect.equals(Dialect.ORACLE_DIALECT)) {
					String modifiedSql = sql;
					ArrayList arr = null;
					if ((rowCount > 0) && (offSet >= 0)) {
						if (offSet == Integer.MAX_VALUE) {
							String sqlRecordCount = "select count(*) from " + "(" + sql + ")";
							long recordCount = template.queryForLong(sqlRecordCount, namedParameters);
							offSet = Math.round(((float) recordCount / (float) rowCount) - 0.6f);
							page.setOffset(offSet);
						}
						startRow = Integer.toString(offSet * rowCount);
						endRow = Integer.toString((offSet * rowCount) + rowCount + 1);
						modifiedSql = "select * from (select a.*, rownum rnum from (" + sql + ") a where rownum < " + endRow + ") where rnum > " + startRow;
						arr = (ArrayList) template.queryForList(modifiedSql, namedParameters);
					}
					page.setPageItems(arr);
				} else {
					page = (Page) template.getNamedParameterJdbcOperations().query(sql, namedParameters, new ResultSetDataExtractor(page, new MetaDataRowMapper()));
				}
				if (page.countPages()) { // determine how many rows are available
					result = new HashMap<String, Object>();
					((Map<String, Object>) result).put( DataStore.KEY_WORD_PAGESAVAILABLE, determinePageCount(template, namedParameters, page.getMaxRows()));
					((Map<String, Object>) result).put("", page.getPageItems()); // set result to root
				} else {
					result = page.getPageItems();
				}
			} else {
				result = template.queryForList(sql, namedParameters);
			}
		}

		return result;
	}

	private Object handleQueryTag(String sql, MapSqlParameterSource namedParameters, SimpleJdbcTemplate template) {
		Object result = null;
		if (sql.trim().toLowerCase().startsWith("select")) {
			result = template.queryForList(sql, namedParameters);
		} else {
			result = template.update(sql, namedParameters);
		}
		return result;
	}

	/**
	 * @param sql
	 * @param namedParameters
	 * @param stmt
	 * @param template
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object handleInsert(String sql, MapSqlParameterSource namedParameters, Insert stmt, SimpleJdbcTemplate template) {
		Object result = null;
		int rowsUpdate = template.update(sql, namedParameters);
		result = String.valueOf(rowsUpdate);
		return result;
	}

	/**
	 * @param jt
	 * @param arguments
	 * @param maxRows
	 * @return
	 */
	private Integer determinePageCount(SimpleJdbcTemplate jt, MapSqlParameterSource arguments, int maxRows) {
		final int ttlRowCount = jt.queryForInt(createCountStatement(), arguments);
		// calculate the number of pages
		int pageCount = ttlRowCount / maxRows;
		if (ttlRowCount > (maxRows * pageCount)) {
			pageCount++; // part of a page
		}
		return new Integer(pageCount);
	}

	/**
	 * strip from clause
	 *
	 * @return
	 */
	private String createCountStatement() {
		return null;
	}

	private Boolean isConcurrentModificationEnabled() {
		return ApplicationCluster.getInstance().isConcurrentModificationEnabled();
	}
}
