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
package com.qualogy.qafe.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserAgentUtil {
	
	public static final String MOBILE_USER_AGENT_FILE ="MobileUserAgent.properties";

	private static Properties loadMobileUserAgents() {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = UserAgentUtil.class.getResourceAsStream(MOBILE_USER_AGENT_FILE);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static boolean isMobile(String userAgent) {
		if (userAgent != null) {
			Properties properties = loadMobileUserAgents();
			for (Object propValue : properties.values()) {
				if (propValue instanceof String) {
					String mobileUserAgent = (String)propValue;
					if (userAgent.indexOf(mobileUserAgent) > -1) {
						return true;
					}
				}
			}	
		}
		return false;
	}
}
