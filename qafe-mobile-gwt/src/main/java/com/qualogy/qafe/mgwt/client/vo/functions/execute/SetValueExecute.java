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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.ActivityHelper;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.component.DataMap;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetValueGVO;

public class SetValueExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof SetValueGVO) {
			SetValueGVO setValueGVO = (SetValueGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				setValue(setValueGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void setValue(SetValueGVO setValueGVO, WindowActivity activity) {
		WindowPlace windowPlace = activity.getPlace();
		String context = windowPlace.getContext();
		String windowId = windowPlace.getId();
		ActivityHelper.setValue(setValueGVO, windowId, context);
	}

//	private void processListBox(SetValueGVO setValue, UIObject uiObject,
//			DataContainerGVO dataContainer) {
//		ListBox listBox = (ListBox) uiObject;
//		if (SetValueGVO.ACTION_SET.equals(setValue.getAction())) {
//			listBox.clear();
//		}
//		if (DropDownRenderer.hasEmptyItem(listBox)){
//			DropDownRenderer.adaptEmptyItem(listBox);
//		}
//		if (dataContainer.getListofDC() != null) {
//			if (setValue.getMapping() == null) {
//				Iterator<DataContainerGVO> itr = dataContainer.getListofDC().iterator();
//				String key = null;
//				while (itr.hasNext()) {
//					DataContainerGVO data = itr.next();
//					if (data.getKind()==DataContainerGVO.KIND_MAP){
//							Map<String,DataContainerGVO> m = data.getDataMap();
//							String id = DataContainerGVO.resolveValue(m.get("id"));
//							String value = DataContainerGVO.resolveValue(m.get("value"));
//							if(id == null) {
//								id = value;
//							}
//							listBox.addItem(value, id);
//					}
//				}
//			} else {
//				Iterator<DataContainerGVO> itr = dataContainer.getListofDC().iterator();
//				while (itr.hasNext()) {
//					DataContainerGVO data = itr.next();
//					if (data.getKind()==DataContainerGVO.KIND_MAP){
//						DataMap m = data.getDataMap();
//				
//						String key = getMappedValue("id", setValue.getMapping(), m);
//						String value = getMappedValue("value", setValue.getMapping(), m);
//						if(key == null) {
//							key = value;
//						}
//						listBox.addItem(value, key); // value is the text that will be displayed.
//					}
//				}
//			}
//		}
//	}

	public static void processWidgets(HasWidgets hasWidgets, SetValueGVO setValue) {
		Iterator<Widget> itr = hasWidgets.iterator();
		while (itr.hasNext()) {
			Widget widget = itr.next();
			processNamedComponent(widget, setValue);
			if (widget instanceof HasWidgets) {
				HasWidgets innerHasWidget = (HasWidgets) widget;
				processWidgets(innerHasWidget, setValue);
			}
		}
	}

	public static void processNamedComponent(Widget widget, SetValueGVO setValue) {
		DataContainerGVO dtc = setValue.getDataContainer();
		if (dtc != null) {
			UIObject uiObject = widget;
			/*
			 * if (widget instanceof TitledComponent) { uiObject =
			 * ((TitledComponent) widget).getDataComponent(); }
			 */

			if (RendererHelper.isNamedComponent(uiObject)) {
				String name = RendererHelper.getNamedComponentName(uiObject);
				if (dtc.getKind() == DataContainerGVO.KIND_MAP) {
					DataMap dataMap = dtc.getDataMap();
					Object value = null;
					DataContainerGVO valueDTC = null;
					if (dataMap.containsKey(name.toUpperCase()) || dataMap.containsKey(name.toLowerCase())) {
						if (dataMap.containsKey(name.toUpperCase())) {// for
							// database
							// interaction
							// every
							// field is
							// capitalized

							value = DataContainerGVO.createType(dataMap.get(name.toUpperCase()));
							valueDTC = dataMap.get(name.toUpperCase());
						} else if (dataMap.containsKey(name.toLowerCase())) {
							value = DataContainerGVO.createType(dataMap.get(name.toLowerCase()));
							valueDTC = dataMap.get(name.toLowerCase());
						}

//						if (widget instanceof TitledComponent) {
//							uiObject = ((TitledComponent) widget).getDataComponent();
//						}

						processValue(uiObject, value, setValue,valueDTC);

					} else if (dataMap.containsKey(name)) { // TODO Probably not
						// needed anymore!

						value = DataContainerGVO.resolveValue(dataMap.get(name));
//						if (widget instanceof TitledComponent) {
//							uiObject = ((TitledComponent) widget).getDataComponent();
//						}
						processValue(uiObject, value, setValue,dataMap.get(name));

					}
				} else if (dtc.getKind() == DataContainerGVO.KIND_STRING) {
					process(uiObject, dtc.getDataString(), setValue,setValue.getDataContainer());
				}
//				else if (dtc.getKind() == DataContainerGVO.KIND_COLLECTION_OF_STRING) {
//					handleCollectionOfStrings(uiObject, dtc, setValue);
//				}
				else if (dtc.getKind() == DataContainerGVO.KIND_VALUE) {
					process(uiObject, setValue.getValue(), setValue, setValue.getDataContainer());
				} else if(dtc.getKind() == DataContainerGVO.KIND_COLLECTION) { 
					if(dtc.getListofDC() != null) {
						List<DataContainerGVO> listofDC = dtc.getListofDC();
						for(DataContainerGVO data: listofDC) {
							DataContainerGVO containerGVO = new DataContainerGVO();
							if (data.getKind()==DataContainerGVO.KIND_MAP){
								containerGVO.setDataMap(data.getDataMap());
								containerGVO.setKind(DataContainerGVO.KIND_MAP);
								setValue.setDataContainer(containerGVO);
								processNamedComponent(widget, setValue);	
							}	
						}						
					}
					
				}

			}
		}

	}



	private static void process(UIObject uiObject, String valueToSet, SetValueGVO setValue,DataContainerGVO valueDTC) {
		if (uiObject != null) {
			if (setValue.getBuiltInComponentGVO() != null && setValue.getBuiltInComponentGVO().getAttributes() != null && setValue.getBuiltInComponentGVO().getAttributes().size() > 0) {
				processAttributes(uiObject, valueToSet, setValue.getBuiltInComponentGVO().getAttributes(), setValue,valueDTC);
			} else {
				processValue(uiObject, valueToSet, setValue,valueDTC);
			}
		}
	}

	public static void processValue(UIObject uiObject, Object valueToSet, SetValueGVO setValue, DataContainerGVO valueDTC) {
		if (uiObject != null) {
//			if (uiObject instanceof HasText) {
//				if (uiObject instanceof QRadioButton) {
//					QRadioButton qRadioButton = (QRadioButton) uiObject;
//					qRadioButton.reset();
//					if (valueToSet != null) {
//						qRadioButton.setValue(valueToSet.toString());
//					}
//
//				} else if (uiObject instanceof CheckBox) {
//					CheckBox checkBox = (CheckBox) uiObject;
//					String checkedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
//					String unCheckedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
//					String checkedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
//					String unCheckedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
//
//					if (valueToSet != null) {
//						if (checkedValue != null && checkedValue.equals(valueToSet)) {
//							checkBox.setChecked(true);
//						} else if (unCheckedValue != null && unCheckedValue.equals(valueToSet)) {
//							checkBox.setChecked(false);
//						} else if (isInDomain(valueToSet.toString(), checkedValueDomain)) {
//							checkBox.setChecked(true);
//						} else if (isInDomain(valueToSet.toString(), unCheckedValueDomain)) {
//							checkBox.setChecked(false);
//						}
//					} else {
//						checkBox.setChecked(false);
//					}
//				} else if (uiObject instanceof HTML) {
//					HTML html = (HTML) uiObject;
//					if (valueToSet != null) {
//						html.setHTML(valueToSet.toString());
//					} else {
//						html.setHTML("");
//					}
//				} else if(uiObject instanceof PushButton) { 
//					((PushButton)uiObject).getUpFace().setText(valueToSet.toString());
//					((PushButton)uiObject).getDownFace().setText(valueToSet.toString());
//				} else {
//					HasText hasText = (HasText) uiObject;
//					if (valueDTC!=null){
//						if (valueDTC.getKind()== DataContainerGVO.KIND_STRING){
//							if (valueDTC.getStringDataType()==DataContainerGVO.TYPE_DATE){
//								if (uiObject instanceof QDatePicker) {
//									((QDatePicker) uiObject).setValue(valueDTC.getDateData());
//								}
//							}else {						
//								hasText.setText(valueToSet.toString());
//								uiObject.setStyleName(uiObject.getStyleName().replaceAll("qafe_invalid_field", "qafe_valid_field"));
//							} 
//						}else {
//							hasText.setText(valueToSet.toString());
//						}
//					} else if (valueToSet != null) {						
//							hasText.setText(valueToSet.toString());
//					} else {
//						hasText.setText("");
//					}
//				}
//			} else if (uiObject instanceof Frame) {
//				Frame frame = (Frame) uiObject;
//				if (valueToSet != null) {
//					frame.setUrl(valueToSet.toString());
//				} else {
//					frame.setUrl("");
//				}
//
//			}
//
//			if (uiObject instanceof ListBox) {
//				ListBox listBox = (ListBox) uiObject;
//				
//				processValue4ListBox(listBox, valueToSet, setValue.getAction());
//			}
//					
//			if (uiObject instanceof Image) {
//				Image image = (Image) uiObject;
//				if (valueToSet != null) {
//					image.setUrl(valueToSet.toString());
//				}
//
//			}
//			if(uiObject instanceof QChart2D){
//				QChart2D chart = (QChart2D)uiObject;
//				DataContainerGVO val = (DataContainerGVO)valueToSet;
//				chart.setChartData(uiObject, val.getListofDC());
//			}
//			if (uiObject instanceof QDatePicker) {
//				QDatePicker qDatePicker = (QDatePicker)uiObject;
//				if(valueDTC != null) {
//					qDatePicker.setValue(valueDTC.getDateData(), true);
//				}
//			}
//			if(uiObject instanceof QSliderBar) {
//				QSliderBar slider = (QSliderBar)uiObject;
//				slider.setValue(valueToSet);
//			}
//			if(uiObject instanceof HasDataGridMethods) {
//				HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
//				List<DataContainerGVO> listOfDataMap = new ArrayList<DataContainerGVO>();
//				if (valueDTC.getKind() == DataContainerGVO.KIND_MAP) {
//					listOfDataMap.add(new DataContainerGVO(valueDTC.getDataMap()));
//				} else if(valueDTC.getKind() == DataContainerGVO.KIND_COLLECTION) {
//					listOfDataMap = valueDTC.getListofDC();
//				}
//				dataGrid.insertData(listOfDataMap, false, setValue.getSenderId(), setValue.getListenerType());
//				dataGrid.redraw();
//			}
		}
	}

//	private static void processValue4ListBox(ListBox listBox, Object value, String action) {
//		if (SetValueGVO.ACTION_SET.equals(action)) {
//			if ((value == null) || ((value instanceof String) && (((String)value).isEmpty()))) {
//				int indexOfValue = -1;
//				if (DropDownRenderer.hasEmptyItem(listBox)) {
//					indexOfValue = 0;
//				}
//				listBox.setSelectedIndex(indexOfValue);
//			} else if (!(value instanceof List)) {
//				int indexOfValue = DropDownRenderer.getIndexOfValue(listBox, String.valueOf(value));
//				if (indexOfValue > -1) {
//					listBox.setSelectedIndex(indexOfValue);
//				} else if (listBox.getItemCount() > 0) {
//					indexOfValue = -1;
//					if (DropDownRenderer.hasEmptyItem(listBox)) {
//						indexOfValue = 0;
//					}
//					listBox.setSelectedIndex(indexOfValue);
//				} else {
//					DropDownRenderer.adaptItem(listBox, value);					
//				}
//			} else if (value instanceof List) {
//				List itemList = (List)value;
//				DropDownRenderer.adaptItems(listBox, itemList, true);
//			} 
//		} else if (SetValueGVO.ACTION_ADD.equals(action)) {
//			if (value instanceof String) {
//				DropDownRenderer.adaptItem(listBox, value);
//			} else if (value instanceof List) {
//				List itemList = (List)value;
//				DropDownRenderer.adaptItems(listBox, itemList, false);
//			} 
//		}
//	}

	private static boolean isInDomain(String valueToSet, String checkedValueDomain) {
		boolean checked = false;
		if (checkedValueDomain != null) {

			String[] domain = checkedValueDomain.split(",");
			if (domain != null) {
				for (int i = 0; i < domain.length && !checked; i++) {
					if (domain[i].equals(valueToSet)) {
						checked = true;
					}
				}
			}
		}
		return checked;
	}

	private static void processAttributes(UIObject uiObject, String valueToSet, List<String> attributes, SetValueGVO setValue,DataContainerGVO valueDTC) {
		if (uiObject != null && attributes != null) {
			Iterator<String> itr = attributes.iterator();
			while (itr.hasNext()) {
				String attribute = itr.next();
				if ("tooltip".equals(attribute)) {
//					if (uiObject instanceof TitledComponent) {
//						TitledComponent titledComponent = (TitledComponent) uiObject;
//						titledComponent.getTitleComponent().setTitle(valueToSet);
//					} else {
//						uiObject.setTitle(valueToSet);
//					}

				} else if ("title".equals(attribute)) {
//					if (uiObject instanceof CaptionLayoutPanel) {
//						CaptionLayoutPanel titledPanel = (CaptionLayoutPanel) uiObject;
//						titledPanel.setTitle(valueToSet);
//
//					} else if (uiObject instanceof Panel) {
//						Panel p = (Panel) uiObject;
//						Widget parent = p.getParent();
//						if (parent != null && parent instanceof DeckPanel) {
//							DeckPanel deckPanel = (DeckPanel) parent;
//							int widgetIndex = deckPanel.getWidgetIndex(p);
//							if (widgetIndex != -1) {
//								deckPanel.showWidget(widgetIndex);
////								if (deckPanel.getParent() != null && deckPanel.getParent().getParent() != null && deckPanel.getParent().getParent() instanceof TabPanel) {
////									// ((TabPanel)
////									// (deckPanel.getParent().getParent())).selectTab(widgetIndex);
////								}
//							}
//						}
//					}
				} else if ("prompt".equals(attribute)) {
//					if (uiObject instanceof HasPrompt) {
//						HasPrompt hasPrompt = (HasPrompt) uiObject;
//						hasPrompt.setPrompt(valueToSet);
//					} else if (uiObject instanceof CheckBox) {
//						((CheckBox) uiObject).setText(valueToSet);
//					}

				} else if ("value".equals(attribute)) {
					processValue(uiObject, valueToSet, setValue,valueDTC);

				}
			}
		}

	}

	private String getMappedValue(final String field, Map<String, String> mapping, DataMap dataMap) {
		String mappedValue = null;

		// first find the field you want the value of:

		if (mapping != null) {
			String fieldValue = mapping.get(field);
			if (fieldValue != null) {
				
				// If a (database) resource has a null key value this key value will be set to ""
				if(dataMap.get(fieldValue) != null) {
					mappedValue = dataMap.get(fieldValue).toString();					
				} else {
					mappedValue ="";
					ClientApplicationContext.getInstance().log("The entry key [" + field + "] has a null value");
				}
			}
		}
		return mappedValue;
	}

}
