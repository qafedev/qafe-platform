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
package com.qualogy.qafe.gwt.client.ui.renderer.cell;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.improved.QCellTable;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;

public class ImprovedDatagridFactory {
	
		private ImprovedDatagridFactory(){}
		
		public static UIObject createPagingDataGrid(DataGridGVO gvo, String uuid, String parent) {
			
		
			QCellTable table = new QCellTable(gvo,uuid,parent);
			RendererHelper.fillIn(gvo, table, uuid, parent, gvo.getContext());
			FlowPanel fp = new FlowPanel();
			fp.add(table);
			
			if (gvo.getPageSize()!=null){
				if (gvo.getPageSize()>0){
					SimplePager pager = new SimplePager();
					pager.setDisplay(table);
					
					fp.add(pager);			
				}
			}
			
			UIObject uiObject = fp;
			return uiObject;
			
		}
			

}
