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
package com.qualogy.qafe.bind.commons.type;

import java.io.Serializable;
import java.util.List;

public class AdapterMapping implements Serializable, AdapterAttribute{
	
	private static final long serialVersionUID = 9042799788817562596L;
	
	protected String typeName;
	protected String id;
	protected List<AdapterAttribute> adapterAttributes;
	protected Boolean adaptAll;//adapt all indicates that also fields that are not specified are adapted
	protected String adaptName;
	
	/**
	 * for outbound adapting (out param for service), required since name of type is used for storage
	 */
	protected TypeDefinition outputType;
	/**
	 * for inbound adapting (in param for service)
	 */
	protected String outputClass;
	protected AdapterMapping parent;
	
	public String getOutputClass() {
		return outputClass;
	}

	public void setOutputClass(String outputClass) {
		this.outputClass = outputClass;
	}

	public boolean hasParent(){
		return parent!=null;
	}
	
	public TypeDefinition getOutputType() {
		return outputType;
	}

	public void setOutputType(TypeDefinition outputType) {
		this.outputType = outputType;
	}

	public AdapterMapping getParent() {
		return parent;
	}

	public void setParent(AdapterMapping parent) {
		this.parent = parent;
	}

	public void setAdaptAll(Boolean adaptAll) {
		this.adaptAll = adaptAll;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean adaptAll(){
		return adaptAll!=null && adaptAll.booleanValue();
	}
	public String getId() {
		return id;
	}

	public String getName() {
		return typeName;
	}

	public List<AdapterAttribute> getAdapterAttributes() {
		return adapterAttributes;
	}

	public void setAdapterAttributes(List<AdapterAttribute> adapterAttributes) {
		this.adapterAttributes = adapterAttributes;
	}

	public String getAdaptName() {
		return adaptName;
	}

	public void setAdaptName(String adaptName) {
		this.adaptName = adaptName;
	}
}