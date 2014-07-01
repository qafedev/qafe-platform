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
package com.qualogy.qafe.mgwt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

public class CellRenderEvent implements QEvent<CellRenderHandler> {
	
	private static final GwtEvent.Type<CellRenderHandler> TYPE = new GwtEvent.Type<CellRenderHandler>();
	
	private UIObject source;
	private Object model;
	private int index;
	
	public static GwtEvent.Type<CellRenderHandler> getType() {
		return TYPE;
	}
	
	public CellRenderEvent(UIObject source, Object model, int index) {
		this.source = source;
		this.model = model;
		this.index = index;
	}

	public UIObject getSource() {
		return source;
	}

	public Object getModel() {
		return model;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public Object execute(CellRenderHandler handler) {
		return handler.doCellRender(this);
	}
}
