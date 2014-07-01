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

import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;

import org.springframework.dao.DataAccessException;

import java.util.Map;
import java.util.Set;


/**
 * Delegate for calling a DAO. This delegate determines, based on a switch (instanceof Call
 * parameter class) which dao to delegate the call to.
 *
 * @author mvanderwurff
 */
public class DAODelegate {
    /**
     * This delegate method determines, based on a switch (instanceof Query
     * parameter class) which dao to delegate the call to.
     *
     * @param ds
     * @param query
     * @param paramsIn
     * @param paramsOut
     * @return object output of the executed query
     * @throws ExternalException
     */
	public static Object delegate(RDBDatasource ds, Query query, Method method, Map<String, AdaptedToService> paramsIn, Set paramsOut, Filters filters,
        DataIdentifier dataId) throws ExternalException {
        if (ds == null) {
            throw new IllegalArgumentException("ds cannot be null");
        }

        DAO dao = null;

        if (query instanceof Call) {
            dao = new CallDAO();
        } else {
            dao = new SQLQueryDAO();
        }

        Object result = null;

        try {
            result = dao.execute(ds, query, method, paramsIn, paramsOut, filters, dataId);
        } catch (DataAccessException e) {
            throw new ExternalException("method [" + method.getId() + "] -> query/call [" + query.getId() + "]", e);
        }

        return result;
    }
}
