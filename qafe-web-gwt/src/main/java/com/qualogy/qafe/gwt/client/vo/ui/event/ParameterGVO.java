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
package com.qualogy.qafe.gwt.client.vo.ui.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ParameterGVO implements IsSerializable {

	private String name;
	private String value;
	private String reference;
    private String source;
    private List<ParameterGVO> placeHolders = new ArrayList<ParameterGVO>();
	
	public ParameterGVO(){}
	
	public ParameterGVO(String name, String value) {
		setName(name);
		setValue(value);
	}

	public ParameterGVO(String name, String reference, String source) {
	    setName(name);
	    setReference(reference);
	    setSource(source);
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    
    public List<ParameterGVO> getPlaceHolders() {
        return placeHolders;
    }

    
    public void setPlaceHolders(List<ParameterGVO> placeHolders) {
        this.placeHolders = placeHolders;
    }
    
    public void addPlaceHolder(ParameterGVO placeHolder){
        placeHolders.add(placeHolder);
    }
    
    
}