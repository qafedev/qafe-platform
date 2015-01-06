/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.server.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIVOCluster;

public interface UIService {
	
	UIGVO getUI(ApplicationIdentifier appId,String sessionId,String windowSession, Locale locale,Map<String,String> parameters);
	
    String getUIFromApplicationMapping(ApplicationMapping applicationMapping,String sessionId,String windowSession,Locale locale,Map<String,String>parameters);

	
	UIGVO getUIFromXML(String xml,String sessionId,String windowSession, Locale locale);
	
	UIGVO getUIByUUID(String uuid);
	
	ApplicationIdentifier getApplicationId(String uuid);

	boolean isValidXML(String xml);

	UIGVO getSystemApplication(ApplicationContext context,String sessionId,String windowSession, Locale locale,Map<String,String> parameters);
	
	void clear();

	UIVOCluster stripCluster(UIVOCluster cluster);
	
	
	
}
