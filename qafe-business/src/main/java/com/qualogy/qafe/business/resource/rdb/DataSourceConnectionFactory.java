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
package com.qualogy.qafe.business.resource.rdb;

import com.qualogy.qafe.business.integration.rdb.ProxyConnectionPropertiesNameEnum;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;

import oracle.jdbc.OracleConnection;

import org.apache.commons.dbcp.DelegatingConnection;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Properties;

import javax.sql.DataSource;


public class DataSourceConnectionFactory {
    /**
     * returns a proxy database connection.
     *
     * @param ds
     * @param dataId
     * @return
     * @throws SQLException
     */
    public static Connection getProxyConnection(DataSource ds,
        DataIdentifier dataId) throws SQLException {
        Connection conn = ds.getConnection();

        //for OracleConnection
        if (((DelegatingConnection) conn).getInnermostDelegate() instanceof OracleConnection) {
            Properties properties = getOracleProxyConnectionProperties(dataId);

            OracleConnection oracleConnection = (OracleConnection) ((DelegatingConnection) conn).getInnermostDelegate();
            oracleConnection.openProxySession(OracleConnection.PROXYTYPE_USER_NAME,
                properties);

            conn = oracleConnection;
        }

        return conn;
    }

    /**
     * Determine two things. First if
     * connection is an oracle connection
     * and second if connection is proxy
     * connection.
     *
     * @param conn
     * @return
     */
    public static boolean isProxyConnection(Connection conn) {
        boolean isProxy = false;

        if (conn instanceof OracleConnection) {
            isProxy = ((OracleConnection) conn).isProxySession();
        }

        return isProxy;
    }

    /**
     * Gets oracle proxy connection  properties
     * used for making proxy connection to the oracle database.
     *
     * @param dataId
     * @return
     */
    public static Properties getOracleProxyConnectionProperties(
        DataIdentifier dataId) {
        Properties properties = new Properties();

        String userName = (String) DataStore.getValue(dataId,
                ProxyConnectionPropertiesNameEnum.USERNAME.propertyName());
        properties.put(ProxyConnectionPropertiesNameEnum.USERNAME.propertyName(),
            userName);

        return properties;
    }
}
