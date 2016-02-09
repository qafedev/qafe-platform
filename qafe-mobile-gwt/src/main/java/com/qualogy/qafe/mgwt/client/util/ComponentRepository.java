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
package com.qualogy.qafe.mgwt.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;

public class ComponentRepository {

	private static ComponentRepository instance = null;
	
	private Map<String,AbstractView> views = new HashMap<String,AbstractView>();
	private Map<String,UIObject> owners = new HashMap<String,UIObject>();
	private Map<String,Map<UIObject,ComponentGVO>> view2Components = new HashMap<String,Map<UIObject,ComponentGVO>>();
	private Map<String,Map<UIObject,ComponentGVO>> owner2Components = new HashMap<String,Map<UIObject,ComponentGVO>>();
	private Map<String,List<UIObject>> idBasedComponents = new HashMap<String,List<UIObject>>();
	private Map<String,List<UIObject>> nameBasedComponents = new HashMap<String,List<UIObject>>();
	private Map<String,List<UIObject>> groupBasedComponents = new HashMap<String,List<UIObject>>();
	
	// TODO
	private Map<String, List<UIObject>> componentMap = new HashMap<String, List<UIObject>>(17);
	private Map<String, List<UIObject>> namedComponentMap = new HashMap<String, List<UIObject>>();
	private Map<String, List<UIObject>> groupedComponentMap = new HashMap<String, List<UIObject>>();

	public static ComponentRepository getInstance() {
		if (instance == null) {
			instance = new ComponentRepository();
		}
		return instance;
	}

	public AbstractView getView(String viewKey) {
		return views.get(viewKey);
	}
	
	public void addView(String windowId, String context, AbstractView view, WindowGVO viewGVO) {
		String viewKey = generateViewKey(windowId, context);
		addView(viewKey, view, viewGVO);
	}
	
	public void addView(String viewKey, AbstractView view, WindowGVO viewGVO) {
		// TODO with WindowGVO
		if (viewKey == null) {
			return;
		}
		if (view == null) {
			return;
		}
		views.put(viewKey, view);
	}
	
	public UIObject getOwner(String viewKey, String ownerId) {
		String ownerKey = generateOwnerKey(ownerId, viewKey);
		return owners.get(ownerKey);
	}
	
	public void addOwner(String ownerId, String windowId, String context, UIObject owner, ComponentGVO ownerGVO) {
		String viewKey = generateViewKey(windowId, context);
		addOwner(viewKey, ownerId, owner, ownerGVO);
	}
	
	public void addOwner(String viewKey, String ownerId, UIObject owner, ComponentGVO ownerGVO) {
		if (viewKey == null) {
			return;
		}
		if (ownerId == null) {
			return;
		}
		if (owner == null) {
			return;
		}
		String ownerKey = generateOwnerKey(ownerId, viewKey);
		owners.put(ownerKey, owner);
	}
	
	public Map<UIObject,ComponentGVO> getComponents(String viewKey) {
		Map<UIObject,ComponentGVO> components = view2Components.get(viewKey);
		return components;
	}
	
	public Map<UIObject,ComponentGVO> getComponents(String viewKey, String ownerId) {
		String ownerKey = generateOwnerKey(ownerId, viewKey);
		Map<UIObject,ComponentGVO> components = owner2Components.get(ownerKey);
		return components;
	}
	
	public boolean removeComponents(String viewKey, String ownerId) {
		Map<UIObject,ComponentGVO> ownerComponents = getComponents(viewKey, ownerId);
		if (ownerComponents == null) {
			return false;
		}
		Map<UIObject,ComponentGVO> viewComponents = getComponents(viewKey);
		if (viewComponents != null) {
			Iterator<UIObject> itrWidget = ownerComponents.keySet().iterator();
			while (itrWidget.hasNext()) {
				UIObject widget = itrWidget.next();
				ComponentGVO componentGVO = ownerComponents.get(widget);
				removeComponent(viewKey, widget, componentGVO, viewComponents);
			}
		}
		ownerComponents.clear();
		return true;
	}
	
	private void removeComponent(String viewKey, UIObject widget, ComponentGVO componentGVO, Map<UIObject,ComponentGVO> components) {
		String componentKey = null;
		String componentId = componentGVO.getId();
		if (componentId != null) {
			componentKey = generateComponentKey(componentId, viewKey);
			idBasedComponents.remove(componentKey);
		}
		String componentName = componentGVO.getFieldName();
		if (componentName != null) {
			componentKey = generateComponentKey(componentName, viewKey);
			List<UIObject> widgets = getComponentByName(componentKey);
			removeComponent(widget, widgets);
		}
		String componentGroup = componentGVO.getGroup();
		if (componentGroup != null) {
			componentKey = generateComponentKey(componentGroup, viewKey);
			List<UIObject> widgets = getComponentByGroup(componentKey);
			removeComponent(widget, widgets);
		}
		components.remove(widget);
	}
	
