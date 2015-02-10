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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.ui.component.ComponentHelper;
import com.qualogy.qafe.mgwt.client.ui.component.HasData;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public abstract class AbstractComponentRenderer implements GWTUIRenderer {
	
	private static final Map<String,GWTUIRenderer> COMPONENT_RENDERER_MAP = new HashMap<String,GWTUIRenderer>();
	static {
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.xml.component.Accordion", null);
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ButtonGVO", new ButtonRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO", new CheckBoxRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ChoiceGVO", new ChoiceRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ChoiceItemGVO", new ChoiceItemRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO", new DataGridRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.DropDownGVO", new DropDownRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.FileUploadGVO", new FileUploadRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.FrameGVO", new FrameRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.HiddenGVO", new HiddenRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.HTMLGVO", new HTMLRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ImageGVO", new ImageRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.LabelGVO", new LabelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.LinkGVO", new LinkRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ListBoxGVO", new ListBoxRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.MapGVO", new MapRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO", new MenuItemRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.MenuItemSeparatorGVO", new MenuItemSeparatorRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.PanelGVO", new PanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.PanelRefGVO", new PanelRefRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.PasswordTextFieldGVO", new PasswordTextFieldRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO", new RootPanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.SliderGVO", new SliderRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.StackPanelGVO", new StackPanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.StackGVO", new StackRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.SplitPanelGVO", new SplitPanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TableGVO", new TableRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TabPanelGVO", new TabPanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TabGVO", new TabRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.Toolbar", new ToolbarRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ToolbarItem", new ToolbarItemRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO", new AnyTextFieldRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TextAreaGVO", new TextAreaRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TileListGVO", new TileListRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TreeGVO", new TreeRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO", new TreeItemRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.WindowGVO", new WindowRenderer());
	}

	protected UIObject[] renderChildComponents(ComponentGVO[] components, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject[] renderedComponents = null;
		if (components != null) {
			renderedComponents = new UIObject[components.length];
			for (int i=0; i<components.length; i++) {
				renderedComponents[i] = renderChildComponent(components[i], owner, uuid, parent, context, activity);
			}	
		}
		return renderedComponents;
	}

	protected UIObject renderChildComponent(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject renderedComponent = null;
		if (component != null) {
			try {
				// TODO use component.getClass().getName()? 
				// ToolbarGVO and ToolbarItemGVO do not return the 'GVO' postfix when calling getClassName()
				String className = component.getClassName();
				Object object = COMPONENT_RENDERER_MAP.get(className);
				if (object instanceof GWTUIRenderer) {
					GWTUIRenderer renderer = (GWTUIRenderer)object;					
					renderedComponent = renderer.render(component, owner, uuid, parent, context, activity);
				} else {
					ClientApplicationContext.getInstance().log("Unable to find renderer for class " + className, null);
				}
			} catch (Exception e) {
				ClientApplicationContext.getInstance().log("AbstractComponentRenderer:renderChildComponent", e);
			}
		}
		return renderedComponent;
	}
	
	protected void registerComponent(ComponentGVO component, UIObject widget, String owner, String parent, String context) {
		ComponentRepository.getInstance().addComponent(owner, parent, context, widget, component);		
	}
	
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		initAvailablitiy(component, widget, uuid, parent, context);
		initVisibility(component, widget, uuid, parent, context);
		initSize(component, widget, uuid, parent, context);
		initStyling(component, widget, uuid, parent, context);
		initTooltip(component, widget, uuid, parent, context);
		initDataName(component, widget, uuid, parent, context);
	}

	protected void initDataName(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		String dataName = component.getFieldName();
		if (QAMLUtil.isEmpty(dataName)) {
			return;
		}
		if (widget instanceof HasData) {
			HasData hasData = (HasData)widget;
			hasData.setDataName(dataName);
		}
	}

	protected void initSize(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		String width = component.getWidth();
		String height = component.getHeight();
		initSize(widget, width, height, uuid, parent, context);
	}
	
	protected void initSize(UIObject widget, String width, String height, String uuid, String parent, String context) {
		if (widget == null) {
			return;
		}
		if (!QAMLUtil.isEmpty(width)) {
			if (!width.startsWith("-")) {
				// Width is not negative
				if (QAMLUtil.isNumber(width)) {
					width = width + QAMLConstants.UNIT;
				}
				widget.setWidth(width);
			}
		}
		if (!QAMLUtil.isEmpty(height)) {
			if (!height.startsWith("-")) {
				// Height is not negative
				if (QAMLUtil.isNumber(height)) {
					height = height + QAMLConstants.UNIT;
				}
				widget.setHeight(height);
			}
		}
		if ("0".equals(width) && "0".equals(height)) {
			widget.setVisible(false);
		}
	}
	
	protected void initAvailablitiy(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		if (widget == null) {
			return;
		}
		if (widget instanceof HasEnabled) {
			HasEnabled availability = (HasEnabled)widget;
			boolean enabled = !component.isDisabled();
			availability.setEnabled(enabled);
		}
	}
	
	protected void initStyling(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		String styleClass = component.getStyleClass();
		String[][] inlineStyles = component.getStyleProperties();
		ComponentHelper.setStyle(widget, styleClass, inlineStyles);
	}
	
	protected void initStyling(ComponentGVO component, Element element, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		String styleClass = component.getStyleClass();
		String[][] inlineStyles = component.getStyleProperties();
		initStyling(element, styleClass, inlineStyles, uuid, parent, context);
	}
	
	protected void initStyling(Element element, String styleClass, String[][] inlineStyles, String uuid, String parent, String context) {
		ComponentHelper.setStyle(element, styleClass, inlineStyles);
	}
	
	protected void initVisibility(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		if (widget == null) {
			return;
		}
		boolean visible = component.getVisible();
		if (!visible) {
			widget.setVisible(visible);
		}
	}
	
	protected void initTooltip(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (component == null) {
			return;
		}
		if (widget == null) {
			return;
		}
		String tooltip = component.getTooltip();
		if (!QAMLUtil.isEmpty(tooltip)) {
			widget.setTitle(tooltip);
		}
	}
	
	protected void applyContextMenu(Event event, ComponentGVO finalComponentGVO, String finalUuid, String finalParent) {
		// TODO not needed?
	}
}
