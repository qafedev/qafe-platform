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
package com.qualogy.qafe.bind.domain;

import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 * Base class for caching the reference (<code>ref</code>) attribute as defined in the JiBX binding. This can
 * be used as user feedback in case the <code>ref</code> attribute cannot be mapped. The <code>ref</code>
 * object mapping itself is maanged by sub classes.
 * 
 */
public abstract class BaseRef extends BindBase {

    private String refId;

    public final String getRefId() {
        return refId;
    }

    public final void setRefId(final IUnmarshallingContext context) {
        final UnmarshallingContext ctx = (UnmarshallingContext) context;
        final int attributeCount = ctx.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = ctx.getAttributeName(i);
            if ("ref".equals(attributeName)) {
                refId = ctx.getAttributeValue(i);
            }
        }
    }

}
