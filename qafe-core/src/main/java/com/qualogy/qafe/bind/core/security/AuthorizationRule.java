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
package com.qualogy.qafe.bind.core.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.core.pattern.IdPattern;

public class AuthorizationRule implements Serializable, PostProcessing{
	
	private static final long serialVersionUID = 6959001625714330089L;
	
	public final static String ACCESS_RULE_IF_ALL_GRANTED = "if-all-granted";
	public final static String ACCESS_RULE_IF_NONE_GRANTED = "if-none-granted";
	public final static String ACCESS_RULE_IF_ANY_GRANTED = "if-any-granted";
	
	public final static String DEFAULT_ACCESS_RULE_ = ACCESS_RULE_IF_ANY_GRANTED;
	
	public final static char ROLE_SEPARATOR = ',';
	
	/**
	 * how should the authorized group be interpreted  
	 */
	protected String access = DEFAULT_ACCESS_RULE_;
	
	/**
	 * to who does this rule apply
	 */
	protected String authorize;
	private String[] authorizedRoles;//postprocessing result of authorize
	
	protected String whenNoAccess;
	
	protected List<IdPattern> idPatterns;
	
	public void performPostProcessing() {
		if(authorize!=null){
			authorize = StringUtils.deleteWhitespace(authorize);
			authorizedRoles = StringUtils.split(authorize, ROLE_SEPARATOR);
		}
	}
	
	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public String[] getAuthorizedRoles() {
		return authorizedRoles;
	}

	public String getAccess() {
		return access;
	}


	public List<IdPattern> getIdPatterns() {
		return idPatterns;
	}

	@SuppressWarnings("unchecked")
	public List<IdPattern> getFilteredAndPrioritizedPatterns(Class patternSubClass) {
		List<IdPattern> filtered = new ArrayList<IdPattern>();
		for (Iterator iter = idPatterns.iterator(); iter.hasNext();) {
			IdPattern pattern = (IdPattern) iter.next();
			if(patternSubClass.isInstance(pattern)){
				filtered.add(pattern);
			}
		}
		
		Collections.sort(filtered);
		
		return filtered;
	}

	public String getWhenNoAccess() {
		return whenNoAccess;
	}
	
}
