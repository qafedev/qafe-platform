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
package com.qualogy.qafe.gwt.client.factory;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.TextArea;
import com.qualogy.qafe.gwt.client.component.DialogComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.exception.GWTActivationException;
import com.qualogy.qafe.gwt.client.factory.async.UiByUUIDCallback;
import com.qualogy.qafe.gwt.client.service.RPCService;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIVOCluster;

public class MainFactoryActions {

	private static final String MESSAGE_SEND_EMAIL_TO_SUPPORT = "Please send an e-mail to the support team of QAFE (support@qafe.com)";
	private static final String MESSAGE_ERROR_REPORT_SEND_TO_SUPPORT = "Error report is sent to support team of QAFE";
	private static final String TITLE_MESSAGE_IS_SENT = "Message is sent";
	private static final String TITLE_NO_REPORT_SERVICE_AVAILABLE = "No report service available";

	private static UiByUUIDCallback uIByUUIDCallBack = null;
	private static AsyncCallback<?> flexCallBack = null;
	private static AsyncCallback<?> xmlCallBack = null;
	private static AsyncCallback<?> callback = null;
	private static AsyncCallback<?> multipleGVOCallBack = null;
	private static AsyncCallback<?> closingCallBack = null;
	private static AsyncCallback<?> removeCallBack = null;
	private static AsyncCallback<?> notificationCallBack = null;

	private static RPCServiceAsync rpcService = null;

	public static void getUIByUUID(String uuid, final String windowId) {
		UIGVO uiPre = ClientApplicationContext.getInstance()
				.getPrerenderedUIVO(uuid);

		if (uiPre == null) {
			ClientApplicationContext.getInstance().setBusy(true);
			RPCServiceAsync service = createService();
			if (uIByUUIDCallBack == null) {
				uIByUUIDCallBack = new UiByUUIDCallback();
			}
			uIByUUIDCallBack.setWindowId(windowId);
			service.getUIByUUID(uuid, uIByUUIDCallBack);
		} else {
			UIGVO ui = uiPre;
			WindowFactory.createWindow(ui, windowId);
			updateTime(ui);
		}
	}

	public static void processUIFromApplicationContext() {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createMultipleGVOCallBack();
		service.getUISFromApplicationContext(ClientApplicationContext
				.getInstance().getParameters(), callback);
	}

	public static void processReloadUIFromApplicationContext() {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createMultipleGVOCallBack();
		service.reload(ClientApplicationContext.getInstance().getParameters(),
				callback);
	}

	public static void processUIXmlFlex(String xml) {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createFlexCallBack();
		service.getUI(xml, callback);
	}

	public static void processUIXml(String xml) {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createCallBack();
		service.getUIVO(xml, callback);
	}

