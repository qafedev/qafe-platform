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
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutAbsolute;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutAuto;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutBorder;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutGrid;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutHorizontal;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutVertical;
import com.qualogy.qafe.mgwt.client.vo.layout.AbsoluteLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.AutoLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.GridLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.HorizontalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.VerticalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.PanelGVO;

public class PanelRenderer extends AbstractPanelRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof PanelGVO){
			PanelGVO panelGVO = (PanelGVO)component;
			widget = super.render(panelGVO, owner, uuid, parent, context, activity);
		}
		return widget;
	}

	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, VerticalLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		QLayoutVertical layout = new QLayoutVertical();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			for (int i=0; i<children.length; i++) {
				UIObject child = children[i];
				if (child instanceof Widget) {
					layout.add((Widget)child);
				}
			}	
		}
		return layout;
	}
	
	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, HorizontalLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		QLayoutHorizontal layout = new QLayoutHorizontal();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			for (int i=0; i<children.length; i++) {
				UIObject child = children[i];
				if (child instanceof Widget) {
					layout.add((Widget)child);
				}
			}	
		}
		return layout;
	}
	
	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, GridLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) { 
		QLayoutGrid layout = new QLayoutGrid();
		ElementGVO[] elements = layoutGVO.getElements();
		if ((elements != null) && (elements.length > 0)) {
			for (ElementGVO elementGVO : elements) {
				handleGridLayoutElement(layout, elementGVO, owner, uuid, parent, context, activity);
			}
		}
		return layout;
	}
	
	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, AutoLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) { 
		QLayoutAuto layout = new QLayoutAuto();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			int numColumns = 1;
			if (layoutGVO.getCols() != null) {
				numColumns = layoutGVO.getCols();
			}
			int numRows = (children.length / numColumns) + 1;
			for (int i=0; i<numRows; i++) {
				for (int j=0; j<numColumns; j++) {
					int index = (i * numColumns) + j;
					if (index < children.length) {
						UIObject child = children[index];
						if (child instanceof Widget) {
							layout.setWidget(i, j, (Widget)child);
						}
					}
				}
			}
		}
		return layout;
	}
	
	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, BorderLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		QLayoutBorder layout = renderBorderLayout(panelGVO, layoutGVO, owner, uuid, parent, context, activity);
		return layout;		
	}
	
	@Override
	protected UIObject renderLayout(PanelGVO panelGVO, AbsoluteLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		QLayoutAbsolute layout = new QLayoutAbsolute();
		ElementGVO[] elements = layoutGVO.getElements();
		if (elements != null) {
			for (ElementGVO elementGVO : elements) {
				UIObject child = renderChildComponent(elementGVO.getComponent(), owner, uuid, parent, context, activity);
				if (child instanceof Widget) {
					int posX = elementGVO.getX();
					int posY = elementGVO.getY();
					layout.add((Widget)child, posX, posY);					
				}
			}
		}
		return layout;
	}
}