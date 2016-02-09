/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.AbstractComponentRenderer.MessageBox;

public class QWindowPanel extends WindowPanel {

	private boolean managed = false;

	private boolean modified = false;
	
	private QRootPanel qRrootPanel = null;
	
	private String height;
	
	private String width;	
	
	public QWindowPanel(String caption) {
		super(caption);
		setAnimationEnabled(true);

	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isManaged() {
		return managed;
	}

	public void setManaged(boolean managed) {
		this.managed = managed;
	}

	public void addMinimizeButton() {
		final ImageButton minimizeBtn = new ImageButton(Caption.IMAGES.windowMinimize());
		minimizeBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				setWindowState(WindowState.MINIMIZED);

			}
		});
		getHeader().add(minimizeBtn, CaptionRegion.RIGHT);
	}

	public void addMaximizeButton() {
		final ImageButton maximizeBtn = new ImageButton(Caption.IMAGES.windowMaximize());
		maximizeBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (getWindowState() == WindowState.MAXIMIZED) {
					setWindowState(WindowState.NORMAL);
				} else {
					setWindowState(WindowState.MAXIMIZED);
				}

			}
		});

		getHeader().add(maximizeBtn, CaptionRegion.RIGHT);
	}

	public void setClosable(boolean booleanValue) {
		// getHeader().getWidget(index, CaptionRegion.LEFT);

	}
	public void showMessage(String message, Integer delay,String styleClass,String[][] styleProperties ){
		Widget w = getWidget();
		if (w!=null){
			if (w instanceof QRootPanel){
				QRootPanel qrp =(QRootPanel)w;
				qrp.showMessage(message, delay, styleClass, styleProperties, getContentWidth());
			}
		}
	
	}
	
	public void showMessage(String message){
			showMessage(message,3000, null, null);
	}

	public QRootPanel getQRootPanel() {
		return qRrootPanel;
	}

	public void setQRootPanel(QRootPanel rootPanel) {
		this.qRrootPanel = rootPanel;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		super.setHeight(height);
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		super.setWidth(width);
		this.width = width;
	}
}
