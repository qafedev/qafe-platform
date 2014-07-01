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
package com.qualogy.qafe.web;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qualogy.qafe.core.QafeApplicationContext;
import com.qualogy.qafe.core.QafeConfigurationManager;
import com.qualogy.qafe.core.application.ApplicationContextLoader;

public class ContextLoader {

    private static final Logger logger = Logger.getLogger(ContextLoader.class.getName());

    private static final String LOG_SETTINGS_CUSTOM = "/logging.properties";

    private static final String LOG_SETTINGS_DEFAULT = "/qafe-logging.properties";

    /**
     * Name of servlet context parameter that can specify the config location for the root context, falling
     * back to the implementation's default otherwise.
     * 
     * @see org.springframework.web.context.support.XmlWebApplicationContext#DEFAULT_CONFIG_LOCATION
     */
    public static final String CONFIG_FILE_PARAM = "QAFEContextFile";

    public static final String QAFE_LICENSE_FILE = "QAFELicenseFile";

    public static final String RELOADABLE_PARAM = "QAFE_reload_enabled";

    // public static final String CONFIG_MDI = "MDI";
    //
    // public static final String CONFIG_DEBUG = "QAFEDebug";

    private static ServletContext context = null;

    private static QafeApplicationContext qafeContext = QafeApplicationContext.getInstance();

    public static ServletContext getContext() {
        return context;
    }

    public static String getContextPath(ServletContext servletContext) throws MalformedURLException,
            URISyntaxException {
        // The default way: works on JBoss/Tomcat/Jetty
        String contextPath = servletContext.getRealPath("/WEB-INF/");

        // This is how a weblogic explicitly wants the fetching of resources.
        if (contextPath == null) {
            URL url = servletContext.getResource("/WEB-INF/");
            logger.log(Level.INFO, "Fallback scenario " + url.toString());
            if (url != null) {
                logger.log(Level.INFO, "URL to config file " + url.toString());
                contextPath = url.toURI().toString();
            } else {
                throw new RuntimeException(
                        "Strange Error: /WEB-INF/ cannot be found. Check the appserver or installation directory.");
            }
        }
        return contextPath;
    }

    private static void handleException(Throwable exception, ServletContext servletContext) {
        if (exception == null) {
            return;
        }
        String message = ExceptionUtils.getRootCauseMessage(exception);
        logger.log(Level.SEVERE, "Context initialization failed", exception);
        servletContext.setAttribute("init_failure", exception);
        servletContext.setAttribute("init_failure_message", message);
    }

    private static void create(ServletContext servletContext)
            throws MalformedURLException, URISyntaxException {
        String configLocation = servletContext.getInitParameter(CONFIG_FILE_PARAM);
        String contextPath = getContextPath(servletContext);
        QafeConfigurationManager contextInitialiser = new QafeConfigurationManager(contextPath);
        ApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        contextInitialiser.setSpringContext(springContext);
        qafeContext.putInstance(QafeConfigurationManager.class.getName(), contextInitialiser);

        StopWatch watch = new StopWatch();
        watch.start();
        ApplicationContextLoader.unload();

        // The default way: works on JBoss/Tomcat/Jetty
        String configFileLocation = servletContext.getRealPath("/WEB-INF/");
        if (configFileLocation != null) {
            configFileLocation += File.separator + configLocation;
            logger.log(Level.INFO, "URL to config file on disk :" + configFileLocation + ")");
            ApplicationContextLoader.load(configFileLocation, true);
        } else {
            // This is how a weblogic explicitly wants the fetching of resources.
            URL url = servletContext.getResource("/WEB-INF/" + configLocation);
            logger.log(Level.INFO, "Fallback scenario" + url.toString());
            if (url != null) {
                logger.log(Level.INFO, "URL to config file " + url.toString());
                ApplicationContextLoader.load(url.toURI(), true);
            } else {
                throw new RuntimeException(
                        "Strange Error: /WEB-INF/ cannot be found. Check the appserver or installation directory.");
            }
        }

        watch.stop();
        logger.log(Level.INFO, "Root WebApplicationContext: initialization completed in " + watch.getTime()
                + " ms");
    }

    /**
     * Initialize QAFE Application context for the given servlet context, according to the "contextClass" and
     * "contextConfigLocation" context-params.
     * 
     * @param servletContext current servlet context
     * @return the new ApplicationContext
     * @throws IllegalStateException if there is already a root application context present
     */
    public void init(ServletContext servletContext) throws IllegalStateException {
        if (servletContext == null) {
            throw new IllegalArgumentException("servletContext cannot be null in init method");
        }

        initLogging();

        context = servletContext;
        servletContext.log("Initializing QAFE root ApplicationContext");
        logger.log(Level.INFO, "Root ApplicationContext: initialization started");

        try {
            create(servletContext);
        } catch (Throwable e) {
            handleException(e, servletContext);
        }
    }

    private void initLogging() {
        // String logSettings = LOG_SETTINGS_CUSTOM;
        // InputStream in = getClass().getResourceAsStream(logSettings);
        // if (in == null) {
        // logSettings = LOG_SETTINGS_DEFAULT;
        // in = getClass().getResourceAsStream(logSettings);
        // }
        // if (in == null) {
        // logger.warning(logSettings + " not found on the classpath");
        // } else {
        // try {
        // LogManager.getLogManager().readConfiguration(in);
        // } catch (Exception e) {
        // try {
        // LogManager.getLogManager().readConfiguration();
        // } catch (Exception e1) {
        // throw new RuntimeException(e1);
        // }
        // logger.log(Level.WARNING, "cannot initialize logging using " + logSettings, e);
        // }
        // }
    }

    public void close(ServletContext servletContext) {
        servletContext.log("Closing QAFE root WebApplicationContext");
        // TODO
        // ApplicationContextLoader.unload();
    }
}
