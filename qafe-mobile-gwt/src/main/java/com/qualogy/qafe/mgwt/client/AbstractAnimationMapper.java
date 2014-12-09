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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.qualogy.qafe.mgwt.client.places.AppPlace;
import com.qualogy.qafe.mgwt.client.places.AppsPlace;
import com.qualogy.qafe.mgwt.client.places.HomePlace;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;

public abstract class AbstractAnimationMapper implements AnimationMapper {

	@Override
	public Animation getAnimation(Place oldPlace, Place newPlace) {
		if (oldPlace == null) {
			return Animation.FADE;
		}
		if (oldPlace instanceof AppsPlace) {
			if (newPlace instanceof HomePlace) {
				return Animation.SLIDE_REVERSE;
			}
		}
		if (oldPlace instanceof AppPlace) {
			if (newPlace instanceof AppsPlace) {
				return Animation.SLIDE_REVERSE;
			}
		}
		if (oldPlace instanceof WindowPlace) {
			if (newPlace instanceof WindowPlace) {
				WindowPlace newWindowPlace = (WindowPlace)newPlace;
				if (newWindowPlace.getFromPlace() == oldPlace) {
					return Animation.SWAP;		
				}
			}
			return Animation.SLIDE_REVERSE;
		}
		return Animation.SLIDE;
	}
}