	private void removeComponent(UIObject widget, List<UIObject> widgets) {
		if (widget == null) {
			return;
		}
		if (widgets == null) {
			return;
		}
		for (int i=0; i<widgets.size(); i++) {
			if (widget == widgets.get(i)) {
				widgets.remove(i);
				break;
			}
		}
	}
	
	public List<UIObject> getComponentById(String componentKey) {
		List<UIObject> components = idBasedComponents.get(componentKey);
		return components;
	}
	
	public List<UIObject> getComponentByName(String componentKey) {
		List<UIObject> components = nameBasedComponents.get(componentKey);
		return components;
	}
	
	public List<UIObject> getComponentByGroup(String componentKey) {
		List<UIObject> components = groupBasedComponents.get(componentKey);
		return components;
	}
	
	public void addComponent(String ownerId, String windowId, String context, UIObject widget, ComponentGVO componentGVO) {
		String viewKey = generateViewKey(windowId, context);
		addComponent(viewKey, ownerId, widget, componentGVO);
	}
	
	public void addComponent(String viewKey, String ownerId, UIObject widget, ComponentGVO componentGVO) {
		if (viewKey == null) {
			return;
		}
		if ((widget == null) ||(componentGVO == null)) {
			return;
		}
		Map<UIObject,ComponentGVO> components = view2Components.get(viewKey);
		if (components == null) {
			components = new HashMap<UIObject,ComponentGVO>();
			view2Components.put(viewKey, components);
		}
		if (components.containsKey(widget)) {
			return;
		}
		components.put(widget, componentGVO);
		
		if (ownerId != null) {
			String ownerKey = generateOwnerKey(ownerId, viewKey);
			Map<UIObject,ComponentGVO> ownerComponents = owner2Components.get(ownerKey);
			if (ownerComponents == null) {
				ownerComponents = new HashMap<UIObject,ComponentGVO>();
				owner2Components.put(ownerKey, ownerComponents);
			}		
			ownerComponents.put(widget, componentGVO);
		}
		
		registerComponentById(viewKey, widget, componentGVO);
		registerComponentByName(viewKey, widget, componentGVO);
		registerComponentByGroup(viewKey, widget, componentGVO);
	}

	private void registerComponentById(String viewKey, UIObject widget, ComponentGVO componentGVO) {
		String componentId = componentGVO.getId();
		registerComponent(viewKey, componentId, widget, componentGVO, idBasedComponents);
	}
	
	private void registerComponentByName(String viewKey, UIObject widget, ComponentGVO componentGVO) {
		String componentName = componentGVO.getFieldName();
		registerComponent(viewKey, componentName, widget, componentGVO, nameBasedComponents);
	}
	
	private void registerComponentByGroup(String viewKey, UIObject widget, ComponentGVO componentGVO) {
		String componentGroup = componentGVO.getGroup();
		registerComponent(viewKey, componentGroup, widget, componentGVO, groupBasedComponents);
	}
	
	private void registerComponent(String viewKey, String component, UIObject widget, ComponentGVO componentGVO, Map<String,List<UIObject>> xBasedComponents) {
		if (component != null) {
			component = generateComponentKey(component, viewKey);
			List<UIObject> components = xBasedComponents.get(component);
			if (components == null) {
				components = new ArrayList<UIObject>();
				xBasedComponents.put(component, components);
			}
			if (components.contains(widget)) {
				return;
			}
			components.add(widget);
		}
	}
	
	public String generateViewKey(String windowId, String context) {
		if (context == null) {
			return null;
		}
		String viewKey = context;
		if (windowId != null) {
			viewKey = windowId + "|" + viewKey;
		}
		return viewKey;
	}
	
	public String generateOwnerKey(String ownerId, String windowId, String context) {
		String viewKey = generateViewKey(windowId, context);
		String ownerKey = generateOwnerKey(ownerId, viewKey);
		return ownerKey;
	}
	
	public String generateOwnerKey(String ownerId, String viewKey) {
		String componentKey = generateComponentKey(ownerId, viewKey);
		return componentKey;
	}
	
	public String generateComponentKey(String component, String windowId, String context) {
		String viewKey = generateViewKey(windowId, context);
		String componentKey = generateComponentKey(component, viewKey);
		return componentKey;
	}
	
	public String generateComponentKey(String component, String viewKey) {
		String componentKey = viewKey;
		if (component != null) {
			componentKey = component + "|" + componentKey;
		}
		return componentKey;
	}
	
	private Map<String, List<UIObject>> getComponentMap() {
		return componentMap;
	}

	public void putNamedComponent(final String key, UIObject uiObject) {
		if (!getNamedComponentMap().containsKey(key)) {
			List<UIObject> listOfUIObject = new ArrayList<UIObject>();
			listOfUIObject.add(uiObject);
			getNamedComponentMap().put(key, listOfUIObject);
		} else {
			List<UIObject> listOfUIObject = getNamedComponentMap().get(key);
			listOfUIObject.add(uiObject);
			// ClientApplicationContext.getInstance().log("Component repository","Key "
			// + key +
			// " is already in the named componentstorage. You are overwriting the same id!!",
			// false,false,null);
		}
	}

