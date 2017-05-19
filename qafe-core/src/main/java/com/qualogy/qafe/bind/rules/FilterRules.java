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
package com.qualogy.qafe.bind.rules;

import java.util.List;
import java.util.ArrayList;

/**
 * Container of rules to filter components.
 *  
 */
public class FilterRules {
    private List<FilterRule> rules = new ArrayList<FilterRule>();

    public List<FilterRule> getRules() {
        return rules;
    }

    public void setRules(List<FilterRule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        String rulesText = "";
        for(FilterRule filterRule : rules){
            rulesText += "rulesText={" + filterRule;
        }
        rulesText += rulesText + '}';
        return rulesText;
    }

}
