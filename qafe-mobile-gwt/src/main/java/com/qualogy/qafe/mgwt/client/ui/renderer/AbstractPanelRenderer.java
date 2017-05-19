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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.HasStacks;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutAbsolute;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutAuto;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutBorder;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutGrid;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutHorizontal;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutVertical;
import com.qualogy.qafe.mgwt.client.ui.component.QPanel;
import com.qualogy.qafe.mgwt.client.vo.layout.AbsoluteLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.AutoLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.GridLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.HorizontalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.LayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.VerticalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.PanelGVO;

public abstract class AbstractPanelRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof PanelGVO) {
			PanelGVO panelGVO = (PanelGVO)component;
			LayoutGVO layoutGVO = panelGVO.getLayout();
			if (layoutGVO instanceof VerticalLayoutGVO) {
				widget = renderLayout(panelGVO, (VerticalLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);
			} else if (layoutGVO instanceof HorizontalLayoutGVO) {
				widget = renderLayout(panelGVO, (HorizontalLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);
			} else if (layoutGVO instanceof GridLayoutGVO) {
				widget = renderLayout(panelGVO, (GridLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);				
			} else if (layoutGVO instanceof AutoLayoutGVO) {
				widget = renderLayout(panelGVO, (AutoLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);
			} else if (layoutGVO instanceof BorderLayoutGVO) {
				widget = renderLayout(panelGVO, (BorderLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);
			} else if (layoutGVO instanceof AbsoluteLayoutGVO) {
				widget = renderLayout(panelGVO, (AbsoluteLayoutGVO)layoutGVO, owner, uuid, parent, context, activity);
			}
			init(component, widget, owner, uuid, parent, context, activity);
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}

	protected UIObject renderLayout(PanelGVO panelGVO, VerticalLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		HasWidgets panel = createPanel();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			if (children.length == 1) {
				UIObject child = children[0];
				if (child instanceof HasStacks) {
					panel.add((Widget)child);
					return (UIObject)panel;
				}
			}
			ScrollPanel scrollPanel = createScrollPanel();
			panel.add(scrollPanel);
			QLayoutVertical layout = new QLayoutVertical();
			for (int i=0; i<children.length; i++) {
				UIObject child = children[i];
				if (child instanceof Widget) {
					layout.add((Widget)child);
				}
			}
			scrollPanel.setWidget((Widget)layout);
			scrollPanel.refresh();
		}
		return (UIObject)panel;
	}
	
	protected UIObject renderLayout(PanelGVO panelGVO, HorizontalLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		HasWidgets panel = createPanel();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			if (children.length == 1) {
				UIObject child = children[0];
				if (child instanceof HasStacks) {
					panel.add((Widget)child);
					return (UIObject)panel;
				}
			}
			ScrollPanel scrollPanel = createScrollPanel();
			panel.add(scrollPanel);
			QLayoutHorizontal layout = new QLayoutHorizontal();
			for (int i=0; i<children.length; i++) {
				UIObject child = children[i];
				if (child instanceof Widget) {
					layout.add((Widget)child);
				}
			}
			scrollPanel.setWidget(layout);
			scrollPanel.refresh();
		}
		return (UIObject)panel;
	}
	
	protected UIObject renderLayout(PanelGVO panelGVO, GridLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) { 
		HasWidgets panel = createPanel();
		ElementGVO[] elements = layoutGVO.getElements();
		if ((elements != null) && (elements.length > 0)) {
			ScrollPanel scrollPanel = createScrollPanel();
			panel.add(scrollPanel);
			QLayoutGrid layout = new QLayoutGrid();
			for (ElementGVO elementGVO : elements) {
				handleGridLayoutElement(layout, elementGVO, owner, uuid, parent, context, activity);
			}
			scrollPanel.setWidget(layout);
			scrollPanel.refresh();
		}
		return (UIObject)panel;
	}
	
	protected void handleGridLayoutElement(QLayoutGrid layout, ElementGVO elementGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject child = renderChildComponent(elementGVO.getComponent(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			int rowIndex = elementGVO.getY();
			int colIndex = elementGVO.getX();
			layout.setWidget(rowIndex, colIndex, (Widget)child);
			
			Element element = layout.getFlexCellFormatter().getElement(rowIndex, colIndex);
			initStyling(element, elementGVO.getStyleClass(), elementGVO.getStyleProperties(), uuid, parent, context);
			
			int rowSpan = elementGVO.getGridheight();
			if (rowSpan > 0) {
				layout.getFlexCellFormatter().setRowSpan(rowIndex, colIndex, rowSpan);
			}
			int colSpan = elementGVO.getGridwidth();
			if (colSpan > 0) {
				layout.getFlexCellFormatter().setColSpan(rowIndex, colIndex, colSpan);
			}
		}
	}
	
	protected UIObject renderLayout(PanelGVO panelGVO, AutoLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) { 
		HasWidgets panel = createPanel();
		UIObject[] children = renderChildComponents(layoutGVO.getComponents(), owner, uuid, parent, context, activity);
		if ((children != null) && (children.length > 0)) {
			ScrollPanel scrollPanel = createScrollPanel();
			panel.add(scrollPanel);
			QLayoutAuto layout = new QLayoutAuto();
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
			scrollPanel.setWidget(layout);
			scrollPanel.refresh();
		}
		return (UIObject)panel;
	}
	
	protected UIObject renderLayout(PanelGVO panelGVO, BorderLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		HasWidgets panel = createPanel();
		ScrollPanel scrollPanel = createScrollPanel();
		panel.add(scrollPanel);
		QLayoutBorder layout = renderBorderLayout(panelGVO, layoutGVO, owner, uuid, parent, context, activity);
		scrollPanel.setWidget(layout);
		scrollPanel.refresh();
		return (UIObject)panel;		
	}
	
	protected QLayoutBorder renderBorderLayout(PanelGVO panelGVO, BorderLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		QLayoutBorder layout = new QLayoutBorder();
		layout.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		layout.setSpacing(3);
		
		UIObject child = renderChildComponent(layoutGVO.getNorth(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			layout.add((Widget)child, DockPanel.NORTH);	
		}
		
		child = renderChildComponent(layoutGVO.getEast(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			layout.add((Widget)child, DockPanel.EAST);	
		}
		
		child = renderChildComponent(layoutGVO.getSouth(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			layout.add((Widget)child, DockPanel.SOUTH);	
		}
		
		child = renderChildComponent(layoutGVO.getWest(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			layout.add((Widget)child, DockPanel.WEST);
		}
		
		child = renderChildComponent(layoutGVO.getCenter(), owner, uuid, parent, context, activity);
		if (child instanceof Widget) {
			layout.add((Widget)child, DockPanel.CENTER);	
		}
		return layout;
	}
	
	protected UIObject renderLayout(PanelGVO panelGVO, AbsoluteLayoutGVO layoutGVO, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		HasWidgets panel = createPanel();
		ElementGVO[] elements = layoutGVO.getElements();
		if ((elements != null) && (elements.length > 0)) {
			ScrollPanel scrollPanel = createScrollPanel();
			panel.add(scrollPanel);
			QLayoutAbsolute layout = new QLayoutAbsolute();
			for (ElementGVO elementGVO : elements) {
				UIObject child = renderChildComponent(elementGVO.getComponent(), owner, uuid, parent, context, activity);
				if (child instanceof Widget) {
					int posX = elementGVO.getX();
					int posY = elementGVO.getY();
					layout.add((Widget)child, posX, posY);					
				}
			}
			scrollPanel.setWidget(layout);
			scrollPanel.refresh();
		}
		return (UIObject)panel;
	}
	
	protected HasWidgets createPanel() {
		return new QPanel();
	}
	
	protected ScrollPanel createScrollPanel() {
		ScrollPanel scrollPanel = new ScrollPanel();
		// Fix for scrolling in a TabPanel  
		scrollPanel.getElement().getStyle().setProperty("WebkitBoxFlex", "1");
		return scrollPanel;
	}
}