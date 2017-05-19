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
package com.qualogy.qafe.mgwt.client.activities.searchbox;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.googlecode.mgwt.ui.client.widget.MSearchBox;
import com.qualogy.qafe.mgwt.client.DetailViewGwtImpl;

/**
 * @author Daniel Kurka
 * 
 */
public class SearchBoxViewGwtImpl extends DetailViewGwtImpl implements SearchBoxView {

	public SearchBoxViewGwtImpl() {

		FormPanel formPanel = new FormPanel("");
		formPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				event.cancel();

			}
		});
		MSearchBox searchBox = new MSearchBox();
		formPanel.setWidget(searchBox);
		scrollPanel.setWidget(formPanel);
	}

}
