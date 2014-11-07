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
package com.qualogy.qafe.gwt.client.vo.ui;

public class LinearAxisGVO extends AxisComponentGVO {

	protected Integer minValue;
	protected Integer maxValue;
	protected Integer tickSize;
	
	public Integer getMinValue() {
		return minValue;
	}
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	public Integer getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	public Integer getTickSize() {
		return tickSize;
	}
	public void setTickSize(Integer tickSize) {
		this.tickSize = tickSize;
	}
	@Override
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.LinearAxisGVO";
	}
}
