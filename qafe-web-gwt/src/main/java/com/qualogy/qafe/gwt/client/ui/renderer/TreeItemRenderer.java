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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QLabel;
import com.qualogy.qafe.gwt.client.component.QTreeItem;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO;

public class TreeItemRenderer extends AbstractComponentRenderer {
	
	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		TreeItem uiObject = null;
		if (component instanceof TreeItemGVO) {
			TreeItemGVO componentGVO = (TreeItemGVO) component;
			uiObject = new QTreeItem(componentGVO.getDisplayname());
		}
		return uiObject;
	}
//	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
//		TreeItem uiObject = null;
//		if (component != null) {
//			if (component instanceof TreeItemGVO) {
//				TreeItemGVO gvo = (TreeItemGVO) component;
//				uiObject = new TreeItem(gvo.getDisplayname());
//				
//				//if (gvo.getComponent()!=null){
//				//RendererHelper.fillIn(component, uiObject,uuid);
//					//Label label  = new Label(gvo.getDisplayname());
//					
//					Label label = new QLabel(gvo.getDisplayname());
//					
//					RendererHelper.fillIn(component, label,uuid,parent, context);
//					RendererHelper.fillIn(component, uiObject,uuid,parent, context);
//					
//					uiObject.setWidget(label);
//
//				//} else {
//				//	UIObject uiObj = renderChildComponent(gvo.getComponent(), uuid);
//				//	if (uiObj instanceof Widget){
//				//		Widget w = (Widget)uiObj;		
//				//		RendererHelper.fillIn(component, uiObject,uuid);
//				//		uiObject.setWidget(w);
//				//	}
//				//}
//				
//				//uiObject.setText(gvo.getLabel());
//				//uiObject.setTitle(gvo.getId());
//				//uiObject.setTitle(gvo.getLabel());
//				
//				TreeItemGVO [] children = gvo.getChildren();
//				if (children !=null){
//					for (int i=0;i<children.length;i++){
//						UIObject treeItem = renderChildComponent(children[i],uuid,parent, context);
//						if (treeItem instanceof TreeItem){
//							TreeItem item = (TreeItem)treeItem;
//							uiObject.addItem(item);
//						}
//					}
//					uiObject.setState(gvo.getExpand().booleanValue());
//				}
//				
//				
//			
//			}
//		}
//
//		return uiObject;
//	}
}
