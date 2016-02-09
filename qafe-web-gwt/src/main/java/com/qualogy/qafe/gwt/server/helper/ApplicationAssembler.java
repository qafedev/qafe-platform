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
package com.qualogy.qafe.gwt.server.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.Bundle;
import com.qualogy.qafe.bind.core.messages.Message;
import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.MenuItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.Listener;
import com.qualogy.qafe.bind.presentation.event.ListenerGroup;
import com.qualogy.qafe.bind.presentation.style.StyleSet;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.HasElements;
import com.qualogy.qafe.gwt.client.vo.ui.BundleGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.gwt.client.vo.ui.HasComponentsI;
import com.qualogy.qafe.gwt.client.vo.ui.HasContainer;
import com.qualogy.qafe.gwt.client.vo.ui.HasParent;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemSeparatorGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.StackGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.server.event.assembler.AnyEventAssembler;
import com.qualogy.qafe.gwt.server.event.assembler.listener.EventListenerGVOFactory;
import com.qualogy.qafe.gwt.server.ui.assembler.ApplicatonUIAssembler;
import com.qualogy.qafe.gwt.server.ui.assembler.MenuItemUIAssembler;
import com.qualogy.qafe.web.util.SessionContainer;

public class ApplicationAssembler {

	public final static Logger logger = Logger.getLogger(ApplicationAssembler.class.getName());

	public static MenuItemGVO renderSystemMenu(ApplicationMapping systemMenu, ApplicationContext context) {
		MenuItemGVO sysMenu = null;
		if (systemMenu!=null){

			for (MenuItem menuItem: systemMenu.getPresentationTier().getView().getContextMenus()) {
				sysMenu =(MenuItemGVO) new MenuItemUIAssembler().convert(menuItem, null, systemMenu, context, null);
				// There are no specific windows defined in the global menu definition, so global events are sufficient

			}
			processMenu(sysMenu, systemMenu.getPresentationTier().getEvents(), null);

		}
		return sysMenu;

	}

	public static UIGVO renderAll(ApplicationMapping applicationMapping, ApplicationContext context,SessionContainer sessionContainer) {

		if (applicationMapping != null) {

			UIGVO uiGVO = ApplicatonUIAssembler.convert(applicationMapping.getPresentationTier().getView(), applicationMapping, context,sessionContainer);

			// Perform modification for events and styling on the components.
			// Start with RootPanel (is there is one)
            if (uiGVO != null) {

                if (uiGVO.getWindows() != null) {
                    for (int i = 0; i < uiGVO.getWindows().length; i++) {
                        WindowGVO windowGVO = uiGVO.getWindows()[i];
                        if (windowGVO != null) {

                            StyleSet styleSet = applicationMapping.getPresentationTier().getStyles();
                            List<Event> events = mergeEvents(windowGVO.getId(), applicationMapping);
                            processComponents(windowGVO, applicationMapping, windowGVO.getId(), styleSet, events);
                        }
                    }
                }

                if (ApplicationCluster.getInstance().isClientSideEventEnabled()) {
                    Map<String, Event> allEvents = applicationMapping.getEvents();
                    if (allEvents != null) {
                        Iterator<String> itrEventKey = allEvents.keySet().iterator();
                        while (itrEventKey.hasNext()) {
                            String eventKey = itrEventKey.next();
                            Event event = allEvents.get(eventKey);
                            EventGVO eventGVO = AnyEventAssembler.assemble(event, context);
                            if (eventGVO != null) {
                                uiGVO.addEvent(eventKey, eventGVO);    
                            }
                        }
                    }
                    processLocalizedMessages(context, uiGVO, sessionContainer);
                }
                
            }
			return uiGVO;
		} else {
			return null;
		}
	}

	/**
	 * Processes localized messages and adds them to a bundle GVO based on bundle id.
	 * 
	 * @param context
	 * @param sessionContainer
	 * @param uiGVO
	 */
	private static void processLocalizedMessages(ApplicationContext context
			, UIGVO uiGVO, SessionContainer sessionContainer) {
		Messages messages = context.getMessages();
		if (messages == null) {
			return;
		}
		String language = Message.DEFAULT_LOCALE_KEY;
		Locale locale = sessionContainer.getLocale();
		if (locale != null) {
			language = locale.toString();
		}
		uiGVO.setCurrentLanguage(language);
		Map<String, Bundle> bundles = messages.getBundles();
		//For each locale within each Message object within each bundle we 
		//construct the BundleGVO.
		//The domain object code is really nasty and has to be re-factored
		//at some point.
		for (String bundleId : bundles.keySet()) {
			BundleGVO bundleGVO = new BundleGVO();
			Bundle bundle = bundles.get(bundleId);
			Map<String, Message> localizedMessages = bundle.getMessages();
			for (Entry<String, Message> messageSet : localizedMessages.entrySet()) {
				String messageKey = messageSet.getKey();
				Message message = messageSet.getValue();
				Map<String, String> messageValues = message.toMap();
				for (String messageLanguage : messageValues.keySet()) {
					String messageValue = messageValues.get(messageLanguage);
					bundleGVO.addLocalizedMessage(messageLanguage, messageKey, messageValue);
				}
			}
			uiGVO.addBundle(bundleId, bundleGVO);
		}
	}

