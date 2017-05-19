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
package com.qualogy.qafe.bind.util;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class is an implementation of the Service Locator pattern. It is used to lookup Datasources. This
 * implementation uses the "singleton" strategy and also the "caching" strategy. This implementation is
 * intended to be used on the web tier and not on the ejb tier.
 **/
public class ServiceLocator {

    private static final Logger LOG = Logger.getLogger(ServiceLocator.class.getName());
    
    private InitialContext ic;

    private Map cache; // used to hold references to EJBHomes/JMS Resources for re-use

    private static ServiceLocator me;

    static {
        try {
            me = new ServiceLocator();
        } catch (Exception se) {
            LOG.log(Level.WARNING, "Problem constructing service locator", se);
        }
    }

    private ServiceLocator() throws Exception {
        try {
            ic = new InitialContext();
            cache = Collections.synchronizedMap(new HashMap());
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    static public ServiceLocator getInstance() {
        return me;
    }

    /**
     * This method obtains the datasource itself for a caller
     * 
     * @param String dataSourceName
     * @return the DataSource corresponding to the name parameter
     */
    public DataSource getDataSource(String dataSourceName) throws Exception {
        DataSource dataSource = null;
        try {
            if (cache.containsKey(dataSourceName)) {
                dataSource = (DataSource) cache.get(dataSourceName);
            } else {
                dataSource = (DataSource) ic.lookup(dataSourceName);
                cache.put(dataSourceName, dataSource);
            }
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return dataSource;
    }

    /**
     * @param Sting envName
     * @return the URL value corresponding to the env entry name.
     */
    public URL getUrl(String envName) throws Exception {
        URL url = null;
        try {
            url = (URL) ic.lookup(envName);
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (Exception e) {
            throw new Exception(e);
        }

        return url;
    }

    /**
     * @param String envName
     * @return the boolean value corresponding to the env entry such as SEND_CONFIRMATION_MAIL property.
     */
    public boolean getBoolean(String envName) throws Exception {
        Boolean bool = null;
        try {
            bool = (Boolean) ic.lookup(envName);
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return bool.booleanValue();
    }

    /**
     * @param String envName
     * @return the String value corresponding to the env entry name.
     */
    public String getString(String envName) throws Exception {
        String envEntry = null;
        try {
            envEntry = (String) ic.lookup(envName);
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return envEntry;
    }
}
