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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.gen2.table.client.DefaultRowRenderer;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractRowView;

public class QDefaultRowRenderer<RowType> extends DefaultRowRenderer<RowType> {
	
	private String[] rowColors;
	
	public QDefaultRowRenderer(String[] rowColors) {
	    this.rowColors = rowColors;
	  }
	
	@Override
	public void renderRowValue(RowType rowValue, AbstractRowView<RowType> view) {
	    if (rowColors != null) {
	      int index = view.getRowIndex() % rowColors.length;
	      view.setStyleName(rowColors[index]);
	    }
	  }

}
