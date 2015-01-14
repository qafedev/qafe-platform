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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.HasChoice;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.HasEditable;
import com.qualogy.qafe.gwt.client.component.HasVisible;
import com.qualogy.qafe.gwt.client.component.QLabel;
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.component.QSliderBar;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.component.SpreadsheetCell;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.TabPanelRenderer;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.util.QAMLUtil;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO;

@Deprecated
public class SetPropertyExecute extends AbstractBuiltInExecute implements ExecuteCommand {

	public static final String MENU = "@menu";
	public static final String TOOLBAR = "@toolbar";

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof SetPropertyGVO) {
			SetPropertyGVO setProperty = (SetPropertyGVO) builtInFunction;
			if (builtInFunction.getComponents() != null) {
				for (BuiltInComponentGVO builtInComponentGVO : builtInFunction.getComponents()) {
					String component = builtInComponentGVO.getComponentIdUUID();
					List<UIObject> uiObjects = null;
					if (component == null) {
						uiObjects = RendererHelper.getNamedComponent(builtInComponentGVO.getComponentName());
					} else if (component.contains(MENU) || component.contains(TOOLBAR)) {
						uiObjects = getUIObjectsForMenuOrToolbar(component);
					} else {
						uiObjects = getUIObjects(component, setProperty);
					}
					if (uiObjects != null){
						for (int i=0; i< uiObjects.size(); i++) {
							UIObject uiObject = uiObjects.get(i);
							if (uiObject != null) {
								processProperty(uiObject, builtInComponentGVO, setProperty);
								if ((uiObject instanceof HasWidgets) && (appliesToChild(uiObject, setProperty))) {
									HasWidgets hasWidgets = (HasWidgets) uiObject;
									processWidgets(hasWidgets, builtInComponentGVO, setProperty);
								}
								// Choice items are also stored with the same id or name of the parent choice in repository to make it accessble for getting values.
								// So When setting a property of a choice we need to skip the items comes just after the Choice.
								// For eg: When we set the visibility of Choice we have to do it for only Choice to for the items.
								if ((uiObject instanceof HasChoice) && (uiObject instanceof IndexedPanel)) {
									int choiceItemCount = ((IndexedPanel)uiObject).getWidgetCount();
									i += choiceItemCount;
								}
							}
						}
					}
				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}

	/**
	 * Get all UIObjects of a UUID. <br>
	 * In case of a datagrid column, the UUID is virtual and starts with "datagridId.columnId|"
	 * All the cells of that column will be returned in that case.
	 *
	 * @param uuid the UUID of a real component or a virtual UUID that denotes a column of a dataGrid.
	 * @param setProperty
	 * @return the UIObjects of the given UUID
	 */
	@SuppressWarnings("unused")
	private List<UIObject> getUIObjects(String uuid, SetPropertyGVO setProperty) {
		List<UIObject> uiObjects = RendererHelper.getComponent(uuid);
		if (uiObjects == null) {
			List<UIObject> parentUIObjects = getParentUIObjects(uuid);
			if (parentUIObjects != null) {
				uiObjects = new ArrayList<UIObject>();
				for (UIObject parentUIObject : parentUIObjects) {
					if (parentUIObject instanceof HasDataGridMethods) {
						HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)parentUIObject;
						int rowIndex = getRowIndex(uuid, hasDataGridMethods);
						boolean rowSelection = (rowIndex > -1);

						boolean applyToAllRecords = false;
						String propertyName = setProperty.getProperty();
						if (QAMLConstants.PROPERTY_DISPLAYNAME.equals(propertyName)) {
							applyToAllRecords = true;
						} else if (QAMLConstants.PROPERTY_VISIBLE.equals(propertyName) && !rowSelection) {
							applyToAllRecords = true;
						}
						if (applyToAllRecords) {
							uiObjects.add(parentUIObject);
							continue;
						}

						uiObjects = collectCellUIObjects(uuid, rowIndex, uiObjects);
					}
				}
			}
		}
		return uiObjects;
	}

	/**
	 * This method is to get the menubar uiobject reference when set-property is used with ref="rootpanelid@menu" or ref="rootpanelid@toolbar"
	 * So we can set properties of menubar or toolbar dynamically.
	 * @param component
	 * @return
	 */
	private List<UIObject> getUIObjectsForMenuOrToolbar(String component) {
		List<UIObject> uiObjects;
		String type = "";
		if (component.contains(MENU)) {
			type = MENU;
			component = component.replace(MENU, "");
		} else if (component.contains(TOOLBAR)) {
			type = TOOLBAR;
			component = component.replace(TOOLBAR, "");
		}
		uiObjects = RendererHelper.getComponent(component);
		QRootPanel p = null;
		UIObject ui = null;

		// get handle to the QRootPanel
		if (uiObjects != null && uiObjects.get(0) instanceof VerticalPanel) {
			VerticalPanel verticalPanel = (VerticalPanel)uiObjects.get(0);
			if (verticalPanel.getParent().getParent().getParent() instanceof QRootPanel) {
				p = (QRootPanel)verticalPanel.getParent().getParent().getParent();
			}
		}
		if (p != null) {
			if (MENU.equals(type)) {
				ui = p.getMenuBar();
			} else if (TOOLBAR.equals(type)) {
				ui = p.getToolbar();
			}
		}
		uiObjects = new ArrayList<UIObject>();
		if (ui != null){
			uiObjects.add(ui);
		}
		return uiObjects;
	}

	private boolean appliesToChild(UIObject uiObject, SetPropertyGVO setProperty) {
		if (uiObject instanceof HasWidgets) {
			return !(QAMLConstants.PROPERTY_HEIGHT.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_WIDTH.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_TOOLTIP.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_DISPLAYNAME.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_TITLE.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_VISIBLE.equals(setProperty.getProperty())
					|| QAMLConstants.PROPERTY_ENABLED.equals(setProperty.getProperty()));
		}
		return false;
	}

	private void processWidgets(HasWidgets hasWidgets, BuiltInComponentGVO builtInComponentGVO, SetPropertyGVO setProperty) {
		for (Widget widget : hasWidgets) {
			processProperty(widget, builtInComponentGVO, setProperty);
			if (widget instanceof HasWidgets) {
				HasWidgets innerHasWidget = (HasWidgets) widget;
				processWidgets(innerHasWidget, builtInComponentGVO, setProperty);
			}
		}
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private void processProperty(UIObject uiObject, BuiltInComponentGVO builtInComponentGVO, SetPropertyGVO setProperty) {
		if (QAMLConstants.PROPERTY_ENABLED.equals(setProperty.getProperty()) || QAMLConstants.PROPERTY_DISABLED.equals(setProperty.getProperty())) {
			boolean value = Boolean.valueOf(setProperty.getValue()).booleanValue();
			if (QAMLConstants.PROPERTY_DISABLED.equals(setProperty.getProperty())) {
				value = !value;
			}
			if (uiObject instanceof HasEnabled) {
				HasEnabled hasEnabled = (HasEnabled)uiObject;
				hasEnabled.setEnabled(value);
			} else if (uiObject instanceof HasWidgets) {
				SetMaskHelper.setMaskEnable(uiObject.getElement().getAttribute(QAMLConstants.PROPERTY_ID), value, true);
				DOM.setElementPropertyBoolean(uiObject.getElement(), QAMLConstants.PROPERTY_DISABLED, !value);
			} else if (uiObject instanceof QSuggestBox) {
				QSuggestBox suggestField = (QSuggestBox)uiObject;
				suggestField.getTextBox().setEnabled(value);
			} else if (uiObject instanceof SpreadsheetCell) {
				SpreadsheetCell cell = (SpreadsheetCell) uiObject;
				cell.setEditable(value);
			}
		} else if (QAMLConstants.PROPERTY_EDITABLE.equals(setProperty.getProperty())) {
			boolean editable = Boolean.valueOf(setProperty.getValue()).booleanValue();
			HasEditable hasEditable = null;
			if (uiObject instanceof HasEditable) {
				hasEditable = (HasEditable)uiObject;
			} else if (uiObject instanceof TextBoxBase) {
				TextBoxBase textboxBase = (TextBoxBase)uiObject;
				if (textboxBase.getParent() instanceof HasEditable) {
					hasEditable = (HasEditable)textboxBase.getParent();
				} else {
					textboxBase.setReadOnly(!editable);
				}
			}
			if (hasEditable != null) {
				hasEditable.setEditable(editable);
			}
		} else if (QAMLConstants.PROPERTY_VISIBLE.equals(setProperty.getProperty())) {
			boolean value = Boolean.valueOf(setProperty.getValue()).booleanValue();
			if(uiObject instanceof HasVisible) {
				((HasVisible)uiObject).processVisible(value);
			} else if (uiObject instanceof HasDataGridMethods) {
				HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
				boolean resolved = false;
				String uuid = builtInComponentGVO.getComponentIdUUID();
				if (uuid != null) {
					boolean containsColumn = uuid.contains(".");
					if (containsColumn) {
						String columnId = uuid.replaceFirst(".+\\.", "").replaceFirst("\\|.+", "");
						hasDataGridMethods.setColumnVisible(columnId, value);
						resolved = true;
					}
				}
				if (!resolved) {
					uiObject.setVisible(value);
					hasDataGridMethods.redraw();
				}
			} else {
				uiObject.setVisible(value);
				if (uiObject instanceof Panel) {
					Panel p = (Panel) uiObject;
					Widget parent = p.getParent();
					if (parent != null && parent instanceof DeckPanel) {
						DeckPanel deckPanel = (DeckPanel) parent;
						int widgetIndex = deckPanel.getWidgetIndex(p);
						if (widgetIndex != -1) {
							if (deckPanel.getParent() != null && deckPanel.getParent().getParent() != null && deckPanel.getParent().getParent() instanceof TabPanel) {
								TabPanel tabs = ((TabPanel) (deckPanel.getParent().getParent()));
								TabPanelRenderer.setTabVisibility(tabs, widgetIndex, value, uiObject);
							}
						}
					}
				}
			}
		} else if (QAMLConstants.PROPERTY_HEIGHT.equals(setProperty.getProperty())) {
			try {
			    String height = setProperty.getValue();
	            if (!QAMLUtil.containsUnitIdentifier(height)) {
	                height += QAMLUtil.DEFAULT_UNIT;
	            }
				uiObject.setHeight(height);
			} catch (Exception e) {
				ClientApplicationContext.getInstance().log("Set Property on height failed", "Please check value of height (" + setProperty.getValue() + ")", true);
			}
		} else if (QAMLConstants.PROPERTY_WIDTH.equals(setProperty.getProperty())) {
			try {
			    String width = setProperty.getValue();
	            if (!QAMLUtil.containsUnitIdentifier(width)) {
	                width += QAMLUtil.DEFAULT_UNIT;
	            }
				uiObject.setWidth(width);
			} catch (Exception e) {
				ClientApplicationContext.getInstance().log("Set Property on width failed", "Please check value of width (" + setProperty.getValue() + ")", true);
			}
		} else if(QAMLConstants.PROPERTY_TOOLTIP.equals(setProperty.getProperty())){
			uiObject.setTitle(setProperty.getValue());
		} else if(QAMLConstants.PROPERTY_TITLE.equals(setProperty.getProperty())){
			if(uiObject instanceof CaptionPanel){
				CaptionPanel p = (CaptionPanel) uiObject;
				p.setCaptionText(setProperty.getValue());
			}
		} else if(QAMLConstants.PROPERTY_DISPLAYNAME.equals(setProperty.getProperty())){
			if (uiObject instanceof HasDataGridMethods) {
				HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
				String uuid = builtInComponentGVO.getComponentIdUUID();
				if (uuid != null) {
					boolean containsColumn = uuid.contains(".");
					if (containsColumn) {
						String columnId = uuid.replaceFirst(".+\\.", "").replaceFirst("\\|.+", "");
						String value = setProperty.getValue();
						hasDataGridMethods.setColumnLabel(columnId, value);
					}
				}
			} else if (uiObject instanceof PushButton) {
				((PushButton)uiObject).getUpFace().setText(setProperty.getValue());
				((PushButton)uiObject).getDownFace().setText(setProperty.getValue());
			} else if (uiObject instanceof HasText) {
				HasText t = (HasText)uiObject;
				t.setText(setProperty.getValue());
			} else if (uiObject instanceof VerticalPanel) {
				VerticalPanel vp = (VerticalPanel)uiObject;
				Widget tabPanelWidget = vp.getParent().getParent().getParent();
				if (tabPanelWidget instanceof TabPanel) {
					TabPanel tp = (TabPanel)tabPanelWidget;
					TabBar tb = tp.getTabBar();
					int tabCount =tp.getWidgetCount();
					for (int i=0;i < tabCount;i++){
						Widget w = tp.getWidget(i);
						if (w == vp){
							tb.setTabText(i, setProperty.getValue());
						}
					}
				}
			} else if (uiObject instanceof QWindowPanel) {
				QWindowPanel p = (QWindowPanel) uiObject;
				p.setCaption(setProperty.getValue());
			}
		} else if(QAMLConstants.PROPERTY_SELECTED_ROW.equals(setProperty.getProperty())){
			if (uiObject instanceof HasDataGridMethods){
				HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
				try {
					int rowNr = Integer.parseInt(setProperty.getValue());
					hasDataGridMethods.selectRow(rowNr);
				} catch (Exception e){
					ClientApplicationContext.getInstance().log("Set property on the datagrid selected row: the value ("+ setProperty.getValue()+") cannot be translated into an integer", e);
				}
			}
		} else if (QAMLConstants.PROPERTY_SELECTED.equals(setProperty.getProperty())){
			if (uiObject instanceof CheckBox){
				boolean value = Boolean.valueOf(setProperty.getValue()).booleanValue();
				((CheckBox)(uiObject)).setValue(value);
			} else if (uiObject instanceof ListBox){
				ListBox listBox = (ListBox)uiObject;
				int size = listBox.getItemCount();
				boolean selected=false;
				for(int i=0;i<size &&!selected;i++){
					if (listBox.getValue(i).equals(setProperty.getValue())){
						selected=true;
						listBox.setSelectedIndex(i);
					}
				}
			}
		} else if (QAMLConstants.PROPERTY_CURRENT_PAGE.equals(setProperty.getProperty())) {
			if (uiObject instanceof HasDataGridMethods) {
				HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
				try {
					if (setProperty.getValue() != null) {
						dataGridSortableTable.setCurrentPage(Integer.parseInt(setProperty.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (QAMLConstants.PROPERTY_PAGESIZE.equals(setProperty.getProperty())) {
			if (uiObject instanceof HasDataGridMethods) {
				HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
				try {
					if (setProperty.getValue() != null) {
						dataGridSortableTable.setPageSize(Integer.parseInt(setProperty.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (QAMLConstants.PROPERTY_MAX_TICKS.equals(setProperty.getProperty())) {
			if (uiObject instanceof QSliderBar){
				QSliderBar slider = (QSliderBar)uiObject;
				slider.setMaxValue(Double.valueOf(setProperty.getValue()));
			}
		} else if (QAMLConstants.PROPERTY_MIN_TICKS.equals(setProperty.getProperty())) {
			if (uiObject instanceof QSliderBar){
				QSliderBar slider = (QSliderBar)uiObject;
				slider.setMinValue(Double.valueOf(setProperty.getValue()));
			}
		} else if (QAMLConstants.PROPERTY_TICKSIZE.equals(setProperty.getProperty())) {
			if (uiObject instanceof QSliderBar){
				QSliderBar slider = (QSliderBar)uiObject;
				slider.setStepSize(Integer.valueOf(setProperty.getValue()));
			}
		} else if (QAMLConstants.PROPERTY_TICK_LABELS.equals(setProperty.getProperty())) {
			if (uiObject instanceof QSliderBar){
				QSliderBar slider = (QSliderBar)uiObject;
				slider.setTickLabels(Integer.valueOf(setProperty.getValue()));
			}
		}
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
}