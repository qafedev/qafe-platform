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
package com.qualogy.qafe.bind.presentation.component;

/**
 * @author rjankie
 */
public class Slider extends Component implements HasVisibleText {

	private static final long serialVersionUID = -1234511365374942807L;
	
	public final static String SLIDER_ORIENTATION_HORIZONTAL 		= "horizontal";
	public final static String SLIDER_ORIENTATION_VERTICAL 			= "vertical";
	public final static int SLIDER_ORIENTATION_HORIZONTAL_ID 		= 1;
	public final static int SLIDER_ORIENTATION_VERTICAL_ID 			= 2;

	private String displayname;
	private String messageKey;
	private Integer minTicks;
	private Integer maxTicks;
	private Integer tickSize;
	private Integer tickLabels;
	private Integer value;
	private String orientation = SLIDER_ORIENTATION_HORIZONTAL;
	
	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
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
