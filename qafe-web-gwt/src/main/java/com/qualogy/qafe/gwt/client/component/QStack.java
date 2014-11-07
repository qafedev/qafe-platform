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

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QStack extends HorizontalPanel implements HasVisible {
	
	private UIObject parentStackPanel; 
	private int stackIndex;
	private String stackText;
	
	public String getStackText() {
		return stackText;
	}

	public void setStackText(String stackText) {
		this.stackText = stackText;
	}

	public int getStackIndex() {
		return stackIndex;
	}

	public void setStackIndex(int stackIndex) {
		this.stackIndex = stackIndex;
	}

	public UIObject getParentStackPanel() {
		return parentStackPanel;
	}

	public void setParentStackPanel(UIObject parentStackPanel) {
		this.parentStackPanel = parentStackPanel;
	}

	
	public void processVisible(boolean visible) {
		if(parentStackPanel instanceof QStackPanel) {
			QStackPanel stackPanel = (QStackPanel) parentStackPanel;
			stackPanel.processVisible(this, visible);
					
		}
	}
	
	private int getIndexToInsertStack(StackPanel stackPanel) {
		int indexToInsert = getStackIndex();
		if(stackPanel.getWidgetCount() <= 0) {
			indexToInsert = 0 ;
		} else if(stackPanel.getWidgetCount() < indexToInsert) {
			indexToInsert = stackPanel.getWidgetCount();
		} else {
			for (int i = 0; i < stackPanel.getWidgetCount(); i++) {
				if(stackPanel.getWidget(i) instanceof QStack) {
					QStack stack = (QStack) stackPanel.getWidget(i);
					if(stack.getStackIndex() > indexToInsert) {
						indexToInsert = i;
						break;
					}
				}
			}
		}
		
		return indexToInsert;
	}
		
}
