/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QTabPanel;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TabGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TabPanelGVO;

public class TabPanelRenderer extends AbstractComponentRenderer {

	/**
	 * Note: 
	 * The TabPanel widget should be placed in a LayoutPanel or ScrollPanel (within a LayoutPanel) widget,
	 * and the LayoutPanel widget is the top level, otherwise the TabPanel widget will not show properly 
	 * Example: 
	 * - OK => LayoutPanel->TabPanel
	 * - OK => LayoutPanel->ScrollPanel->TabPanel
	 * - NOT OK => LayoutPanel->ScrollPanel->LayoutPanel->TabPanel
	 * - NOT OK => LayoutPanel->ScrollPanel->LayoutPanel->ScrollPanel->TabPanel
	 */
	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {		
		UIObject widget = null;
		if (component instanceof TabPanelGVO) {
			TabPanelGVO tabPanelGVO = (TabPanelGVO)component;
			QTabPanel tabPanel = new QTabPanel();
			init(tabPanelGVO, tabPanel, owner, uuid, parent, context, activity);
			widget = tabPanel;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		TabPanelGVO tabPanelGVO = (TabPanelGVO)component;
		QTabPanel tabPanel = (QTabPanel)widget;
		renderChildren(tabPanelGVO, tabPanel, owner, uuid, parent, context, activity);
	}
	
	private void renderChildren(TabPanelGVO component, QTabPanel widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		TabGVO[] tabs = component.getTabs();
		if (tabs != null){
			for (int i=0; i<tabs.length; i++){
				TabGVO tabGVO = tabs[i];
				UIObject child = renderChildComponent(tabGVO, owner, uuid, parent, context, activity);
				if (child instanceof Widget) {
					String title = tabGVO.getTitle();		
					widget.add(title, (Widget)child);
				}
			}
		}
	}
	
//	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
//		TabPanel uiObject = null;
//		if (component != null) {
//			if (component instanceof TabPanelGVO) {
//				TabPanelGVO gvo = (TabPanelGVO) component;
//				//uiObject = new TabPanel();
//				if (gvo.getMenu() != null) {
//					final ComponentGVO finalComponentGVO = component;
//					final String finalUuid = uuid;
//					final String finalParent = parent;
//					uiObject = new TabPanel() {
//						@Override
//						public void onBrowserEvent(Event event) {
//							if (event.getTypeInt() == Event.ONCONTEXTMENU) {
//								DOM.eventPreventDefault(event);
//								applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
//							}
//							super.onBrowserEvent(event);
//						}
//
//						@Override
//						protected void setElement(Element elem) {
//							super.setElement(elem);
//							sinkEvents(Event.ONCONTEXTMENU);
//						}
//					};
//				} else {
//					uiObject = new TabPanel();
//				}
//				uiObject.setAnimationEnabled(true);
//				RendererHelper.fillIn(component, uiObject, uuid,parent, context);
//				//uiObject.setTitle(gvo.getId());
//				
//				TabGVO[] tabs =gvo.getTabs();
//				
//				if (tabs!=null){
//					for (int i=0;i<tabs.length;i++){
//						String title= tabs[i].getTitle();
//						// since the panel renderer is used, the title (not null) will create a titledPanel.
//						// The tab already has the title so that is overkill
//						tabs[i].setTitle(null);
//						UIObject tabUI = renderChildComponent(tabs[i],uuid,parent, context);
//						tabs[i].setTitle(title);
//						if (tabUI instanceof Widget){
//							
//							uiObject.add((Widget)tabUI/*(Widget)renderChildComponent(tabs[i],uuid)*/, title);
//							setTabVisibility(uiObject,i,tabs[i].getVisible(),tabUI);
//							
////							if (tabUI instanceof Panel) {
////								Panel p = (Panel) tabUI;
////								Widget prnt = p.getParent();
////								if (prnt != null && prnt instanceof DeckPanel) {
////									DeckPanel deckPanel = (DeckPanel) prnt;
////									int widgetIndex = deckPanel.getWidgetIndex(p);
////									
////									if (widgetIndex != -1) {
////										deckPanel.getWidget(widgetIndex).setVisible(tabs[i].getVisible());
////										 UIObject.setVisible(deckPanel.getWidget(widgetIndex).getElement(), tabs[i].getVisible());	
////									}
////
////								}
////							}
//							
//						}
//						RendererHelper.addEvents(tabs[i], uiObject,uuid);
//					}
//				}
//				if(uiObject.getWidgetCount()>0){
//					uiObject.selectTab(0);
//				}
//			}
//		}
//
//		return uiObject;
//	}
//	
//	public static void setTabVisibility(TabPanel tabs, int index, boolean visible, UIObject tabUI) {
//		try {
//			if (index >= tabs.getTabBar().getTabCount() || index < 0)
//				return;
//			// Native Google implementation of TabBar uses an HorizontalPanel,
//			// so, the DOM primary element is a table element (with just one tr)
//			Element table = tabs.getTabBar().getElement();
//			Element tr = DOM.getFirstChild(DOM.getFirstChild(table));
//			// (index + 1) to account for 'first' placeholder td.
//			Element td = DOM.getChild(tr, index + 1);
//			UIObject.setVisible(td, visible);
//			tabUI.setVisible(visible);
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	} 
}
