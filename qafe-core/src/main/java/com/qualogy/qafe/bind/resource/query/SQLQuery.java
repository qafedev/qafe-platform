/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.resource.query;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author 
 *
 */
//TODO: order by
public abstract class SQLQuery extends Query implements Serializable{
	
	protected String sqlAsText;
	protected String sqlAsAttribute;
	protected String table;

	private MetaData metaData = new MetaData();
	
	public SQLQuery(String sqlAsText, String sqlAsAttribute, String table) {
		super();
		this.sqlAsText = sqlAsText;
		this.sqlAsAttribute = sqlAsAttribute;
		this.table = table;
	}

	public void setSqlAsAttribute(String sqlAsAttribute) {
		this.sqlAsAttribute = sqlAsAttribute;
	}

	public void setSqlAsText(String sqlAsText) {
		this.sqlAsText= sqlAsText;
	}

	public SQLQuery() {
		super();
	}
	
	public abstract String getKeyword();
	
	public String getSqlAsAttribute() {
		return sqlAsAttribute;
	}
	public String getSqlAsText() {
		return sqlAsText;
	}
	public String getTable() {
		return table;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
	public void validate(){
		containsErrors = (!StringUtils.isBlank(table) && metaData.getPrimaryKeys().size()==0);
	}

	public void setTable(String table) {
		this.table = table;
	}
}
