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
package com.qualogy.qafe.mgwt.server.event.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.mgwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;

public class AbstractEventRenderer {

	public final static Logger logger = Logger.getLogger(AbstractEventRenderer.class.getName());

	// / TODO
	public void setComponents(BuiltInFunctionGVO out, BuiltInFunction in, EventDataGVO eventData) {
		if (out != null && in != null) {
			List<Component> components = in.getComponents();
			if (components != null) {
				List<BuiltInComponentGVO> componentIds = new ArrayList<BuiltInComponentGVO>();
				for (Component component : components) {
					
				
					String[] componentData = StringUtils.split(component.getComponentId(), ".");

					BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
					builtInComponentGVO.setUUID(eventData.getUuid());
					builtInComponentGVO.setComponentName(generateId(component.getComponentName(), eventData));
					if (componentData != null && componentData.length == 1) { // so
																				// only
																				// the
																				// name
						// is
						// provided
						if (containsAttribute(componentData[0])) {
							fillBuiltInComponentGVO(builtInComponentGVO, componentData[0]);
						} else {
							builtInComponentGVO.setComponentId(componentData[0]);
						}

						builtInComponentGVO.setComponentIdUUID(generateId(builtInComponentGVO.getComponentId(), eventData));
					} else if (componentData != null && componentData.length == 2) {
						// strip uud
						String[] uuidSplit = StringUtils.split(eventData.getUuid(), "|");
						if (uuidSplit != null) {
							if (uuidSplit.length == 1) {// standard
								// RootPanel
								// processing
								if (containsAttribute(componentData[1])) {
									fillBuiltInComponentGVO(builtInComponentGVO, componentData[1]);
								} else {
									builtInComponentGVO.setComponentId(component.getComponentId());
								}
								builtInComponentGVO.setComponentIdUUID(generateId(builtInComponentGVO.getComponentId(), eventData));
								builtInComponentGVO.setWindowId(eventData.getParent());
							} else if (uuidSplit.length == 2) {
								if (containsAttribute(componentData[1])) {
									fillBuiltInComponentGVO(builtInComponentGVO, componentData[1]);
								} else {
									builtInComponentGVO.setComponentId(componentData[1]);
								}
								builtInComponentGVO.setComponentIdUUID(generateId(builtInComponentGVO.getComponentId(), componentData[0], eventData.getContext()));
								builtInComponentGVO.setWindowId(componentData[0]);
							}
						} else {
							logger.info("The UUID is not of correct format at all:" + eventData.getUuid());
						}
//						// strip uud
//						String[] uuidSplit = StringUtils.split(eventData.getUuid(), "|");
//						if (uuidSplit != null) {
//							if (uuidSplit.length == 1) {// standard
//								// RootPanel
//								// processing
//								if (containsAttribute(componentData[1])) {
//									fillBuiltInComponentGVO(builtInComponentGVO, componentData[1]);
//								} else {
//									builtInComponentGVO.setComponentId(component.getComponentId());
//									//builtInComponentGVO.setComponentId(componentData[1]);
//								}
//								builtInComponentGVO.setComponentIdUUID(generateId(builtInComponentGVO.getComponentId(), eventData));
//								//builtInComponentGVO.setWindowId(componentData[0]);
//								builtInComponentGVO.setWindowId(eventData.getParent());
//							}
//							if (uuidSplit.length == 2) {
//								if (containsAttribute(componentData[1])) {
//									fillBuiltInComponentGVO(builtInComponentGVO, componentData[1]);
//								} else {
//									builtInComponentGVO.setComponentId(componentData[1]);
//								}
//
//								builtInComponentGVO.setComponentIdUUID(builtInComponentGVO.getComponentId() + "|" + componentData[0] + "|" + uuidSplit[1]);
//								builtInComponentGVO.setWindowId(componentData[0]);
//							}
//						} else {
//							logger.info("The UUID is not of correct format at all:" + eventData.getUuid());
//						}
					}
					componentIds.add(builtInComponentGVO);
				}
				out.setComponents(componentIds);
			}
		}
	}

