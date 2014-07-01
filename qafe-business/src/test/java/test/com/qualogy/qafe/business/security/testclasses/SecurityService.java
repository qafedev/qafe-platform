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
package test.com.qualogy.qafe.business.security.testclasses;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.core.security.Authentication;
import com.qualogy.qafe.core.security.Restriction;


public class SecurityService implements Authentication {
	
	public final static String USERNAME = "qafe";
    public final static String PASSWORD = "qafe";
    public final static Integer RESULT_VALID_CODE = 0;
    public final static Integer RESULT_INVALID_CODE = 1;
    
    public class RestrictionImpl implements Restriction {

    	private String applicationId;
    	private String windowId;
    	private String componentId;
    	private String noAccess;
    	private String roleName = "guest";
    	private Integer roleOrder = 1;
    	
    	public RestrictionImpl(String applicationId, String windowId, String componentId) {
    		this(applicationId, windowId, componentId, "disabled");
    	}
    	
    	public RestrictionImpl(String applicationId, String windowId, String componentId, String noAccess) {
    		this.applicationId = applicationId;
    		this.windowId = windowId;
    		this.componentId = componentId;
    		this.noAccess = noAccess;
    	}
    	
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
		public String getNoAccess() {
			return noAccess;
		}
		public void setNoAccess(String noAccess) {
			this.noAccess = noAccess;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		public Integer getRoleOrder() {
			return roleOrder;
		}
		public void setRoleOrder(Integer roleOrder) {
			this.roleOrder = roleOrder;
		}
    }
    
    public Integer authenticate(String username, String password){
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            return RESULT_VALID_CODE;
        }
		return RESULT_INVALID_CODE;
    }

	public List<Restriction> getRestrictions(String username) {
		List<Restriction> restrictionList = new ArrayList<Restriction>();
		if (username.equals("qafe")) {
			Restriction restriction =  new RestrictionImpl("appIdA", "windowIdA", "componentId1");
			restrictionList.add(restriction);
			restriction =  new RestrictionImpl("appIdA", "windowIdA", "componentId2", "hidden");
			restrictionList.add(restriction);
		}
		return restrictionList;
	}
}
