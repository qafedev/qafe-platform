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
package com.qualogy.qafe.mgwt.client.vo.functions;

public class SetRestrictionGVO extends BuiltInFunctionGVO {

	private String applicationId;
	private String windowId;
	private String componentId;
	private String property;
	private Boolean propertyValue;
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getWindowId() {
		return windowId;
	}
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	public Boolean getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(Boolean propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	@Override
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.functions.SetRestrictionGVO";
	}
	
	@Override
	public int hashCode() {
		// Auto-generated code
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = (prime * result) + ((windowId == null) ? 0 : windowId.hashCode());
		result = (prime * result) + ((componentId == null) ? 0 : componentId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		// Auto-generated code
		if (this == obj) {
			return true;
		}	
		if (obj == null) {
			return false;
		}	
		if (getClass() != obj.getClass()) {
			return false;
		}	
		SetRestrictionGVO other = (SetRestrictionGVO)obj;
		if (applicationId == null) {
			if (other.applicationId != null) {
				return false;
			}	
		} else if (!applicationId.equals(other.applicationId)) {
			return false;
		}	
		if (windowId == null) {
			if (other.windowId != null) {
				return false;
			}	
		} else if (!windowId.equals(other.windowId)) {
			return false;
		}
		if (componentId == null) {
			if (other.componentId != null) {
				return false;
			}	
		} else if (!componentId.equals(other.componentId)) {
			return false;
		}
		return true;
	}
}