	public BuiltInComponentGVO getBuiltInComponentGVO(String component, EventDataGVO eventData) {
		BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
		builtInComponentGVO.setUUID(eventData.getUuid());
		String[] componentData = StringUtils.split(component, ".");
		if (componentData != null) {
			// componentid="xxy"
			if (componentData.length == 1) { // so only the name is provided
				if (containsAttribute(componentData[0])) {
					fillBuiltInComponentGVO(builtInComponentGVO, componentData[0]);
				} else {
					builtInComponentGVO.setComponentId(componentData[0]);
				}
				builtInComponentGVO.setComponentIdUUID(generateId(builtInComponentGVO.getComponentId(), eventData.getParent(), eventData.getContext()));
			} else if (componentData.length == 2) {
				if(eventData.getIndex() != null){
					component = "#"+eventData.getIndex()+"#"+component;
				}
				builtInComponentGVO.setComponentIdUUID(generateId(component, eventData.getParent(), eventData.getContext()));
				builtInComponentGVO.setWindowId(componentData[0]);
				builtInComponentGVO.setComponentId(component);
			}

		}
		return builtInComponentGVO;
	}

	protected String parseComponent(String component, EventDataGVO eventData) {
		String parsedComponent = null;
		String[] componentData = StringUtils.split(component, ".");
		if (componentData != null) {
			// componentid="xxy"
			if (componentData.length == 1) { // so only the name is provided
				if (containsAttribute(componentData[0])) {
					// fillBuiltInComponentGVO(builtInComponentGVO,componentData[0]);
				} else {
					parsedComponent = componentData[0];
				}
				parsedComponent = generateId(parsedComponent, eventData.getParent(), eventData.getContext());
			} else if (componentData.length == 2) {
				if(eventData.getIndex() != null){
					component = "#"+eventData.getIndex()+"#"+component;
				}
				parsedComponent = generateId(component, eventData.getParent(), eventData.getContext());
			}
		} else {
			logger.info("The UUID is not of correct format at all:" + eventData.getUuid());
		}

		return parsedComponent;
	}

	private void fillBuiltInComponentGVO(BuiltInComponentGVO builtInComponentGVO, String string) {
		String componentIdInComponentData = StringUtils.substringBefore(string, "[");
		String componentAttributesInComponentData = StringUtils.substringBetween(string, "[", "]");
		String[] componentAttributes = StringUtils.split(componentAttributesInComponentData, ",");
		List<String> attributes = new ArrayList<String>();
		for (int k = 0; componentAttributes != null && k < componentAttributes.length; k++) {
			attributes.add(componentAttributes[k]);
		}
		builtInComponentGVO.setAttributes(attributes);
		builtInComponentGVO.setComponentId(componentIdInComponentData);
	}

	private boolean containsAttribute(String string) {
		return (StringUtils.contains(string, '[') && StringUtils.contains(string, ']'));

	}

	void fillIn(EventItem eventItem, BuiltInFunctionGVO gvo, EventDataGVO eventData) {
		if (eventItem != null && gvo != null) {
			if (eventItem instanceof BuiltInFunction) {
				BuiltInFunction builtInFunction = (BuiltInFunction) eventItem;
				gvo.setUuid(eventData.getUuid());
				setComponents(gvo, builtInFunction, eventData);

			}
		}
	}

	/**
	 * Since this method is also in RendererHelper, but is converted to
	 * Javascript (for GWT), this method is copied to this place.
	 * 
	 * @param id
	 * @param parent
	 * @param context TODO
	 * @return
	 * @see RendererHelper class
	 */
	public static String generateId(final String id, final String parent, String context) {
		return (id + "|" + (parent == null ? "" : (parent + "|")) + context);
	}

	public static String generateId(final String id, EventDataGVO eventData) {
		return generateId(id, eventData.getParent(), eventData.getContext());
	}
}
