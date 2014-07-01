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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.WidgetList;

public class QTable extends WidgetList {
	
	private Map<Integer,QLayoutBorder> rowContainers = new HashMap<Integer,QLayoutBorder>();
	private Map<Integer,Widget> rowWestRegions = new HashMap<Integer,Widget>();
	private Map<Integer,Widget> rowEastRegions = new HashMap<Integer,Widget>();
	
	private int numColumns;
	
	public QTable() {
		setRound(true);
	}
	
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public void add(int row, int column, Widget widget) {
		QLayoutBorder container = rowContainers.get(row);
		if (container == null) {
			container = createContainer();
			rowContainers.put(row, container);

			QLayoutHorizontal westRegion = createRegion(container, DockPanel.WEST);
			rowWestRegions.put(row, westRegion);
			
			QLayoutHorizontal eastRegion = createRegion(container, DockPanel.EAST);
			rowEastRegions.put(row, eastRegion);
			
			add(container);
		}
		if ((column == 0) || (column < Math.round(numColumns/2d))) {
			QLayoutHorizontal westRegion = (QLayoutHorizontal)rowWestRegions.get(row);
			westRegion.add(widget);
		} else {
			QLayoutHorizontal eastRegion = (QLayoutHorizontal)rowEastRegions.get(row);
			eastRegion.add(widget);
		}		
	}
	
	private QLayoutBorder createContainer() {
		QLayoutBorder container = new QLayoutBorder();
		container.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		return container;
	}
	
	private QLayoutHorizontal createRegion(QLayoutBorder container, DockLayoutConstant direction) {
		QLayoutHorizontal region = new QLayoutHorizontal();
		region.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		region.setSpacing(2);
		container.add(region, direction);
		container.setCellWidth(region, "100%");
		return region;
	}
	
	@Override
	public void clear() {
		rowWestRegions.clear();
		rowEastRegions.clear();
		rowContainers.clear();
		super.clear();
	}
}