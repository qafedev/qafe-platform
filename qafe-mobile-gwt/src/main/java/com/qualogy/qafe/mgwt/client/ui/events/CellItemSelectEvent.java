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

public class CellItemSelectEvent extends GwtEvent<CellItemSelectHandler> implements QEvent<CellItemSelectHandler> {

	private static final GwtEvent.Type<CellItemSelectHandler> TYPE = new GwtEvent.Type<CellItemSelectHandler>();
	
	private UIObject source;
	private String cellItem;
	private Object model;
	private int index;
	
	public static GwtEvent.Type<CellItemSelectHandler> getType() {
		return TYPE;
	}
	
	public CellItemSelectEvent(UIObject source, String cellItem, Object model, int index) {
		this.source = source;
		this.cellItem = cellItem;
		this.model = model;
		this.index = index;
	}
	
	public UIObject getSource() {
		return source;
	}

	public String getCellItem() {
		return cellItem;
	}

	public Object getModel() {
		return model;
	}

	public int getIndex() {
		return index;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CellItemSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CellItemSelectHandler handler) {
		handler.onCellItemSelect(this);
	}

	@Override
	public Object execute(CellItemSelectHandler handler) {
		dispatch(handler);
		return null;
	}
}
