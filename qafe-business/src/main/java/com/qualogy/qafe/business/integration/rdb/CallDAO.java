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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.resource.rdb.query.call.CallFactory;
import com.qualogy.qafe.business.resource.rdb.query.call.DBCall;
import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;
import com.qualogy.qafe.core.datastore.DataIdentifier;


/**
 * Generic Data access class
 *
 * @author mvanderwurff
 */
public class CallDAO extends DAO {
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
        Map<String, Object> narrowedIn = new HashMap<String, Object>();

        if (paramsIn != null) {
            narrowedIn = narrow(paramsIn);
        }

        Object result = executeCall(new JdbcTemplate(ds.getDataSource()), method, ds.getDialect(), (Call) query, narrowedIn, paramsOut, filters);

        return result;
    }

    private static Object executeCall(final JdbcTemplate jt, final Method method, final Dialect dialect, final Call call, final Map<String, Object> paramsIn, final Set paramsOut, final Filters filters) {
        final DBCall dbCall = CallFactory.createCall(dialect);

        Object o = jt.execute(new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                        CallableStatement stmt = dbCall.prepareCall(conn, call, method, paramsIn);
                        return stmt;
                    }
                },
                new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement stmt) throws SQLException, DataAccessException {
                    	stmt.execute();
                        return dbCall.readResults(stmt, call, method, paramsOut, filters);
                    }
                });

        return o;
    }
}
