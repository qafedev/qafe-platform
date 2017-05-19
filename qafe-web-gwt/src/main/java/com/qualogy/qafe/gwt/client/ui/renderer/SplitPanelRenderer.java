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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.SplitPanelGVO;

public class SplitPanelRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		Panel uiObject = null;
		if (component != null) {
			if (component instanceof SplitPanelGVO) {
				SplitPanelGVO gvo = (SplitPanelGVO) component;
				if (gvo.getHorizontalOrientation()!=null){
					if (gvo.getHorizontalOrientation().booleanValue()){
						uiObject=new HorizontalSplitPanel();
					} else {
						uiObject=new VerticalSplitPanel();
					}
					
				} else {
					uiObject = new HorizontalSplitPanel();
				}
				
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
//				RendererHelper.addId(component, uiObject,uuid,parent);
//				RendererHelper.addUUID(component, uiObject, uuid);
//				RendererHelper.addDisabledInfo(component, uiObject);	
//				
//				//addStyle(vo, ui);
//				RendererHelper.addEvents(component, uiObject,uuid);
//				RendererHelper.addVisibleInfo(component,uiObject);
//			
//				
				Widget left =(Widget) renderChildComponent(gvo.getFirst(), uuid,parent, context);
				RendererHelper.fillIn(gvo.getFirst(), left, uuid, parent, context);
				Widget right=(Widget) renderChildComponent(gvo.getSecond(), uuid,parent, context);
				RendererHelper.fillIn(gvo.getSecond(), right, uuid, parent, context);
				
				if (uiObject instanceof VerticalSplitPanel){
					VerticalSplitPanel vsp = (VerticalSplitPanel)uiObject;
					vsp.setSplitPosition(gvo.getPosition());
					if (left!=null){
						vsp.setTopWidget(left);
					}
					if (right!=null){
						vsp.setBottomWidget(right);
					}
					//vsp.setSize("auto", "50px");
					
					
				}
				
				if (uiObject instanceof HorizontalSplitPanel){
					HorizontalSplitPanel vsp = (HorizontalSplitPanel)uiObject;
					vsp.setSplitPosition(gvo.getPosition());
					if (left!=null){
						vsp.setLeftWidget(left);
					}
					if (right!=null){
						vsp.setRightWidget(right);
					}
					//vsp.setSize("100%", "auto");
				}
				DecoratorPanel simplePanel = new DecoratorPanel();
				simplePanel.add(uiObject);
				uiObject = simplePanel;
			}
		}

		return uiObject;
	}
}
