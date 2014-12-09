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
/**
 * 
 */
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

/**
 * @author rjankie
 */

public class DropDown extends EditableComponent implements PostProcessing, HasRequiredProperty {

    private static final Logger LOG = Logger.getLogger(DropDown.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = 4225013574017207708L;

    /**
	 * 
	 */
    protected List<DropDownItem> dropDownItems = new ArrayList<DropDownItem>();

    /**
	 * 
	 */
    protected String emptyItemDisplayName;

    protected String emptyItemValue;

    protected String emptyItemMessageKey;

    protected Boolean required = Boolean.FALSE;

    protected String requiredStyleClassName;

    /**
     * @return
     */
    public List<DropDownItem> getDropDownItems() {
        return dropDownItems;
    }

    /**
     * @param dropDownItems
     */
    public void setDropDownItems(List<DropDownItem> dropDownItems) {
        this.dropDownItems = dropDownItems;
    }

    /**
     * @return
     */
    public String getEmptyItemDisplayName() {
        return emptyItemDisplayName;
    }

    /**
     * @param emptyItemDisplayName
     */
    public void setEmptyItemDisplayName(String emptyItemDisplayName) {
        this.emptyItemDisplayName = emptyItemDisplayName;
    }

    /**
     * @return
     */
    public String getEmptyItemValue() {
        return emptyItemValue;
    }

    /**
     * @param emptyItemValue
     */
    public void setEmptyItemValue(String emptyItemValue) {
        this.emptyItemValue = emptyItemValue;
    }

    /**
     * @return
     */
    public String getEmptyItemMessageKey() {
        return emptyItemMessageKey;
    }

    /**
     * @param emptyItemMessageKey
     */
    public void setEmptyItemMessageKey(String emptyItemMessageKey) {
        this.emptyItemMessageKey = emptyItemMessageKey;
    }

    /**
     * Post setter
     * 
     * @param context
     */
    public void postset(IUnmarshallingContext context) {
        performPostProcessing();
    }

    public void performPostProcessing() {
        // check the number of selected items !!!
        if (getDropDownItems() != null) {
            Iterator<DropDownItem> itr = getDropDownItems().iterator();
            int selectedCount = 0;
            while (itr.hasNext()) {
                Object o = itr.next();
                if (o instanceof DropDownItem) {
                    DropDownItem dropDownItem = (DropDownItem) o;
                    if (dropDownItem.isSelected() != null) {
                        if (dropDownItem.isSelected().booleanValue() == true) {
                            selectedCount++;
                        }
                    }
                }
            }
            if (selectedCount > 1) {
                LOG.log(Level.WARNING, "There are more selected items in dropdown " + getId());
            }
        }

    }

    /**
     * @param dropDownItem
     */
    public void add(DropDownItem dropDownItem) {
        if (dropDownItem != null) {
            getDropDownItems().add(dropDownItem);
        } else {
            // throw NullPointerException
        }
    }

    /**
     * @param dropDownItem
     */
    public void remove(DropDownItem dropDownItem) {
        if (dropDownItem != null) {
            getDropDownItems().remove(dropDownItem);
        } else {
            // throw NullPointerException
        }
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getRequiredStyleClassName() {
        return requiredStyleClassName;
    }

    public void setRequiredStyleClassName(String requiredStyleClassName) {
        this.requiredStyleClassName = requiredStyleClassName;
    }
}
