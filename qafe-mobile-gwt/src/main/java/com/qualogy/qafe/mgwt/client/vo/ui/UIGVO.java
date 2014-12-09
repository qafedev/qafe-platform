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
package com.qualogy.qafe.mgwt.client.vo.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;


/**
 * @author rjankie
 *
 */
public class UIGVO extends UIVO implements  IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8231082420446322633L;
	private Long time;
	private String rootMenu =null;
	private Map<String,EventListenerGVO> eventMap = new HashMap<String, EventListenerGVO>();
	
	public String getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(String rootMenu) {
		this.rootMenu = rootMenu;
	}

	private String appId =null;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	private Boolean debug = Boolean.FALSE;
	
	private String css;
	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	/**
	 * 
	 */
	private String title;
	
		
	private WindowGVO[] windows;
	
	private MenuItemGVO menus;
	

	
	
	public MenuItemGVO getMenus() {
		return menus;
	}

	public void setMenus(MenuItemGVO menus) {
		this.menus = menus;
	}

	public  String getTitle() {
		return title;
	}

	public  void setTitle(String title) {
		this.title = title;
	}

	public WindowGVO[] getWindows() {
		return windows;
	}

	public void setWindows(WindowGVO[] windows) {
		this.windows = windows;
	}


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public Map<String, EventListenerGVO> getEventMap() {
		return eventMap;
	}

	public void setEventMap(Map<String, EventListenerGVO> eventMap) {
		this.eventMap = eventMap;
	}

	public UIGVO strip() {
		UIGVO uiGVO = new UIGVO();
		uiGVO.setAppId(getAppId());
		uiGVO.setCss(getCss());
		uiGVO.setDebug(getDebug());
		uiGVO.setMenus(getMenus());
		uiGVO.setTime(getTime());
		uiGVO.setTitle(getTitle());
		uiGVO.setUuid(getUuid());
		uiGVO.setRootMenu(getRootMenu());
		uiGVO.setEventMap(getEventMap());
		if (getWindows()!=null){
			WindowGVO[] wStripped = new WindowGVO[getWindows().length];
			for (int i=0;i<getWindows().length;i++){
				wStripped[i] = getWindows()[i].strip();
			} 
			uiGVO.setWindows(wStripped);
			
		}
		return uiGVO;
	}
}
