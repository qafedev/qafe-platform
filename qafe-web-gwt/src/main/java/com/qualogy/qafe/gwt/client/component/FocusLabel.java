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

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;

/**
 * A label that is a little more active than your typical label
 */
public class FocusLabel extends FocusPanel implements HasText {

	HTML wrappedLabel = new HTML("");
	public HTML getWrappedLabel() {
		return wrappedLabel;
	}

	public FocusLabel() {
		add(wrappedLabel);
	}

	public FocusLabel(String text) {
		this();
		setText(text);
	}

	public String getText() {
		return wrappedLabel.getText();
	}

	public void setText(String text) {
		wrappedLabel.setHTML(text);
	}

}
