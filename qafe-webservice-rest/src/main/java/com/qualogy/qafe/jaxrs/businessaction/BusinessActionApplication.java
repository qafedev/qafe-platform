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
package com.qualogy.qafe.jaxrs.businessaction;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.qualogy.qafe.businessaction.BusinessActionHandler;
import com.qualogy.qafe.businessaction.impl.BusinessActionHandlerImpl;

/**
 * Singleton binder for binding the <code>BusinessActionHandler</code> to resources that are interested in it.
 * For example, see <code>BusinessActionResource</code>.
 * 
 * @author sdahlberg
 * 
 */
public final class BusinessActionApplication extends ResourceConfig {

    public BusinessActionApplication() {
        register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(new BusinessActionHandlerImpl()).to(BusinessActionHandler.class);
            }
        });
    }
}
