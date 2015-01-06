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
package com.qualogy.qafe.gwt.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;

public class ComponentRepository {

	private Map<String, List<UIObject>> componentMap = new HashMap<String, List<UIObject>>(
			17);

	private Map<String, List<UIObject>> namedComponentMap = new HashMap<String, List<UIObject>>();

	private Map<String, List<UIObject>> groupedComponentMap = new HashMap<String, List<UIObject>>();

	private static ComponentRepository singleton = new ComponentRepository();

	public static ComponentRepository getInstance() {

		return singleton;
	}

	public Map<String, List<UIObject>> getComponentMap() {
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
		clearMap(getGroupedComponentMap());
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
					clearComponentWithId(widget);
					clearComponentWithName(widget);					
					clearComponentWithGroup(widget);					

					clearContainerComponent(widget);
				}
			}
		}

	}

	private void clearComponentWithGroup(Widget widget) {
		// search repository using component group and remove it.
		String group = RendererHelper.getGroupName(widget);
		if ((group == null) || (group.trim().length() == 0)) {
			return;
		}
		
		String[] groupNames = group.split(",");
		for (String groupName : groupNames) {
			groupName = groupName.trim();
			String groupKey = RendererHelper.generateId(groupName, widget);
			List<UIObject> groupedUIObjects = getGroupedComponentMap().get(groupKey);
			if (groupedUIObjects != null) {
				for (UIObject groupedUIObject : groupedUIObjects) {
					if (groupedUIObject == widget) {
						groupedUIObjects.remove(groupedUIObject);
						break;
					}
				}
			}
		}
	}

	private void clearComponentWithName(Widget widget) {
		// search repository using component name and remove it.
		String name = RendererHelper.getNamedComponentName(widget);
		if(name == null || name.trim().length() == 0) {
			return;
		}
		String nameKey = RendererHelper.generateId(name, widget);
		
		List<UIObject> namedUiObjects = getNamedComponentMap().get(nameKey);
		if(namedUiObjects != null) {
			for(UIObject uiobject: namedUiObjects) {
				if(uiobject == widget) {
					namedUiObjects.remove(uiobject);
					break;
				}
			}
		}		
	}

	private void clearComponentWithId(Widget widget) {
		// search repository using component id and remove it.
		String key = RendererHelper.getComponentId(widget);
		if(key == null || key.trim().length() == 0) {
			return;
		}
		List<UIObject> uiObjects = getComponentMap().get(key);
		if (uiObjects != null) {
			// getting ready to garbage collect.
			uiObjects.clear();
		}
		getComponentMap().remove(key);
	}

	public Map<String, List<UIObject>> getGroupedComponentMap() {
		return groupedComponentMap;
	}

	public void setGroupedComponentMap(
			Map<String, List<UIObject>> groupedComponentMap) {
		this.groupedComponentMap = groupedComponentMap;
	}

}
