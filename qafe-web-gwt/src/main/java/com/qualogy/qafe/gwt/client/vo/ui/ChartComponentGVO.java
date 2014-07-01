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

import java.util.List;

public abstract class ChartComponentGVO extends ComponentGVO implements HasVisibleTextI, HasComponentsI  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2159914480400539269L;
	public static final String LEGEND_TOP = "top";
	public static final String LEGEND_LEFT = "left";
	public static final String LEGEND_BOTTOM = "bottom";
	public static final String LEGEND_RIGHT = "right";
	public static final String LEGEND_NONE = "none";
	
	protected CategoryAxisGVO categoryAxis;
	protected List<ChartItemGVO> chartItems;
	protected String displayname;
	protected String messageKey;
	protected String legend = LEGEND_RIGHT;

	public ComponentGVO[] getComponents() {
		return getChartItems().toArray(new ComponentGVO[]{});
	}
	
	public CategoryAxisGVO getCategoryAxis() {
		return categoryAxis;
	}
	public void setCategoryAxis(CategoryAxisGVO categoryAxis) {
		this.categoryAxis = categoryAxis;
	}
	public List<ChartItemGVO> getChartItems() {
		return chartItems;
	}
	public void setChartItems(List<ChartItemGVO> chartItems) {
		this.chartItems = chartItems;
	}
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
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
}
