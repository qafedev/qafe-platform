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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TableCellGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TableGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TableRowGVO;

public class TableRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		ScrollTable uiObject = null;
		if (component != null) {
			if (component instanceof TableGVO) {
				TableGVO gvo = (TableGVO) component;
				// setup the table
				FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
				FixedWidthGrid dataTable = new FixedWidthGrid();
				//dataTable.setHoveringPolicy(SelectionGrid.HOVERING_POLICY_ROW);
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new ScrollTable(dataTable, headerTable) {
						@Override
						public void onBrowserEvent(Event event) {
							if (event.getTypeInt() == Event.ONCONTEXTMENU) {
								DOM.eventPreventDefault(event);
								applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
							}
							super.onBrowserEvent(event);
						}

						@Override
						protected void setElement(Element elem) {
							super.setElement(elem);
							sinkEvents(Event.ONCONTEXTMENU);
						}
					};
				} else {
					uiObject = new ScrollTable(dataTable, headerTable);
				}


				uiObject.setCellPadding(3);
				uiObject.setCellSpacing(1);
				uiObject.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
				
				// Header processing
				
				TableRowGVO header = gvo.getHeader();
				if (header != null) {
					TableCellGVO[] cells = header.getCells();
					if (cells != null) {

						for (int i = 0; i < cells.length; i++) {
							UIObject componentUIObject = renderChildComponent(
									cells[i].getComponent(), uuid,parent, context);
							if (componentUIObject != null
									&& componentUIObject instanceof Widget) {
								Widget w = (Widget) componentUIObject;
								headerTable.setWidget(0, i, w);
							}

						}
					}
				}
				// Row processing
				TableRowGVO[] rows = gvo.getRows();
				if (rows != null) {
					for (int i = 0; i < rows.length; i++) {
						int beforeRow  = dataTable.insertRow(i);
						TableCellGVO[] cells = rows[i].getCells();
						if (cells != null) {
							dataTable.resizeColumns(cells.length);
							for (int j = 0; j < cells.length; j++) {
								UIObject componentUIObject = renderChildComponent(
										cells[j].getComponent(), uuid,parent, context);
								if (componentUIObject != null
										&& componentUIObject instanceof Widget) {
									Widget w = (Widget) componentUIObject;
									
									dataTable.setWidget(beforeRow, j, w);
								}

							}
						}
					}
				}
				// Redraw the scroll table
				RendererHelper.fillIn(gvo, uiObject, uuid, parent, context);
				
				uiObject.redraw();
				
			}

		}

		return uiObject;
	}
}
