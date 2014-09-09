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
package com.qualogy.qafe.business.resource.rdb;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.DatasourceBindResource;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;
import com.qualogy.qafe.business.transaction.SupportsLocalTransactions;

public abstract class RDBDatasource extends Resource implements SupportsLocalTransactions {

    private static final Logger LOG = Logger.getLogger(RDBDatasource.class.getName());

    private Dialect dialect;

    protected DataSource dataSource;

    /**
     * Holder for {@link Query}. The {@link Query} are read and bound from a file and contained in an
     * {@link QueryContainer}.
     */
    private QueryContainer queryContainer;

    public QueryContainer getQueryContainer() {
        return queryContainer;
    }

    public void setQueryContainer(QueryContainer queryContainer) {
        this.queryContainer = queryContainer;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DatasourceBindResource getResource() {
        return (DatasourceBindResource) super.get();
    }

    public RDBDatasource(BindResource resource) {
        super(resource);
    }

    public boolean isEqualTo(Resource otherResource) {
        return false;
    }

    public void init() {
        setDialect(((DatasourceBindResource) getBindResource()).getDialect());
    }

    public String toLogString() {
        return ToStringBuilder.reflectionToString(dataSource);
    }

    public Query get(String queryName) {
        return (Query) queryContainer.get(queryName);
    }

    public void put(Query query) {
        queryContainer.put(query);
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = Dialect.create(dialect);
    }

    /**
     * @deprecated move to dialect
     * @param namedParametersSupported
     */
    public void setNamedParametersSupported(boolean namedParametersSupported) {
        if (dialect != null)
            this.dialect.setNamedParametersSupported(namedParametersSupported);
    }

    public void destroy(ApplicationContext context) {
        if (dataSource != null) {
            try {
                DataSourceUtils.releaseConnection(dataSource.getConnection(), dataSource);
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Problem releasing db connection", e);
            }
            // if (!dataSource.getConnection().isClosed()){
            // // You call close() on the Connection object when you're done. Note that this doesn't really
            // close the connection - it just returns it to the pool.
            // dataSource.getConnection().close();
            // }
        }
    }
}
