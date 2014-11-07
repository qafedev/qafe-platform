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
package com.qualogy.qafe.mgwt.server.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;

public class ApplicationStore {

	
		public final static Logger logger = Logger.getLogger(ApplicationStore.class.getName());
		private  Map<String,UIGVO> guiMap = Collections.synchronizedMap( new HashMap<String,UIGVO>(17));
		private  Map<String,ApplicationIdentifier> appIdMap = Collections.synchronizedMap(new HashMap<String,ApplicationIdentifier>(17));
		private  Map<String,ApplicationMapping> applicationMappingMap =  Collections.synchronizedMap(new HashMap<String,ApplicationMapping>(17));
		
		private static ApplicationStore singleton = null; 
		
		public  Map<String,UIGVO> getGUIMap(){
			return guiMap;			
		}
		
		public Map<String,ApplicationMapping> getApplicationMappingMap(){
			return applicationMappingMap;
		}
		
		private ApplicationStore(){};
		
		public synchronized static ApplicationStore getInstance(){
			if (singleton==null){
				singleton = new ApplicationStore();
			}
			return singleton;
		}
		
		public  String storeUI(UIGVO ui){
			if (ui!=null){
				String appId = ui.getAppId();
				ui.setUuid(appId);
				Map<String,UIGVO> map = getGUIMap();
				if (!map.containsKey(appId)){
					map.put(appId, ui);				
				} 
				return appId;
			} else {
				return null;
			}
		
		}
		
		public  String storeApplicationMapping(ApplicationMapping am,String uuid){
			
			Map<String,ApplicationMapping> map = getApplicationMappingMap();
			if (!map.containsKey(uuid)){
				//ui.setUUID(uuid);
				map.put(uuid, am);				
			} else {
				logger.warning("This uuid "+  uuid + "\t is not unique (either being reused or crappy generator");
			
			}
			return uuid;
		}
		
		public  ApplicationMapping retrieveApplicationMapping(String uuid){
			Map<String,ApplicationMapping> map = getApplicationMappingMap();
			ApplicationMapping am = (ApplicationMapping)map.get(uuid);
			if (am==null){
				logger.warning("No applicationMapping found for UUID " + uuid +" in ApplicationMapping map" );
			
			}
			return am; 
		}
		
		
		public  UIGVO retrieveUI(String uuid){
			Map<String,UIGVO> map = getGUIMap();
			UIGVO ui = (UIGVO)map.get(uuid);
			if (ui==null){
				logger.warning("No UI found for UUID " + uuid);
			}
			return ui; 
		}
		
		public void store(String uuid, ApplicationIdentifier appId){
			Map<String,ApplicationIdentifier> map = getAppIdMap();
			if (!map.containsKey(uuid)){
				map.put(uuid,appId);
			} else {
				logger.warning("This uuid "+  uuid + "\t is not unique (either being reused or crappy generator");
			}
		}
		
		
		
		
		public ApplicationIdentifier getApplicationId(String uuid){
			ApplicationIdentifier appId=null;
			Map<String,ApplicationIdentifier> map = getAppIdMap();
			if (map.containsKey(uuid)){
				appId = ((ApplicationIdentifier)(map.get(uuid))); 
			}			
			return appId;
		}
		
		
		
		public Map<String,ApplicationIdentifier> getAppIdMap() {
			return appIdMap;
		}

//		public void clear(List<String> uuids) {
//			if (uuids!=null){
//				Map<String,UIGVO> map = getGUIMap();
//				Map<String,ApplicationMapping> appMappingMap = getApplicationMappingMap();
//				logger.debug("BEFORE: guiMap size=" + map.size() + ", appmap size="+appMappingMap.size());
//				for (String string : uuids) {
//					if (map.containsKey(string)){
//						map.remove(string);
//					}
//					if (appMappingMap.containsKey(string)){
//						appMappingMap.remove(string);
//					}
//					presentationService.clearApplicationLocalStore(string);
//				}
//				
//				logger.debug("AFTER:  guiMap size=" + map.size() + ", appmap size="+appMappingMap.size());
//			}
//		}
		
	
}

