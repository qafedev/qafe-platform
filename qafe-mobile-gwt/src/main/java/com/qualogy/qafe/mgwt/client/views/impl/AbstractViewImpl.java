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
package com.qualogy.qafe.mgwt.client.views.impl;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.Dialogs;
import com.googlecode.mgwt.ui.client.theme.base.HeaderCss;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.qualogy.qafe.mgwt.client.ui.component.ComponentHelper;
import com.qualogy.qafe.mgwt.client.views.AbstractView;

public abstract class AbstractViewImpl extends Widget implements AbstractView {

	public static class MessageBox extends Label {
		
		private String className;
		
		public MessageBox() {
			HeaderCss headerCss = MGWTStyle.getTheme().getMGWTClientBundle().getHeaderCss();
			addStyleName(headerCss.headerPanel());
			className = getElement().getClassName();
		}
		
		private void reset() {
			getElement().setClassName(className);
			getElement().setAttribute("style", "text-align: center");
		}
		
		public void setMessage(String message) {
			reset();
			setText(message);
		}
	}

	protected LayoutPanel mainPanel;
	protected HeaderPanel headerPanel;
	protected HTML headerTitle;
	protected HeaderButton headerBackButton;
	private MessageBox messageBox;
	
	public AbstractViewImpl() {
		messageBox = new MessageBox();
		
		headerPanel = new HeaderPanel();
		mainPanel = new LayoutPanel();
		mainPanel.add(headerPanel);
		
		headerBackButton = new HeaderButton();
		headerBackButton.setBackButton(true);
		headerPanel.setLeftWidget(headerBackButton);

		headerTitle = new HTML();
		headerPanel.setCenterWidget(headerTitle);
	}
	
	@Override
	public void setTitle(String text) {
		headerTitle.setText(text);
	}
	
	@Override
	public HasTapHandlers getBackButton() {
		return headerBackButton;
	}

	@Override
	public void setBackButtonText(String text) {
		headerBackButton.setText(text);
	}
	
	@Override
	public void displayBackButton(boolean display) {
		if (display && !MGWT.getOsDetection().isPhone()) {
			headerBackButton.removeStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getUtilCss().portraitonly());	
		}
	}

	@Override
	public void setBusy(boolean busy) {
		// Implement this method if you want, 
		// otherwise it is handled by the mobile device
	}
	
	@Override
	public void alert(String title, String message) {
		Dialogs.alert(title, message, null);
	}

	@Override
	public void notify(String message, int delay, String styleClass, String[][] inlineStyles) {
		headerPanel.removeFromParent();
		messageBox.setMessage(message);
		ComponentHelper.setStyle(messageBox.getElement(), styleClass, inlineStyles);
		mainPanel.insert(messageBox, 0);
		new Timer(){
			@Override
			public void run() {
				messageBox.removeFromParent();
				mainPanel.insert(headerPanel, 0);
			}
		}.schedule(delay);
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	@Override
	public Element getElement() {
		return asWidget().getElement();
	}
}