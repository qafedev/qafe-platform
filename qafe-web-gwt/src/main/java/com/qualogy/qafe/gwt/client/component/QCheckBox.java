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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DirectionalTextHelper;

public class QCheckBox extends CheckBox implements HasEditable {
	
	private static final String DISPLAYNAME_POSITION_LEFT = "left";
	
	private DirectionalTextHelper directionalTextHelper;
	private InputElement inputElem;
	private LabelElement labelElem;
	
	public QCheckBox(Element elem, String displaynamePosition) {
	    inputElem = (InputElement) this.getElement().getChild(0);
	    labelElem =  (LabelElement) this.getElement().getChild(1);
	    if (displaynamePosition.equals(DISPLAYNAME_POSITION_LEFT)) {
	    	getElement().appendChild(labelElem);
	    	getElement().appendChild(inputElem);
	    } else {
	    	getElement().appendChild(inputElem);
		    getElement().appendChild(labelElem);
	    }
	    String uid = DOM.createUniqueId();
	    inputElem.setPropertyString("id", uid);
	    labelElem.setHtmlFor(uid);
	    directionalTextHelper = new DirectionalTextHelper(labelElem, true);

	    // Accessibility: setting tab index to be 0 by default, ensuring element
	    // appears in tab sequence. FocusWidget's setElement method already
	    // calls setTabIndex, which is overridden below. However, at the time
	    // that this call is made, inputElem has not been created. So, we have
	    // to call setTabIndex again, once inputElem has been created.
	    setTabIndex(0);
	}

	public boolean isEditable() {
		return isEnabled();
	}

	public void setEditable(boolean editable) {
		setEnabled(editable);
	}
}