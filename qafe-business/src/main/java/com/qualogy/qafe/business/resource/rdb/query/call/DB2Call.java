/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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


/**
 * DB2 Call implementation
 *
 */
public class DB2Call{}
//public class DB2Call extends DBCall
//{
//    /** Prepare a stored procedure call
//     * @param conn the connection to the database
//     * @param procedure the stored procedure name including whatever package it's in
//     * @param inputParams the number of input parameters
//     * @param outputTypes the output types
//     * @exception SQLException if it goes wrong
//     */
//    public CallableStatement prepareCall(Connection conn, String procedure, int inputParams, int[] outputTypes) throws SQLException{
//    	int i;
//        
//        StringBuffer sql = new StringBuffer("call ");
//        sql.append(procedure);
//        sql.append(" (");
//        
//        for (i=0; i<inputParams; i++) {
//            sql.append("?,");
//        }
//        
//        for (i=0; i<outputTypes.length; i++) {
//            sql.append("?,");
//        }
//        
//        if (inputParams>0 || outputTypes.length>0)
//            sql.setLength(sql.length()-1); // remove the last comma
//        
//        sql.append(")");
//        
//        call = conn.prepareCall(sql.toString());
//        
//        // register the output parameters
//        for (i=0; i<outputTypes.length; i++) {
//            call.registerOutParameter(inputParams+i+1, outputTypes[i]);
//        }
//        return call;
//    }
//}
