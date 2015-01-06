/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBarButtonCss;
import com.googlecode.mgwt.ui.client.widget.base.ButtonBase;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class QToolbarItem extends ButtonBase {

	public class IconHandler {
		public void setIcons(Element element, Image icon, Image highlight, boolean active) {
			if (icon == null) {
				return;
			}
			String iconSize = icon.getWidth() + QAMLConstants.UNIT + " " + icon.getHeight() + QAMLConstants.UNIT;
			if (!active) {
				element.getStyle().setBackgroundImage("url(" + icon.getUrl() + ")");
				if (MGWT.getOsDetection().isRetina() || MGWT.getOsDetection().isIPadRetina()) {
					element.getStyle().setProperty("backgroundSize", iconSize);
				}
			} else if (highlight != null) {
				element.getStyle().setBackgroundImage("url(" + highlight.getUrl() + "), url(" + icon.getUrl() + ")");
				if (MGWT.getOsDetection().isRetina() || MGWT.getOsDetection().isIPadRetina()) {
					String higlightSize = highlight.getWidth() + QAMLConstants.UNIT + " " + highlight.getHeight() + QAMLConstants.UNIT;
					element.getStyle().setProperty("backgroundSize", higlightSize +", " + iconSize);
				}
			}
		}
	}
	
	private IconHandler iconHandler = new IconHandler();
	
	public QToolbarItem(String iconURL) {
		this(MGWTStyle.getTheme().getMGWTClientBundle().getButtonBarButtonCss(), new Image(iconURL), new Image(MGWTStyle.getTheme().getMGWTClientBundle().getButtonBarHighlightImage()));
	}
	
	public QToolbarItem(ButtonBarButtonCss css, Image icon, Image highlight) {
		super(css);
		initialize(css, icon, highlight);
	}
	
	private void initialize(ButtonBarButtonCss css, final Image icon, final Image highlight) {
		iconHandler.setIcons(getElement(), icon, highlight, false);
		addStyleName(css.barButton());
		addTouchHandler(new TouchHandler() {
			@Override
			public void onTouchCanceled(TouchCancelEvent event) {
				iconHandler.setIcons(getElement(), icon, highlight, false);
			}

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				iconHandler.setIcons(getElement(), icon, highlight, false);
			}

			@Override
			public void onTouchMove(TouchMoveEvent event) {
			}

			@Override
			public void onTouchStart(TouchStartEvent event) {
				iconHandler.setIcons(getElement(), icon, highlight, true);
			}
		});
	}
}
