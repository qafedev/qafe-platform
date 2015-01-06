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
package com.qualogy.qafe.gwt.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIVOCluster;
import com.qualogy.qafe.gwt.server.helper.ApplicationAssembler;
import com.qualogy.qafe.gwt.server.helper.ApplicationStore;
import com.qualogy.qafe.gwt.server.service.UIService;
import com.qualogy.qafe.util.ApplicationMappingUtil;
import com.qualogy.qafe.web.util.SessionContainer;

public class UIServiceImpl implements UIService {
	
	private Map<String, UIGVO> applicationMappingUIGVOs = new HashMap<String, UIGVO>();

	public boolean isValidXML(String xml) {
		return ApplicationMappingUtil.isValidXML(xml);
	}



	public UIGVO getUIFromXML(String xml,String sessionId,String windowSession,Locale locale) {

		ApplicationMapping applicationMapping = ApplicationMappingUtil.readFromXML(xml);
		UIGVO applicationMappingUIGVO = ApplicationAssembler.renderAll(applicationMapping, null,new SessionContainer(locale,null));
		applicationMappingUIGVO.setAppId("QAFEXMLLabs");
		ApplicationStore.getInstance().storeUI(applicationMappingUIGVO);
		return applicationMappingUIGVO;
	}

	public String getUIFromApplicationMapping(ApplicationMapping applicationMapping,String sessionId,String windowSession, Locale locale, Map<String,String> parameters) {
		UIGVO applicationMappingUIGVO = ApplicationAssembler.renderAll(applicationMapping, null,new SessionContainer(locale,parameters));
		applicationMappingUIGVO.setAppId("FromApplicationMapping");
		String uuid = ApplicationStore.getInstance().storeUI(applicationMappingUIGVO);
		ApplicationStore.getInstance().storeApplicationMapping(applicationMapping, uuid);
		return uuid;

	}

	public UIGVO getUIByUUID(String uuid) {
		UIGVO ui = ApplicationStore.getInstance().retrieveUI(uuid);
		return ui;

	}

	public UIGVO getUI(ApplicationIdentifier appId,String sessionId,String windowSession, Locale locale,Map<String,String> parameters) {
		return getUI(appId, null, null,sessionId,windowSession,locale,parameters);
	}

	public UIGVO getUI(ApplicationIdentifier appId, String applicationName, String location,String sessionId, String windowSession,Locale locale,Map<String,String> parameters) {
		ApplicationContext context = ApplicationCluster.getInstance().get(appId);
		ApplicationMapping applicationMapping = context.getApplicationMapping();
		String key = locale.getLanguage() + appId.toString();
		UIGVO applicationMappingUIGVO = ApplicationAssembler.renderAll(applicationMapping, context,new SessionContainer(locale,parameters));		
		if (applicationMappingUIGVO!=null){
			applicationMappingUIGVO.setAppId(key);
			ApplicationStore.getInstance().storeUI(applicationMappingUIGVO);
			ApplicationStore.getInstance().store(applicationMappingUIGVO.getAppId(), appId);
			applicationMappingUIGVO.setTitle(applicationName);
		}
		return applicationMappingUIGVO;
	}

	public ApplicationIdentifier getApplicationId(String uuid) {
		return (ApplicationIdentifier) ApplicationStore.getInstance().getApplicationId(uuid);
	}

	public UIGVO getSystemApplication(ApplicationContext context,String sessionId,String windowSession, Locale locale,Map<String,String> parameters) {
		UIGVO uigvo = null;
		if (context != null) {
			uigvo = getUI(context.getId(),sessionId,windowSession,locale,parameters);

			if (uigvo != null &&  context.getApplicationMapping() != null) {
				uigvo.setMenus(ApplicationAssembler.renderSystemMenu(context.getApplicationMapping(), context));
			}
		}
		return uigvo;

	}


	public UIVOCluster stripCluster(final UIVOCluster cluster) {
		UIVOCluster rc = null;
		if (cluster!=null){
			rc = new UIVOCluster();
			rc.setDebugMode(cluster.isDebugMode());
			rc.setShowLog(cluster.getShowLog());
			rc.setGlobalDateFormat(cluster.getGlobalDateFormat());
			rc.setReloadable(cluster.getReloadable());
			rc.setUseDockMode(cluster.getUseDockMode());
			rc.setSystemMenuApplication(cluster.getSystemMenuApplication());
			rc.setExternalProperties(cluster.getExternalProperties());
			rc.setMessages(cluster.getMessages());
			rc.setMessagesWithType(cluster.getMessagesWithType());
			rc.setClientSideEventEnabled(cluster.isClientSideEventEnabled());
			rc.setVos(cluster.getVos());
			
			if (!rc.isClientSideEventEnabled()) {
			    if (cluster.getVos()!=null){
	                UIGVO[] uiStripped = new UIGVO[cluster.getVos().length];
	                for (int i=0;i<cluster.getVos().length;i++){
	                    uiStripped[i] = cluster.getVos()[i].strip();
	                }
	                rc.setVos(uiStripped);
	            }    
			}
		}
		return rc;

	}



	public void clear() {
		ApplicationStore.getInstance().getGUIMap().clear();		
	}

}
