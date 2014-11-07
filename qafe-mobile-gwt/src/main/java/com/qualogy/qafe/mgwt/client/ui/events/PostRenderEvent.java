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

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

public class PostRenderEvent extends GwtEvent<PostRenderHandler> implements QEvent<PostRenderHandler> {

	private static final GwtEvent.Type<PostRenderHandler> TYPE = new GwtEvent.Type<PostRenderHandler>();
	
	private UIObject source;
	private List model;
	
	public static GwtEvent.Type<PostRenderHandler> getType() {
		return TYPE;
	}

	public PostRenderEvent(UIObject source, List model) {
		this.source = source;
		this.model = model;
	}

	public UIObject getSource() {
		return source;
	}

	public List getModel() {
		return model;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PostRenderHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PostRenderHandler handler) {
		handler.onPostRender(this);
	}

	@Override
	public Object execute(PostRenderHandler handler) {
		dispatch(handler);
		return null;
	}
}