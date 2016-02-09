/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasData;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.HasPrompt;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QMultiWordSuggestion;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.component.QSliderBar;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.component.Tiles;
import com.qualogy.qafe.gwt.client.component.TitledComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.DropDownRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.UpdateModelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;

@Deprecated
public class SetValueExecute implements ExecuteCommand {

	private static boolean checksumSet = false;

    // CHECKSTYLE.OFF: CyclomaticComplexity
 	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof SetValueGVO) {
			SetValueGVO setValue = (SetValueGVO) builtInFunction;

			List<UIObject> uiObjects = RendererHelper.getComponent(setValue.getBuiltInComponentGVO().getComponentIdUUID());
			if (uiObjects != null) {
				for (UIObject uiObject : uiObjects) {

					if (uiObject != null) {
						DataContainerGVO dataContainer = setValue.getDataContainer();
						if (dataContainer != null) {
							if (dataContainer.getKind() == DataContainerGVO.KIND_VALUE) {
								process(uiObject, setValue.getValue(), setValue,dataContainer);
							} else if (dataContainer.getKind() == DataContainerGVO.KIND_STRING) {
								process(uiObject, dataContainer.getDataString(), setValue,dataContainer);
							} else if (dataContainer.getKind() == DataContainerGVO.KIND_MAP) {
								if (uiObject instanceof HasDataGridMethods) {
									HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
									List<DataContainerGVO> listOfDataMap = new ArrayList<DataContainerGVO>();
									listOfDataMap.add(new DataContainerGVO(dataContainer.getDataMap()));
									boolean append = SetValueGVO.ACTION_ADD.equals(setValue.getAction());
									dataGrid.insertData(listOfDataMap, append, builtInFunction.getSenderId(), builtInFunction.getListenerType());
								} else if (uiObject instanceof HasData) {
									HasData hasData = (HasData) uiObject;
									hasData.setData(dataContainer.getDataMap(), setValue.getAction(), setValue.getMapping());
								} else if (uiObject instanceof Tiles) {
									Tiles tiles = (Tiles) uiObject;
									tiles.fillDataFromMap(new DataContainerGVO(dataContainer.getDataMap()), setValue);
								}
							} else if (dataContainer.getKind() == DataContainerGVO.KIND_COLLECTION) {
								if (uiObject instanceof ListBox) {
									processListBox(setValue, uiObject, dataContainer);
								} else if (uiObject instanceof HasDataGridMethods) {
									HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
									boolean append = SetValueGVO.ACTION_ADD.equals(setValue.getAction());
									dataGrid.insertData(dataContainer.getListofDC(), append, builtInFunction.getSenderId(), builtInFunction.getListenerType());
									dataGrid.redraw();
								} else if (uiObject instanceof HasData) {
									HasData hasData = (HasData) uiObject;
									hasData.setData(dataContainer.getListofDC(), setValue.getAction(), setValue.getMapping());
								} else if (uiObject instanceof QSuggestBox) {
									QSuggestBox suggestionBox = (QSuggestBox) uiObject;
									suggestionBox.clearSuggestions();
									String displayField = QMultiWordSuggestion.DISPLAYFIELD_DEFAULT;
									if (setValue.getMapping() != null) {
										displayField = setValue.getMapping().get("value");
									}
									for (DataContainerGVO hashMap : dataContainer.getListofDC()) {
										QMultiWordSuggestion qms = new QMultiWordSuggestion(hashMap, displayField);
										suggestionBox.getOracle().add(qms);
									}
									suggestionBox.showSuggestionList();

								} else if (uiObject instanceof Tiles) {
									Tiles tiles = (Tiles) uiObject;
									if (SetValueGVO.ACTION_SET.equals(setValue.getAction())) {
										tiles.getTileElements().clear();
										tiles.clear();
									}
									tiles.fillDataFromMapList(dataContainer.getListofDC(), setValue);
								}
							}
						} else { // datacontainer is null, so no data can be found. Check the UIObjects on their kind and clear them.
							//TODO: Check we can avoid this flow- if data is null also the component should be able to do the clearing itself
							ClearExecute.clear(uiObject, builtInFunction);

						}
					}
				}
			} else if (setValue.getNamedComponentId() != null && setValue.getNamedComponentId().length() > 0) {
				uiObjects = RendererHelper.getNamedComponent(setValue.getNamedComponentId());
				if (uiObjects != null) {
					setChecksumSet(false);
					for (UIObject uiObject : uiObjects) {
						if (uiObject != null) {
							DataContainerGVO dataContainer = setValue.getDataContainer();
							if (dataContainer != null) {
								if (uiObject instanceof HasDataGridMethods) {
									HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
									boolean append = SetValueGVO.ACTION_ADD.equals(setValue.getAction());
									dataGrid.insertData(setValue.getDataContainer().getListofDC(), append, builtInFunction.getSenderId(), builtInFunction.getListenerType());
									dataGrid.redraw();
								} else if (uiObject instanceof QSliderBar) {
									processNamedComponent((Widget) uiObject, setValue);
								} else if (uiObject instanceof HasWidgets) {
									HasWidgets hasWidgets = (HasWidgets) uiObject;
									processWidgets(hasWidgets, setValue);
								} else if (uiObject instanceof Widget) {
									processNamedComponent((Widget) uiObject, setValue);
								}
							} else {
								ClearExecute.clear(uiObject, builtInFunction, true);
							}
						}

					}
					if (isChecksumSet()) {
						handleUpdateChecksum(setValue.getNamedComponentId());
						setChecksumSet(false);
					}
				}

			} else if (setValue.getGroup() != null && setValue.getGroup().length() > 0) {
				uiObjects = RendererHelper.getGroupedComponent(setValue.getGroup());
				if (uiObjects != null) {
					for (UIObject uiObject : uiObjects) {
						if (uiObject != null) {
							DataContainerGVO dataContainer = setValue.getDataContainer();
							if (dataContainer != null) {
								if (uiObject instanceof HasDataGridMethods) {
									HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
									boolean append = SetValueGVO.ACTION_ADD.equals(setValue.getAction());
									dataGrid.insertData(setValue.getDataContainer().getListofDC(), append, builtInFunction.getSenderId(), builtInFunction.getListenerType());
									dataGrid.redraw();
								} else if (uiObject instanceof QSliderBar) {
									processNamedComponent((Widget) uiObject, setValue);
								} else if (uiObject instanceof HasWidgets) {
									HasWidgets hasWidgets = (HasWidgets) uiObject;
									processWidgets(hasWidgets, setValue);
								} else if (uiObject instanceof Widget) {
									processNamedComponent((Widget) uiObject, setValue);
								}
							} else {
								ClearExecute.clear(uiObject, builtInFunction, true);
							}
						}

					}
				}

			} else {
				// Trying to find out if a drop down inside a datagrid column is being set.
				String[] columnIdSplits = setValue.getBuiltInComponentGVO().getComponentIdUUID().split("\\|");
				String[] senderIdSplits = setValue.getSenderId().split("\\|");
				uiObjects = RendererHelper.getComponent(columnIdSplits[1]+"|"+senderIdSplits[1]+"|"+columnIdSplits[2]); // Datagrid of that column is picked up.
				if (uiObjects != null) {
					for (UIObject uiObject : uiObjects) {
						if (uiObject != null) {
							if (uiObject instanceof HasDataGridMethods) {
								HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
								dataGrid.addToDropDownValuesForColumnList(columnIdSplits[0], setValue); // Value to set in the drop dwon inside the column is stored and used later in cell renderer.
							}
						}
					}
				} else {
					ClientApplicationContext.getInstance().log("Trying to set value on non-existing component", "There is no component with name: " + setValue.getComponentId() + " or " + setValue.getNamedComponentId() + ". Please check the Application mapping.", false, false, null);
				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void processListBox(SetValueGVO setValue, UIObject uiObject,
			DataContainerGVO dataContainer) {
		ListBox listBox = (ListBox) uiObject;
		if (SetValueGVO.ACTION_SET.equals(setValue.getAction())) {
			listBox.clear();
		}
		if (DropDownRenderer.hasEmptyItem(listBox)){
			DropDownRenderer.adaptEmptyItem(listBox);
		}
		if (dataContainer.getListofDC() != null) {
			if (setValue.getMapping() == null) {
				Iterator<DataContainerGVO> itr = dataContainer.getListofDC().iterator();
				String key = null;
				while (itr.hasNext()) {
					DataContainerGVO data = itr.next();
					if (data.getKind()==DataContainerGVO.KIND_MAP){
						Map<String,DataContainerGVO> m = data.getDataMap();
						String id = DataContainerGVO.resolveValue(m.get("id"));
						String value = DataContainerGVO.resolveValue(m.get("value"));
						if(id == null) {
							id = value;
						}
						listBox.addItem(value, id);
					}
				}
			} else {
				Iterator<DataContainerGVO> itr = dataContainer.getListofDC().iterator();
				while (itr.hasNext()) {
					DataContainerGVO data = itr.next();
					if (data.getKind()==DataContainerGVO.KIND_MAP){
						DataMap m = data.getDataMap();

						// TODO: id should be changed to value and value should be changed to displayname.
						String key = getMappedValue("id", setValue.getMapping(), m);
						String value = getMappedValue("value", setValue.getMapping(), m);
						if(key == null) {
							key = value;
						}
						listBox.addItem(value, key); // value is the text that will be displayed.
					}
				}
			}
		}
	}

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

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static void processNamedComponent(Widget widget, SetValueGVO setValue) {
		DataContainerGVO dtc = setValue.getDataContainer();
		if (dtc != null) {
			UIObject uiObject = widget;
			if (RendererHelper.isNamedComponent(uiObject)) {
				String name = RendererHelper.getNamedComponentName(uiObject);
				if (DataContainerGVO.QAFE_CHECKSUM.equals(name)) {
					setChecksumSet(true);
				}
				if (dtc.getKind() == DataContainerGVO.KIND_MAP) {
					DataMap dataMap = dtc.getDataMap();
					Object value = null;
					DataContainerGVO valueDTC = null;
					if (dataMap.containsKey(name.toUpperCase()) || dataMap.containsKey(name.toLowerCase())) {
						if (dataMap.containsKey(name.toUpperCase())) {// for database interaction every field is capitalized.
							value = DataContainerGVO.createType(dataMap.get(name.toUpperCase()));
							valueDTC = dataMap.get(name.toUpperCase());
						} else if (dataMap.containsKey(name.toLowerCase())) {
							value = DataContainerGVO.createType(dataMap.get(name.toLowerCase()));
							valueDTC = dataMap.get(name.toLowerCase());
						}
						if (widget instanceof TitledComponent) {
							uiObject = ((TitledComponent) widget).getDataComponent();
						}
						processValue(uiObject, value, setValue,valueDTC);

					} else if (dataMap.containsKey(name)) { // TODO Probably not needed anymore!
						value = DataContainerGVO.resolveValue(dataMap.get(name));
						if (widget instanceof TitledComponent) {
							uiObject = ((TitledComponent) widget).getDataComponent();
						}
						processValue(uiObject, value, setValue,dataMap.get(name));
					}
				} else if (dtc.getKind() == DataContainerGVO.KIND_STRING) {
					process(uiObject, dtc.getDataString(), setValue,setValue.getDataContainer());
				}
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
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void process(UIObject uiObject, String valueToSet, SetValueGVO setValue,DataContainerGVO valueDTC) {
		if (uiObject != null) {
			if (setValue.getBuiltInComponentGVO() != null && setValue.getBuiltInComponentGVO().getAttributes() != null && setValue.getBuiltInComponentGVO().getAttributes().size() > 0) {
				processAttributes(uiObject, valueToSet, setValue.getBuiltInComponentGVO().getAttributes(), setValue,valueDTC);
			} else {
				processValue(uiObject, valueToSet, setValue,valueDTC);
			}
		}
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static void processValue(UIObject uiObject, Object valueToSet, SetValueGVO setValue, DataContainerGVO valueDTC) {
		if (uiObject != null) {
			if (uiObject instanceof HasText) {
				if (uiObject instanceof QRadioButton) {
					QRadioButton qRadioButton = (QRadioButton) uiObject;
					qRadioButton.reset();
					if (valueToSet != null) {
						qRadioButton.setValue(valueToSet.toString());
					}

				} else if (uiObject instanceof CheckBox) {
					CheckBox checkBox = (CheckBox) uiObject;
					String checkedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
					String unCheckedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
					String checkedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
					String unCheckedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);

					if (valueToSet != null) {
						if (checkedValue != null && checkedValue.equals(valueToSet)) {
							checkBox.setChecked(true);
						} else if (unCheckedValue != null && unCheckedValue.equals(valueToSet)) {
							checkBox.setChecked(false);
						} else if (isInDomain(valueToSet.toString(), checkedValueDomain)) {
							checkBox.setChecked(true);
						} else if (isInDomain(valueToSet.toString(), unCheckedValueDomain)) {
							checkBox.setChecked(false);
						}
					} else {
						checkBox.setChecked(false);
					}
				} else if (uiObject instanceof HTML) {
					HTML html = (HTML) uiObject;
					if (valueToSet != null) {
						html.setHTML(valueToSet.toString());
					} else {
						html.setHTML("");
					}
				} else if(uiObject instanceof PushButton) {
					((PushButton)uiObject).getUpFace().setText(valueToSet.toString());
					((PushButton)uiObject).getDownFace().setText(valueToSet.toString());
				} else if (uiObject instanceof RichTextArea) {
					RichTextArea richTextArea = (RichTextArea)uiObject;
					richTextArea.setHTML(valueToSet.toString());					
				} else {
					HasText hasText = (HasText) uiObject;
					if (valueDTC!=null){
						if (valueDTC.getKind()== DataContainerGVO.KIND_STRING){
							if (valueDTC.getStringDataType()==DataContainerGVO.TYPE_DATE){
								if (uiObject instanceof QDatePicker) {
									((QDatePicker) uiObject).setValue(valueDTC.getDateData());
								}
							}else {
								hasText.setText(valueToSet.toString());
								uiObject.setStyleName(uiObject.getStyleName().replaceAll("qafe_invalid_field", "qafe_valid_field"));
							}
						}else {
							hasText.setText(valueToSet.toString());
						}
					} else if (valueToSet != null) {
							hasText.setText(valueToSet.toString());
					} else {
						hasText.setText("");
					}
				}
			} else if (uiObject instanceof Frame) {
				Frame frame = (Frame) uiObject;
				if (valueToSet != null) {
					frame.setUrl(valueToSet.toString());
				} else {
					frame.setUrl("");
				}
			}

			if (uiObject instanceof ListBox) {
				ListBox listBox = (ListBox) uiObject;
				// If it is needed to populate data and select a data from dropdown it should be seperate calls.
				if(valueDTC != null && valueDTC.getListofDC() != null){
					processListBox(setValue, uiObject, valueDTC);
				} else {
					processValue4ListBox(listBox, valueToSet, setValue.getAction());
				}

			}

			if (uiObject instanceof Image) {
				Image image = (Image) uiObject;
				if (valueToSet != null) {
					image.setUrl(valueToSet.toString());
				}

			}

			if (uiObject instanceof QDatePicker) {
				QDatePicker qDatePicker = (QDatePicker)uiObject;
				if(valueDTC != null) {
					qDatePicker.setValue(valueDTC.getDateData(), true);
				}
			}

			if(uiObject instanceof QSliderBar) {
				QSliderBar slider = (QSliderBar)uiObject;
				slider.setValue(valueToSet);
			}

			if(uiObject instanceof HasDataGridMethods) {
				HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
				List<DataContainerGVO> listOfDataMap = new ArrayList<DataContainerGVO>();
				if (valueDTC.getKind() == DataContainerGVO.KIND_MAP) {
					listOfDataMap.add(new DataContainerGVO(valueDTC.getDataMap()));
					dataGrid.insertData(listOfDataMap, false, setValue.getSenderId(), setValue.getListenerType());
				} else if(valueDTC.getKind() == DataContainerGVO.KIND_COLLECTION) {
					listOfDataMap = valueDTC.getListofDC();
					dataGrid.insertData(listOfDataMap, false, setValue.getSenderId(), setValue.getListenerType());
				} else if(valueDTC.getKind() == DataContainerGVO.KIND_STRING) {
					String cellOnRowToSet = setValue.getComponentId();
					listOfDataMap.add(new DataContainerGVO(valueDTC.getDataString()));
					dataGrid.setDataToCell(new DataContainerGVO(valueDTC.getDataString()), false, setValue.getSenderId(), cellOnRowToSet);
				}
				dataGrid.redraw();
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void processValue4ListBox(ListBox listBox, Object value, String action) {
		if (SetValueGVO.ACTION_SET.equals(action)) {
			if (value == null) {
				int indexOfValue = -1;
				if (DropDownRenderer.hasEmptyItem(listBox)) {
					indexOfValue = 0;
				}
				listBox.setSelectedIndex(indexOfValue);
			} else if (!(value instanceof List)) {
				int indexOfValue = DropDownRenderer.getIndexOfValue(listBox, String.valueOf(value));
				if (indexOfValue > -1) {
					listBox.setSelectedIndex(indexOfValue);
				} else if (listBox.getItemCount() > 0) {
					indexOfValue = -1;
					if (DropDownRenderer.hasEmptyItem(listBox)) {
						indexOfValue = 0;
					}
					listBox.setSelectedIndex(indexOfValue);
				} else {
					DropDownRenderer.adaptItem(listBox, value);
				}
			} else if (value instanceof List) {
				List itemList = (List)value;
				DropDownRenderer.adaptItems(listBox, itemList, true);
			}
		} else if (SetValueGVO.ACTION_ADD.equals(action)) {
			if (value instanceof String) {
				DropDownRenderer.adaptItem(listBox, value);
			} else if (value instanceof List) {
				List itemList = (List)value;
				DropDownRenderer.adaptItems(listBox, itemList, false);
			}
		}
	}

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

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private static void processAttributes(UIObject uiObject, String valueToSet, List<String> attributes, SetValueGVO setValue,DataContainerGVO valueDTC) {
		if (uiObject != null && attributes != null) {
			Iterator<String> itr = attributes.iterator();
			while (itr.hasNext()) {
				String attribute = itr.next();
				if ("tooltip".equals(attribute)) {
					if (uiObject instanceof TitledComponent) {
						TitledComponent titledComponent = (TitledComponent) uiObject;
						titledComponent.getTitleComponent().setTitle(valueToSet);
					} else {
						uiObject.setTitle(valueToSet);
					}

				} else if ("title".equals(attribute)) {
					if (uiObject instanceof CaptionLayoutPanel) {
						CaptionLayoutPanel titledPanel = (CaptionLayoutPanel) uiObject;
						titledPanel.setTitle(valueToSet);

					} else if (uiObject instanceof Panel) {
						Panel p = (Panel) uiObject;
						Widget parent = p.getParent();
						if (parent != null && parent instanceof DeckPanel) {
							DeckPanel deckPanel = (DeckPanel) parent;
							int widgetIndex = deckPanel.getWidgetIndex(p);
							if (widgetIndex != -1) {
								deckPanel.showWidget(widgetIndex);
							}
						}
					}
				} else if ("prompt".equals(attribute)) {
					if (uiObject instanceof HasPrompt) {
						HasPrompt hasPrompt = (HasPrompt) uiObject;
						hasPrompt.setPrompt(valueToSet);
					} else if (uiObject instanceof CheckBox) {
						((CheckBox) uiObject).setText(valueToSet);
					}

				} else if ("value".equals(attribute)) {
					processValue(uiObject, valueToSet, setValue,valueDTC);
				}
			}
		}

	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static String getMappedValue(final String field, Map<String, String> mapping, DataMap dataMap) {
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

	private static boolean isChecksumSet() {
		return checksumSet;
	}

	private static void setChecksumSet(boolean value) {
		checksumSet = value;
	}

	private static void handleUpdateChecksum(String checksumContainerNameUUID) {
		UpdateModelGVO updateModelGVO = new UpdateModelGVO();
		updateModelGVO.setRef(checksumContainerNameUUID);
		updateModelGVO.setUpdateAction(DataContainerGVO.QAFE_CHECKSUM);
		FunctionsExecutor.getInstance().execute(updateModelGVO);
	}
}
