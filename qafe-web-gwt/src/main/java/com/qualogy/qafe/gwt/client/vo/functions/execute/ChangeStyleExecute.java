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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleActionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO;

public class ChangeStyleExecute implements ExecuteCommand {

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof ChangeStyleGVO) {

			if (builtInFunction.getComponents() != null) {
				for (BuiltInComponentGVO builtInComponentGVO : builtInFunction.getComponents()) {

					String component = builtInComponentGVO.getComponentIdUUID();
					List<UIObject> uiObjects = null;
					if (component != null) {
						uiObjects = RendererHelper.getComponent(component);
					} else {
						uiObjects = RendererHelper.getNamedComponent(builtInComponentGVO.getComponentName());
					}

					if (uiObjects==null) { // Components within a window are not found. Check for SDI.
						if(!(ClientApplicationContext.getInstance().isMDI())) {
							UIObject mainPanel = ClientApplicationContext.getInstance().getMainPanel().getWidget();
							uiObjects = new ArrayList<UIObject>();
							if (mainPanel!=null){
								uiObjects.add(mainPanel);
							}
						}
					}
					if(uiObjects != null) {
					    for (UIObject object : uiObjects) {

	                        if (object != null) {
	                            if(object instanceof QWindowPanel) {
	                                object = ((QWindowPanel)object).getQRootPanel();
	                            }
	                            Element element = object.getElement();
	                            if (element != null) {
	                                ChangeStyleGVO changeStyle = (ChangeStyleGVO) builtInFunction;
	                                if (changeStyle.getActions() != null) {
	                                    for (ChangeStyleActionGVO changeStyleAction : changeStyle.getActions()) {

	                                        if ("remove".equalsIgnoreCase(changeStyleAction.getAction())) {
	                                            if (changeStyleAction.getKey() != null) {
	                                                if (changeStyleAction.getKey().trim().length() > 0) {
	                                                    RendererHelper.setStyleForElement(element, changeStyleAction.getKey(), null);
	                                                }
	                                            }
	                                            if (changeStyleAction.getStyle() != null) {
	                                                if (changeStyleAction.getStyle().trim().length() > 0) {
	                                                    // UIObject uiObject =
	                                                    // RendererHelper.getComponent(component);
	                                                    object.removeStyleName(changeStyleAction.getStyle());
	                                                }
	                                            }

	                                        } else if ("set".equalsIgnoreCase(changeStyleAction.getAction())) {

	                                            if (changeStyleAction.getKey() != null) {
	                                                if (changeStyleAction.getKey().trim().length() > 0) {

	                                                    RendererHelper.setStyleForElement(element, changeStyleAction.getKey(), changeStyleAction.getValue());
	                                                }
	                                            }
	                                            if (changeStyleAction.getStyle() != null) {
	                                                if (changeStyleAction.getStyle().trim().length() > 0) {
	                                                    // UIObject uiObject =
	                                                    // RendererHelper.getComponent(component);
	                                                    //object.addStyleName(changeStyleAction.getStyle());
	                                                    object.setStyleName(changeStyleAction.getStyle());
	                                                }
	                                            }

	                                        }

	                                    }
	                                }
	                            }
	                        }
	                    }
					}
					

				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);

	}
    // CHECKSTYLE.ON: CyclomaticComplexity

}
