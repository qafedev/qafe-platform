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
package com.qualogy.qafe.business.resource.rdb.statement.dialect;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Dialect {
	
	public final static String ORACLE_DIALECT_KEY = "oracle";
	public final static String SYBASE_DIALECT_KEY = "sybase";
	public final static String SQLSERVER_DIALECT_KEY = "sqlserver";
	public final static String MYSQL_DIALECT_KEY = "mysql";
	public final static String HSQL_DIALECT_KEY = "hsql";
	public final static String DERBY_DIALECT_KEY = "derby";
	
	public final static Dialect NO_DIALECT = new Dialect(-1);
	public final static Dialect ORACLE_DIALECT = new Dialect(1);
	public final static Dialect SYBASE_DIALECT = new Dialect(2);
	public final static Dialect SQLSERVER_DIALECT = new Dialect(3);
	public final static Dialect MYSQL_DIALECT = new Dialect(4);
	public final static Dialect HSQL_DIALECT = new Dialect(5);
	public final static Dialect DERBY_DIALECT = new Dialect(6);
	 
	private static Map<String, Dialect> SUPPORTED_DIALECTS = new HashMap<String, Dialect>();
	static{
		SUPPORTED_DIALECTS.put(ORACLE_DIALECT_KEY, ORACLE_DIALECT);
	}
	
	private int dialect;
	private boolean namedParametersSupported;
	
	public boolean supportsNamedParameters(){
		return namedParametersSupported;
	}
	
	public void setNamedParametersSupported(boolean namedParametersSupported) {
		this.namedParametersSupported = namedParametersSupported;
	}
	
	private Dialect(int dialectStr){
		dialect = dialectStr;
	}
	
	/**
	 * Factory method to create a Dialect object. If
	 * the String is blank according StringUtils.isBlank method, dialect
	 * NO_DIALECT will be set.
	 * @param dialectStr
	 * @return
	 */
	public static Dialect create(String dialectStr){
		Dialect result = null;
		if(StringUtils.isBlank(dialectStr))
			result = Dialect.NO_DIALECT;
		else	
			result = (Dialect)SUPPORTED_DIALECTS.get(dialectStr);
		
		return result; 
	}
	
	public int intValue(){
		return dialect;
	}
	
}
