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


import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.qualogy.qafe.gwt.client.vo.ui.ButtonGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

/**
 * A column that displays its contents with a {@link TextCell} and does not make
 * use of view data.
 *
 * @param <T> the row type
 */
public abstract class QButtonColumn<T> extends Column<T, String> {

  
  
  public QButtonColumn(String uuid,String context, String window, String component, String id,String parent,String tooltip,final DataGridColumnGVO column, ButtonGVO bc) {
	    super(new QButtonCell(uuid,context,window,component,id,parent,tooltip,column,bc));
	    
	  }
  
}