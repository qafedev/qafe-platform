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
package com.qualogy.qafe.business.resource.rdb.query.call;

import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;


/**
 * Factory class which generates a call class to deal with database
 * specific CallableStatements.
 */
public class CallFactory{
    
	/**
     * Create an implementation of the DBCall which is specific to the
     * database being used.
     * @param dialect
     * @return call instance or null if it can't be created
     */
    public static DBCall createCall(Dialect dialect){
    	DBCall dbCall = null;
    	//if(Dialect.ORACLE_DIALECT == dialect)
        	dbCall = new OracleBaseCall(true);
//        else if(Dialect.SYBASE_DIALECT == dialect)
//        	dbCall = new SybaseBaseCall();
//        else
//        	throw new UnsupportedFeatureException("dialect ["+dialect+"] not supported");
        return dbCall;
    }
}
