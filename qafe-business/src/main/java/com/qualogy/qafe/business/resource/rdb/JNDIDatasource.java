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

import javax.sql.DataSource;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.JNDIDatasourceResource;
import com.qualogy.qafe.bind.util.ServiceLocator;
import com.qualogy.qafe.business.resource.ResourceInitializationException;

public class JNDIDatasource extends RDBDatasource {

    public JNDIDatasource(BindResource resource) {
        super(resource);
    }

    public void init(final ApplicationContext context) {
        try {
            final DataSource dataSource =
                ServiceLocator.getInstance().getDataSource(
                    ((JNDIDatasourceResource) getBindResource()).getJndiname());

            setDataSource(dataSource);
            /*
             * Context initialContext = new InitialContext(); if (initialContext == null) { throw new
             * ResourceInitializationException("initialContext = null, cannot retrieve datasource"); }
             * dataSource = (DataSource) initialContext.lookup(
             * ((JNDIDatasourceResource)getBindResource()).getJndiname());
             */
        } catch (Exception e) {
            String jndiName = null;
            if (getBindResource() != null) {
                jndiName = ((JNDIDatasourceResource) getBindResource()).getJndiname();
            }
            throw new ResourceInitializationException("Exception in finding Datasource (" + jndiName + ")");
        }

        if (context == null) {
            throw new IllegalArgumentException("cannot initialize without context");
        }

        postInit(context);
    }

    public void validate() throws ValidationException {
        // TODO
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[jndiname:\t" + ((JNDIDatasourceResource) getBindResource()).getJndiname() + "\n");
        buffer.append("id:\t" + ((JNDIDatasourceResource) getBindResource()).getId() + "]" + "\n");
        return buffer.toString();
    }
}
