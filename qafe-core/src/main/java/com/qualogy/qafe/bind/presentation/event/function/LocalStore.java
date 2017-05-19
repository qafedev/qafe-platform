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

import java.util.logging.Logger;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;

/**
 * Builtinfunction for operations on the localstore
 * @author rjankie
 */
public class LocalStore extends BuiltInFunction {

	private static final long serialVersionUID = -8465079416432637760L;
	public final static Logger logger = Logger.getLogger(LocalStore.class.getName());
	protected Parameter parameter;

	public final static String ACTION_ADD = "add";
	public final static String ACTION_DELETE = "delete";
	public final static String ACTION_SET="set";
	
	// Only window level
	//public final static String SCOPE_LOCAL = "local";
	// Application level
	//public final static String SCOPE_GOBAL = "global";
	
	protected String action=ACTION_SET;
	
	protected String field;
	
	protected String target = Reference.SOURCE_DATASTORE_ID;
	
	//protected String scope = SCOPE_LOCAL;
	
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private Object dataObject;

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
		logger.fine("LocalStore :"+ (dataObject!=null ? dataObject.toString(): "null"));
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	/*public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}*/
}