	public static List<Event> mergeEvents(String window, ApplicationMapping applicationMapping) {

		List<Event> events = new ArrayList<Event>();
		if (applicationMapping.getPresentationTier().getEvents() != null) {
			events.addAll(applicationMapping.getPresentationTier().getEvents());
		}
		if (window != null) {
			if (applicationMapping.getPresentationTier().getView() != null) {
				List<Window> windows = applicationMapping.getPresentationTier().getView().getWindows();
				if (windows != null) {
					Iterator<Window> itr = windows.iterator();
					boolean found = false;
					while (itr.hasNext() && !found) {
						Window w = (Window) itr.next();
						if (w.getId().equals(window)) {
							if (w.getEvents() != null) {
								events.addAll(w.getEvents());
								found = true;
							}
						}
					}
				}

			}
		}
		return events;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static void processComponents(ComponentGVO component, ApplicationMapping applicationMapping, String windowId, StyleSet styleSet, List<Event> events) {
		if (component != null) {

			processMenu(component.getMenu(), events, styleSet);
			processToolbar(component, events, styleSet);
			addStyle(component, styleSet);
			addEvent(component, events);
			if (component instanceof PanelGVO) { // so also RootPanelGVO is
				// included
				PanelGVO panel = (PanelGVO) component;
				ComponentGVO[] panelComponents = null;
				if (panel.getLayout() instanceof HasElements) {
					HasElements hasElements = (HasElements) panel.getLayout();
					ElementGVO[] elements = hasElements.getElements();
					if (elements != null) {
						for (int i = 0; i < elements.length; i++) {
							ComponentGVO c = elements[i].getComponent();
							processComponents(c, applicationMapping, windowId, styleSet, events);
						}
					}

				} else if (panel.getLayout() instanceof BorderLayoutGVO) {
					BorderLayoutGVO borderLayoutGVO = (BorderLayoutGVO) (panel.getLayout());
					processComponents(borderLayoutGVO.getCenter(), applicationMapping, windowId, styleSet, events);
					processComponents(borderLayoutGVO.getNorth(), applicationMapping, windowId, styleSet, events);
					processComponents(borderLayoutGVO.getWest(), applicationMapping, windowId, styleSet, events);
					processComponents(borderLayoutGVO.getEast(), applicationMapping, windowId, styleSet, events);
					processComponents(borderLayoutGVO.getSouth(), applicationMapping, windowId, styleSet, events);

				} else {
					panelComponents = panel.getLayout().getComponents();
					if (panelComponents != null) {
						for (int i = 0; i < panelComponents.length; i++) {
							processComponents(panelComponents[i], applicationMapping, windowId, styleSet, events);
						}
					}
				}
				if (panel.getDataPanelControl() != null) {
					for (ComponentGVO componentGVO : panel.getDataPanelControl()) {
						// processing of callback for save button for a
						// datapanel
						processComponents(componentGVO, applicationMapping, windowId, styleSet, events);

					}
				}

			} else if (component instanceof HasComponentsI) {
				HasComponentsI hasComponentsI = (HasComponentsI) component;
				ComponentGVO[] cs = hasComponentsI.getComponents();
				if (cs != null) {
					for (int i = 0; i < cs.length; i++) {
						processComponents(cs[i], applicationMapping, windowId, styleSet, events);
					}
				}
			} else if (component instanceof StackGVO) {
				StackGVO stackGVO = (StackGVO) component;
				processComponents(stackGVO.getComponent(), applicationMapping, windowId, styleSet, events);
			} else if (component instanceof DataGridGVO) {
				DataGridGVO dataGridGVO = (DataGridGVO) component;
				DataGridColumnGVO[] columns = dataGridGVO.getColumns();
				if (columns != null) {
					for (int i = 0; i < columns.length; i++) {
						processComponents(columns[i], applicationMapping, windowId, styleSet, events);
					}
				}
				processComponents(dataGridGVO.getDeleteComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getAddComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getSaveComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getPageSizeComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getOffSetComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getExportComponent(), applicationMapping, windowId, styleSet, events);
				processComponents(dataGridGVO.getOverflow(), applicationMapping, windowId, styleSet, events);

			} else if (component instanceof WindowGVO) {
				processComponents(((WindowGVO) component).getRootPanel(), applicationMapping, windowId, styleSet, events);
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void processMenu(MenuItemGVO rootMenu, List<Event> events, StyleSet styleSet) {
		if (rootMenu != null) {
			if (!(rootMenu instanceof MenuItemSeparatorGVO)) {
				addEvent(rootMenu, events);
				addStyle(rootMenu, styleSet);
				if (rootMenu.getSubMenus() != null) {
					for (int i = 0; i < rootMenu.getSubMenus().length; i++) {
						processMenu(rootMenu.getSubMenus()[i], events, styleSet);
					}
				}
			}
		}

	}

	private static void processToolbar(ComponentGVO component, List<Event> events, StyleSet styleSet) {

		if (component != null) {
			if (component instanceof RootPanelGVO) {
				RootPanelGVO rootPanelGVO = (RootPanelGVO) component;
				if (rootPanelGVO.getToolbarGVO() != null && rootPanelGVO.getToolbarGVO().getToolbarItems() != null) {
					for (int i = 0; i < rootPanelGVO.getToolbarGVO().getToolbarItems().length; i++) {
						addEvent(rootPanelGVO.getToolbarGVO().getToolbarItems()[i], events);
						addStyle(rootPanelGVO.getToolbarGVO().getToolbarItems()[i], styleSet);
					}
				}
			}
		}

	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private static void addEvent(ComponentGVO componentGVO, final List<Event> events) {
		if (componentGVO != null) {
			if (events != null) {
				for (Event event : events) {

					List<ListenerGroup> listenerGroups = event.getListeners();
					if (listenerGroups != null) {
						for (ListenerGroup listenerGroup : listenerGroups) {

							List<com.qualogy.qafe.bind.presentation.event.Component> listenerGroupComponents = listenerGroup.getComponents();
							// tempList contains events listener objects,
							// each of the event inputvariables must be given
							// to the frontend=
							if (listenerGroupComponents != null) {
								for (com.qualogy.qafe.bind.presentation.event.Component component : listenerGroupComponents) {
									if (component.getComponentId() != null) {
										if (component.getComponentId().equals(componentGVO.getId())) {
											addListenerToComponent(listenerGroup.getListenerTypes(), componentGVO, component, event);
										} else if(componentGVO.getParent() != null && component.getComponentId().equals(componentGVO.getParent()+"."+ componentGVO.getId())) {
											componentGVO.setId(componentGVO.getParent()+"."+componentGVO.getId());
											addListenerToComponent(listenerGroup.getListenerTypes(), componentGVO, component, event);
										} else if (componentGVO instanceof HasContainer) {
											HasContainer hasContainer = (HasContainer) componentGVO;
											if (component.getComponentId().equals(hasContainer.getContainerName() + "." + componentGVO.getId())) {
												addListenerToComponent(listenerGroup.getListenerTypes(), componentGVO, component, event);
											}
										} else if (componentGVO instanceof HasParent) {
											HasParent hasParent = (HasParent) componentGVO;
											if (hasParent.hasParent() && component.getComponentId().equals(hasParent.getParent() + "." + componentGVO.getId())) {
												componentGVO.setId(hasParent.getParent() + "." + componentGVO.getId());
												addListenerToComponent(listenerGroup.getListenerTypes(), componentGVO, component, event);
											}
										} else if (component.getComponentId().indexOf(".") > -1) {
											int dotIndex = component.getComponentId().indexOf(".");
											String eventComponentId = component.getComponentId().substring(0, dotIndex);
											if (eventComponentId.equals(componentGVO.getId())) {
												addListenerToComponent(listenerGroup.getListenerTypes(), componentGVO, component, event);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void addListenerToComponent(List<Listener> listeners, ComponentGVO componentGVO, Component component, Event event) {
		if (listeners != null) {
			EventListenerGVO[] eventListener = componentGVO.getEvents();
			List<EventListenerGVO> tempList = null;
			if (eventListener != null) {
				tempList = new ArrayList<EventListenerGVO>(Arrays.asList(eventListener));
			} else {
				tempList = new ArrayList<EventListenerGVO>();
			}
			for (Listener listener : listeners) {

				EventListenerGVO eventListenerGVO = EventListenerGVOFactory.createEventListenerGVO(listener, component, event);
				if (eventListenerGVO != null) {
					tempList.add(eventListenerGVO);
				}
			}
			componentGVO.setEvents((EventListenerGVO[]) tempList.toArray(new EventListenerGVO[] {}));

		} else {
			logger.warning("listener type " + "event-component " + component.getComponentId() + " has no listeners for it in event " + event.getId());

		}
	}

	/**
	 * The styleset has a ComponentStyle map, which is generated after loading
	 * from the XML file. This componetStyles map is a map on (componet-id, list
	 * of styles). Only the name of the style is important for setting the style
	 * on the component, since it is css based.
	 *
	 * @param c
	 *            Component to add styling information to
	 * @param styleSet
	 *            The styleset which has been read by XML processing
	 */
	private static void addStyle(ComponentGVO c, final StyleSet styleSet) {

	}

}
