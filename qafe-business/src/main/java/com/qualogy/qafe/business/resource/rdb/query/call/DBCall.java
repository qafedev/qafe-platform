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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.business.integration.filter.Filters;

/**
 * An abstraction of the CallableStatment class which allows concrete implementations
 * to do database specific operations. In particular, these are setting up stored
 * procedure calls and obtaining result sets.
 */
public abstract class DBCall{
    protected CallableStatement call = null;

    public final static String CALLABLESTATEMENT_KEYWORD = "call";
    
    /**
     * Prepare a stored procedure call
     * @param conn the connection to the database
     * @param method 
     * @param procedure the stored procedure name including whatever package it's in
     * @param inputParams the number of input parameters
     * @param outputTypes the output types
     * @exception SQLException if it goes wrong
     */
    public abstract CallableStatement prepareCall(Connection conn, Call call, Method method, Map inputParameters) throws SQLException;
    
    public abstract Map readResults(CallableStatement stmt, Call call, Method method, Set outputMapping, Filters filters) throws SQLException;
}
