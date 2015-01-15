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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
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
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.component.QSliderBar;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.component.SpreadsheetCell;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.TabPanelRenderer;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.util.QAMLUtil;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.SetMaskHelper;


public class SetPropertyHandler extends AbstractBuiltInHandler {

    public static final String MENU = "@menu";
    public static final String TOOLBAR = "@toolbar";
    
        
    @Override
    protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
            Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId,
            String eventSessionId, Queue derivedBuiltIns) {
        
        handleSetProperty(sender, builtInGVO, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }

    public void handleSetProperty(UIObject sender, BuiltInFunctionGVO builtInFunction, String appId, String windowId, String eventSessionId) {
        SetPropertyGVO setProperty = (SetPropertyGVO) builtInFunction;
        if (setProperty.getComponents() != null) {
            collectComponents(sender, setProperty, appId, windowId, eventSessionId);
        }
    }
    
    private void collectComponents(UIObject sender, SetPropertyGVO setProperty, String appId, String windowId, String eventSessionId) {
        for (BuiltInComponentGVO builtInComponentGVO : setProperty.getComponents()) {
            String componentId = builtInComponentGVO.getComponentId();
            String componentUUID = generateId(componentId, windowId, appId, eventSessionId);
            List<UIObject> uiObjects = getUIObjects(componentUUID, setProperty);
            if (uiObjects == null) {
                uiObjects = getUIObjectsByName(componentUUID);
            }
            if (uiObjects == null) {
                if (componentUUID.contains(MENU) || componentUUID.contains(TOOLBAR)) {
                    uiObjects = getUIObjectsForMenuOrToolbar(componentUUID);
                }
            }
            if (uiObjects != null){
                processSetProperty(appId, windowId, eventSessionId, setProperty, builtInComponentGVO, uiObjects);
            }
        }
    }

    private void processSetProperty(String appId, String windowId, String eventSessionId, SetPropertyGVO setProperty, BuiltInComponentGVO builtInComponentGVO,
            List<UIObject> uiObjects) {
        for (int i=0; i< uiObjects.size(); i++) {
            UIObject uiObject = uiObjects.get(i);
            if (uiObject != null) {
                processProperty(uiObject, appId, windowId, eventSessionId, builtInComponentGVO, setProperty);
                if ((uiObject instanceof HasWidgets) && (appliesToChild(uiObject, setProperty))) {
                    HasWidgets hasWidgets = (HasWidgets) uiObject;
                    processWidgets(hasWidgets, appId, windowId, eventSessionId, builtInComponentGVO, setProperty);
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
        List<UIObject> uiObjects = getUIObjectsById(uuid);
        if (uiObjects == null) {
            List<UIObject> parentUIObjects = getParentUIObjects(uuid);
            if (parentUIObjects != null) {
                uiObjects = new ArrayList<UIObject>();
                for (UIObject parentUIObject : parentUIObjects) {
                    uiObjects = handleDataGridObject(uuid, setProperty, uiObjects, parentUIObject);
                }
            }
        }
        return uiObjects;
    }

    private List<UIObject> handleDataGridObject(String uuid, SetPropertyGVO setProperty,
            List<UIObject> uiObjects, UIObject parentUIObject) {
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
                return uiObjects;
            }

            uiObjects = collectCellUIObjects(uuid, rowIndex, uiObjects);
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
            String property = setProperty.getProperty();
            return !(QAMLConstants.PROPERTY_HEIGHT.equals(property)
                    || QAMLConstants.PROPERTY_WIDTH.equals(property)
                    || QAMLConstants.PROPERTY_TOOLTIP.equals(property)
                    || QAMLConstants.PROPERTY_DISPLAYNAME.equals(property)
                    || QAMLConstants.PROPERTY_TITLE.equals(property)
                    || QAMLConstants.PROPERTY_VISIBLE.equals(property)
                    || QAMLConstants.PROPERTY_ENABLED.equals(property));
        }
        return false;
    }

    private void processWidgets(HasWidgets hasWidgets, String appId, String windowId, String eventSessionId, 
            BuiltInComponentGVO builtInComponentGVO, SetPropertyGVO setProperty) {
        for (Widget widget : hasWidgets) {
            processProperty(widget, appId, windowId, eventSessionId, builtInComponentGVO, setProperty);
            if (widget instanceof HasWidgets) {
                HasWidgets innerHasWidget = (HasWidgets) widget;
                processWidgets(innerHasWidget, appId, windowId, eventSessionId, builtInComponentGVO, setProperty);
            }
        }
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    private void processProperty(UIObject uiObject, String appId, String windowId, String eventSessionId, 
            BuiltInComponentGVO builtInComponentGVO, SetPropertyGVO setProperty) {
        String property = setProperty.getProperty();
        String value = getValue(uiObject, setProperty.getParameter(), appId, windowId, eventSessionId).toString();
        if (QAMLConstants.PROPERTY_ENABLED.equals(property) || QAMLConstants.PROPERTY_DISABLED.equals(property)) {
            handleEnabled(uiObject, value, property);
        } else if (QAMLConstants.PROPERTY_EDITABLE.equals(property)) {
            handleEditable(uiObject, value);
        } else if (QAMLConstants.PROPERTY_VISIBLE.equals(property)) {
            handleVisible(uiObject, builtInComponentGVO, value);
        } else if (QAMLConstants.PROPERTY_HEIGHT.equals(property)) {
            handleHeight(uiObject, value);
        } else if (QAMLConstants.PROPERTY_WIDTH.equals(property)) {
            handleWidth(uiObject, value);
        } else if(QAMLConstants.PROPERTY_TOOLTIP.equals(property)){
            uiObject.setTitle(value);
        } else if(QAMLConstants.PROPERTY_TITLE.equals(property)){
            handleTitle(uiObject, value);
        } else if(QAMLConstants.PROPERTY_DISPLAYNAME.equals(property)){
            handleDisplayName(uiObject, builtInComponentGVO, value);
        } else if(QAMLConstants.PROPERTY_SELECTED_ROW.equals(property)){
            handleSelectedRow(uiObject, value);
        } else if (QAMLConstants.PROPERTY_SELECTED.equals(property)){
            handleSelected(uiObject, value);
        } else if (QAMLConstants.PROPERTY_CURRENT_PAGE.equals(property)) {
            handleCurrentPage(uiObject, value);
        } else if (QAMLConstants.PROPERTY_PAGESIZE.equals(property)) {
            handlePageSize(uiObject, value);
        } else if (QAMLConstants.PROPERTY_MAX_TICKS.equals(property)) {
            handleMaxTicks(uiObject, value);
        } else if (QAMLConstants.PROPERTY_MIN_TICKS.equals(property)) {
            handleMinTicks(uiObject, value);
        } else if (QAMLConstants.PROPERTY_TICKSIZE.equals(property)) {
            handleTickSize(uiObject, value);
        } else if (QAMLConstants.PROPERTY_TICK_LABELS.equals(property)) {
            handleTickLabel(uiObject, value);
        }
    }
    // CHECKSTYLE.ON: CyclomaticComplexity

    private void handleTitle(UIObject uiObject, String value) {
        if(uiObject instanceof CaptionPanel){
            CaptionPanel p = (CaptionPanel) uiObject;
            p.setCaptionText(value);
        }
    }

    private void handleMaxTicks(UIObject uiObject, String value) {
        if (uiObject instanceof QSliderBar){
            QSliderBar slider = (QSliderBar)uiObject;
            slider.setMaxValue(Double.valueOf(value));
        }
    }

    private void handleMinTicks(UIObject uiObject, String value) {
        if (uiObject instanceof QSliderBar){
            QSliderBar slider = (QSliderBar)uiObject;
            slider.setMinValue(Double.valueOf(value));
        }
    }

    private void handleTickSize(UIObject uiObject, String value) {
        if (uiObject instanceof QSliderBar){
            QSliderBar slider = (QSliderBar)uiObject;
            slider.setStepSize(Integer.valueOf(value));
        }
    }

    private void handleTickLabel(UIObject uiObject, String value) {
        if (uiObject instanceof QSliderBar){
            QSliderBar slider = (QSliderBar)uiObject;
            slider.setTickLabels(Integer.valueOf(value));
        }
    }
    
    // @TODO must be fixed after pagination is fixed for client side handling
    private void handlePageSize(UIObject uiObject, String value) {
        if (uiObject instanceof HasDataGridMethods) {
            HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
            try {
                if (value != null) {
                    dataGridSortableTable.setPageSize(Integer.parseInt(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCurrentPage(UIObject uiObject, String value) {
        if (uiObject instanceof HasDataGridMethods) {
            HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
            try {
                if (value != null) {
                    dataGridSortableTable.setCurrentPage(Integer.parseInt(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSelected(UIObject uiObject, String setPropertyvalue) {
        if (uiObject instanceof CheckBox){
            boolean value = Boolean.valueOf(setPropertyvalue).booleanValue();
            ((CheckBox)(uiObject)).setValue(value);
        } else if (uiObject instanceof ListBox){
            ListBox listBox = (ListBox)uiObject;
            int size = listBox.getItemCount();
            boolean selected=false;
            for(int i=0;i<size &&!selected;i++){
                if (listBox.getValue(i).equals(setPropertyvalue)){
                    selected=true;
                    listBox.setSelectedIndex(i);
                }
            }
        }
    }

    private void handleSelectedRow(UIObject uiObject, String value) {
        if (uiObject instanceof HasDataGridMethods){
            HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
            try {
                int rowNr = Integer.parseInt(value);
                hasDataGridMethods.selectRow(rowNr);
            } catch (Exception e){ 
                log("Set property on the datagrid selected row: the value ("+ value +") cannot be translated into an integer");
            }
        }
    }

    private void handleDisplayName(UIObject uiObject, BuiltInComponentGVO builtInComponentGVO,
            String value) {
        if (uiObject instanceof HasDataGridMethods) {
            HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
            String uuid = builtInComponentGVO.getComponentIdUUID();
            if (uuid != null) {
                boolean containsColumn = uuid.contains(".");
                if (containsColumn) {
                    String columnId = uuid.replaceFirst(".+\\.", "").replaceFirst("\\|.+", "");
                    hasDataGridMethods.setColumnLabel(columnId, value);
                }
            }
        } else if (uiObject instanceof PushButton) {
            ((PushButton)uiObject).getUpFace().setText(value);
            ((PushButton)uiObject).getDownFace().setText(value);
        } else if (uiObject instanceof HasText) {
            HasText t = (HasText)uiObject;
            t.setText(value);
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
                        tb.setTabText(i, value);
                    }
                }
            }
        } else if (uiObject instanceof QWindowPanel) {
            QWindowPanel p = (QWindowPanel) uiObject;
            p.setCaption(value);
        }
    }

    private void handleWidth(UIObject uiObject, String value) {
        try {
            // uiObject.setWidth expects a css unit, so if no unit is available, we set it to the default
            // if no unit is specified, uiObject.setWidth() generates invalid css
            String width = value;
            if (!QAMLUtil.containsUnitIdentifier(width)) {
                width += QAMLUtil.DEFAULT_UNIT;
            }
            uiObject.setWidth(width);
        } catch (Exception e) {
            log("Set Property on height failed","Please check value of width (" + value + ")", true);
        }
    }

    private void handleHeight(UIObject uiObject, String value) {
        try {
            // uiObject.setWidth expects a css unit, so if no unit is available, we set it to the default
            // if no unit is specified, uiObject.setWidth() generates invalid css.
            String height = value;
            if (!QAMLUtil.containsUnitIdentifier(height)) {
                height += QAMLUtil.DEFAULT_UNIT;
            }
            uiObject.setHeight(height);
        } catch (Exception e) {
            log("Set Property on height failed", "please check value of height (" + value + ")", true);
        }
    }

    private void handleVisible(UIObject uiObject, BuiltInComponentGVO builtInComponentGVO,
            String setPropertyValue) {
        boolean value = Boolean.valueOf(setPropertyValue).booleanValue();
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
    }

    private void handleEnabled(UIObject uiObject, String propertyValue, String propertyType) {
        boolean value = Boolean.valueOf(propertyValue).booleanValue();
        if (QAMLConstants.PROPERTY_DISABLED.equals(propertyType)) {
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
    }

    private void handleEditable(UIObject uiObject, String propertyValue) {
        boolean editable = Boolean.valueOf(propertyValue).booleanValue();
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
    }

}
