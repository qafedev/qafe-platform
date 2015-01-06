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

public class SuggestionEvent extends GwtEvent<SuggestionHandler> implements QEvent<SuggestionHandler>{

	private static final GwtEvent.Type<SuggestionHandler> TYPE = new GwtEvent.Type<SuggestionHandler>();
	
	private UIObject source;
	
	public static GwtEvent.Type<SuggestionHandler> getType() {
		return TYPE;
	}

	public SuggestionEvent(UIObject source) {
		this.source = source;
	}

	public UIObject getSource() {
		return source;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SuggestionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SuggestionHandler handler) {
		handler.onSuggestion(this);
	}

	@Override
	public Object execute(SuggestionHandler handler) {
		dispatch(handler);
		return null;
	}
}