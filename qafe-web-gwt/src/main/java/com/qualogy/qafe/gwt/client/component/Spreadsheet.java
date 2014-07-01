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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A two celled spreadsheet with cells named west and east.
 */
public class Spreadsheet extends FlexTable implements KeyPressHandler,KeyUpHandler,KeyDownHandler, EntryPoint {

	final SpreadsheetCell west=null;
	final SpreadsheetCell east=null;

	public Spreadsheet() {

	}

	public void onModuleLoad() {
		RootPanel.get().add(this);
	}



	public void onKeyPress(KeyPressEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyUp(KeyUpEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyDown(KeyDownEvent event) {
		if (((SpreadsheetCell) event.getSource()).getMode() == SpreadsheetCell.DISPLAY) {
			switch (event.getNativeKeyCode()) {
			case KeyCodes.KEY_LEFT:
				if (event.getSource() == west) {
					west.setFocus(false);
				} else {
					west.setFocus(true);
				}
				break;
			case KeyCodes.KEY_RIGHT:
				if (event.getSource() == east) {
					east.setFocus(false);
				} else {
					east.setFocus(true);
				}
				break;
			default:
				;
			}
		}
		
	}

}