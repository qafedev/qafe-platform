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

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.PopupMenu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemSeparatorGVO;

public abstract class AbstractComponentRenderer implements GWTUIRenderer {
	
	public static class MessageBox extends HorizontalPanel {
		
		private Label label = new Label();

		public MessageBox(){
			add(label);
			setStyleName("qmessagebox");
			setVisible(false);
			label.setStyleName("qmessageboxlabel");
		}
		public Label getLabel() {
			return label;
		}

		public void setLabel(Label label) {
			this.label = label;
		}
	}
	
	private static final Map<String, GWTUIRenderer> COMPONENT_RENDERER_MAP = new HashMap<String, GWTUIRenderer>();
	static {
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.xml.component.Accordion", null);
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ButtonGVO", new ButtonRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO", new CheckBoxRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ChoiceGVO", new ChoiceRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.DropDownGVO", new DropDownRenderer());
		//COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO", new DataGrid2Renderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO", new PagingDataGridRenderer());

		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.FileUploadGVO", new FileUploadRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.HiddenGVO", new HiddenRenderer());

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
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TabGVO", new PanelRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO", new TextFieldRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TextAreaGVO", new TextAreaRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TreeGVO", new TreeRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO", new TreeItemRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.WindowGVO", new WindowRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.HTMLGVO", new HTMLRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.FrameGVO", new FrameRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.TileListGVO", new TileListRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ChartComponentGVO", new ChartComponentRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.BarChartGVO", new BarChartRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ColumnChartGVO", new ColumnChartRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.LineChartGVO", new LineChartRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.PlotChartGVO", new PlotChartRenderer());
		COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.PieChartGVO", new PieChartRenderer());
		//COMPONENT_RENDERER_MAP.put("com.qualogy.qafe.gwt.client.vo.ui.ChartItemGVO", new ChartItemRenderer());
		
//		COMPONENT_RENDERER_MAP.put(CategoryAxis.class, new CategoryAxisUIAssembler());
//		COMPONENT_RENDERER_MAP.put(ChartItem.class, new ChartItemUIAssembler());
//		COMPONENT_RENDERER_MAP.put(ColumnChart.class, new ColumnChartUIAssembler());
//		COMPONENT_RENDERER_MAP.put(LinearAxis.class, new LinearAxisUIAssembler());
//		COMPONENT_RENDERER_MAP.put(LineChart.class, new LineChartUIAssembler());
//		COMPONENT_RENDERER_MAP.put(PieChart.class, new PieChartUIAssembler());
//		COMPONENT_RENDERER_MAP.put(PlotChart.class, new PlotChartUIAssembler());
		
	}

	protected UIObject[] renderChildComponents(ComponentGVO[] components, String uuid, String parent, String context) {
		UIObject[] renderedComponents = null;
		if (components != null) {
			renderedComponents = new UIObject[components.length];
			for (int i = 0; i < components.length; i++)
				renderedComponents[i] = renderChildComponent(components[i], uuid, parent, context);
		}

		return renderedComponents;
	}

	protected UIObject renderChildComponent(ComponentGVO component, String uuid, String parent, String context) {
		UIObject renderedComponent = null;
		if (component != null) {
			try {
				Object object = COMPONENT_RENDERER_MAP.get(component.getClassName());
				if (object != null && object instanceof GWTUIRenderer) {
					GWTUIRenderer renderer = (GWTUIRenderer) object;					
					renderedComponent = renderer.render(component, uuid, parent, context);
				} else {
					ClientApplicationContext.getInstance().log("Unable to find renderer for class " + component.getClassName(), null);
				}
			} catch (Exception e) {
				ClientApplicationContext.getInstance().log("AbstractComponentRenderer:renderChildComponent", e);

			}
		}

		return renderedComponent;
	}

	private static String tempString;
	private static PopupMenu contextMenu;

	public static void applyContextMenu(final Event event, ComponentGVO componentGVO, String uuid, String parent) {
		contextMenu = new PopupMenu();
		MenuItemGVO menuItemGVO = componentGVO.getMenu();
		if (menuItemGVO != null && menuItemGVO.getSubMenus() != null) {
			RendererHelper.fillIn(menuItemGVO, contextMenu, uuid, parent, componentGVO.getContext());

			for (int i = 0; i < menuItemGVO.getSubMenus().length; i++) {
				if (menuItemGVO.getSubMenus()[i].getSubMenus() != null && menuItemGVO.getSubMenus()[i].getSubMenus().length > 0) {
					MenuBar menu = new MenuBar(true);
					tempString = menuItemGVO.getSubMenus()[i].getDisplayname();
					processMenu(menu, menuItemGVO.getSubMenus()[i].getSubMenus(), menuItemGVO.getSubMenus()[i].getDisplayname(), uuid, parent);
				} else {
					MenuItem menuItem = new MenuItem(menuItemGVO.getSubMenus()[i].getDisplayname(), new Command() {
						public void execute() {
						}
					});
					RendererHelper.fillIn(menuItemGVO.getSubMenus()[i], menuItem, uuid, parent, componentGVO.getContext());
					contextMenu.addItem(menuItem);
				}
			}
		}
		contextMenu.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				contextMenu.setPopupPosition(event.getClientX(), event.getClientY());
			}
		});
	}

	private static void processMenu(MenuBar menu, MenuItemGVO[] subMenus, String name, String uuid, String parent) {
		MenuBar subMenu = new MenuBar(true);
		for (int j = 0; j < subMenus.length; j++) {
			if (subMenus[j].getSubMenus() != null && subMenus[j].getSubMenus().length > 0) {
				processMenu(subMenu, subMenus[j].getSubMenus(), subMenus[j].getDisplayname(), uuid, parent);
			} else {
				if (subMenus[j] instanceof MenuItemSeparatorGVO) {
					menu.addSeparator();
				} else {
					MenuItem menuItem = new MenuItem(subMenus[j].getDisplayname(), (Command) null);
					RendererHelper.fillIn(subMenus[j], menuItem, uuid, parent, subMenus[j].getContext());
					subMenu.addItem(menuItem);
				}
			}
		}
		if (tempString.equals(name)) {
			contextMenu.addItem(new MenuItem(name, subMenu));
		} else {
			menu.addItem(new MenuItem(name, subMenu));
		}
	}
}