	private static AsyncCallback<?> createFlexCallBack() {
		if (flexCallBack == null) {
			flexCallBack = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (result != null) {
						String url = result.toString();
						MainFactory.createWindowWithUrl("Flex output", url,
								600, 400, true, true, 0, 0, false);
					}
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log(
							"Processing screen creation failed",
							"Error creating dynamic panel", true, true, caught);
					ClientApplicationContext.getInstance().setBusy(false);
					updateTime(null);
				}
			};
		}
		return flexCallBack;
	}

	private static AsyncCallback<?> createXmlCallBack(final TextArea textArea) {
		if (xmlCallBack == null) {
			xmlCallBack = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (result != null) {
						String xml = result.toString();
						textArea.setText(xml);
						DialogComponent
								.showDialog(
										"Conversion successful",
										"Please select from the menu of this window the \"GWT\" rendering option. <br>A \"Try Me\" menu item will appear in the top menu bar. <br><br> All the windows from this FMB will be listed. ",
										GenericDialogGVO.TYPE_INFO,
										"Please select from the menu of this window the \"GWT output\" rendering option. <br>A \"Try Me\" menu item will appear in the top menu bar. <br><br> All the windows from this FMB will be listed in this \"Try Me\" menu.  ",
										0, 0);
					}
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log(
							"Processing screen creation failed",
							"Error creating dynamic panel", true, true, caught);
					ClientApplicationContext.getInstance().setBusy(false);
					updateTime(null);
				}
			};
		}
		return xmlCallBack;
	}

	public static void remove(String uuid) {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createCallBack();
		service.removeUI(uuid, callback);
	}

	public static void processUIByUUID(String uuid, TextArea textArea) {
		ClientApplicationContext.getInstance().setBusy(true);
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createXmlCallBack(textArea);
		service.getXMLUIByUUID(uuid, callback);
	}

	private static AsyncCallback<?> createCallBack() {
		if (callback == null) {
			callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (result != null) {
						UIGVO ui = (UIGVO) result;
						WindowFactory.createWindow(ui);
						updateTime(ui);
					}
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log(
							"Processing screen creation failed",
							caught.getMessage(), true, true, caught);
					ClientApplicationContext.getInstance().setBusy(false);
					updateTime(null);
				}
			};
		}
		return callback;
	}

	private static AsyncCallback<?> createMultipleGVOCallBack() {
		if (multipleGVOCallBack == null) {
			multipleGVOCallBack = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (result != null) {
						try {
							ClientApplicationContext.getInstance().clearAll();
							UIVOCluster ui = (UIVOCluster) result;
							// store the ExternalProperties map in the
							// ClientApplicationContext
							ClientApplicationContext.getInstance()
									.setGlobalConfigurations(
											ui.getExternalProperties());
							ClientApplicationContext.getInstance()
									.clearPrerendering();
							WindowFactory.createMenu(ui
									.getSystemMenuApplication());
							if (ui.getVos() != null) {
								if (ui.getVos().length > 0) {
									for (int i = 0; i < ui.getVos().length; i++) {
										WindowFactory
												.createWindow(ui.getVos()[i]);
										updateTime(ui.getVos()[i]);
									}
									if (ui.getCss() != null
											&& ui.getCss().length() > 0) {
										if (isIE()) {
											addStyleSheet(ui.getCss());
										} else {
											Element ele = ClientApplicationContext
													.getInstance()
													.getStyleElement();
											if (ele != null) {
												ele.setAttribute("type",
														"text/css");
												ele.setInnerText(ui.getCss());
											}
										}
									}
								} else {
									ClientApplicationContext
											.getInstance()
											.log("Warning: Getting intial state results in no applications!",
													"There seems to be no application deployed. Please check if the following: <br>- web.xml (is there an application-config.xml), <br>- does the application-config.xml exist ? <br>- Are there applications defined in the application-config.xml and are the referenced files available ?"
															+ "", true, false,
													null);
								}
							}
							ClientApplicationContext.getInstance()
									.postInitialization(ui);
						} catch (Exception e) {
							ClientApplicationContext.getInstance().log(
									"Initialization failed",
									" While processing mainfactory functions, something went wrong <br>"
											+ e.getMessage(), true, true, e);
						}
					}
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public native boolean isIE() /*-{ 
												var ua = navigator.userAgent.toLowerCase(); 
												if ((ua.indexOf("msie 6.0") != -1) ||  (ua.indexOf("msie 7.0") != -1)) { 
												return true; 
												} 
												else {
												return false;
												}
												}-*/;

				public native void addStyleSheet(String css)/*-{ 
															var ss = $doc.createStyleSheet(); 
															ss.cssText = css; 
															}-*/;

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log(
							"Initialization failed",
							"Error creating dynamic panel<br><br>"
									+ caught.getMessage(), true, true,
							caught);
					
					ClientApplicationContext.getInstance().setBusy(false);
					updateTime(null);
				}
			};
		}
		return multipleGVOCallBack;
	}

	public static void updateTime(UIGVO ui) {
		String time = "";
		if (ui != null && ui.getTime() != null) {
			time = ui.getTime().toString();
		}
		ClientApplicationContext.getInstance().setLogText(
				"Execution time:" + time + " ms");
	}

	public static RPCServiceAsync createService() {
		if (rpcService == null) {
			rpcService = (RPCServiceAsync) GWT.create(RPCService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) rpcService;
			String moduleRelativeURL = GWT.getModuleBaseURL() + "/rpc.service";
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return rpcService;
	}

	private static AsyncCallback<?> createClosingCallBack() {
		if (closingCallBack == null) {
			closingCallBack = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {

				}

				public void onFailure(Throwable caught) {
				}
			};
		}
		return closingCallBack;
	}

	public static void clearOnServer() {
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createClosingCallBack();
		List<String> uuids = ClientApplicationContext.getInstance()
				.getWindowUUIDs();
		// service.clear(uuids, callback);
	}

	public static void removeUploadedFile(String appUUID, String windowId,
			String uploadUUID) {

		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createRemoveCallBack();
		service.removeFileFromLocalStore(appUUID, windowId, uploadUUID,
				callback);
	}

	public static void removeWindowsEventData(String windowSession, String windowId) {
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createRemoveCallBack();
		service.removeWindowsEventData(windowSession, windowId, callback);
	}

	private static AsyncCallback<?> createRemoveCallBack() {
		if (removeCallBack == null) {
			removeCallBack = new AsyncCallback<Object>() {
				public void onFailure(Throwable caught) {
					if (!ClientApplicationContext.getInstance().isClientSideEventEnabled()){
						ClientApplicationContext.getInstance().log(
								"Removing uploaded file failed",
								caught.getMessage(), true, true, caught);
					}
				}

				public void onSuccess(Object result) {
				}
			};
		}
		return removeCallBack;
	}

	public static void notify(String subject, String message) {
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createNotificationCallBack();
		service.notify(subject, message, callback);
	}

	private static AsyncCallback<?> createNotificationCallBack() {
		if (notificationCallBack == null) {
			notificationCallBack = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					ClientApplicationContext.getInstance().log(
							TITLE_MESSAGE_IS_SENT,
							MESSAGE_ERROR_REPORT_SEND_TO_SUPPORT, false, true,
							null);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log(
							TITLE_NO_REPORT_SERVICE_AVAILABLE,
							caught.getMessage() + ". "
									+ MESSAGE_SEND_EMAIL_TO_SUPPORT, true,
							true, caught);
				}
			};
		}
		return notificationCallBack;
	}

	public static void removeGloballyStoredData(String windowSession,
			String applicationId) {
		RPCServiceAsync service = createService();
		AsyncCallback<?> callback = createRemoveCallBack();
		service.removeGloballyStoredData(windowSession, applicationId, callback);
	}
}
