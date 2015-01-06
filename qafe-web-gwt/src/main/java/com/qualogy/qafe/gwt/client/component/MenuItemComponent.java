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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.MainFactory;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;

public class MenuItemComponent extends HorizontalPanel {

	private static final String DEFAULT_STYLE = "gwmdemo-MenuItem";

	public MenuItemComponent(final String menuItemName, final String uuid, final String windowId) {
		setSpacing(2);

		Hyperlink hyperlink = new Hyperlink(menuItemName, "history");

		hyperlink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (event.getSource() instanceof Widget) {
					Widget sender = (Widget) event.getSource();
					ClientApplicationContext.getInstance().removeWindow(windowId, null, uuid);
					MainFactoryActions.getUIByUUID(uuid, windowId);

					// try to close the disclosurePanel

					Widget parent = sender.getParent();
					boolean found = false;
					while (parent != null && !found) {
						if (parent instanceof DisclosurePanel) {
							DisclosurePanel disclosurePanel = (DisclosurePanel) (parent);
							disclosurePanel.setOpen(false);
							found = true;
						} else {
							parent = parent.getParent();
						}
					}

					if (sender.getParent() != null && sender.getParent().getParent() != null) {
						if (sender.getParent().getParent() instanceof DisclosurePanel) {
							DisclosurePanel disclosurePanel = (DisclosurePanel) (sender.getParent().getParent());
							disclosurePanel.setOpen(false);

						}
					}

				}

			}
		});
		add(hyperlink);
		setStyleName(DEFAULT_STYLE);

	}

	public MenuItemComponent(final String menuItemName, String history, String styleName, final String url) {

		setSpacing(2);

		Hyperlink hyperlink = new Hyperlink(menuItemName, "history");

		hyperlink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				MainFactory.createWindowWithUrl(menuItemName, url);

				postRender();

			}
		});
		add(hyperlink);
		setStyleName(styleName);
	}



	private void postRender() {

	}
}