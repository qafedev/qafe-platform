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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * @author rjankie
 */
public class SliderGVO extends ComponentGVO implements IsSerializable{

	private static final long serialVersionUID = -7374053729836106551L;

	private String displayname;
	private Integer minTicks;
	private Integer maxTicks;
	private Integer tickSize;
	private Integer tickLabels;
	private Integer value;
	private String orientation;

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.SliderGVO";
	}

	public Integer getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(Integer maxTicks) {
		this.maxTicks = maxTicks;
	}

	public Integer getMinTicks() {
		return minTicks;
	}

	public void setMinTicks(Integer minTicks) {
		this.minTicks = minTicks;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getTickSize() {
		return tickSize;
	}

	public void setTickSize(Integer tickSize) {
		this.tickSize = tickSize;
	}

	public Integer getTickLabels() {
		return tickLabels;
	}

	public void setTickLabels(Integer tickLabels) {
		this.tickLabels = tickLabels;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
