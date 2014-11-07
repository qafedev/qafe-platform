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
package com.qualogy.qafe.bind.rules;

import com.qualogy.qafe.bind.commons.type.Parameter;

import java.util.List;
import java.util.ArrayList;

/**
 * Contain rule regarding one kind of component
 * for example in oracle form situation we can
 * have rule on trigger or program unit components. 
 */
public class FilterRule {

    private List<Parameter> expressions = new ArrayList<Parameter>();
    private String name;
    private int elementId; 

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addExpression(Parameter expression){
        expressions.add(expression);    
    }

    public void deleteExpression(Parameter expression){
        expressions.remove(expression);
    }

    public List<Parameter> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Parameter> expressions) {
        this.expressions = expressions;
    }

    private String node;
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "FilterRule{" +
                "node= '" + node + '\'' +
                "name= '" + name + '\'' +
                "elementId= '" + elementId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && getClass() == o.getClass()) {

            FilterRule that = (FilterRule) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result;
//        result = (expressions != null ? expressions.hashCode() : 0);
        result =  (name != null ? name.hashCode() : 0);
//        result = 31 * result + (node != null ? node.hashCode() : 0);
        return result;
    }
    
}
