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
package com.qualogy.qafe.gwt.server.ui.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.TableCell;
import com.qualogy.qafe.bind.presentation.component.TableRow;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TableCellGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TableRowGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class TableRowUIAssembler implements UIAssembler {

	public TableRowUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof TableRow) {
				TableRow tableRow = (TableRow)object;
				TableRowGVO voTemp  = new TableRowGVO();
				UIAssemblerHelper.copyFields(tableRow, currentWindow,voTemp,applicationMapping, context, ss);
				if (tableRow.getCells()!=null){
					int index=0;
					TableCellGVO[] cells = new TableCellGVO[tableRow.getCells().size()];
					
					for (TableCell cell: tableRow.getCells()) {
					
						cells[index] = (TableCellGVO)ComponentUIAssembler.convert(cell, currentWindow,applicationMapping,context, ss);
						index++;
					}
					voTemp.setCells(cells);
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "tableRow";
	}
}
