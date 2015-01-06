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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

//import com.qualogy.qafe.mgwt.client.component.AreaWidget;
import com.qualogy.qafe.mgwt.client.component.HasData;
import com.qualogy.qafe.mgwt.client.component.HasDataChangeHandlers;
import com.qualogy.qafe.mgwt.client.component.HasEnable;
//import com.qualogy.qafe.mgwt.client.component.LabeledPasswordFieldWidget;
//import com.qualogy.qafe.mgwt.client.component.LabeledTextFieldWidget;
//import com.qualogy.qafe.mgwt.client.component.QDatePicker;
//import com.qualogy.qafe.mgwt.client.component.QSuggestBox;
//import com.qualogy.qafe.mgwt.client.component.QWindowPanel;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequired;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequiredClass;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.ClickEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.DoubleClickEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnChangeEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnEnterEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnExitEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnFinishEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnFocusEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyDownEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyPressEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyUpEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnLoadEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseDownEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseEnterEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseExitEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseMoveEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseUpEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnTimerEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnUnLoadEventListenerGVO;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.EventFactory;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.OnTimerHandler;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;

public class RendererHelper {

	public static final String ATTRIBUTE_STYLE = "style";
	public static final String ATTRIBUTE_ORIGIN_STYLE = "origin-style";
	public static final String EMPTY_VALUE = "EMPTY";
	public static final String ATTRIBUTE_ALIGN = "align";
	public static final String ELEMENT_TD = "TD";
	public static final String ELEMENT_TH = "TH";
	public static final String DEFAULT_INVALID_STYLE = "qafe_invalid_field";
	public static final String DEFAULT_VALID_STYLE = "qafe_valid_field";

	private RendererHelper() {
	}

	public static String generateId(final String id, final String parent, String context) {
		return (id + (parent == null ? "" : ("|" + parent)) + (context == null ? "" : ("|" + context)));
	}

	public static void fillIn(ComponentGVO vo, UIObject ui, UIObject mainUI, String uuid, String parent, String context) {

		addDimension(vo, ui, uuid, parent);
		addId(vo, ui, mainUI, uuid, parent, context, true);
		addUUID(vo, ui, uuid);
		addWindowID(vo, ui, uuid);
		addDisabledInfo(vo, ui);
		addStyle(vo, ui);
		addEvents(vo, ui, uuid);
		addVisibleInfo(vo, ui);
		addTooltip(vo, ui);
		addChangeListenerForWindowModified(vo, ui, uuid);

	}
	
	public static void fillIn(ComponentGVO vo, UIObject ui, String uuid, String parent, String context) {
		fillIn(vo, ui, null, uuid, parent, context);
	}
	public static void addDimension(ComponentGVO vo, UIObject ui, String uuid, String parent) {
		// Test if the component or the object are null
		if (vo != null && ui != null) {

			// The width of the component
			String strVoWidth = vo.getWidth();

			// The height of the component
			String strVoHeight = vo.getHeight();

			// Test if the width is null or the length <= 0
			if (strVoWidth != null && strVoWidth.length() > 0) {

				// Test if the width is negative
				if (strVoWidth.indexOf("-") != -1) {
					ClientApplicationContext.getInstance().log("The width should not be negative:" + strVoWidth);
				} else {
					ui.setWidth(strVoWidth);
				}
			}

			// Test if the height is null or the length <= 0
			if (strVoHeight != null && strVoHeight.length() > 0) {

				// Test if the height is negative
				if (strVoHeight.indexOf("-") != -1) {
					ClientApplicationContext.getInstance().log("The height should not be negative:" + strVoHeight);
				} else {
					ui.setHeight(strVoHeight);
				}
			}
			
			if (strVoWidth != null && strVoHeight != null && strVoWidth.equals("0") && strVoHeight.equals("0")){
				ui.setVisible(false);
			}
		}

	}

