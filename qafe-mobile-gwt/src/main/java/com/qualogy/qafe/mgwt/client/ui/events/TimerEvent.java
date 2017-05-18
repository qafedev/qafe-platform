/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

public class TimerEvent extends GwtEvent<TimerHandler> implements QEvent<TimerHandler> {

	private static final GwtEvent.Type<TimerHandler> TYPE = new GwtEvent.Type<TimerHandler>();
	
	public static GwtEvent.Type<TimerHandler> getType() {
		return TYPE;
	}

	public TimerEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TimerHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TimerHandler handler) {
		handler.onTimer(this);
	}

	@Override
	public Object execute(TimerHandler handler) {
		dispatch(handler);
		return null;
	}
}
