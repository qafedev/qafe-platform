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
package com.qualogy.qafe.gwt.client.vo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UIVOCluster implements IsSerializable {

	private UIGVO[] vos = null;
	private Boolean debugMode = Boolean.FALSE;
	private Boolean clientSideEventEnabled = Boolean.FALSE;
	private Boolean showLog = Boolean.FALSE;
	private String globalDateFormat = TextFieldGVO.DEFAULT_DATE_FORMAT;
	private Boolean reloadable = Boolean.FALSE;
	private Boolean useDockMode= Boolean.FALSE;
	private Map<String,String> externalProperties = new HashMap<String, String>();
	private String licenseThresholdMessage;
	private UIGVO systemMenuApplication;
	private String css;
	private List<String> messages = new ArrayList<String>();
	private List<Map> messagesWithType = new ArrayList<Map>();
	
	public String getLicenseThresholdMessage() {
		return licenseThresholdMessage;
	}
	public void setLicenseThresholdMessage(String licenseThresholdMessage) {
		this.licenseThresholdMessage = licenseThresholdMessage;
	}
	public Map<String, String> getExternalProperties() {
		return externalProperties;
	}
	public void setExternalProperties(Map<String, String> externalProperties) {
		this.externalProperties = externalProperties;
	}
	public Boolean getUseDockMode() {
		return useDockMode;
	}
	public void setUseDockMode(Boolean useDockMode) {
		this.useDockMode = useDockMode;
	}
	public Boolean getReloadable() {
		return reloadable;
	}
	public void setReloadable(Boolean reloadable) {
		this.reloadable = reloadable;
	}
	
	public String getGlobalDateFormat() {
		return globalDateFormat;
	}
	public void setGlobalDateFormat(String globalDateFormat) {
		this.globalDateFormat = globalDateFormat;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public List<Map> getMessagesWithType() {
		return messagesWithType;
	}
	
	public void setMessagesWithType(List<Map> messagesWithType) {
		this.messagesWithType = messagesWithType;
	}
	
	public UIGVO getSystemMenuApplication() {
		return systemMenuApplication;
	}
	public void setSystemMenuApplication(UIGVO systemMenuApplication) {
		this.systemMenuApplication = systemMenuApplication;
	}
	public Boolean getShowLog() {
		return showLog;
	}
	public void setShowLog(Boolean showLog) {
		this.showLog = showLog;
	}
	public Boolean isDebugMode() {
		return debugMode;
	}
	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}
	public UIGVO[] getVos() {
		return vos;
	}
	public void setVos(UIGVO[] vos) {
		this.vos = vos;
	}
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
    public Boolean isClientSideEventEnabled() {
        return clientSideEventEnabled;
    }
    public void setClientSideEventEnabled(Boolean clientSideEventEnabled) {
        this.clientSideEventEnabled = clientSideEventEnabled;
    }
}