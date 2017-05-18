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

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.commons.db.procedure.CallArguments;

/**
 * class intended for holding callablestatement properties
 * @author 
 *
 */
public class Call extends Query{
	
	public final static String CALLABLESTATEMENT_KEYWORD = "call";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1993445288733951190L;
	
	private CallArguments arguments;
	private String callName;
	private String sqlAsText;
    private String sqlAsAttribute;

    public String getSqlAsAttribute() {
        return sqlAsAttribute;
    }

    public void setSqlAsAttribute(String sqlAsAttribute) {
        this.sqlAsAttribute = sqlAsAttribute;
    }

	public Call() {
		super();
	}

	public static Call createWithCallName(String callName){
		Call call = new Call();
		call.callName = callName;
		return call;
	}
	
	public static Call createWithSQLStr(String sql){
		Call call = new Call();
		call.sqlAsText = sql;
		return call;
	}
	
	/**
	 * retrieve the call name either from the bound value or the 
	 * fullblown sql string to perform the call (also set through binding)
	 * @return
	 */
	public String getCallName() {
		String result = null;
		if(callName!=null){
			result = callName;
		}else{
			result = parseProcedureNameFromSQL(getSql());
		}
		
		if(result == null)
			throw new NullPointerException("cannot determine call name");
		
		return result.toUpperCase();
	}
	public void setArgumentInformation(List argumentInformation) {
		this.arguments = new CallArguments(argumentInformation); 
	}

	public CallArguments getArguments() {
		return arguments; 
	}
	
	/**
	 * based upon the existence of return or result parameters (when true
	 * it's a function)
	 */
	public boolean isFunction(){
		return arguments.containsResultArgument();
	}

	public String getSql() {
		String sql = null;
		if(StringUtils.isNotBlank(sqlAsAttribute))
			sql = sqlAsAttribute;
		else if(StringUtils.isNotBlank(sqlAsText))
			sql = sqlAsText;
		return sql;
	}
	
	/**
	 * a call is prepared when a full blown sql statement is supplied
	 * @return
	 */
	public boolean isPrepared(){
		return StringUtils.isNotBlank(getSql());
	}
	
	/**
	 * parse the calls name from a fullblown statement
	 * 
	 * 	{ call ? = cursors_pkg.getdirect( from_city ?, to_city ?) }
	 * 	{ call cursors_pkg.getdirect( from_city ?, to_city ?) }
	 * 
	 * @param call
	 * @return
	 */
	private String parseProcedureNameFromSQL(String call){
		int start = 0;
		
		if (call.indexOf(CALLABLESTATEMENT_KEYWORD)!= -1){
			start =call.indexOf(CALLABLESTATEMENT_KEYWORD) + CALLABLESTATEMENT_KEYWORD.length();
		}
		
		call = call.substring(start);
		
		if(call.indexOf("=")>0)
			call = call.substring(call.indexOf("=")+1);
		
		call = StringUtils.trim(call);
		
		if(call.indexOf(" ")>-1)
			call = call.substring(0, call.indexOf(" "));
		if(call.indexOf("(")>-1)
			call = call.substring(0, call.indexOf("("));
		if(call.indexOf(";")>-1)
			call = call.substring(0, call.indexOf(";"));
		
		return call;
	}

	public void validate() {
	}
}
