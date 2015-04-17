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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QStack;
import com.qualogy.qafe.gwt.client.component.QStackPanel;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.StackGVO;
import com.qualogy.qafe.gwt.client.vo.ui.StackPanelGVO;

public class StackPanelRenderer extends AbstractComponentRenderer {

    public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
        QStackPanel uiObject = null;
        if (component != null) {
            if (component instanceof StackPanelGVO) {
                StackPanelGVO gvo = (StackPanelGVO) component;
                if (gvo.getMenu() != null) {
                    final ComponentGVO finalComponentGVO = component;
                    final String finalUuid = uuid;
                    final String finalParent = parent;
                    uiObject = new QStackPanel() {

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
                    uiObject = new QStackPanel();
                }

                RendererHelper.fillIn(component, uiObject, uuid, parent, context);
                String selectedStack = gvo.getSelected();
                StackGVO[] stacks = gvo.getStacks();
                if (stacks != null) {
                    for (int i = 0; i < stacks.length; i++) {
                        QStack stack = (QStack) renderChildComponent(stacks[i], uuid, parent, context);
                        stack.setStackIndex(i);
                        stack.setParentStackPanel(uiObject);
                        stack.setStackText(stacks[i].getTitle());
                        if (stacks[i].getVisible()) {
                            uiObject.add(stack, stacks[i].getTitle());
                        }
                        if (isSelectedStack(stacks[i], selectedStack)) {
                            uiObject.showStack(stack);
                        }

                    }
                }
                uiObject.storeSelectedWidget();

            }
        }
        return uiObject;
    }

    private boolean isSelectedStack(StackGVO stackGvo, String selectedStack) {
        if (selectedStack != null && selectedStack.equals(stackGvo.getId())) {
            return true;
        }
        return false;
    }

}
