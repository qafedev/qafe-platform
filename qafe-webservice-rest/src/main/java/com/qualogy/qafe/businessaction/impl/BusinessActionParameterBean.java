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
package com.qualogy.qafe.businessaction.impl;

/**
 * Business action parameter bean.
 * 
 * @author sdahlberg
 * 
 */
public final class BusinessActionParameterBean {

    private String key;

    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(final String newKey) {
        this.key = newKey;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object newValue) {
        this.value = newValue;
    }
}
