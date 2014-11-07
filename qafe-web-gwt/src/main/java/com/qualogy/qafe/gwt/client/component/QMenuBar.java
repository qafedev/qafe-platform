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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class QMenuBar extends MenuBar {
	
	public QMenuBar() {
		super();
	
	}

	public QMenuBar(boolean arg0, MenuBarImages arg1) {
		super(arg0, arg1);
		
	}

	public QMenuBar(boolean arg0) {
		super(arg0);
	

	}

	public void removeMenu(MenuBar menuBar) { 
         MenuItem wrapper = null; 
         for (MenuItem menuItem : getItems()) {
					
                 if( menuItem.getSubMenu() == menuBar ){ 
                         wrapper = menuItem; 
                 } 
         } 
         if( null != wrapper ){ 
                 this.removeItem( wrapper ); 
         } 
 } 
}
