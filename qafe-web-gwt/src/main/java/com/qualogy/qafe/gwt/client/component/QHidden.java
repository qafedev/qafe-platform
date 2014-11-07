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

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hidden;
/**
 * Convenience class, instead of testing this class individually by the
 * setValue and EventHandling
 * @author rjankie
 *
 */
public class QHidden extends Hidden implements HasText {

	public String getText() {
		return getValue();
	}

	public void setText(String arg0) {
		setValue(arg0);

	}

}
