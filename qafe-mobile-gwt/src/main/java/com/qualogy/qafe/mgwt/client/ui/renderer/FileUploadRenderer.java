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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.FileUploadGVO;

public class FileUploadRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		HorizontalPanel uiObject = null;
		final String finalParent = parent;
		if (component != null) {
			if (component instanceof FileUploadGVO) {
				if (component.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					uiObject = new HorizontalPanel() {
						@Override
						public void onBrowserEvent(Event event) {
							if (event.getTypeInt() == Event.ONCONTEXTMENU) {
								DOM.eventPreventDefault(event);
								applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
							}
							super.onBrowserEvent(event);
						}

						@Override
						protected void setElement(Element elem) {
							super.setElement(elem);
							sinkEvents(Event.ONCONTEXTMENU);
						}
					};
				} else {
					uiObject = new HorizontalPanel();
				}
				final FormPanel formPanel = new FormPanel();
				formPanel.setAction(GWT.getModuleBaseURL() + "/rpc.upload");
				formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
				formPanel.setMethod(FormPanel.METHOD_POST);

				FileUpload fileUploadComponent = new FileUpload();
				fileUploadComponent.setName("uploadElement");

				Button uploadButtonComponent = new Button("Upload");
				uploadButtonComponent.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						formPanel.submit();
					}
				});

				Hidden appId = new Hidden();
				appId.setName("APPUUID=" + uuid);
				final String appID = uuid;
				
				Hidden windowId = new Hidden();
				windowId.setName("WINDOWID=" + parent);

				Grid gridPanel = new Grid(2, 4);
				gridPanel.setWidget(0, 0, fileUploadComponent);
				gridPanel.setWidget(0, 1, uploadButtonComponent);
				gridPanel.setWidget(0, 2, appId);
				gridPanel.setWidget(0, 3, windowId);
				formPanel.add(gridPanel);

				RendererHelper.fillIn(component, formPanel, uuid, parent, context);
				formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

					public void onSubmitComplete(SubmitCompleteEvent event) {
						String uuId = event.getResults();
						boolean success = false;
						if (uuId != null && uuId.indexOf("=") > 0) {
							uuId = uuId.substring(uuId.indexOf("=") + 1);
							success = true;
						}
						if (formPanel instanceof HasWidgets) {
							HasWidgets hasWidgets = (HasWidgets) formPanel;
							Iterator<Widget> itr = hasWidgets.iterator();
							while (itr.hasNext()) {
								Widget widget = itr.next();
								if (widget instanceof Grid) {
									Grid gridPanel = (Grid) widget;
									final FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
									final Button uploadButton = (Button) gridPanel.getWidget(0, 1);
									if (success) {
										fileUpload.setVisible(false);
										uploadButton.setVisible(false);
										String fileName = event.getResults().substring(0, event.getResults().indexOf("#"));
										final String viewUrl = GWT.getModuleBaseURL() + "/rpc.view?APPUUID=" + appID + "&WINDOWID=" + finalParent + "&VIEW=" + uuId;
										final Label fileNameLabel = new Label(fileName);
										DOM.setStyleAttribute(fileNameLabel.getElement(), "textDecoration", "underline");
										DOM.setStyleAttribute(fileNameLabel.getElement(), "cursor", "hand");
										fileNameLabel.addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {
												Window.open(viewUrl, "UploadedFile", null);
											}
										});
										fileNameLabel.setVisible(true);

										final Label removeLabel = new Label("Remove");
										removeLabel.setVisible(true);
										final String finalUuId = uuId;

										DOM.setStyleAttribute(removeLabel.getElement(), "textDecoration", "underline");
										DOM.setStyleAttribute(removeLabel.getElement(), "cursor", "hand");

										removeLabel.addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {
												MainFactoryActions.removeUploadedFile(appID, finalParent, finalUuId);
												DOM.setElementAttribute(fileUpload.getElement(), "fu-uuid", "");
												fileNameLabel.setVisible(false);
												removeLabel.setVisible(false);
												fileUpload.setVisible(true);
												uploadButton.setVisible(true);
											}
										});
										gridPanel.setWidget(1, 0, fileNameLabel);
										gridPanel.setWidget(1, 1, removeLabel);

										DOM.setElementAttribute(fileUpload.getElement(), "fu-uuid", uuId);
										CallbackHandler.createCallBack(formPanel, "onfinish", null, null);
									} else {
										Hyperlink fileNameLabel = (Hyperlink) gridPanel.getWidget(1, 0);
										fileNameLabel.setText("Uploading unsuccessfull.");
										fileNameLabel.setVisible(true);
									}
								}
							}
						}
					}
				});
				uiObject.add(formPanel);
			}
		}
		return uiObject;
	}
}
