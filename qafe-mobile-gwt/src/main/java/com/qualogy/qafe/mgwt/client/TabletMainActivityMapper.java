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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.MGWT;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;

public class TabletMainActivityMapper extends AbstractActivityMapper {

	public TabletMainActivityMapper(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof WindowPlace) {
			return new WindowActivity(getClientFactory(), (WindowPlace)place);
		}
		ORIENTATION orientation = MGWT.getOrientation();
		if (orientation.equals(ORIENTATION.PORTRAIT)) {
			return super.getActivity(place);
		}
		return null;
	}
}
