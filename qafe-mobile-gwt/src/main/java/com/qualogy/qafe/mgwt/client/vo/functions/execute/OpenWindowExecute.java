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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class OpenWindowExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof OpenWindowGVO) {
			OpenWindowGVO openWindowGVO = (OpenWindowGVO)builtInFunctionGVO;
			openWindow(openWindowGVO, activity);
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void openWindow(OpenWindowGVO openWindowGVO, AbstractActivity activity) {
		String windowId = openWindowGVO.getWindow();
		String context = openWindowGVO.getContext();
		AbstractPlace currentPlace = resolveCurrentPlace(windowId, context, activity);
		AbstractPlace toPlace = new WindowPlace(windowId, context, currentPlace);
		activity.goTo(toPlace);
	}
	
	private AbstractPlace resolveCurrentPlace(String windowId, String context, AbstractActivity activity) {
		if (activity instanceof WindowActivity) {
			WindowActivity windowActivity = (WindowActivity)activity;
			WindowPlace currentPlace = windowActivity.getPlace();
			if (currentPlace.getFromPlace() instanceof WindowPlace) {
				WindowPlace fromPlace = (WindowPlace)currentPlace.getFromPlace();
				String fromWindowId = fromPlace.getId();
				String fromContex = fromPlace.getContext();
				if (fromWindowId.equals(windowId) && fromContex.equals(context)) {
					currentPlace = (WindowPlace)fromPlace.getFromPlace();
				}
			}
			if (currentPlace != null) {
				if (!QAMLConstants.WINDOW_AUTHENTICATION.equals(currentPlace.getId())) {
					return currentPlace;	
				}	
			}
		}
		return null;
	}
	
//	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
//		if (builtInFunctionGVO instanceof OpenWindowGVO) {
//			OpenWindowGVO openWindow = (OpenWindowGVO) builtInFunctionGVO;
//			if (openWindow.getWindow() != null && openWindow.getWindow().length() != 0) {
//				if (ClientApplicationContext.getInstance().isMDI()) {
//			//		ClientApplicationContext.getInstance().removeWindow(openWindow.getWindow(), openWindow.getContext(), openWindow.getUuid());
//				} else {
//					WindowFactory.clearWidgetFromMainPanel();
//				}
//				MainFactoryActions.getUIByUUID(openWindow.getUuid(), openWindow.getWindow());
//			} else if (openWindow.getUrl() != null && openWindow.getUrl().length() != 0) {
//				String title = openWindow.getUrl();
//				if (openWindow.getTitle() != null) {
//					title = openWindow.getTitle();
//					title = title.replace(" ", "_");
//				}
//				int width = 0;
//				int height = 0;
//				int left = 20;
//				int top = 20;
//				String menubar = "no";
//				String scrollbars = "no";
//				String toolbar = "no";
//				String status = "no";
//				String resize = "yes";
//				String modal = "no";
//				String features = "";
//				if (openWindow.getParams() != null) {
//					String[] paramArr = openWindow.getParams().split(",");
//					String temp = null;
//					for (int i = 0; i < paramArr.length; i++) {
//						if (paramArr[i].indexOf("width") > -1) {
//							width = Integer.parseInt(paramArr[i].substring(paramArr[i].indexOf("=") + 1));
//						} else if (paramArr[i].indexOf("height") > -1) {
//							height = Integer.parseInt(paramArr[i].substring(paramArr[i].indexOf("=") + 1));
//						} else if (paramArr[i].indexOf("left") > -1) {
//							left = Integer.parseInt(paramArr[i].substring(paramArr[i].indexOf("=") + 1));
//						} else if (paramArr[i].indexOf("top") > -1) {
//							top = Integer.parseInt(paramArr[i].substring(paramArr[i].indexOf("=") + 1));
//						} else if (paramArr[i].indexOf("menubar") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
//								menubar = "yes";
//							}
//							features = features + "menubar=" + menubar + ",";
//						} else if (paramArr[i].indexOf("scrollbars") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
//								scrollbars = "yes";
//							}
//							features = features + "scrollbars=" + scrollbars + ",";
//						} else if (paramArr[i].indexOf("toolbar") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
//								toolbar = "yes";
//							}
//							features = features + "toolbar=" + toolbar + ",";
//						} else if (paramArr[i].indexOf("status") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
//								status = "yes";
//							}
//							features = features + "status=" + status + ",";
//						} else if (paramArr[i].indexOf("resizable") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("no") || temp.equalsIgnoreCase("no") || temp.equals("1")){
//								resize = "no";
//							}
//							features = features + "resizable=" + resize + ",";
//						} else if (paramArr[i].indexOf("modal") > -1) {
//							temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
//							if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
//								modal = "yes";
//							}
//						}   
//					}
//				}
//				if(openWindow.getExternal()){
//					if (openWindow.getParams() != null) {
//						int index = openWindow.getParams().indexOf("left") + openWindow.getParams().indexOf("top") + openWindow.getParams().indexOf("screenX") + openWindow.getParams().indexOf("screenY");
//						if (index > -1) {
//							Window.open(openWindow.getUrl(), title, openWindow.getParams());
//						} else {
//							
//							ClientApplicationContext.getInstance().externalWindowCount++;
//							if (openWindow.getPlacement().equals(OpenWindowGVO.PLACEMENT_CASCADE)) {
//								if (ClientApplicationContext.getInstance().externalWindowCount > 1) {
//									for (int i = 1; i < ClientApplicationContext.getInstance().externalWindowCount; i++) {
//										left = left + 20;
//										top = top + 20;
//									}
//								}
//								features = features + ",screenX=" + left + ",screenY=" + top;
//							} else if (openWindow.getPlacement().equals(OpenWindowGVO.PLACEMENT_CENTER_CASCADE)) {
//								String centerCordinates = centeredWindow(width, height);
//								if (ClientApplicationContext.getInstance().externalWindowCount > 1) {
//									String[] centerCordinatesArr = centerCordinates.split(",");
//									for (int i = 0; i < centerCordinatesArr.length; i++) {
//										if (centerCordinatesArr[i].indexOf("screenX") == 0) {
//											left = Integer.parseInt(centerCordinatesArr[i].substring(centerCordinatesArr[i].indexOf("=") + 1));
//										}
//										if (centerCordinatesArr[i].indexOf("screenY") == 0) {
//											top = Integer.parseInt(centerCordinatesArr[i].substring(centerCordinatesArr[i].indexOf("=") + 1));
//										}
//									}
//									for (int i = 1; i < ClientApplicationContext.getInstance().externalWindowCount; i++) {
//										left = left + 20;
//										top = top + 20;
//									}
//									features = features + ",screenX=" + left + ",screenY=" + top;
//								} else {
//									features = features + centerCordinates;
//								}
//							}
//							Window.open(openWindow.getUrl(), title, features);
//						}
//					} else {
//						Window.open(openWindow.getUrl(), title, "");
//					}
//				} else {
//					boolean resizable = true;
//					boolean isModal = false;
//					if(resize.equals("")|| resize.equals("no")){
//						resizable = false;
//					}
//					if(modal.equals("yes")){
//						isModal = true;
//					}					
//					boolean centered = false;
//					if (openWindow.getPlacement().equals(OpenWindowGVO.PLACEMENT_CASCADE)) {
//						if (ClientApplicationContext.getInstance().internalWindowCount > 0) {
//							for (int i = 0; i < ClientApplicationContext.getInstance().internalWindowCount; i++) {
//								left = left + 20;
//								top = top + 20;
//							}
//						}
//					} else if (openWindow.getPlacement().equals(OpenWindowGVO.PLACEMENT_CENTER_CASCADE)) {
//						String[] centerCordinatesArr = centeredWindow(width, height).split(",");
//						for (int i = 0; i < centerCordinatesArr.length; i++) {
//							if (centerCordinatesArr[i].indexOf("screenX") == 0) {
//								left = Integer.parseInt(centerCordinatesArr[i].substring(centerCordinatesArr[i].indexOf("=") + 1));
//							}
//							if (centerCordinatesArr[i].indexOf("screenY") == 0) {
//								top = Integer.parseInt(centerCordinatesArr[i].substring(centerCordinatesArr[i].indexOf("=") + 1));
//							}
//						}
//						if (ClientApplicationContext.getInstance().internalWindowCount > 0) {
//							for (int i = 0; i < ClientApplicationContext.getInstance().internalWindowCount; i++) {
//								left = left + 20;
//								top = top + 20;
//							}
//						}
//					} else if (openWindow.getPlacement().equals(OpenWindowGVO.PLACEMENT_TILED)) {
//						top = 30;
//						left = 0;
//						if (ClientApplicationContext.getInstance().internalWindowCount > 0) {
//							int row = 1;
//							int column = 1;
//							boolean makeNextRow = false;
//							for (int i = 0; i < ClientApplicationContext.getInstance().internalWindowCount; i++) {
//								left = (width * (i + 1));
//								if ((left + width) > screenWidth()) {
//									left = 0;
//									makeNextRow = true;
//									if (row > 1) {
//										left = (width * column);
//										column++;
//										if ((left + width) > screenWidth()) {
//											makeNextRow = true;
//										} else {
//											makeNextRow = false;
//										}
//									}
//									if (makeNextRow) {
//										left = 0;
//										column = 1;
//										top = 30 + (height * row);
//										row++;
//										makeNextRow = false;
//									}
//								} else {
//									top = 30;
//								}
//							}
//						}
//					}
//					// corrections for the height
//					if (width==0){
//						width=600;
//					}
//					
//					if (height==0){
//						height=450;
//					}
//	//				MainFactory.createWindowWithUrl(title, openWindow.getUrl(), width, height, resizable, centered, top, left, isModal);
//					ClientApplicationContext.getInstance().internalWindowCount++;
//				}
//			}
//			FunctionsExecutor.setProcessedBuiltIn(true);
//		}
//
//	}
//
//	public native String centeredWindow(int w, int h) /*-{
//	    var left = parseInt(((screen.width - w)/2));
//	    var top = parseInt(((screen.height - h)/2));
//	    var windowFeatures = ",screenX=" + left + ",screenY=" + top;
//	    return windowFeatures;
//	}-*/;
//
//	public native int screenWidth() /*-{
//		return screen.width;
//	}-*/;
}
