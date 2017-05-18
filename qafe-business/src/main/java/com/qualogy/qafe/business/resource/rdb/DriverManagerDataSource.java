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
package com.qualogy.qafe.business.resource.rdb;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.DriverManagerResource;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.business.resource.Resource;

public class DriverManagerDataSource extends RDBDatasource {

    public DriverManagerDataSource(BindResource resource) {
        super(resource);
    }

    public boolean isEqualTo(Resource otherResource) {
        return false;
    }

    public void init(ApplicationContext context) {
        DriverManagerResource driverManagerResource = (DriverManagerResource) getBindResource();
        String userName = driverManagerResource.getUsername();
        String password = driverManagerResource.getPassword();
        String url = driverManagerResource.getUrl();
        String driverClassName = driverManagerResource.getDriverClassName();

        DataSource springDS =
            new org.springframework.jdbc.datasource.DriverManagerDataSource(url, userName, password);
        ((org.springframework.jdbc.datasource.DriverManagerDataSource) springDS)
            .setDriverClassName(driverClassName);

        GenericObjectPool pool = new GenericObjectPool();
        pool.setMinEvictableIdleTimeMillis(300000);
        pool.setTimeBetweenEvictionRunsMillis(60000);
        PoolableConnectionFactory connectionFactory =
            new PoolableConnectionFactory(new DataSourceConnectionFactory(springDS), pool, null, null, false,
                    true);

        PoolingDataSource poolingDataSource = new PoolingDataSource(pool);
        poolingDataSource.setAccessToUnderlyingConnectionAllowed(true);
        setDataSource(poolingDataSource);

        postInit(context);
    }

    public void validate() throws ValidationException {
        QueryContainer queryContainer = getQueryContainer();
        if (queryContainer == null) {
            return;
        }
        List<String> duplicateQueries = queryContainer.getDuplicateQueries();
        if (duplicateQueries == null) {
            return;
        }
        String resourceId = getName();
        String message =
            "Query duplication detected for resource " + resourceId + " " + duplicateQueries
                    + ", id of an query should be unqiue within the resource.";
        throw new ValidationException(message);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[userName:\t" + ((DriverManagerResource) getBindResource()).getUsername() + "\n");
        buffer.append("password:\t" + ((DriverManagerResource) getBindResource()).getPassword() + "\n");
        buffer.append("url:\t" + ((DriverManagerResource) getBindResource()).getUrl() + "\n");
        buffer.append("driverClassName:\t" + ((DriverManagerResource) getBindResource()).getDriverClassName()
                + "\n");
        buffer.append("id:\t" + ((DriverManagerResource) getBindResource()).getId() + "]" + "\n");
        return buffer.toString();
    }

}
