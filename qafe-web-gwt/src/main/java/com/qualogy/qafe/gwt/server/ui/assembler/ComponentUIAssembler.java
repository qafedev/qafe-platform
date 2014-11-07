/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.server.ui.assembler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Area;
import com.qualogy.qafe.bind.presentation.component.BarChart;
import com.qualogy.qafe.bind.presentation.component.Button;
import com.qualogy.qafe.bind.presentation.component.CategoryAxis;
import com.qualogy.qafe.bind.presentation.component.ChartItem;
import com.qualogy.qafe.bind.presentation.component.CheckBox;
import com.qualogy.qafe.bind.presentation.component.Choice;
import com.qualogy.qafe.bind.presentation.component.ChoiceItem;
import com.qualogy.qafe.bind.presentation.component.ColumnChart;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.DataGrid;
import com.qualogy.qafe.bind.presentation.component.DataGridColumn;
import com.qualogy.qafe.bind.presentation.component.DropDown;
import com.qualogy.qafe.bind.presentation.component.DropDownItem;
import com.qualogy.qafe.bind.presentation.component.FileUpload;
import com.qualogy.qafe.bind.presentation.component.FrameComponent;
import com.qualogy.qafe.bind.presentation.component.HTMLComponent;
import com.qualogy.qafe.bind.presentation.component.Hidden;
import com.qualogy.qafe.bind.presentation.component.Image;
import com.qualogy.qafe.bind.presentation.component.Label;
import com.qualogy.qafe.bind.presentation.component.LineChart;
import com.qualogy.qafe.bind.presentation.component.LinearAxis;
import com.qualogy.qafe.bind.presentation.component.Link;
import com.qualogy.qafe.bind.presentation.component.ListBox;
import com.qualogy.qafe.bind.presentation.component.MapComponent;
import com.qualogy.qafe.bind.presentation.component.MenuDefinitionItem;
import com.qualogy.qafe.bind.presentation.component.MenuItem;
import com.qualogy.qafe.bind.presentation.component.MenuItemSeparator;
import com.qualogy.qafe.bind.presentation.component.OverFlowPanel;
import com.qualogy.qafe.bind.presentation.component.Panel;
import com.qualogy.qafe.bind.presentation.component.PanelDefinition;
import com.qualogy.qafe.bind.presentation.component.PanelRef;
import com.qualogy.qafe.bind.presentation.component.PasswordTextField;
import com.qualogy.qafe.bind.presentation.component.PieChart;
import com.qualogy.qafe.bind.presentation.component.PlotChart;
import com.qualogy.qafe.bind.presentation.component.RootPanel;
import com.qualogy.qafe.bind.presentation.component.Slider;
import com.qualogy.qafe.bind.presentation.component.SplitPanel;
import com.qualogy.qafe.bind.presentation.component.Stack;
import com.qualogy.qafe.bind.presentation.component.StackPanel;
import com.qualogy.qafe.bind.presentation.component.Tab;
import com.qualogy.qafe.bind.presentation.component.TabPanel;
import com.qualogy.qafe.bind.presentation.component.Table;
import com.qualogy.qafe.bind.presentation.component.TableCell;
import com.qualogy.qafe.bind.presentation.component.TableHeader;
import com.qualogy.qafe.bind.presentation.component.TableRow;
import com.qualogy.qafe.bind.presentation.component.TextArea;
import com.qualogy.qafe.bind.presentation.component.TextField;
import com.qualogy.qafe.bind.presentation.component.TileList;
import com.qualogy.qafe.bind.presentation.component.Toolbar;
import com.qualogy.qafe.bind.presentation.component.ToolbarItem;
import com.qualogy.qafe.bind.presentation.component.Tree;
import com.qualogy.qafe.bind.presentation.component.TreeItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class ComponentUIAssembler {
	
	public final static Logger logger = Logger.getLogger(ComponentUIAssembler.class.getName());

	private static final Map<Class<?>,UIAssembler> COMPONENT_RENDERER_MAP			= new HashMap<Class<?>,UIAssembler>();
	static {
	
		COMPONENT_RENDERER_MAP.put(Area.class, new AreaUIAssembler());
		COMPONENT_RENDERER_MAP.put(Button.class,new ButtonUIAssembler());
		COMPONENT_RENDERER_MAP.put(CheckBox.class, new CheckBoxUIAssembler()); 
		COMPONENT_RENDERER_MAP.put(Choice.class, new ChoiceUIAssembler());
		COMPONENT_RENDERER_MAP.put(ChoiceItem.class, new ChoiceItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(DataGrid.class, new DataGridUIAssembler());
		COMPONENT_RENDERER_MAP.put(DataGridColumn.class, new DataGridColumnUIAssembler());		
		COMPONENT_RENDERER_MAP.put(DropDown.class, new DropDownUIAssembler()); 
		COMPONENT_RENDERER_MAP.put(DropDownItem.class, new DropDownItemUIAssembler());		
		COMPONENT_RENDERER_MAP.put(FileUpload.class, new FileUploadUIAssembler());
		COMPONENT_RENDERER_MAP.put(Hidden.class, new HiddenUIAssembler());		
			
		COMPONENT_RENDERER_MAP.put(Image.class,new ImageUIAssembler());
		COMPONENT_RENDERER_MAP.put(Label.class, new LabelUIAssembler());
		COMPONENT_RENDERER_MAP.put(Link.class, new LinkUIAssembler());
		COMPONENT_RENDERER_MAP.put(ListBox.class, new ListBoxUIAssembler());		
		COMPONENT_RENDERER_MAP.put(MenuDefinitionItem.class, new MenuItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(MapComponent.class, new MapUIAssembler());		
		COMPONENT_RENDERER_MAP.put(MenuItem.class, new MenuItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(MenuItemSeparator.class, new MenuItemSeparatorUIAssembler());
		COMPONENT_RENDERER_MAP.put(Panel.class, new PanelUIAssembler());		
		COMPONENT_RENDERER_MAP.put(OverFlowPanel.class, new PanelUIAssembler());				
		COMPONENT_RENDERER_MAP.put(PanelRef.class, new PanelRefUIAssembler());
		COMPONENT_RENDERER_MAP.put(PanelDefinition.class, new PanelUIAssembler());
		COMPONENT_RENDERER_MAP.put(PasswordTextField.class, new PasswordTextFieldUIAssembler());		
		COMPONENT_RENDERER_MAP.put(RootPanel.class, new RootPanelUIAssembler());
		COMPONENT_RENDERER_MAP.put(Slider.class, new SliderUIAssembler());
		COMPONENT_RENDERER_MAP.put(StackPanel.class, new StackPanelUIAssembler());
		COMPONENT_RENDERER_MAP.put(Stack.class, new StackUIAssembler());
		COMPONENT_RENDERER_MAP.put(SplitPanel.class, new SplitPanelUIAssembler());		
		COMPONENT_RENDERER_MAP.put(Table.class, new TableUIAssembler());		
		COMPONENT_RENDERER_MAP.put(TableCell.class, new TableCellUIAssembler());
		COMPONENT_RENDERER_MAP.put(TableRow.class, new TableRowUIAssembler());
		COMPONENT_RENDERER_MAP.put(TableHeader.class, new TableHeaderUIAssembler());
		COMPONENT_RENDERER_MAP.put(TabPanel.class,new TabPanelUIAssembler());
		COMPONENT_RENDERER_MAP.put(Tab.class, new TabUIAssembler());
		COMPONENT_RENDERER_MAP.put(TextField.class, new TextFieldUIAssembler());
		COMPONENT_RENDERER_MAP.put(TextArea.class, new TextAreaUIAssembler());
		COMPONENT_RENDERER_MAP.put(Tree.class,new TreeUIAssembler());		
		COMPONENT_RENDERER_MAP.put(TreeItem.class,new TreeItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(Toolbar.class,new ToolbarUIAssembler());
		COMPONENT_RENDERER_MAP.put(ToolbarItem.class,new ToolbarItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(Window.class, new WindowUIAssembler());
		COMPONENT_RENDERER_MAP.put(HTMLComponent.class, new HTMLUIAssembler());
		COMPONENT_RENDERER_MAP.put(FrameComponent.class, new FrameUIAssembler());
		COMPONENT_RENDERER_MAP.put(TileList.class, new TileListUIAssembler());
		COMPONENT_RENDERER_MAP.put(BarChart.class, new BarChartUIAssembler());
		COMPONENT_RENDERER_MAP.put(CategoryAxis.class, new CategoryAxisUIAssembler());
		COMPONENT_RENDERER_MAP.put(ChartItem.class, new ChartItemUIAssembler());
		COMPONENT_RENDERER_MAP.put(ColumnChart.class, new ColumnChartUIAssembler());
		COMPONENT_RENDERER_MAP.put(LinearAxis.class, new LinearAxisUIAssembler());
		COMPONENT_RENDERER_MAP.put(LineChart.class, new LineChartUIAssembler());
		COMPONENT_RENDERER_MAP.put(PieChart.class, new PieChartUIAssembler());
		COMPONENT_RENDERER_MAP.put(PlotChart.class, new PlotChartUIAssembler());
		
		
		
	}
	
	public static ComponentGVO convert(Component object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer ss){
		ComponentGVO vo = null;
		if (object!=null){
			UIAssembler assembler = COMPONENT_RENDERER_MAP.get(object.getClass());
			if (assembler!=null){
				vo = assembler.convert(object, currentWindow, applicationMapping,context, ss);
		
			}else {
				logger.warning("Unable to find renderer for class " + object.getClass().getName() );
				
			}
		}
		
		return vo;
	}
}
