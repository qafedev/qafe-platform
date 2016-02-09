/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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

public class SuggestionSelectEvent extends GwtEvent<SuggestionSelectHandler> implements QEvent<SuggestionSelectHandler>{

	private static final GwtEvent.Type<SuggestionSelectHandler> TYPE = new GwtEvent.Type<SuggestionSelectHandler>();
	
	private UIObject source;
	private Object selection;
	
	public static GwtEvent.Type<SuggestionSelectHandler> getType() {
		return TYPE;
	}

	public SuggestionSelectEvent(UIObject source, Object selection) {
		this.source = source;
		this.selection = selection;
	}

	public UIObject getSource() {
		return source;
	}
	
	public Object getSelection() {
		return selection;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SuggestionSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SuggestionSelectHandler handler) {
		handler.onSuggestionSelect(this);
	}

	@Override
	public Object execute(SuggestionSelectHandler handler) {
		dispatch(handler);
		return null;
	}
}