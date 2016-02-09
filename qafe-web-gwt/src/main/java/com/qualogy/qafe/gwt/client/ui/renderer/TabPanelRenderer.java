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
package com.qualogy.qafe.gwt.client.ui.renderer;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TabGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TabPanelGVO;

public class TabPanelRenderer extends AbstractComponentRenderer {

    private static final Logger LOG = Logger.getLogger(TabPanelRenderer.class.getName());

    public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
        TabPanel uiObject = null;
        if (component != null) {
            if (component instanceof TabPanelGVO) {
                TabPanelGVO gvo = (TabPanelGVO) component;
                if (gvo.getMenu() != null) {
                    final ComponentGVO finalComponentGVO = component;
                    final String finalUuid = uuid;
                    final String finalParent = parent;
                    uiObject = new TabPanel() {

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
                    uiObject = new TabPanel();
                }
                uiObject.setAnimationEnabled(gvo.isAnimationEnabled());
                RendererHelper.fillIn(component, uiObject, uuid, parent, context);

                TabGVO[] tabs = gvo.getTabs();
                if (tabs != null) {
                    for (int i = 0; i < tabs.length; i++) {
                        TabGVO tabGVO = tabs[i];

                        // since the panel renderer is used, the title (not null) will create a titledPanel.
                        // The tab already has the title so that is overkill
                        String title = tabGVO.getTitle();
                        tabGVO.setTitle(null);
                        UIObject tabUI = renderChildComponent(tabGVO, uuid, parent, context);
                        tabGVO.setTitle(title);

                        if (tabUI instanceof Widget) {
                            uiObject.add((Widget) tabUI, title);
                            setTabVisibility(uiObject, i, tabGVO.getVisible(), tabUI);

                            int tabCount = uiObject.getTabBar().getTabCount();
                            UIObject tabComponent = (UIObject) uiObject.getTabBar().getTab(tabCount - 1);
                            RendererHelper.addAttributesRequiredByEventHandling(tabGVO, tabComponent, uuid,
                                parent, context);
                            RendererHelper.addEvents(tabGVO, tabComponent, uuid);
                        }
                    }
                }

                if (uiObject.getWidgetCount() > 0) {
                    uiObject.selectTab(0);
                }
            }
        }
        return uiObject;
    }

    public static void setTabVisibility(TabPanel tabs, int index, boolean visible, UIObject tabUI) {
        try {
            if (index >= tabs.getTabBar().getTabCount() || index < 0)
                return;
            // Native Google implementation of TabBar uses an HorizontalPanel,
            // so, the DOM primary element is a table element (with just one tr)
            Element table = tabs.getTabBar().getElement();
            Element tr = DOM.getFirstChild(DOM.getFirstChild(table));
            // (index + 1) to account for 'first' placeholder td.
            Element td = DOM.getChild(tr, index + 1);
            UIObject.setVisible(td, visible);
            tabUI.setVisible(visible);
        } catch (Exception e) {
            LOG.log(Level.WARNING,
                "Not sure what can go wrong, but setting tab visibility might have failed", e);
        }
    }
}
