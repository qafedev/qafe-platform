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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.qualogy.qafe.mgwt.client.activities.AppActivity;
import com.qualogy.qafe.mgwt.client.activities.AppsActivity;
import com.qualogy.qafe.mgwt.client.activities.HomeActivity;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.places.AppPlace;
import com.qualogy.qafe.mgwt.client.places.AppsPlace;
import com.qualogy.qafe.mgwt.client.places.HomePlace;

public abstract class AbstractActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public AbstractActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof HomePlace) {
			return new HomeActivity(getClientFactory(), (HomePlace)place);
		}
		if (place instanceof AppsPlace) {
			return new AppsActivity(getClientFactory(), (AppsPlace)place);
		}
		if (place instanceof AppPlace) {
			return new AppActivity(getClientFactory(), (AppPlace)place);
		}
		return null;
	}

	protected ClientFactory getClientFactory() {
		return clientFactory;
	}
}
