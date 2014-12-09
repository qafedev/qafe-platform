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
package com.qualogy.qafe.presentation.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.QAFEKeywords;

public class ApplicationClusterUtil {

		private static ApplicationIdentifier systemAppId = new ApplicationIdentifier(QAFEKeywords.SYSTEM_APP);
		public final static Logger logger = Logger.getLogger(ApplicationClusterUtil.class.getName());
		public static boolean isDebugMode(){
			return true;
		}
		
		public static boolean isSystemApplication(ApplicationIdentifier appId){
			if (appId!=null){
				if (appId.equals(systemAppId)){
					return true;
				}
			}
			return false;
		}
		
		public static boolean isSystemApplication(ApplicationContext appContext){
			if (appContext!=null){
				return isSystemApplication(appContext.getId());
			}
			return false;
		}
		
		public static String getGlobalDateFormat(){
			String globalDateFormat = null;
			if (ApplicationCluster.getInstance().getConfigurationItem(Configuration.DATE_FORMAT)!=null){
				globalDateFormat = ApplicationCluster.getInstance().getConfigurationItem(Configuration.DATE_FORMAT);
			}
			return globalDateFormat;
		}
		
		
		/**
		 * convert ExternalProperties to Map<String,String>
		 */
		public static Map<String,String> getExternalProperties(){
			Map<String,String> externalProperties = new HashMap<String,String>();
			if (ApplicationCluster.getInstance().getConfigurationItem(Configuration.EXTERNAL_PROPERTIES_FILE)!=null){
				Iterator itr = ApplicationCluster.getInstance().getGlobalConfiguration().keySetIterator();
				while(itr.hasNext()){
					String key = itr.next().toString();
					externalProperties.put(key, ApplicationCluster.getInstance().getConfigurationItem(key));
				}
			}
			return externalProperties;
		}


}
