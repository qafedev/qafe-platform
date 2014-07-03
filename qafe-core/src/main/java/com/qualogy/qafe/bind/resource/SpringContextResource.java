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
package com.qualogy.qafe.bind.resource;

import java.util.List;

public class SpringContextResource extends BindResource {

    private static final long serialVersionUID = 1L;

    protected boolean useWebConfig;

    protected String configFiles;

    protected List<SpringBeanResource> beans;

    public boolean isUseWebConfig() {
        return useWebConfig;
    }

    public void setUseWebConfig(boolean useWebConfig) {
        this.useWebConfig = useWebConfig;
    }

    public String getConfigFiles() {
        return configFiles;
    }

    public void setConfigFiles(String configFiles) {
        this.configFiles = configFiles;
    }

    public List<SpringBeanResource> getBeans() {
        return beans;
    }

    public void setBeans(List<SpringBeanResource> beans) {
        this.beans = beans;
    }
}