	private static void storeNamedComponent(ComponentGVO vo, UIObject ui, String uuid, String parent, String context) {
		if (vo != null && ui != null) {
			if (vo.getFieldName() != null && vo.getFieldName().length() > 0) {
				String key = generateId(vo.getFieldName(), parent, context);
				ComponentRepository.getInstance().putNamedComponent(key, ui);
			}
		}
	}

	private static void storeGroupedComponent(ComponentGVO vo, UIObject ui, String uuid, String parent, String context) {
		if (vo != null && ui != null) {
			if (vo.getGroup() != null && vo.getGroup().length() > 0) {
				String key = generateId(vo.getGroup(), parent, context);
				ComponentRepository.getInstance().putGroupedComponent(key, ui);
			}
		}
	}
	public static void addTooltip(ComponentGVO vo, UIObject ui) {
		if (vo != null && ui != null) {
			if (vo.getTooltip() != null) {
				ui.setTitle(vo.getTooltip());
			}
		}

	}

	private static void addChangeListenerForWindowModified(final ComponentGVO vo, UIObject ui, final String uuid) {
		if (ui != null) {
			if (ui instanceof HasValueChangeHandlers) {
				HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers) ui;
				hasValueChangeHandlers.addValueChangeHandler(new ValueChangeHandler() {

					public void onValueChange(ValueChangeEvent event) {
//						WindowPanel window = ClientApplicationContext.getInstance().getWindow(vo.getWindow(), uuid);
//						if (window != null && window instanceof QWindowPanel) {
//							((QWindowPanel) window).setModified(true);
//						}
					}
				});
			}
		}

	}


	public static void addWindowID(ComponentGVO vo, UIObject ui, String uuid) {
		if (vo != null && ui != null && vo.getWindow() != null && vo.getWindow().length() > 0) {
			DOM.setElementProperty(ui.getElement(), "windowid", vo.getWindow());
		}

	}

	public static void addVisibleInfo(ComponentGVO vo, UIObject ui) {
		if (vo != null && ui != null) {
			if (!vo.getVisible().booleanValue()) {
				ui.setVisible(vo.getVisible().booleanValue());
			}
		}
	}

	public static void addUUID(ComponentGVO vo, UIObject ui, String uuid) {
		if (vo != null && ui != null) {
			DOM.setElementProperty(ui.getElement(), "uuid", uuid);
		}
	}

	public static List<UIObject> getComponent(String uuid) {
		return ComponentRepository.getInstance().getComponent(uuid);

	}

	public static String getComponentId(UIObject sender) {
		if (sender != null) {
			return DOM.getElementProperty(sender.getElement(), "id");
		} else {
			return null;
		}
	}

	
	
	public static String getComponentAttributeValue(UIObject uiObject, String attributeName) {
		if (uiObject != null) {
			return DOM.getElementAttribute(getElement(uiObject), attributeName);
		} else {
			return null;
		}
	}
	
	public static String getComponentPropertyValue(UIObject uiObject, String propertyName) {
		if (uiObject != null) {
			return DOM.getElementProperty(getElement(uiObject), propertyName);
		} else {
			return null;
		}
	}
	
	public static Element getElement(UIObject uiObject) {
		if(uiObject != null) {
//			if(uiObject instanceof LabeledTextFieldWidget ) {
//				UIObject textBox = ((LabeledTextFieldWidget) uiObject).getTextbox();
//				if(textBox != null) {
//					return ((LabeledTextFieldWidget) uiObject).getTextbox().getElement();					
//				}
//			} else if(uiObject instanceof LabeledPasswordFieldWidget ) {
//				return ((LabeledPasswordFieldWidget) uiObject).getDataComponent().getElement();
//			} 
			return uiObject.getElement();
		}
		return null;
	}
	
	
	public static List<UIObject> getNamedComponent(String uuid) {
		return ComponentRepository.getInstance().getNamedComponent(uuid);
	}
	
	public static List<UIObject> getGroupedComponent(String uuid) {
		return ComponentRepository.getInstance().getGroupedComponent(uuid);
	}

	public static void storeComponent(ComponentGVO vo, String uuid, UIObject uiObject, String parent, String context) {
		String key = generateId(vo.getId(), parent, context);
		ComponentRepository.getInstance().putComponent(key, uiObject);
	}

	public static void addMenu(ComponentGVO vo, UIObject ui, String uuid, String parent) {
		if (vo != null && ui != null) {
			if (vo.getMenu() != null) {
				MenuBar menu = new MenuBar();
				menu.setAutoOpen(true);
				menu.setWidth("100%");
				UIObject renderedComponent = new MenuItemRenderer().render(vo.getMenu(), null, uuid, parent, vo.getContext(), null);
				if (renderedComponent instanceof MenuBar) {
					menu.addItem(vo.getMenu().getDisplayname(), (MenuBar) renderedComponent);
				} else if (renderedComponent instanceof MenuItem) {
					menu.addItem((MenuItem) renderedComponent);
				}
				if (ui instanceof DockPanel) {
					((DockPanel) ui).add(menu, DockPanel.NORTH);
				} else if (ui instanceof Grid || ui instanceof VerticalPanel || ui instanceof HorizontalPanel)
					((Panel) ui).add(menu);
			}
		}

	}



	public static void addDisabledInfo(ComponentGVO vo, UIObject ui) {
		if (vo != null && ui != null) {
			if (ui instanceof HasEnable) {
				boolean enabled = !vo.isDisabled();
				HasEnable hasEnable = (HasEnable)ui;
				hasEnable.setEnabled(enabled);
			} else if (vo.isDisabled().booleanValue()) {
				DOM.setElementAttribute(getElement(ui), "disabled", "true");
			}
		}

	}

	public static void addStyle(ComponentGVO vo, UIObject ui) {
		if (vo != null) {
			String styles = vo.getStyleClass();
			if (styles!=null && !ui.getStyleName().contains(styles)){
				ui.addStyleName(styles);
			}
			if (ui != null) {
				setStyleForElement(ui.getElement(), vo.getStyleProperties());
			}
		}
	}

	public static void setStyleForElement(Element element, String[][] styleProperties) {
		if (styleProperties != null && element != null) {
			for (int i = 0; i < styleProperties.length; i++) {
				if (styleProperties[i].length == 2) {
					setStyleForElement(element, styleProperties[i][0], styleProperties[i][1]);	
				}
			}
		}
	}

	public static void setStyleForElement(Element element, String property, String value) {
		try {
			DOM.setStyleAttribute(element, property, value);
			updateOriginStyleIfExists(element, property, value);
			Element parentElement = (Element)element.getParentElement();
		
			if(parentElement!=null && ATTRIBUTE_ALIGN.equals(property) && (ELEMENT_TD.equals(parentElement.getTagName()) || ELEMENT_TH.equals(parentElement.getTagName()) ) ){				
				DOM.setElementAttribute(parentElement, property, value);
			}
		} catch (Exception e) {
			ClientApplicationContext.getInstance().log("RendererHelper: Setting style failed", "error in addstyle invalid property: " + property + ":" + value + "\n Mind that the property might be in camelCase (see http://www.w3schools.com/htmldom/dom_obj_style.asp for more info)", false, false, e);
		}
	}
	
	private static void updateOriginStyleIfExists(Element element, String property, String value) {
		// Update the original style, if exists. This is needed for conditional styling
		String originInlineStyle = DOM.getElementAttribute(element, ATTRIBUTE_STYLE);
		if ((originInlineStyle != null) && (originInlineStyle.length() > 0)) {
			if (originInlineStyle.equals(EMPTY_VALUE)) {
				originInlineStyle = "";
			}
			value = (value != null) ? value : "";
			originInlineStyle += toCSSDeclaration(property, value);
			originInlineStyle = distinctStyle(originInlineStyle);
			DOM.setElementAttribute(element, ATTRIBUTE_STYLE, originInlineStyle);
		} 
	}
	
	private static String toCSSDeclaration(String property, String value) {
		return property + ":" + value + ";";
	}
	
	private static String distinctStyle(String style) {
		if (style != null) {
			Map<String,String> cssDeclarationsMap = new LinkedHashMap<String,String>();
			String[] cssDeclarations = style.split(";");
			for (int i=0; i<cssDeclarations.length; i++) {
				String cssStyle = cssDeclarations[i];
				if (cssStyle.length() > 0) {
					String[] cssDeclaration = cssStyle.split(":");
					if (cssDeclaration.length > 0) {
						String property = splitCamelCase(cssDeclaration[0]);
						if (cssDeclaration.length == 2) {
							String value = cssDeclaration[1];
							cssDeclarationsMap.put(property, value);
						} else if (cssDeclaration.length == 1) {
							cssDeclarationsMap.remove(property);
						}
					}
				}
			}
			StringBuffer distinctStyle = new StringBuffer(); 
			for (Entry<String, String> entry: cssDeclarationsMap.entrySet()){
					String cssDeclaration = toCSSDeclaration(entry.getKey(), entry.getValue());
				distinctStyle.append(cssDeclaration);
			}
			return distinctStyle.toString();
		}
		return null;
	}
	
	public static String splitCamelCase(String camelCase) {
		String result = null;
		if (camelCase != null) {
			result = "";
			String[] words = camelCase.split("(?=[A-Z])");
			for (int i=0; i<words.length; i++) {
				result += words[i].toLowerCase() + (i < words.length - 1 ? "-" : "");	
			}	
		}
		return result;
	}
	
	public static void addId(ComponentGVO vo, UIObject ui, String uuid, String parent, String context, boolean addToStore) {
		addId(vo, ui, null, uuid, parent, context, addToStore);
	}
	public static void addId(ComponentGVO vo, UIObject ui, UIObject mainUI, String uuid, String parent, String context, boolean addToStore) {
		if (vo != null && ui != null) {
			if (vo.getId() != null) {
				String id = generateId(vo.getId(), parent, context);
				DOM.setElementAttribute(ui.getElement(), "id", id);
				if(addToStore){
					UIObject uiToStore = mainUI != null ? mainUI : ui;
					storeComponent(vo, uuid, uiToStore, parent, context);
				}
			}
			addName(vo, ui, mainUI, uuid, parent, context);
			addGroup(vo,ui,mainUI,uuid,parent,context);

			DOM.setElementAttribute(ui.getElement(), "pc", parent);
			DOM.setElementAttribute(ui.getElement(), "app-context", context);

		}
	}
	
	public static void addName(ComponentGVO vo, UIObject ui, String uuid, String parent, String context) {
		addName(vo, ui, null, uuid, parent, context);
	}
	public static void addName(ComponentGVO vo, UIObject ui, UIObject mainUI, String uuid, String parent, String context) {
		if (vo != null && ui != null) {
			if (vo.getFieldName() != null) {
				DOM.setElementAttribute(ui.getElement(), "fn", vo.getFieldName());
				UIObject uiToStore = mainUI != null ? mainUI : ui;
				storeNamedComponent(vo, uiToStore, uuid, parent, context);
			}
		}
	}
	
	public static void addGroup(ComponentGVO vo, UIObject ui, UIObject mainUI, String uuid, String parent, String context) {
		if (vo != null && ui != null) {
			if (vo.getGroup() != null && vo.getGroup().length()>0) {
				DOM.setElementAttribute(ui.getElement(), "grp", vo.getGroup());
				UIObject uiToStore = mainUI != null ? mainUI : ui;
				storeGroupedComponent(vo, uiToStore, uuid, parent, context);
			}
		}
	}

	public static String getComponentContext(UIObject ui) {
		return DOM.getElementAttribute(ui.getElement(), "app-context");
	}
	
	public static void setComponentContext(UIObject ui, String componentContext) {
		DOM.setElementAttribute(ui.getElement(), "app-context", componentContext);
	}
	
	public static String getParentComponent(UIObject ui) {
		return DOM.getElementAttribute(ui.getElement(), "pc");
	}
	
	public static void setParentComponent(UIObject ui, String parent) {
		DOM.setElementAttribute(ui.getElement(), "pc", parent);
	}

	public static String getNamedComponentName(UIObject ui) {
		return DOM.getElementAttribute(ui.getElement(), "fn");
	}

	public static boolean isNamedComponent(UIObject ui) {
		return checkIsComponentOfType(ui, "fn");
//		if (ui!=null){
//			String fn = DOM.getElementAttribute(ui.getElement(), "fn");
//			return (fn != null && fn.length() > 0);
//		} else {
//			return false;
//		}
	}
	public static boolean isGroupedComponent(UIObject ui) {
		return checkIsComponentOfType(ui, "grp");
	}
	
	private static boolean checkIsComponentOfType(UIObject ui,String type){
		if (ui!=null){
			String t = DOM.getElementAttribute(ui.getElement(), type);
			return (t != null && t.length() > 0);
		} else {
			return false;
		}
	}
	
	public static boolean isRequiredComponent(UIObject ui) {
		String required = DOM.getElementAttribute(getElement(ui), "required");
		// When rendering disabled is set as attribute of ui
		String disabledAttributeValue = DOM.getElementAttribute(getElement(ui), "disabled");
		// When using set-property disabled is set as property of ui
		String disabledPropertyValue = DOM.getElementProperty(getElement(ui), "disabled");
		
		boolean isDisabled = ("true".equalsIgnoreCase(disabledAttributeValue)) || ("disabled".equalsIgnoreCase(disabledAttributeValue) 
				|| "true".equalsIgnoreCase(disabledPropertyValue)) || ("disabled".equalsIgnoreCase(disabledPropertyValue));
		boolean isRequired = required != null && required.length() > 0 && ( "true".equalsIgnoreCase(required) || "required".equalsIgnoreCase(required));

		return (!isDisabled && isRequired);
	}

	public static void addEvents(ComponentGVO vo, UIObject ui, String uuid) {
		if (vo != null) {
			EventListenerGVO[] events = vo.getEvents();
			if (events != null) {
				for (int i = 0; i < events.length; i++) {

					processEvents(vo, ui, events[i], events[i].getInputvariablesList(), uuid);

				}
			}
		}
	}

	/*
	 * General remark about stylesheets :
	 * 
	 * The order of applied style classes is dependent on the order of classes in the stylesheets
	 * and the order of insertion in html of the stylesheets themselves e.g.
	 * 
	 * .style1 {
	 *		font-color: red;
	 * }
	 * .style2 {
	 * 		font-color: blue;
	 * }
	 * 
	 * <label class="style2 style1"/>
	 * 
	 * label font-color will be blue -> style2 overrides style1
	 * 
	 */
	public static void handleRequiredAttribute(ComponentGVO gvo, UIObject uiObject) {

		if (gvo instanceof HasRequired) {
			HasRequired hasRequiredPropertyI = (HasRequired)gvo;
			final ComponentGVO finalGvo = gvo;
			if (hasRequiredPropertyI.getRequired() != null) {
				DOM.setElementAttribute(getElement(uiObject), "required", hasRequiredPropertyI.getRequired().toString());
				
				handleRequiredStyle(gvo, uiObject);

				if (uiObject instanceof HasValueChangeHandlers) {
					HasValueChangeHandlers<String> hasValueChangeHandlers = (HasValueChangeHandlers<String>) uiObject;
					hasValueChangeHandlers.addValueChangeHandler(new ValueChangeHandler<String>() {

						public void onValueChange(ValueChangeEvent<String> event) {
							if (event.getSource() instanceof UIObject) {
								handleRequiredStyle(finalGvo, (UIObject)event.getSource());
							}							
						}
					});

				}				
				if (uiObject instanceof HasChangeHandlers){
					HasChangeHandlers hasChangeHandlers = (HasChangeHandlers)uiObject;
					hasChangeHandlers.addChangeHandler(new ChangeHandler(){
												
						public void onChange(ChangeEvent event) {
							if (event.getSource() instanceof ListBox) {
								handleRequiredStyle(finalGvo, (UIObject)event.getSource());
							}						
						}});
				}
			}
		}
	}

	public static void handleRequiredStyle(final ComponentGVO gvo, UIObject uiObject) {
		boolean applyRequiredStyle = isEmptyOrNotSelected(uiObject);
		if(hasNotEmptyRequiredClass(gvo)) {
			HasRequiredClass requiredClassNameComponent = (HasRequiredClass) gvo;
			String invalidStyleName = requiredClassNameComponent.getRequiredStyleClassName();
			handleRequiredClassStyle(uiObject, invalidStyleName, applyRequiredStyle);
		} else {
			handleRequiredClassStyle(uiObject, DEFAULT_INVALID_STYLE, applyRequiredStyle);
		}
	}
	
	private static boolean isEmptyOrNotSelected(UIObject uiObject) {
		boolean emptyOrNotSelected = true;
		if(hasData(uiObject)) {
			Object value = getData(uiObject); 
			if (value != null && value.toString().length() > 0) {
				emptyOrNotSelected = false;
			} else {
				emptyOrNotSelected = Boolean.parseBoolean(DOM.getElementAttribute(getElement(uiObject), "required"));
			}
		}
		return emptyOrNotSelected;
	}
	
	private static boolean hasData(UIObject ui) {
		return (ui instanceof HasText) || (ui instanceof HasValue) || (ui instanceof HasData) 
			|| (ui instanceof ListBox);// || (ui instanceof ValueSpinner);
	}

	private static Object getData(UIObject ui) {
		if (ui instanceof HasValue) {
			return ((HasValue)ui).getValue();
		} else if (ui instanceof HasText) {
			return ((HasText)ui).getText();
		} else if (ui instanceof HasData) {
			return ((HasData)ui).getData();			
		} else if (ui instanceof ListBox) {
			ListBox listBox = (ListBox) ui;
			if(listBox.getSelectedIndex() > 0) {
				return listBox.getValue(listBox.getSelectedIndex());				
			}
		} 
//		else if (ui instanceof ValueSpinner) {
//			ValueSpinner spinner = (ValueSpinner) ui;
//			return spinner.getTextBox().getValue();
//		}
		return null;
	}
		
	public static boolean hasNotEmptyRequiredClass(ComponentGVO gvo) {
		boolean hasRequiredClassName = false;
		if(gvo instanceof HasRequiredClass) {
			HasRequiredClass requiredComponent = (HasRequiredClass) gvo;
			String requiredStyleClassName = requiredComponent.getRequiredStyleClassName();
			if (requiredStyleClassName != null && requiredStyleClassName.length() > 0) {
				hasRequiredClassName = true;
			}
		}
		return hasRequiredClassName;
	}

	public static void handleRequiredClassStyle(UIObject ui, String invalidClassName, Boolean applyRequiredStyle) {
		if(applyRequiredStyle) {
			ui.removeStyleName(DEFAULT_VALID_STYLE);
			ui.addStyleName(invalidClassName);
		} else {
			ui.removeStyleName(invalidClassName);
			ui.addStyleName(DEFAULT_VALID_STYLE);
		}
	}
	
	public static void processEvents(ComponentGVO vo, UIObject ui, EventListenerGVO event, List<com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO> inputVariables) {
		processEvents(vo, ui, event, inputVariables, null);
	}
	
	public static void processEvents(ComponentGVO vo, UIObject ui, EventListenerGVO event, List<com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO> inputVariables, String appId) {
		
		if (ui != null) {

//			if (event instanceof OnEnterEventListenerGVO) {
//				if (ui instanceof HasAllKeyHandlers) {
//					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers) ui;
//					hasAllKeyHandlers.addKeyDownHandler(EventFactory.createOnEnterListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onenter listener", null);
//				}
//			}
//
//			if (event instanceof OnFocusEventListenerGVO) {
//				if (ui instanceof HasFocusHandlers) {
//					HasFocusHandlers hasFocusHandlers = (HasFocusHandlers) ui;
//					hasFocusHandlers.addFocusHandler(EventFactory.createFocusListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support focus listener", null);
//				}
//			}
//
//			if (event instanceof OnExitEventListenerGVO) {
//				if (ui instanceof HasBlurHandlers) {
//					HasBlurHandlers hasBlurHandlers = (HasBlurHandlers) ui;
//					hasBlurHandlers.addBlurHandler(EventFactory.createOnExitListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onexit listener", null);
//				}
//			}
//
//			if (event instanceof ClickEventListenerGVO) {
//				if (ui instanceof HasClickHandlers) {
//					ui.addStyleName("qafe_hand");
//					HasClickHandlers hasClickHandlers = (HasClickHandlers) ui;
//					hasClickHandlers.addClickHandler(EventFactory.createClickListener(event, inputVariables));
//				} else if (ui instanceof MenuItem) {
//					MenuItem menuItem = (MenuItem) ui;
//					menuItem.setCommand(EventFactory.createCommandListener(menuItem, event, inputVariables));
//					ui.addStyleName("qafe_hand");
//				} 
////				else if (ui instanceof AreaWidget) {
////					AreaWidget area = (AreaWidget) ui;
////					area.setCommand(EventFactory.createCommandListener(area, event, inputVariables));
////					ui.addStyleName("qafe_hand");
////				} 
//				else if (ui instanceof TabPanel) {
//					TabPanel tabPanel = (TabPanel) ui;
//					tabPanel.addSelectionHandler(EventFactory.createTabPanelListener(tabPanel, event, inputVariables));
//					ui.addStyleName("qafe_hand");
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onclick listener", null);
//				}
//
//			}
//			if (event instanceof DoubleClickEventListenerGVO){
//				if (ui instanceof HasDoubleClickHandlers) {
//					//ui.addStyleName("qafe_hand");
//					HasDoubleClickHandlers hasDoubleClickHandlers = (HasDoubleClickHandlers) ui;
//					hasDoubleClickHandlers.addDoubleClickHandler(EventFactory.createDoubleClickListener(event, inputVariables));
//				}
//			}
//
//			if (event instanceof OnChangeEventListenerGVO) {
////				 if (ui instanceof QSuggestBox){
////					 QSuggestBox suggestionBox = (QSuggestBox)ui;
////					 suggestionBox.addKeyUpHandler(EventFactory.createSuggestionOnKeyUpHandler(suggestionBox,event,inputVariables));
////				 } else
////					 if(ui instanceof QDatePicker && ui instanceof HasValueChangeHandlers) {
////					 HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers) ui;
////					 hasValueChangeHandlers.addValueChangeHandler((EventFactory.createOnValueChangeHandler(event, inputVariables)));
////				 } else 
//					 if (ui instanceof HasDataChangeHandlers) {
//					 HasDataChangeHandlers hasDataChangeHandlers = (HasDataChangeHandlers) ui;
//					// hasDataChangeHandlers.addDataChangeHandler((EventFactory.createOnDataChangeHandler(event, inputVariables)));
//				 } else if (ui instanceof HasChangeHandlers) {
//					 HasChangeHandlers hasChangeHandlers = (HasChangeHandlers) ui;
//					 hasChangeHandlers.addChangeHandler((EventFactory.createOnChangeListener(event, inputVariables)));
//				} else if (ui instanceof SourcesChangeEvents){
//					SourcesChangeEvents sourcesChangeEvents = (SourcesChangeEvents)ui;
//					sourcesChangeEvents.addChangeListener(EventFactory.createLegacyOnChangeListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onchange listener", null);
//				}
//			}
//
//			if (event instanceof OnMouseEnterEventListenerGVO) {
//				if (ui instanceof HasAllMouseHandlers) {
//					HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) ui;
//					hasAllMouseHandlers.addMouseOverHandler(EventFactory.createOnMouseEnterListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onmouse-enter listener", null);
//				}
//			}
//			if (event instanceof OnMouseExitEventListenerGVO) {
//				if (ui instanceof HasAllMouseHandlers) {
//					HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) ui;
//					hasAllMouseHandlers.addMouseOutHandler(EventFactory.createOnMouseExitListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onmouse-exit listener", null);
//				}
//			}
//
//			if (event instanceof OnMouseMoveEventListenerGVO) {
//				if (ui instanceof HasAllMouseHandlers) {
//					HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) ui;
//					hasAllMouseHandlers.addMouseMoveHandler(EventFactory.createOnMouseMoveListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onmpuse-move listener", null);
//				}
//			}
//			if (event instanceof OnMouseUpEventListenerGVO) {
//				if (ui instanceof HasAllMouseHandlers) {
//					HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) ui;
//					hasAllMouseHandlers.addMouseUpHandler(EventFactory.createOnMouseUpListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onmouse-up listener", null);
//				}
//			}
//			if (event instanceof OnMouseDownEventListenerGVO) {
//				if (ui instanceof HasAllMouseHandlers) {
//					HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) ui;
//					hasAllMouseHandlers.addMouseDownHandler(EventFactory.createOnMouseDownListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onmouse-down listener", null);
//				}
//			}
//
//			if (event instanceof OnKeyPressEventListenerGVO) {
//				if (ui instanceof HasAllKeyHandlers) {
//					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers) ui;
//					hasAllKeyHandlers.addKeyPressHandler(EventFactory.createOnKeyPressListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onkeypress listener", null);
//				}
//			}
//
//			if (event instanceof OnKeyDownEventListenerGVO) {
//				if (ui instanceof HasAllKeyHandlers) {
//					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers) ui;
//					hasAllKeyHandlers.addKeyDownHandler(EventFactory.createOnKeyDownListener(event, inputVariables));
//
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onkeydown listener", null);
//				}
//			}
//
//			if (event instanceof OnKeyUpEventListenerGVO) {
//				if (ui instanceof HasAllKeyHandlers) {
//					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers) ui;
//					hasAllKeyHandlers.addKeyUpHandler(EventFactory.createOnKeyUpListener(event, inputVariables));
//				} else {
//					ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onkeyup listener", null);
//				}
//			}
//
//			if (event instanceof OnLoadEventListenerGVO && vo != null && !(vo instanceof WindowGVO)) {
//				ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onload listener (only supported on window objects)", null);
//			}
//
//			if (event instanceof OnUnLoadEventListenerGVO && vo != null && !(vo instanceof WindowGVO)) {
//				ClientApplicationContext.getInstance().log("This component object[" + DOM.getElementAttribute(ui.getElement(), "id") + "] for event [" + event.getEventId() + "] does not support onunload listener (only supported on window objects)", null);
//			}
//			if(event instanceof OnFinishEventListenerGVO){
//				if(ui instanceof FormPanel){
//					FormPanel formPanel = (FormPanel)ui;
//					formPanel.addSubmitCompleteHandler(EventFactory.createSubmitCompleteHandler(ui, event, inputVariables));
//				}
//			}
//			
//			if (event instanceof OnTimerEventListenerGVO) {
//				//check a timer is already scheduled for this event- this happens when there are multiple component reference mentioned in that event
////				if(!ClientApplicationContext.getInstance().isTimerScheduledForEvent(appId, vo.getWindow(), event.getEventId())) {
////					OnTimerHandler timerHandler= new OnTimerHandler(); 
////					timerHandler.processOnTimer(ui, vo, appId, event, inputVariables);
////				}
//			}
			
		}
	}

	
}
