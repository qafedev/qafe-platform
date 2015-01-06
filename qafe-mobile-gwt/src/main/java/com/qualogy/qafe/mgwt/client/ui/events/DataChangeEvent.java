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
package com.qualogy.qafe.mgwt.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

public class DataChangeEvent extends GwtEvent<DataChangeHandler> implements QEvent<DataChangeHandler> {

	private static final GwtEvent.Type<DataChangeHandler> TYPE = new GwtEvent.Type<DataChangeHandler>();
	
	private UIObject source;
	private Object newValue;
	private Object oldValue;
	
	public static GwtEvent.Type<DataChangeHandler> getType() {
		return TYPE;
	}

	public DataChangeEvent(UIObject source, Object newValue, Object oldValue) {
		this.source = source;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	public UIObject getSource() {
		return source;
	}

	public Object getNewValue() {
		return newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DataChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DataChangeHandler handler) {
		handler.onDataChange(this);
	}

	@Override
	public Object execute(DataChangeHandler handler) {
		dispatch(handler);
		return null;
	}
}