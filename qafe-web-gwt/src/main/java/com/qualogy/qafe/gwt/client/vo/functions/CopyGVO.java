/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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


public class CopyGVO extends BuiltInFunctionGVO  {

	public static final String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.CopyGVO";

	protected String from;

	protected String to;
	
	protected BuiltInComponentGVO fromGVO;
	protected BuiltInComponentGVO toGVO;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BuiltInComponentGVO getFromGVO() {
		return fromGVO;
	}

	public void setFromGVO(BuiltInComponentGVO fromGVO) {
		this.fromGVO = fromGVO;
	}

	public BuiltInComponentGVO getToGVO() {
		return toGVO;
	}

	public void setToGVO(BuiltInComponentGVO toGVO) {
		this.toGVO = toGVO;
	}

	public String getClassName() {
		return CLASS_NAME;
	}	
}
