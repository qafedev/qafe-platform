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
package com.qualogy.qafe.business.resource.java.spring;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.bind.resource.SpringBeanResource;
import com.qualogy.qafe.bind.resource.SpringContextResource;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.java.ClassInfo;
import com.qualogy.qafe.core.QafeApplicationContext;
import com.qualogy.qafe.core.QafeConfigurationManager;

public class SpringContext extends Resource {

    private org.springframework.context.ApplicationContext springContext;

    private Map<String, String> beanNames = new HashMap<String, String>();

    public SpringContext(SpringContextResource resource) {
        super(resource);
    }

    @Override
    public void init(ApplicationContext context) {
        SpringContextResource springContextResource = (SpringContextResource) getBindResource();
        if (springContextResource.isUseWebConfig()) {
            springContext = getWebSpringContext();
            if (springContext == null) {
                throw new ValidationException("Not able to get spring context from web.xml file.");
            }
        } else {
            springContext = getQafeSpringContext(context, springContextResource);
        }

        List<SpringBeanResource> beanList = springContextResource.getBeans();
        for (SpringBeanResource bean : beanList) {
            beanNames.put(bean.getId(), bean.getBeanName());
        }
    }

    private org.springframework.context.ApplicationContext getQafeSpringContext(ApplicationContext context,
            SpringContextResource springContextResource) {
        org.springframework.context.ApplicationContext springContext = null;
        String configFiles = springContextResource.getConfigFiles();
        String[] xmlConfigFiles = configFiles.split(",");
        for (int i = 0; i < xmlConfigFiles.length; i++) {
            String xmlConfigFile = xmlConfigFiles[i];
            if (!xmlConfigFile.startsWith("classpath")) {
                xmlConfigFiles[i] = resolveXMLConfigFile(context.getRoot(), xmlConfigFile);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "XMLFile " + xmlConfigFiles[i]);
        }

        springContext = new FileSystemXmlApplicationContext(xmlConfigFiles);
        return springContext;
    }

    private org.springframework.context.ApplicationContext getWebSpringContext() {
        org.springframework.context.ApplicationContext webSpringContext = null;
        final QafeApplicationContext qafeApplicationContext = QafeApplicationContext.getInstance();
        final QafeConfigurationManager contextInitialiser =
            (QafeConfigurationManager) qafeApplicationContext.getInstance(QafeConfigurationManager.class
                .getName());
        if (contextInitialiser != null) {
            webSpringContext = contextInitialiser.getSpringContext();
        }
        return webSpringContext;
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity

    public String resolveXMLConfigFile(String root, String xmlConfigFile) {
        URI uri = null;

        if (!StringUtils.isEmpty(root) && !root.endsWith("/") && !root.endsWith("\\")) {
            root += File.separator;
        }

        String path = ((root != null) ? root : "") + ((xmlConfigFile != null) ? xmlConfigFile : "");

        if (File.separatorChar == '\\') {
            path = path.replace('\\', '/');
        }

        if (path.startsWith(FileLocation.SCHEME_HTTP + FileLocation.COMMON_SCHEME_DELIM)) {
            try {
                URL url = new URL(path);
                uri = url.toURI();
            } catch (MalformedURLException e) {
                throw new BindException(e);
            } catch (URISyntaxException e) {
                throw new BindException(e);
            }
        } else if (path.startsWith(FileLocation.SCHEME_FILE)) {
            try {
                uri = new URI(path);
            } catch (URISyntaxException e) {
                throw new BindException(e);
            }
        } else {
            if (!StringUtils.isEmpty(root) && !root.startsWith("/") && !root.startsWith("\\")) {
                root = File.separator + root;
            }
            File file =
                StringUtils.isEmpty(root) ? new File(((xmlConfigFile != null) ? xmlConfigFile : ""))
                        : new File(root, ((xmlConfigFile != null) ? xmlConfigFile : ""));
            uri = file.toURI();
            return "file:" + uri.getPath();
        }
        return uri.getPath();
    }

    // CHECKSTYLE.ON: CyclomaticComplexity

    public Object getBean(String beanRef) {
        if (beanNames.containsKey(beanRef)) {
            String beanName = beanNames.get(beanRef);
            Object bean = springContext.getBean(beanName);
            return bean;
        }
        return null;
    }

    protected ClassInfo getBeanInfo(Object bean) {
        if (bean != null) {
            ClassInfo beanInfo = ClassInfo.create(bean.getClass());
            return beanInfo;
        }
        return null;
    }

    public Method getMethod(Object bean, String methodName, Class<?>[] parameterClasses)
            throws NoSuchMethodException {
        return getBeanInfo(bean).getMethod(methodName, parameterClasses);
    }

    @Override
    public void destroy(ApplicationContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isEqualTo(Resource otherResource) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String toLogString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void validate() throws ValidationException {
        if (((SpringContextResource) getBindResource()).getConfigFiles() == null) {
            throw new ValidationException("config-files must be set for resource type spring");
        }
    }
}
