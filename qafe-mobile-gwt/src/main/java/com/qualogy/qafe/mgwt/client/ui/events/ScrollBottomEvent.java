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

public class ScrollBottomEvent extends GwtEvent<ScrollBottomHandler> implements QEvent<ScrollBottomHandler> {

	private static final GwtEvent.Type<ScrollBottomHandler> TYPE = new GwtEvent.Type<ScrollBottomHandler>();
	
	private UIObject source;
	
	public static GwtEvent.Type<ScrollBottomHandler> getType() {
		return TYPE;
	}

	public ScrollBottomEvent(UIObject source) {
		this.source = source;
	}
	
	public UIObject getSource() {
		return source;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ScrollBottomHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScrollBottomHandler handler) {
		handler.onScrollBottom(this);
	}

	@Override
	public Object execute(ScrollBottomHandler handler) {
		dispatch(handler);
		return null;
	}
}
