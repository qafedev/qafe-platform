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
package com.qualogy.qafe.web.util;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.core.application.ApplicationCluster;

public class MessageUtil {
	
    private static final Logger LOG = Logger.getLogger(MessageUtil.class.getName());
    
	public static String hostUrl = ApplicationCluster.getInstance().getConfigurationItem(Configuration.WEBSERVICE_MAIL_URL);
	
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static void notifyMessage(String subject, String message, HttpServletRequest httpServletRequest) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (httpServletRequest != null) {
			buildMessageMap(map, httpServletRequest);
		}
		
		// HERE YOU CAN IMPLEMENT HOW NOTIFICATIONS NEEDS TO BE HANDLED. EG; SEND MAIL
	}
	
	private static void buildMessageMap(Map<String, String> map, HttpServletRequest httpServletRequest) {
		Enumeration headerNames = httpServletRequest.getHeaderNames();
		boolean foundProxy = false;
		while (headerNames.hasMoreElements()) {
			Object name = headerNames.nextElement();
			if (name != null) {
				if ("x-forwarded-for".equalsIgnoreCase(name.toString())) {
					map.put("filename", httpServletRequest.getHeader((String) name));
					foundProxy = true;
				}
				map.put("Header - " + name, httpServletRequest.getHeader((String) name));
			}
		}
		map.put("User-Agent", httpServletRequest.getHeader("User-Agent"));
		map.put("ServletRemoteAddr", httpServletRequest.getRemoteAddr());
		map.put("Remote Host", httpServletRequest.getRemoteHost());
		if (!foundProxy) {
			map.put("filename", httpServletRequest.getRemoteHost());
		}
		map.put("Remote User", httpServletRequest.getRemoteUser());
		map.put("Protocol", httpServletRequest.getProtocol());
		map.put("Server Name", httpServletRequest.getServerName());
		map.put("Server Port", httpServletRequest.getServerPort() + "");
		map.put("Request URL", httpServletRequest.getRequestURL().toString());
		map.put("Request URL", httpServletRequest.getRequestURL().toString());
	}
	
	/*private static void buildMessageMap(Map<String, String> map, ClientApplication clientApplication) {
		map.put("Application Name", clientApplication.getApplicationName());
		map.put("Application Version", clientApplication.getApplicationVersion());
		map.put("Installed License Info", licenseContentToString(clientApplication.getInstalledLicenseContent()));
	}*/
	
	/*public static String licenseContentToString(LicenseContentVO licenseContentVO) {
		StringBuilder strB = new StringBuilder();
		int durationInMin = Integer.parseInt(licenseContentVO.getLicenseDuration());
		strB.append("################### LICENSE CONTENT Qualogy QAFE (c) ###################\n");
		strB.append("            Name: " + licenseContentVO.getFirstName() + "\n");
		strB.append("        LastName: " + licenseContentVO.getLastName() + "\n");
		strB.append("           Email: " + licenseContentVO.getEmail() + "\n");
		strB.append("        BirthDay: " + licenseContentVO.getBirthDay() + "\n");
		strB.append("            City: " + licenseContentVO.getCity() + "\n");
		strB.append("           State: " + licenseContentVO.getState() + "\n");
		strB.append("         Country: " + licenseContentVO.getCountry() + "\n");
		strB.append("    Organisation: " + licenseContentVO.getOrganisation() + "\n");
		strB.append("OrganisationUnit: " + licenseContentVO.getOrganisationUnit() + "\n");
		strB.append("         AppName: " + licenseContentVO.getAppName() + "\n");
		strB.append("      AppVersion: " + licenseContentVO.getAppVersion() + "\n");
		strB.append("      IssuedDate: " + licenseContentVO.getIssued() + "\n");
		strB.append("       ExpireDat: " + licenseContentVO.getLicenseExpireDate() + "\n");
		strB.append("        Duration: " + durationInMin + "(min)" + " or " + durationInMin/(60) + "(hours)" + " or " + durationInMin/(60 * 24) + "(days)" + " or " + durationInMin/(60 * 24 * 365.25) + "(years)" + "\n");
		strB.append("           extra: " + licenseContentVO.getLicenseExtraCriteria() + "\n");
		strB.append("          issuer: " + licenseContentVO.getIssuer() + "\n");
		strB.append("########################################################################\n");
		return strB.toString();
	}*/
}
