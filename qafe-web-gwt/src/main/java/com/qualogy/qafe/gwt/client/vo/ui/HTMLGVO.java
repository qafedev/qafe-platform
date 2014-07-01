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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;


/**
 * @author rjankie 
 */

public  class HTMLGVO extends ComponentGVO{

	private String html;
	private Boolean escape= Boolean.FALSE;

	public Boolean getEscape() {
		return escape;
	}



	public void setEscape(Boolean escape) {
		this.escape = escape;
	}



	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.HTMLGVO";
	}



	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	
}
