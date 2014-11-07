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

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;

public class QShowPanel extends PopupPanel {
	
	private FlexTable innerPanel;
	
	@Override
	public void setAutoHideEnabled(boolean autoHide) {
		super.setAutoHideEnabled(autoHide);
		if (isAutoHideEnabled()) {
			if (innerPanel != null) {
				Widget widget = innerPanel.getWidget(1, 0);
				setWidget(widget);
			}
		} else if (innerPanel == null) {
			Widget widget = super.getWidget();
			setWidget(widget);
		}
	}
	
	@Override
	public void setWidget(Widget widget) {
		if (isAutoHideEnabled()) {
			innerPanel = null;
			super.setWidget(widget);
		} else {
			if (innerPanel == null) {
				innerPanel = createInnerPanel();
				super.setWidget(innerPanel);
			}
			innerPanel.setWidget(1, 0, widget);
		}
	}

	private FlexTable createInnerPanel() {
		FlexTable panel = new FlexTable();
		QLabel labelClose = new QLabel("X");
		labelClose.setStyleName(ComponentHelper.STYLE_SHOWPANEL_CLOSE);
		labelClose.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				hide();
			}
		});
		panel.setWidget(0, 1, labelClose);
		return panel;
	}
}