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
package com.qualogy.qafe.mgwt.client.activities;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.widget.celllist.HasCellSelectedHandler;


/**
 * @author Daniel Kurka
 *
 */
public interface UIView extends IsWidget {
	public void setBackButtonText(String text);

	public HasTapHandlers getBackButton();

	public void setTitle(String title);

	public HasCellSelectedHandler getList();

	public void renderItems(List<Item> items);

	public void setSelectedIndex(int index, boolean selected);
}
