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

import com.google.gwt.user.client.Timer;

public class TimerHandlerImpl extends Timer implements TimerHandler {

	private String key;
	
	public TimerHandlerImpl(String key, int timeout, int repeat) {
		this.key = key;
		if (timeout == -1) {
			return;
		}
		if (repeat == 0) {
			scheduleRepeating(timeout);
		} else {
			schedule(timeout);
		}
	}
	
	@Override
	public void onTimer(TimerEvent event) {
		// Subclasses should override this method
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void dispose() {
		cancel();
	}

	@Override
	public void run() {
		TimerEvent event = new TimerEvent();
		event.execute(this);
	}
}
