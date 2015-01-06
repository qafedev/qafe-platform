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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TreeGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TreeItemGVO;

public class TreeRenderer extends AbstractComponentRenderer {

	TreeImages treeImages = null;

	public TreeRenderer() {
		treeImages = GWT.create(TreeImages.class);
	}

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		Tree uiObject = null;
		if (component != null) {
			if (component instanceof TreeGVO) {
				TreeGVO gvo = (TreeGVO) component;
				
			//	QTreeItem rootItem = new QTreeItem(gvo.getDisplayname());
				
//				if (gvo.getMenu() != null) {
//					final ComponentGVO finalComponentGVO = component;
//					final String finalUuid = uuid;
//					final String finalParent = parent;
//					uiObject = new QTree(rootItem, hasDisplayName(gvo)) {
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
//					uiObject = new QTree(rootItem, hasDisplayName(gvo));
//				}
				uiObject.setAnimationEnabled(true);
				
//				Label rootItemLabel = rootItem.getLabel();
//				fillInRootItemLabel(uuid, parent, context, uiObject, gvo, rootItemLabel);
//				
//				if (gvo.getChildren() != null) {
//					for (TreeItemGVO mainItemGVO : gvo.getChildren()) {						
//						String label = mainItemGVO.getDisplayname();
//						processMainItem(uuid, parent, context, (QTree)uiObject, mainItemGVO, mainItemGVO.getChildren(), label);						
//					}						
//				}
				RendererHelper.fillIn(gvo, uiObject, uuid, parent, context);
				
			}
		}

		return uiObject;
	}

//	private void processMainItem(String uuid, String parent, String context, QTree uiObject, ComponentGVO gvo, TreeItemGVO[] children, String label) {
//		QTreeItem mainItem = new QTreeItem(label, uiObject);
//		Label mainItemLabel = mainItem.getLabel();
//		RendererHelper.fillIn(gvo, mainItem, uuid, parent, context);
//		RendererHelper.fillIn(gvo, mainItemLabel, uuid, parent, context);
//		processChildren(mainItem, children, uuid, parent, context);		
//		uiObject.addItem(mainItem);
//	}
	
	private boolean hasDisplayName(TreeGVO gvo) {
		String displayName = gvo.getDisplayname();
		return (displayName != null) && (displayName.length() > 0);
	}

	/**
	 *  To listen to the root tree item we need to add id,uuid,events etc to the root item label. 
	 * 	RendererHelper.fillIn method is doing addDimension to the label which is not needed for us.
	 *  If addDimension is done the tree alignment will go wrong based on width and height.
	 *  So this method calls all other fillIn except the addDimension();
	 */
	private void fillInRootItemLabel(String uuid, String parent, String context, Tree uiObject, ComponentGVO gvo, Label mainItemLabel) {
		RendererHelper.addId(gvo, mainItemLabel, uuid, parent, context, true);
		RendererHelper.addUUID(gvo, mainItemLabel, uuid);
		RendererHelper.addWindowID(gvo, uiObject, uuid);
		RendererHelper.addDisabledInfo(gvo, mainItemLabel);
		RendererHelper.addStyle(gvo, mainItemLabel);
		RendererHelper.addEvents(gvo, mainItemLabel, uuid);
		RendererHelper.addVisibleInfo(gvo, mainItemLabel);
		RendererHelper.addTooltip(gvo, mainItemLabel);
	}

	void processChildren(TreeItem rootItem, TreeItemGVO[] children, String uuid, String parent, String context) {
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				TreeItemGVO componentGVO = children[i];
				UIObject uiObject = renderChildComponent(componentGVO, null, uuid, parent, context, null);
//				if (uiObject instanceof QTreeItem) {
//					QTreeItem treeItem = (QTreeItem) uiObject;
//					treeItem.setState(componentGVO.getExpand().booleanValue());
//					rootItem.addItem(treeItem);
//					// TODO set Tree
//					RendererHelper.fillIn(componentGVO, treeItem.getLabel(), uuid, parent, context);
//					processChildren(treeItem, componentGVO.getChildren(), uuid, parent, context);
//				}
				RendererHelper.fillIn(componentGVO, uiObject, uuid, parent, context);
			}
		}
	}
}
