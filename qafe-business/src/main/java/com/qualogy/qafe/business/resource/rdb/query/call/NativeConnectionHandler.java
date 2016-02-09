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
package com.qualogy.qafe.business.resource.rdb.query.call;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

public class NativeConnectionHandler {
    
    private static final Logger LOG = Logger.getLogger(BaseCall.class.getName());

    private static List<String> possibleConnectionClasses = new ArrayList<String>();
    
    static {
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.WebLogicNativeJdbcExtractor");
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor");
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor");
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.JBossNativeJdbcExtractor");
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.WebSphereNativeJdbcExtractor");
    	possibleConnectionClasses.add("org.springframework.jdbc.support.nativejdbc.XAPoolNativeJdbcExtractor");
    }
    
    public static Connection nativeConnection;
    
    public static Connection getNativeConnection(Connection conn) throws SQLException {
        // Caching purposes
    	if (nativeConnection != null) {
        	return nativeConnection;
        }
        
    	Connection nativeConnection = conn;
    	NativeJdbcExtractor extractor = findNativeConnectionExtractor();
        LOG.info("Native Connection Extractor :" + extractor.getClass());
        if (extractor != null) {
            nativeConnection = extractor.getNativeConnection(conn);
        }
        LOG.info("Native Connection  :" + nativeConnection.getClass());

        return nativeConnection;
    }

    private static NativeJdbcExtractor findNativeConnectionExtractor() {
        NativeJdbcExtractor extractor = null;
        
        for (final String connectionClass : possibleConnectionClasses) {
        	try {
				extractor = (NativeJdbcExtractor) Class.forName(connectionClass).newInstance();
				LOG.info("Current Extractor successful:" + connectionClass);
				return extractor;
			} catch (Exception e) {
				LOG.info("Current Extractor failed:" + connectionClass);
			}
        }

        return extractor;
    }
}
