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

import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class ShowPanelGVO extends BuiltInFunctionGVO {

	public static final String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO";
	
	private BuiltInComponentGVO builtInComponentGVO;
	private ComponentGVO src;
	private boolean autoHide;
	private boolean modal;
	private int left;
	private int top;
	private String position;
	
	public BuiltInComponentGVO getBuiltInComponentGVO() {
		return builtInComponentGVO;
	}

	public void setBuiltInComponentGVO(BuiltInComponentGVO builtInComponentGVO) {
		this.builtInComponentGVO = builtInComponentGVO;
	}

	public ComponentGVO getSrc() {
		return src;
	}

	public void setSrc(ComponentGVO src) {
		this.src = src;
	}

	public boolean isAutoHide() {
		return autoHide;
	}

	public void setAutoHide(boolean autoHide) {
		this.autoHide = autoHide;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}
}
