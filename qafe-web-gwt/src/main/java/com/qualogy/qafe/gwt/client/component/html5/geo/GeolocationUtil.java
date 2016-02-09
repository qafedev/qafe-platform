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
package com.qualogy.qafe.gwt.client.component.html5.geo;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.code.gwt.geolocation.client.Coordinates;
import com.google.code.gwt.geolocation.client.Geolocation;
import com.google.code.gwt.geolocation.client.Position;
import com.google.code.gwt.geolocation.client.PositionCallback;
import com.google.code.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Timer;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;

public class GeolocationUtil {

	public final static String LATITUDE = "lat";
	public final static String LONGITUDE = "lon";
	public final static String ACCURACY = "accuracy";
	public final static String ALTITUDE = "alt";
	public final static String ALTITUDE_ACCURACY = "alt-accuracy";
	public final static String HEADING = "heading";
	public final static String SPEED = "speed";
	public final static double NO_VALUE = -1.0;
	public static final String TIMESTAMP = "timestamp";

	private static GeolocationUtil instance = new GeolocationUtil();
	private Geolocation geo = null;
	private boolean isSupported = false;
	private boolean isInitialized = false;
	private PositionCallback myCallback = null;
	Logger logger = Logger.getLogger("GeoUtil");
	
	private int initialIntervalToRequest = 0;
	private int currentIntervalToRequest = 0;
	
	private boolean isEnabledFromConfig = false;

	public static GeolocationUtil getInstance() {
		return instance;
	}

	private GeolocationUtil() {
		isEnabledFromConfig = Boolean.valueOf(ClientApplicationContext.getInstance().getGlobalConfigurations().get("geo.location.enabled"));	
		if(isEnabledFromConfig) {
			if (!Geolocation.isSupported()) {
				log("Obtaining Geolocation FAILED! Geolocation API is not supported.");
			} else {
				isSupported = true;
			}
			geo = Geolocation.getGeolocation();
			if (geo == null) {
				log("Obtaining Geolocation FAILED! Object is null.");
	
			} else {
				isInitialized = true;
				myCallback = new MyPositionCallback();
				getInitialIntervalToRequest();
				currentIntervalToRequest=0;
			}
			obtainPosition(geo);
		}
	}

	private void getInitialIntervalToRequest() {
		String initialIntervalFromGlobalConfiguration = ClientApplicationContext.getInstance().getGlobalConfigurations().get("geo.location.request.threshold");
		if(initialIntervalFromGlobalConfiguration != null) {
			initialIntervalToRequest = Integer.parseInt(ClientApplicationContext.getInstance().getGlobalConfigurations().get("geo.location.request.threshold"));			
		} else {
			initialIntervalToRequest = 0;
		}
	}

	public void obtainPosition() {
		obtainPosition(geo);
	}

	private void obtainPosition(Geolocation geo) {
		if (isSupported && isInitialized) {
				geo.getCurrentPosition(myCallback);
		}
	}

	private void log(String message) {
		logger.info(message);
	}
	
	public Map<String, Double> getLocationDataMap() {
		return ((MyPositionCallback)myCallback).getLocationDataMap();
	}
	
	private class MyPositionCallback implements PositionCallback {
		private Map<String, Double> geoLocationMap = new HashMap<String, Double>();

		public void onFailure(PositionError error) {
			String message = "";
			switch (error.getCode()) {
			case PositionError.UNKNOWN_ERROR:
				message = "Unknown Error";
				break;
			case PositionError.PERMISSION_DENIED:
				message = "Permission Denied";
				break;
			case PositionError.POSITION_UNAVAILABLE:
				message = "Position Unavailable";
				break;
			case PositionError.TIMEOUT:
				message = "Time-out";
				break;
			default:
				message = "Unknown error code.";
			}
			log("Obtaining position FAILED! Message: '" + error.getMessage()
					+ "', code: " + error.getCode() + " (" + message + ")");
		}

		public void onSuccess(Position position) {
			Coordinates c = position.getCoords();
			geoLocationMap.put(LATITUDE, c.getLatitude());
			geoLocationMap.put(LONGITUDE, c.getLongitude());
			geoLocationMap.put(ACCURACY, c.getAccuracy());
			geoLocationMap.put(ALTITUDE, c.hasAltitude() ? c.getAltitude()
					: NO_VALUE);
			geoLocationMap.put(ALTITUDE_ACCURACY,
					c.hasAltitudeAccuracy() ? c.getAltitudeAccuracy()
							: NO_VALUE);
			geoLocationMap.put(HEADING, c.hasHeading() ? c.getHeading()
					: NO_VALUE);
			geoLocationMap.put(SPEED, c.hasSpeed() ? c.getSpeed() : NO_VALUE);
			log("In OnSuccess:" + geoLocationMap.toString());
		}

		public Map<String, Double> getLocationDataMap() {
			return geoLocationMap;
		}
	}

	public static EventDataGVO addLocationData(final EventDataGVO eventDataObject) {
		if (instance !=null && instance.isEnabledFromConfig){
			if (instance.currentIntervalToRequest %  instance.initialIntervalToRequest ==0 ) { 				
				instance.obtainPosition();
				instance.currentIntervalToRequest++;
				eventDataObject.setLocationDataMap(instance.getLocationDataMap()); 
	
				Timer timer = new Timer(){		
					@Override
					public void run() {
							eventDataObject.setLocationDataMap(instance.getLocationDataMap());
					}			
				};
				timer.schedule(250);
			} else {
				instance.currentIntervalToRequest++;
				eventDataObject.setLocationDataMap(instance.getLocationDataMap());
			}
		}
		return eventDataObject;
	}
}

