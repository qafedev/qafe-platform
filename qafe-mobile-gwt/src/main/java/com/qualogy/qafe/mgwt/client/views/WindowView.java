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
package com.qualogy.qafe.mgwt.client.views;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.qualogy.qafe.mgwt.client.ui.events.HasLoadHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasTimerHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasUnloadHandlers;

public interface WindowView extends AbstractView, HasLoadHandlers, HasUnloadHandlers, HasTimerHandlers {
	
	public void setContent(Widget widget);
	public void setToolbar(ButtonBar toolbar);
}
