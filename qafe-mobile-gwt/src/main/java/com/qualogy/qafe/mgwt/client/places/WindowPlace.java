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
package com.qualogy.qafe.mgwt.client.places;

import com.google.gwt.place.shared.PlaceTokenizer;

public class WindowPlace extends AbstractPlace {

	public static class WindowPlaceTokenizer implements PlaceTokenizer<WindowPlace> {

		@Override
		public WindowPlace getPlace(String token) {
			// TODO
			return new WindowPlace(null, null);
		}

		@Override
		public String getToken(WindowPlace place) {
			// TODO
			return "";
		}
	}
	
	private String id;
	private AbstractPlace fromPlace;
	
	public WindowPlace(String id, String context) {
		this(id, context, null);
	}

	public WindowPlace(String id, String context, AbstractPlace fromPlace) {
		super(context);
		this.id = id;
		this.fromPlace = fromPlace;
	}
	
	public String getId() {
		return id;
	}

	public AbstractPlace getFromPlace() {
		return fromPlace;
	}
}
