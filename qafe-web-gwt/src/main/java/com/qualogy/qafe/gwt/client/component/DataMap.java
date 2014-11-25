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
package com.qualogy.qafe.gwt.client.component;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataMap extends HashMap<String, DataContainerGVO> implements IsSerializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1873453039049810608L;

    private Set<String> keys = new LinkedHashSet<String>();

    @Deprecated
    public static String ROW_STATUS_KEY = "rowStatus";

    @Deprecated
    public static String ROW_NUMBER = "rowNumber";

    @Deprecated
    public static String ROW_STATUS_UNMODIFIED = "$$UNMODIFIED";

    @Deprecated
    public static String ROW_STATUS_NEW = "$$NEW";

    @Deprecated
    public static String ROW_STATUS_DELETED = "$$DELETED";

    @Deprecated
    public static String ROW_STATUS_MODIFIED = "$$MODIFIED";

    public static String SELECTED_INDEX = "$SELECTED_INDEX";

    /**
     * (non-Javadoc)
     * 
     * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public DataContainerGVO put(String key, DataContainerGVO value) {
        keys.add(key);
        return super.put(key, value);
    }

    @Override
    public DataContainerGVO remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    /**
     * Returns backing ordered keySet
     */
    @Override
    public Set<String> keySet() {
        return keys;
    }

    /**
     * @see java.util.HashMap#clear()
     */
    @Override
    public void clear() {
        keys.clear();
        super.clear();
    }
}
