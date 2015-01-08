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
package com.qualogy.qafe.business.resource.rdb;

import java.io.File;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.DatasourceBindResource;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.rdb.query.enhancer.EnhancementManager;
import com.qualogy.qafe.business.resource.rdb.statement.dialect.Dialect;
import com.qualogy.qafe.business.transaction.SupportsLocalTransactions;

public abstract class RDBDatasource extends Resource implements SupportsLocalTransactions {

    private static final Logger LOG = Logger.getLogger(RDBDatasource.class.getName());

    private Dialect dialect;

    private DataSource dataSource;

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

    private List<FileLocation> getFileLocations(final ApplicationContext context) {

        final List<FileLocation> fileLocations = new ArrayList<FileLocation>();
        final FileLocation stmtFileNameTemp = getResource().getStatementsFileUrl();

        if (stmtFileNameTemp != null) {
            if (stmtFileNameTemp.getLocation().contains(",")) {
                final String[] stmtFiles = getResource().getStatementsFileUrl().getLocation().split(",");
                for (final String stmtFile : stmtFiles) {
                    String root = stmtFileNameTemp.getRoot();
                    final FileLocation fileLocation;
                    if (root == null && stmtFile.startsWith(FileLocation.SCHEME_FILE)) {
                        fileLocation = new FileLocation(stmtFile);
                    } else {
                        root = context.getRoot();
                        fileLocation = new FileLocation(StringUtils.trim(root), StringUtils.trim(stmtFile));
                    }
                    fileLocations.add(fileLocation);
                }
            } else {
                fileLocations.add(getResource().getStatementsFileUrl());
            }
        }
        return fileLocations;
    }

    private URI fileLocationToURI(final ApplicationContext context, final FileLocation stmtFileName) {

        URI uri = null;

        final String stmtFileLocation = stmtFileName.getLocation();

        if ((stmtFileLocation.startsWith(FileLocation.SCHEME_HTTP + FileLocation.COMMON_SCHEME_DELIM))
                || (stmtFileLocation.startsWith(FileLocation.SCHEME_FILE))) {
            uri = stmtFileName.toURI();
        } else {
            LOG.info(" Statement [" + stmtFileName
                    + "] file could not be found. Now iterating the filelocations in the applicationmapping");

            final List<FileLocation> list = context.getMappingFileLocations();
            if (list != null) {
                for (FileLocation fileLocation : list) {
                    LOG.info("Trying Statement ["
                            + fileLocation.getLocation()
                            + File.separator
                            + stmtFileName
                            + "] file could not be found. Now iterating the filelocations in the applicationmapping");

                    final String baseLocation;
                    if (FilenameUtils.indexOfExtension(fileLocation.getLocation()) == -1) {
                        baseLocation = fileLocation.getLocation();
                    } else {
                        baseLocation = FilenameUtils.getPath(fileLocation.getLocation());
                    }

                    final FileLocation fileLoc =
                        new FileLocation(context.getRoot(), baseLocation + File.separator
                                + stmtFileName.getLocation());
                    uri = fileLoc.toURI();
                    if (uri != null) {
                        break;
                    }
                }
            } else {
                uri = stmtFileName.toURI();
            }
        }
        return uri;
    }

    final void postInit(final ApplicationContext context) {

        QueryContainer container = null;

        final boolean validating =
            Boolean.valueOf(context.getConfigurationItem(Configuration.QAFE_XML_VALIDATION)).booleanValue();

        for (final FileLocation stmtFileName : getFileLocations(context)) {
            if (stmtFileName == null) {
                throw new IllegalArgumentException("DBSTATEMENTS_FILE_URL property not set");
            }

            final URI uri = fileLocationToURI(context, stmtFileName);

            LOG.info("loading from [" + ((uri != null) ? uri.toString() : "<empty URI>") + "]");

            if (uri == null) {
                throw new IllegalArgumentException("DBSTATEMENTS_FILE_URL statements not found "
                        + stmtFileName.getLocation());
            }

            if (container == null) {
                container = (QueryContainer) new Reader(QueryContainer.class, validating).read(uri);
            } else {
                final QueryContainer containerTemp =
                    (QueryContainer) new Reader(QueryContainer.class, validating).read(uri);
                final Collection<Query> queries = containerTemp.values();
                for (Query query : queries) {
                    container.put(query);
                }
            }
        }

        container = EnhancementManager.enhance(container, this);
        setQueryContainer(container);

        validate();
    }
}
