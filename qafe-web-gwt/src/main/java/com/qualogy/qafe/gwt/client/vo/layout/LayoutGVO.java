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
package com.qualogy.qafe.gwt.client.vo.layout;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class LayoutGVO implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2000246052619153576L;

	protected ComponentGVO[] components;

	public ComponentGVO[] getComponents() {
		return components;
	}

	public void setComponents(ComponentGVO[] components) {
		this.components = components;
	}

}