	public void putGroupedComponent(final String key, UIObject uiObject) {
		if (!getGroupedComponentMap().containsKey(key)) {
			List<UIObject> listOfUIObject = new ArrayList<UIObject>();
			listOfUIObject.add(uiObject);
			getGroupedComponentMap().put(key, listOfUIObject);
		} else {
			List<UIObject> listOfUIObject = getGroupedComponentMap().get(key);
			listOfUIObject.add(uiObject);
	
		}
	}

	public List<UIObject> getNamedComponent(final String key) {
		if (getNamedComponentMap().containsKey(key)) {
			return getNamedComponentMap().get(key);
		} else {
			return null;
		}
	}
	
	public List<UIObject> getGroupedComponent(final String key) {
		if (getGroupedComponentMap().containsKey(key)) {
			return getGroupedComponentMap().get(key);
		} else {
			return null;
		}
	}

	public void remove(String key) {
		if (getComponentMap().containsKey(key)) {
			getComponentMap().remove(key);
		}
	}

	public void putComponent(final String key, UIObject uiObject) {
		if (!getComponentMap().containsKey(key)) {
			List<UIObject> listOfUIObject = new ArrayList<UIObject>();
			listOfUIObject.add(uiObject);
			getComponentMap().put(key, listOfUIObject);
		} else {
			List<UIObject> listOfUIObject = getComponentMap().get(key);
			listOfUIObject.add(uiObject);

			// ClientApplicationContext.getInstance().log("Component repository","Key "
			// + key +
			// " is already in the componentstorage. You are overwriing the same id!!",
			// false,false,null);
		}
	}

	public List<UIObject> getComponent(final String key) {
		if (getComponentMap().containsKey(key)) {
			return getComponentMap().get(key);
		} else {
			return null;
		}
	}

	public void removeAllItemsForWindow(String windowId, String context) {
		if ((windowId != null) && (context != null)) {

			// The id of the ui-window is winId|winId|appId
			// (<id>|<parent>|<context>),
			// while the id of ui-components within a window is
			// componentId|winId|appId,
			// so the key to match is |winId|appId
			String key = RendererHelper.generateId("", windowId, context);

			removeItemsWhenMatchKey(getComponentMap(), key);
			removeItemsWhenMatchKey(getNamedComponentMap(), key);
			removeItemsWhenMatchKey(getGroupedComponentMap(), key);
		}
	}

	private void removeItemsWhenMatchKey(Map<String, List<UIObject>> map,
			String key2Match) {
		if ((map != null) && (key2Match != null)) {
			List<String> removeKeyList = new ArrayList<String>();
			Iterator<String> itrKey = map.keySet().iterator();
			while (itrKey.hasNext()) {
				String key = (String) itrKey.next();
				if (key.indexOf(key2Match) > -1) {
					removeKeyList.add(key);
				}
			}

			for (String removeKey : removeKeyList) {
				List<UIObject> uiObjectList = map.get(removeKey);
				if (uiObjectList != null) {
					uiObjectList.clear();
				}
				map.remove(removeKey);
			}
		}
	}

	

	public Map<String, List<UIObject>> getNamedComponentMap() {
		return namedComponentMap;
	}

	public void clearAll() {
		clearMap(getNamedComponentMap());
		clearMap(getComponentMap());
	}

	private void clearMap(Map<String, List<UIObject>> map) {
		if (map != null) {
			map.clear();
		}
	}

	public void clearContainerComponent(Widget innerComponent) {
		if (innerComponent != null) {
			if (innerComponent instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) innerComponent;
				for (Widget widget : hasWidgets) {

					// search repository using component id and remove it.
					String key = DOM.getElementAttribute(widget.getElement(),
							"id");
					List<UIObject> uiObjects = getComponentMap().get(key);
					if (uiObjects != null) {
						// getting ready to garbage collect.
						uiObjects.clear();
					}
					getComponentMap().remove(key);

					// search repository using component name and remove it.
					String nameKey = DOM.getElementAttribute(
							widget.getElement(), "fn");
					List<UIObject> namedUiObjects = getNamedComponentMap().get(
							nameKey);
					if (namedUiObjects != null) {
						// getting ready to garbage collect.
						namedUiObjects.clear();
					}
					getNamedComponentMap().remove(key);
					
					// search repository using component group and remove it.
					String groupKey = DOM.getElementAttribute(
							widget.getElement(), "grp");
					List<UIObject> groupedUiObjects = getGroupedComponentMap().get(
							groupKey);
					if (groupedUiObjects != null) {
						// getting ready to garbage collect.
						groupedUiObjects.clear();
					}
					getGroupedComponentMap().remove(key);
					

					clearContainerComponent(widget);

				}
			}
		}

	}

	public Map<String, List<UIObject>> getGroupedComponentMap() {
		return groupedComponentMap;
	}

	public void setGroupedComponentMap(
			Map<String, List<UIObject>> groupedComponentMap) {
		this.groupedComponentMap = groupedComponentMap;
	}	
}
