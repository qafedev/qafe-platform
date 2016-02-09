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
package com.qualogy.qafe.mgwt.server.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.MenuItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.Listener;
import com.qualogy.qafe.bind.presentation.event.ListenerGroup;
import com.qualogy.qafe.bind.presentation.style.StyleSet;
import com.qualogy.qafe.mgwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.HasElements;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.HasComponentsI;
import com.qualogy.qafe.mgwt.client.vo.ui.HasParent;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemSeparatorGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.StackGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.server.event.assembler.listener.EventListenerGVOFactory;
import com.qualogy.qafe.mgwt.server.ui.assembler.ApplicatonUIAssembler;
import com.qualogy.qafe.mgwt.server.ui.assembler.MenuItemUIAssembler;
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

	public static UIGVO renderAll(ApplicationMapping applicationMapping, ApplicationContext context,SessionContainer sc) {

		if (applicationMapping != null) {

			UIGVO uiGVO = ApplicatonUIAssembler.convert(applicationMapping.getPresentationTier().getView(), applicationMapping, context,sc);

			// Perform modification for events and styling on the components.
			// Start with RootPanel (is there is one)
			if (uiGVO != null) {

				if (uiGVO.getWindows() != null) {
					for (int i = 0; i < uiGVO.getWindows().length; i++) {
						WindowGVO w = uiGVO.getWindows()[i];
						if (w != null) {

							StyleSet styleSet = applicationMapping.getPresentationTier().getStyles();
							List<Event> events = mergeEvents(w.getId(), applicationMapping);
							processComponents(w, applicationMapping, w.getId(), styleSet, events);
							/*
							 * if (w.getRootPanel() != null) {
							 * processComponents(w.getRootPanel(),
							 * applicationMapping, w.getId(), styleSet, events);
							 * }
							 */
						}
					}
				}
			}
			return uiGVO;
		} else {
			return null;
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

	// private static StyleSet mergeStyleSet(WindowGVO w, ApplicationMapping
	// applicationMapping) {
	// StyleSet styleSet = new StyleSet();
	// if (applicationMapping.getPresentationTier().getStyles() != null) {
	//
	// StyleSet rootStyleSet =
	// applicationMapping.getPresentationTier().getStyles();
	// styleSet.setStyles(rootStyleSet.getStyles());
	// styleSet.setComponentStyles(rootStyleSet.getComponentStyles());
	// //styleSet.setStaticComponentStyles(rootStyleSet.getStaticComponentStyles());
	// } else {
	// styleSet.setStyles(new ArrayList());
	// }
	// if (w != null) {
	// if (applicationMapping.getPresentationTier().getView() != null) {
	// List windows =
	// applicationMapping.getPresentationTier().getView().getWindows();
	// if (windows != null) {
	// Iterator itr = windows.iterator();
	// boolean found = false;
	// while (itr.hasNext() && !found) {
	// Window window = (Window) itr.next();
	// if (window.getId().equals(w.getId())) {
	// StyleSet windowStyleSet = window.getStyles();
	//
	// if (windowStyleSet != null) {
	// if (windowStyleSet.getStyles() != null) {
	// styleSet.getStyles().addAll(windowStyleSet.getStyles());
	// styleSet.getComponentStyles().putAll(windowStyleSet.getComponentStyles());
	// //styleSet.getStaticComponentStyles().putAll(windowStyleSet.getStaticComponentStyles());
	// found = true;
	// }
	// }
	// }
	// }
	//
	// }
	// }
	//
	// }
	// return styleSet;
	// }

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
			} /*
			 * else if (component instanceof TabPanelGVO) { TabPanelGVO tabPanel
			 * = (TabPanelGVO) component; TabGVO[] tabs = tabPanel.getTabs(); if
			 * (tabs != null) { for (int i = 0; i < tabs.length; i++) {
			 * processComponents(tabs[i], applicationMapping, windowId,
			 * styleSet, events); } }
			 *
			 * } else if (component instanceof TreeGVO) { TreeGVO tree =
			 * (TreeGVO) component; TreeItemGVO[] children = tree.getChildren();
			 * if (children != null) { for (int i = 0; i < children.length; i++)
			 * { processComponents(children[i], applicationMapping, windowId,
			 * styleSet, events); } } } else if (component instanceof
			 * TreeItemGVO) { TreeItemGVO tree = (TreeItemGVO) component;
			 * TreeItemGVO[] children = tree.getChildren(); if (children !=
			 * null) { for (int i = 0; i < children.length; i++) {
			 * processComponents(children[i], applicationMapping, windowId,
			 * styleSet, events); } } }
			 */else if (component instanceof DataGridGVO) {
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
	private static void addEvent(ComponentGVO c, final List<Event> events) {

		boolean foundMatch = false;
		if (c != null) {
			if (events != null) {
				for (Event event : events) {

					List<ListenerGroup> listeners = event.getListeners();
					if (listeners != null) {
						for (ListenerGroup lg : listeners) {

							List<com.qualogy.qafe.bind.presentation.event.Component> components = lg.getComponents();
							// tempList contains events listener objects,
							// each of the event inputvariables must be given
							// to the frontend=
							if (components != null) {
								for (com.qualogy.qafe.bind.presentation.event.Component cEvent : components) {
									if (cEvent.getComponentId() != null) {
										if (cEvent.getComponentId().equals(c.getId())) {
											addListenerToComponent(lg.getListenerTypes(), c, cEvent, event);
											foundMatch = true;
										} else if(c.getParent() != null && cEvent.getComponentId().equals(c.getParent()+"."+c.getId())) {
											c.setId(c.getParent()+"."+c.getId());
											addListenerToComponent(lg.getListenerTypes(), c, cEvent, event);
											foundMatch = true;
										} else if (c instanceof DataGridColumnGVO) {
											DataGridColumnGVO gvo = (DataGridColumnGVO) c;

											if (cEvent.getComponentId().equals(gvo.getParentName() + "." + c.getId())) {
												addListenerToComponent(lg.getListenerTypes(), c, cEvent, event);
												foundMatch = true;
											}
										} else if (c instanceof HasParent) {
											HasParent hasParent = (HasParent) c;
											if (hasParent.hasParent() && cEvent.getComponentId().equals(hasParent.getParent() + "." + c.getId())) {
												c.setId(hasParent.getParent() + "." + c.getId());
												addListenerToComponent(lg.getListenerTypes(), c, cEvent, event);
												foundMatch = true;
											}
										} else if (cEvent.getComponentId().indexOf(".") > -1) {
											int dotIndex = cEvent.getComponentId().indexOf(".");
											String eventComponentId = cEvent.getComponentId().substring(0, dotIndex);
											if (eventComponentId.equals(c.getId())) {
												addListenerToComponent(lg.getListenerTypes(), c, cEvent, event);
												foundMatch = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (!foundMatch) {
				 logger.warning("There is no event defined for component "+c.getId());
			}
		}

	}
	// CHECKSTYLE.ON: CyclomaticComplexity

	private static void addListenerToComponent(List<Listener> listeners, ComponentGVO c, Component cEvent, Event event) {
		if (listeners != null) {
			EventListenerGVO[] l = c.getEvents();
			List<EventListenerGVO> tempList = null;
			if (l != null) {
				tempList = new ArrayList<EventListenerGVO>(Arrays.asList(l));
			} else {
				tempList = new ArrayList<EventListenerGVO>();
			}
			for (Listener listener : listeners) {

				EventListenerGVO eventListenerGVO = EventListenerGVOFactory.createEventListenerGVO(listener, cEvent, event);
				if (eventListenerGVO != null) {
					tempList.add(eventListenerGVO);
				}
			}
			c.setEvents((EventListenerGVO[]) tempList.toArray(new EventListenerGVO[] {}));

		} else {
			logger.warning("listener type " + "event-component " + cEvent.getComponentId() + " has no listeners for it in event " + event.getId());

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
		/*
		 * if (c != null && styleSet != null) { String id = c.getId(); List
		 * styles = (List) styleSet.getComponentStyles().get(id); if (styles !=
		 * null) { Iterator itr = styles.iterator(); List l = new ArrayList();
		 * while (itr.hasNext()) { Style s = (Style) itr.next();
		 * l.add(s.getId()); String[][] styleProperties =
		 * CSSStyleRenderer.getStyleProperties(s);
		 * c.setStyleProperties(styleProperties); } c.setStyleClass((String[])
		 * l.toArray(new String[] {})); }
		 *
		 * if (styleSet != null) { List typeStyles = (List)
		 * styleSet.getStaticComponentStyles().get(c.getStyleName()); if
		 * (typeStyles != null) { Iterator itr = typeStyles.iterator(); while
		 * (itr.hasNext()) { // Get the type style
		 *
		 * Style typeStyle = (Style) itr.next(); String[][] typeStyleProperties
		 * = CSSStyleRenderer.getStyleProperties(typeStyle); // Merge with the
		 * style properties already found int newLength = 0; int
		 * stylePropertiesLength = 0; if (c.getStyleProperties() != null) {
		 * newLength = c.getStyleProperties().length +
		 * typeStyleProperties.length; stylePropertiesLength =
		 * c.getStyleProperties().length; } else { newLength =
		 * typeStyleProperties.length; } String[][] mergedStyleProperties = new
		 * String[newLength][2]; // process style properties, starting with the
		 * default // and after // that the typestyle for (int i = 0; i <
		 * stylePropertiesLength; i++) { mergedStyleProperties[i][0] =
		 * c.getStyleProperties()[i][0]; mergedStyleProperties[i][1] =
		 * c.getStyleProperties()[i][1]; } for (int i = stylePropertiesLength; i
		 * < mergedStyleProperties.length; i++) { mergedStyleProperties[i][0] =
		 * typeStyleProperties[i][0]; mergedStyleProperties[i][1] =
		 * typeStyleProperties[i][1]; } // overwrite the previous value
		 * c.setStyleProperties(mergedStyleProperties); } } } }
		 */

	}

}
