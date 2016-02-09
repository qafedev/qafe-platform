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
package com.qualogy.qafe.mgwt.client.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public class QPlaceController extends PlaceController {

	public static class QDefaultDelegate extends DefaultDelegate {
		@Override
		public boolean confirm(String message) {
			// Not showing confirmation dialog
			// when mayStop returns a non-nullable message
			return false;
		}
	}
	
	private Place newPlace;
	
	public QPlaceController(EventBus eventBus, Delegate delegate) {
		super(eventBus, delegate);
	}

	public QPlaceController(EventBus eventBus) {
		super(eventBus, (Delegate)GWT.create(QDefaultDelegate.class));
	}
	
	@Override
	public void goTo(Place newPlace) {
		this.newPlace = newPlace;
		super.goTo(newPlace);
		if (this.newPlace == getWhere()) {
			this.newPlace = null;
		}
	}
	
	public void flush() {
		if (newPlace != null) {
			goTo(newPlace);
		}
	}
}