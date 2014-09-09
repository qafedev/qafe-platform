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

import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.SQLQuery;

import org.apache.commons.lang.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLQueryEnhancer implements Enhancer {

    private static final Logger LOG = Logger.getLogger(SQLQueryEnhancer.class.getName());

    public Query enhance(Query query, DatabaseMetaData databaseMetaData) throws EnhancementFailedException {
        SQLQuery sqlQuery = (SQLQuery) query;

        ResultSet resultSet = null;

        try {
            if (StringUtils.isBlank(sqlQuery.getSqlAsAttribute())
                    && StringUtils.isBlank(sqlQuery.getSqlAsText())
                    && StringUtils.isNotBlank(sqlQuery.getTable())) {
                // The Oracle database stores its table names as Upper-Case,
                // if you pass a table name in lowercase characters, it will not work.
                // MySQL database does not care if table name is uppercase/lowercase.
                //
                // TODO:check if there is a way to catch table data
                // TODO: dialect needed for upper/lowercase
                String userName = databaseMetaData.getUserName();
                resultSet = databaseMetaData.getTables(null, null, sqlQuery.getTable().toUpperCase(), null);

                String tableSchema = null;

                // Knowing schema name is not necessary but we gain performance
                // by using it during retrieving meta data.
                while (resultSet.next() && (null != userName)) {
                    // some vendors like MySQL do not provide schema name
                    // that's why we have to check whether the schema name is "null"
                    if ((null != resultSet.getString("TABLE_SCHEM"))
                            && resultSet.getString("TABLE_SCHEM").equals(userName)) {
                        tableSchema = userName;

                        break;
                    }

                    // TABLE_TYPE
                }

                try {
                    sqlQuery.getMetaData().setSupportsGetGeneratedKeys(
                        databaseMetaData.supportsGetGeneratedKeys());
                } catch (AbstractMethodError e) {
                    LOG.log(Level.WARNING,
                        "On the database driver there is no support for Metadata reading (sqlquery: "
                                + sqlQuery.getId() + ")", e);
                }

                // if you pass null values for the first two parameters, then
                // it might take too long to return the result.
                resultSet =
                    databaseMetaData.getPrimaryKeys(null, tableSchema, sqlQuery.getTable().toUpperCase());

                while (resultSet.next()) {
                    sqlQuery.getMetaData().addPrimaryKey(resultSet.getString("COLUMN_NAME"));
                }

                // if no primarykeys found on a table, an update statement cannot be generated
                // so the query will be marked as error containing.
                sqlQuery.validate();
            }
        } catch (SQLException e) {
            throw new EnhancementFailedException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new EnhancementFailedException(e);
                }
            }
        }

        return sqlQuery;
    }
}
