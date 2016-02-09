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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.List;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class OpenWindowGVO extends BuiltInFunctionGVO {

    public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO";
    
    public static final String PLACEMENT_CASCADE = "cascade";
    public static final String PLACEMENT_CENTER_CASCADE = "center-cascade";
    public static final String PLACEMENT_TILED = "tiled";
    
    /* Introduced for client side */
    private ParameterGVO urlGVO;
    private ParameterGVO titleGVO;
    private ParameterGVO paramsGVO;
    private List<ParameterGVO> dataParamGVOList;
    
    private ParameterGVO windowGVO;
	private String window;
	private String url;
	private String title;
	private String params = "";
	private Boolean external=Boolean.FALSE;
	private List<DataParameterGVO> dataParameterGVOList;
	private String placement=PLACEMENT_CASCADE;
	
	public List<DataParameterGVO> getDataParameterGVOList() {
		return dataParameterGVOList;
	}

	public void setDataParameterGVOList(List<DataParameterGVO> dataParameterGVOList) {
		this.dataParameterGVOList = dataParameterGVOList;
	}

	public Boolean getExternal() {
		return external;
	}

	public void setExternal(Boolean external) {
		this.external = external;
	}
	
	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public ParameterGVO getWindowGVO() {
		return windowGVO;
	}

	public void setWindowGVO(ParameterGVO windowGVO) {
		this.windowGVO = windowGVO;
	}

    public ParameterGVO getUrlGVO() {
        return urlGVO;
    }

    public void setUrlGVO(ParameterGVO urlGVO) {
        this.urlGVO = urlGVO;
    }

    public ParameterGVO getTitleGVO() {
        return titleGVO;
    }

    public void setTitleGVO(ParameterGVO titleGVO) {
        this.titleGVO = titleGVO;
    }

    public ParameterGVO getParamsGVO() {
        return paramsGVO;
    }

    public void setParamsGVO(ParameterGVO paramsGVO) {
        this.paramsGVO = paramsGVO;
    }

    public List<ParameterGVO> getDataParamGVOList() {
        return dataParamGVOList;
    }

    public void setDataParamGVOList(List<ParameterGVO> dataParamGVOList) {
        this.dataParamGVOList = dataParamGVOList;
    }
    
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}