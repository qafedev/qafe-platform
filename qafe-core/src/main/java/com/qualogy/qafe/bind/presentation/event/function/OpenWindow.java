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
package com.qualogy.qafe.bind.presentation.event.function;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.commons.type.Parameter;



public class OpenWindow extends BuiltInFunction implements PostProcessing{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5481076047995476L;

	public static final String PLACEMENT_CASCADE = "cascade";
	public static final String PLACEMENT_CENTER_CASCADE = "center-cascade";
	public static final String PLACEMENT_TILED = "tiled";
	
	protected Boolean external=Boolean.FALSE;
	
	public Boolean getExternal() {
		return external;
	}

	public void setExternal(Boolean external) {
		this.external = external;
	}

	protected Parameter window;
	protected String windowData;
	protected Parameter url;
	protected String urlData;
	protected Parameter title;
	protected String titleData;
	protected Parameter params;
	protected String paramsData;
	protected List<Parameter> dataParameters;
	
		
	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	protected String placement=PLACEMENT_CASCADE;
	
	
	public String getParamsData() {
		return paramsData;
	}

	public void setParamsData(String paramsData) {
		this.paramsData = paramsData;
	}

	public Parameter getParams() {
		return params;
	}

	public void setParams(Parameter params) {
		this.params = params;
	}
	
	public Parameter getWindow() {
		return window;
	}

	public void setWindow(Parameter window) {
		this.window = window;
	}

	public String getWindowData() {
		return windowData;
	}

	public void setWindowData(String windowData) {
		this.windowData = windowData;
	}

	public Parameter getUrl() {
		return url;
	}

	public void setUrl(Parameter url) {
		this.url = url;
	}

	public String getUrlData() {
		return urlData;
	}

	public void setUrlData(String urlData) {
		this.urlData = urlData;
	}

	public Parameter getTitle() {
		return title;
	}

	public void setTitle(Parameter title) {
		this.title = title;
	}

	public String getTitleData() {
		return titleData;
	}

	public void setTitleData(String titleData) {
		this.titleData = titleData;
	}

	public List<Parameter> getDataParameters() {
		return dataParameters;
	}

	public void setDataParameters(List<Parameter> dataParameters) {
		this.dataParameters = dataParameters;
	}

	public void postset(IUnmarshallingContext context){
		performPostProcessing();
	}
	
	public void performPostProcessing() {
		if ( (windowData!=null && windowData.length()==0 && urlData!=null && urlData.length()==0)   ) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "As well ref as url are invalid. They are empty");			
		}
	}

	
}
