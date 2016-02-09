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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;

/**
 * @author rjankie The Panel class is the container
 */
public class StackPanelGVO extends ComponentGVO implements HasComponentsI {

    protected StackGVO[] stacks;

    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getClassName() {
        return "com.qualogy.qafe.gwt.client.vo.ui.StackPanelGVO";
    }

    public StackGVO[] getStacks() {
        return stacks;
    }

    public void setStacks(StackGVO[] stacks) {
        this.stacks = stacks;
    }

    public ComponentGVO[] getComponents() {
        return getStacks();
    }

}
