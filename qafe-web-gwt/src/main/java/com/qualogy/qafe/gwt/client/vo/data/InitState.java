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
package com.qualogy.qafe.gwt.client.vo.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InitState implements  IsSerializable {

	/**
	 * 
	 */
		private Boolean mdiMode=Boolean.TRUE;
	
	public InitState(){}
		
	public InitState(Boolean mdiMode, Boolean dockMode) {
		super();
		this.mdiMode = mdiMode;
		this.dockMode = dockMode;
	}
	public Boolean getMdiMode() {
		return mdiMode;
	}
	public void setMdiMode(Boolean mdiMode) {
		this.mdiMode = mdiMode;
	}
	public Boolean getDockMode() {
		return dockMode;
	}
	public void setDockMode(Boolean dockMode) {
		this.dockMode = dockMode;
	}
	private Boolean dockMode= Boolean.FALSE;
	
}
