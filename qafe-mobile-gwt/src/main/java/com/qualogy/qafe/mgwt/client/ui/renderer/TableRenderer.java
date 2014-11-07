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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QTable;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TableCellGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TableGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TableRowGVO;

public class TableRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof TableGVO) {
			TableGVO tableGVO = (TableGVO)component;
			QTable table = new QTable();
			init(tableGVO, table, owner, uuid, parent, context, activity);
			widget = table;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		TableGVO tableGVO = (TableGVO)component;
		QTable table = (QTable)widget;
		renderChildren(tableGVO, table, uuid, parent, context, activity);
	}
	
	private void renderChildren(TableGVO component, QTable widget, String uuid, String parent, String context, AbstractActivity activity) {
		TableRowGVO[] rows = component.getRows();
		if (rows == null) {
			return;
		}
		for (int i=0; i<rows.length; i++) {
			TableRowGVO rowGVO = rows[i];
			TableCellGVO[] cells = rowGVO.getCells();
			if (cells == null) {
				continue;
			}
			int numColumns = cells.length;
			widget.setNumColumns(numColumns);
			for (int j=0; j<numColumns; j++) {
				TableCellGVO cellGVO = cells[j];
				ComponentGVO childGVO = cellGVO.getComponent();
				UIObject child = renderChildComponent(cellGVO.getComponent(), null, uuid, parent, context, activity);
				registerComponent(childGVO, child, null, parent, context);
				if (child instanceof Widget) {
					Widget childWidget = (Widget)child;
					widget.add(i, j, childWidget);
				}
			}
		}
	}
}
