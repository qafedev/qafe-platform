/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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


public class Update extends SQLQuery{
	
	private static final long serialVersionUID = -5785941410843162015L;
	
	public final static String SQL_UPDATE_KEYWORD = "update";
	
	// Additional parameters to add in runtime to the WHERE clause
	protected String where;
	
	public Update(){
		super();
	}
	
	public Update(String sqlAsText, String sqlAsAttribute, String table) {
		super(sqlAsText,sqlAsAttribute,table);
		
	}
	/**
	 * @return the where
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	public String getKeyword() {
		return SQL_UPDATE_KEYWORD;
	}

	

}
