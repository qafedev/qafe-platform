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

public class ResultEvent extends GwtEvent<ResultHandler> implements QEvent<ResultHandler> {
	
	private static final GwtEvent.Type<ResultHandler> TYPE = new GwtEvent.Type<ResultHandler>();

	private Object result;
	private String listenerType;
	
	public static GwtEvent.Type<ResultHandler> getType() {
		return TYPE;
	}
	
	public ResultEvent(Object result, String listenerType) {
		this.result = result;
		this.listenerType = listenerType;
	}

	public Object getResult() {
		return result;
	}

	public String getListenerType() {
		return listenerType;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResultHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResultHandler handler) {
		handler.onResult(this);
	}
	
	@Override
	public Object execute(ResultHandler handler) {
		dispatch(handler);
		return null;
	}
}