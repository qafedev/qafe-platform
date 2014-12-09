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
package com.qualogy.qafe.business.resource.rdb.query.enhancer;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.commons.db.procedure.CallArgument;

public class CallEnhancer implements Enhancer{
	/**
	 * 
	 * @param dataSource
	 * @param callQueries
	 * @return
	 * @throws EnhancementFailedException 
	 * @throws SQLException
	 */
	public Query enhance(Query query, DatabaseMetaData md) throws EnhancementFailedException{
		Call call = (Call)query;
		//nasty hack by refrence to set this boolean
		//dsWrapper.setNamedParametersSupported(md.supportsNamedParameters());
		
		// If possible, split schema, package and procedure/function
		String callName = ((Call)query).getCallName();
		String schemaName = null;
		String packageName = null;
		String procedureName = null;
		String[] itemsOfCall = callName.split("\\.");
		int numItems = itemsOfCall.length;
		switch (numItems) {
            case 1: {
                procedureName = itemsOfCall[0]; 
            } break;
            case 2: {
                packageName = itemsOfCall[0];
                procedureName = itemsOfCall[1];
            } break;
            case 3: {
                schemaName = itemsOfCall[0];
                packageName = itemsOfCall[1];
                procedureName = itemsOfCall[2];
            } break;
        }
		
		try {
			List<CallArgument> args = new ArrayList<CallArgument>();
			ResultSet rs = null;
			try{
				
				// TESTING PURPOSE
				// Show information of all procedures and functions
//				ResultSet rs2 = md.getProcedures(null, md.getUserName(), "%");
//		        while (rs2.next()) {
//		        	String catalog = rs2.getString(1);
//		            String procSchema = rs2.getString(2);
//		            String procName = rs2.getString(3);
//		            String remarks = rs2.getString(7);
//		            String procType = rs2.getString(8);
//		            System.out.println(catalog + " : " + procSchema +"."+ procName + " - " + procType);
//		            System.out.println("Remarks: " + remarks);
//		        }
//		        rs2.close();

			    // If schema is specified explicitly, no need to do the filtering on schema when getting meta-data
			    String userName = md.getUserName();
			    if (schemaName != null) {
			        userName = null;
			    }
			    int numDeclarations = 0;
			    rs = md.getProcedures(packageName, userName, procedureName);
				while(rs.next()){
				    numDeclarations++;
				}
				
				// If the procedure cannot be found 
				// this will result in an error marked on the query
				call.setContainsErrors(numDeclarations==0);
					
				if(!call.containsErrors()){
					rs = md.getProcedureColumns(packageName, userName, procedureName, "%");
					if (schemaName == null) {
					    schemaName = md.getUserName();    
					}
					int columnIndex = 1;
					while(rs.next()){
					    String callSchemaName = rs.getString(2);
					    if (!callSchemaName.toUpperCase().equals(schemaName.toUpperCase())) {
					        continue;
					    }
						
						// TESTING PURPOSE
						// Show all columns of a procedure or function
//						String catalog = rs.getString(1);
//			            String procSchema = rs.getString(2);
//			            String procName = rs.getString(3);
//			            String colName = rs.getString(4);
//			            String colType = rs.getString(5);
//			            System.out.println(catalog + " : " + procSchema +"."+ procName + " - " + colName+":"+colType);

						// Noticed that the sequence is not always correct, 
						// when creating a CallArgument by calling the method CallArgument.create;
						// whereas the DatabaseMetaData.getProcedureColumns will return columns in call order
						CallArgument wrapper = CallArgument.create(rs, columnIndex);
						args.add(wrapper);
						columnIndex++;
					}
					((Call)query).setArgumentInformation(args);
				}
			}finally{
				if(rs!=null)
					rs.close();
			}
		} catch (SQLException e) {
			throw new EnhancementFailedException(e);
		}
		return query;
	}
}