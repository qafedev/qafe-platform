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
package com.qualogy.qafe.business.integration.rdb;

import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;


/**
 * DAO abstract class to have a uniform way of accessing data from a database.
 *
 * @author mvanderwurff
 */
public abstract class DAO {
    /**
     * uniform method to execute a query with the given dialect against the given
     * datasource with given ins and outs
     *
     * @param ds
     * @param query
     * @param method 
     * @param paramsIn
     * @param paramsOut
     * @param filters
     * @return
     * @throws ExternalException
     */
    public abstract Object execute(RDBDatasource ds, Query query,
        Method method, Map<String, AdaptedToService> paramsIn, Set paramsOut, Filters filters,
        DataIdentifier dataId) throws ExternalException;

    /**
     * this method is needed to narrow the values in the given map
     * TODO:refactor
     *
     * @param inParams
     * @return
     */
    protected Map<String, Object> narrow(Map<String, AdaptedToService> inParams) {
        Map<String, Object> pms = new HashMap<String, Object>();

        if (inParams != null) {
            Set<String> keys = inParams.keySet();

            for (String paramName : keys) {
                if (paramName == null) {
                    throw new IllegalArgumentException(
                        "Name is mandatory for 'in' parameters on services");
                }

                //pms.put(paramName.toLowerCase(), (inParams.get(paramName)!=null)?inParams.get(paramName).getValue():null);
                pms.put(paramName,
                    (inParams.get(paramName) != null)
                    ? inParams.get(paramName).getValue() : null);
            }
        }

        return pms;
    }

    /**
     * method tries to determine if the query is named or numbered;
     * named 'id=:id, name=:name'
     * numbered '?,?'
     *
     * @param keys
     * @param stmt
     * @return
     * @deprecated, move to query, make query a business domain class
     */
    protected boolean isNamedQuery(String[] keys, String stmt) {
        boolean isNamed = false;

        for (int i = 0; (i < keys.length) && !isNamed; i++) {
            if (!NumberUtils.isNumber(keys[i])) {
                isNamed = true;
            } else { //if number

                if (stmt.contains(":" + keys[i])) {
                    isNamed = true;
                }
            }
        }

        return isNamed;
    }
}
