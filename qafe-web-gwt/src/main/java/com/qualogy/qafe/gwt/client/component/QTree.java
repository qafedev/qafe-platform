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
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO;

public class QTree extends Tree implements HasData {

	public static final String NODE = "id";
	public static final String NODE_TYPE = "fqn";
	public static final String PARENT_NODE = "parent";
	public static final String KEY_DELIMITER = "|";
	
	private class TreeHelper implements SelectionHandler<TreeItem> {
		
		public void onSelection(SelectionEvent<TreeItem> event) {
			doItemSelection(event);
		}
	}
	
	private Map<String,Object> items = new HashMap<String, Object>();
	private Map<String,String> key2Parent = new LinkedHashMap<String,String>();
	private Map<UIObject,Object> uiToKey = new HashMap<UIObject,Object>();
	private Map<Object,UIObject> keyToUI = new HashMap<Object, UIObject>();
	private Map<Object,Object> keyToUIModel = new HashMap<Object, Object>();
	
	private TreeHelper helper = new TreeHelper();
	private TreeItem selectedItem = null;
	private boolean structuredData;
	private TreeItem rootItem;
	private boolean showRootItem = true;
	

	public QTree(TreeItem rootItem) {
		this(rootItem, true);
	}
	
	public QTree(TreeItem rootItem, boolean showRootItem) {
		this.rootItem = rootItem;
		this.showRootItem = showRootItem;
		if (showRootItem) {
			super.addItem(rootItem);	
		}
		addSelectionHandler(helper);
	}
	
	@Override
	public void addItem(TreeItem item) {
		addItem(item, null);
	}

	public void addItem(TreeItem item, TreeItem parent) {
		if (parent != null) {
			parent.addItem(item);	
		} else {
			if (showRootItem) {
				getItem(0).addItem(item);
			} else {
				super.addItem(item);
			}
		}
	}

	public Object getData() {
		List data = null;
		if (selectedItem != null) {
			Object key = uiToKey.get(selectedItem);
			if ((key != null) || ((key == null) && isRootItem(selectedItem))) {
				data = new ArrayList();
				if (key != null) {
					Object uiModel = keyToUIModel.get(key);
					data.add(uiModel);
				}
				
				List itemList = null;
				
				populateData(data, key, structuredData);
								
			}
		}
		return data;
	}

	private boolean isRootItem(TreeItem treeItem) {
		return (rootItem == treeItem);
	}
	
	private void populateData(List data, Object key, boolean structured) {
		Object uiModel;
		Iterator<String> itrKey = key2Parent.keySet().iterator();
		while (itrKey.hasNext()) {
			String nodeKey = itrKey.next();
			String parentKey = key2Parent.get(nodeKey);
			if ((parentKey == key) || ((parentKey != null) && parentKey.equals(key))) {
				uiModel = keyToUIModel.get(nodeKey);
				data.add(uiModel);
				
				// in case of structured data the model itself contains the child items in it ,
				// so we have child items and relation implicitly.
				if ( !structured ) {
					populateData(data, nodeKey, structured);
				}
			}
		}
	}
	
	public void setData(Object data, String action, Object mapping) {
		List itemList = null;
		if (data instanceof Map) {
			itemList = new ArrayList();
			itemList.add(data);
		} else if (data instanceof List) {
			itemList = (List)data;
		}
		if (isSetAction(action)) {
			clearAll();
		}
		processItems(itemList, action, mapping);
	}

	private void clearAll() {
		keyToUI.clear();
		keyToUIModel.clear();
		key2Parent.clear();
		uiToKey.clear();
		
		clearItems();
	}
	
	private void clearItems() {
		if (showRootItem) {
			TreeItem rootItem = getItem(0);
			if (rootItem != null) {
				int childCount = rootItem.getChildCount();
				for (int i=childCount-1; i>=0; i--) {
					rootItem.getChild(i).remove();
			    }	
			}
		} else {
			int childCount = this.getItemCount();
			for (int i=childCount-1; i>=0; i--) {
				this.getItem(i).remove();
		    }	
		}	
	}
	
	private void processItems(List itemList, String action, Object mapping) {
		createItems(itemList, action, mapping);
		connectItems();
	}
	
	private void createItems(List itemList, String action, Object mapping) {
		if(itemList == null) {
			return;
		}
		for (Object item : itemList) {
			Object itemKey = getKeyValue(item, NODE, mapping);
			if (itemKey != null) {
				items.put(itemKey.toString(), item);
			}
		}
		
		if (hasStructuredRelation(itemList)) {
			structuredData = true;
			createItemsFromStructuredData(null, itemList, action, mapping);
		} else {
			structuredData = false;
			createItemsFromKeyData(itemList, action, mapping);
		}
	}
	
	/**
	 *  In sructured data - from java servive the relation between the items are implicit by its structure.
	 *  We have store the relation for intenal use by iterating the items properties.
	 * 
	 */
	private void createItemsFromStructuredData(String parentKey, List itemList, String action, Object mapping) {
		if (itemList != null) {
			for (Object item : itemList) {
				String nodeKey = generateKey4Map(item, mapping);
				if (nodeKey != null) {
					if (parentKey != null) {
						nodeKey += KEY_DELIMITER + parentKey;
					}
					
					TreeItem treeItem = createTreeItem(item, mapping);
					
					storeUIAndModel(nodeKey, treeItem, item);
					key2Parent.put(nodeKey, parentKey);
					
					if (isMapContainer(item)) {
						//Map map = (Map)item;
						Map map = ((DataContainerGVO)item).getDataMap();
						Iterator<String> itrKeys = map.keySet().iterator();
						while (itrKeys.hasNext()) {
							String key = itrKeys.next();
							//Object value = getKeyValue(map, key);
							Object value = map.get(key);
							if (value instanceof DataContainerGVO) {
								DataContainerGVO valueGVO = (DataContainerGVO)value;
								if (valueGVO.getKind() == DataContainerGVO.KIND_COLLECTION) {
									TreeItem subTreeItem = createTreeItem(key);
									String subNodeKey = key + KEY_DELIMITER + nodeKey;
									storeUIAndModel(subNodeKey, subTreeItem, null);
									createItemsFromStructuredData(subNodeKey, valueGVO.getListofDC(), action, mapping);
									key2Parent.put(subNodeKey, nodeKey);
								}
							}
						}
					}					
				}
			}	
		}
	}
	
	// If the data contains id and parent as attributes or if user specified in mapping the values for it. 
	private void createItemsFromKeyData(List itemList, String action, Object mapping) {
		if (itemList != null) {
			for (Object item : itemList) {
				String nodeKey = generateKey4Map(item, mapping);
				if (nodeKey != null) {					
					Object parentItem = getParentItem(item, mapping);
					String parentKey = generateKey4Map(parentItem, mapping);
					
					TreeItem treeItem = createTreeItem(item, mapping);
					
					storeUIAndModel(nodeKey, treeItem, item);
					key2Parent.put(nodeKey, parentKey);
				}
			}	
		}
	}

	private Object getParentItem(Object item, Object mapping) {
		Object parentItemKey = getKeyValue(item, PARENT_NODE, mapping);
		if (parentItemKey != null) {
			return items.get(parentItemKey.toString());	
		}
		return null;
	}
	
	private void connectItems() {
		Iterator<String> itrKeys = key2Parent.keySet().iterator();
		while (itrKeys.hasNext()) {
			String nodeKey = itrKeys.next();
			String parentKey = key2Parent.get(nodeKey);
			TreeItem treeItem = (TreeItem)getUIObject(nodeKey);
			TreeItem parentTreeItem = (TreeItem)getUIObject(parentKey);
			if (treeItem != null) {
				addItem(treeItem, parentTreeItem);	
			}
		}
	}

	private boolean hasStructuredRelation(List itemList) {
		if (itemList != null) {
			for (Object item : itemList) {
				if (isMapContainer(item)) {
					//Map map = (Map)item;
					Map map = ((DataContainerGVO)item).getDataMap();
					Iterator<String> itrKeys = map.keySet().iterator();
					while (itrKeys.hasNext()) {
						String key = itrKeys.next();
						//Object value = getKeyValue(map, key);
						Object value = map.get(key);
						if (value instanceof DataContainerGVO) {
							DataContainerGVO valueGVO = (DataContainerGVO)value;
							if (valueGVO.getKind() == DataContainerGVO.KIND_COLLECTION) {
								return true;
							}
						}
					}
				}					
			}	
		}
		return false;
	}	
	
	private void doItemSelection(SelectionEvent<TreeItem> event) {
		selectedItem = event.getSelectedItem();
		fireItemClickEvent(event);
	}
	
	private void fireItemClickEvent(final SelectionEvent<TreeItem> event) {
		// When a qaml is listening to a tree with its id, and when clicks on parent tree , as the parent tree is also an item 
		// we dont have to do implicit click handling of item. otherwise 2 times the event will be executed.
		if (isRootItem(event.getSelectedItem())) {
			return;
		}
		
		if (rootItem instanceof QTreeItem) {
			final Label rootItemlabel =((QTreeItem)rootItem).getLabel();
			ClickEvent clickEvent = new ClickEvent() {
				// This is to make sure that we are making the item id and value available as src-id and src-value
				@Override
	            public Object getSource() {
					if (event.getSelectedItem() instanceof QTreeItem) {
						return ((QTreeItem)event.getSelectedItem()).getLabel();
					}
	                return rootItemlabel;
	            }
			};
			rootItemlabel.fireEvent(clickEvent);
		}
	}
	
	private TreeItem createTreeItem(String name) {
		TreeItem treeItem = new QTreeItem(name, this);
		fillInAttributesFromRoot(name, treeItem);
		return treeItem;
	}
	
	private TreeItem createTreeItem(Object item, Object mapping) {
		TreeItemGVO itemGVO = populateGVO(item, mapping);
		
		TreeItem treeItem = new QTreeItem(itemGVO.getDisplayname(), this);			
		fillInItemProperties(itemGVO, treeItem);		
		fillInAttributesFromRoot(itemGVO.getId(), treeItem);
		return treeItem;
	}
	
	private void fillInAttributesFromRoot(String itemId, TreeItem treeItem) {
		String uuid = "";
		String appContext = "";
		String parent = "";

		String rootTreeId = RendererHelper.getComponentAttributeValue(this,  "id");
		itemId = itemId + rootTreeId.substring(rootTreeId.indexOf("|"));
		uuid = RendererHelper.getComponentPropertyValue(this, "uuid");
		appContext = RendererHelper.getComponentContext(this);
		parent = RendererHelper.getParentComponent(this);
		
		Label itemLabel = ((QTreeItem)treeItem).getLabel();
		DOM.setElementAttribute(itemLabel.getElement(), "id", itemId);
		DOM.setElementProperty(itemLabel.getElement(), "uuid", uuid);
		RendererHelper.setComponentContext(itemLabel, appContext);
		RendererHelper.setParentComponent(itemLabel, parent);
	}
	
	private void fillInItemProperties(TreeItemGVO itemGVO, TreeItem treeItem) {
		treeItem.setState(itemGVO.getExpand());
	}
	
	private TreeItemGVO populateGVO(Object item,  Object mapping) {
		Object id = getKeyValue(item, "id", mapping);
		Object displayName = getKeyValue(item, "displayname", mapping);
		Object disabled = getKeyValue(item, "disabled", mapping);
		Object visible = getKeyValue(item, "visible", mapping);
		Object tooltip = getKeyValue(item, "tooltip", mapping);
		Object value = getKeyValue(item, "value", mapping);
		Boolean expand = false;
		Object propExpand = getKeyValue(item, "expand", mapping);
		if (propExpand instanceof Boolean) {
			expand = (Boolean)propExpand;
		}
		if (displayName == null) {
			displayName = new String("Item");
		}
		
		TreeItemGVO itemGVO = new TreeItemGVO();
		itemGVO.setId(getDataString(id));
		itemGVO.setDisplayname(getDataString(displayName));
		itemGVO.setExpand(expand);
		itemGVO.setTooltip(getDataString(tooltip));
		
		return itemGVO;
	}
	
	public String getDataString(Object item) {
		String value = null;
		if(item instanceof DataContainerGVO) {
			value = ((DataContainerGVO)item).getDataString();
		}
		return value;
	}
	
	private void storeUIAndModel(Object key, UIObject uiObject, Object uiObjectModel) {
		uiToKey.put(uiObject, key);
		keyToUI.put(key, uiObject);
		keyToUIModel.put(key, uiObjectModel);
	}

	private UIObject getUIObject(Object key) {
		return keyToUI.get(key);
	}
	
	private Object getUIModel(Object key) {
		return keyToUIModel.get(key);
	}

	private String generateKey4Map(Object item, Object mapping) {
		return generateKey4Map(item, NODE, mapping);
	}
	
	private String generateKey4Map(Object item, String key, Object mapping) {
		Object nodeKey = getKeyValue(item, key, mapping);
		Object nodeTypeKey = getKeyValue(item, NODE_TYPE, mapping);
		if ((nodeKey != null) ) {	
			return nodeKey + KEY_DELIMITER + nodeTypeKey;	
		}
		return null;
	}
	
	private Object getKeyValue(Object item, String key) {
		return getKeyValue(item, key, null);
	}
	
	private Object getKeyValue(Object item, String key, Object mapping) {
		if (isMapContainer(item) && (key != null)) {
			//Map map = (Map)item;
			Map map = ((DataContainerGVO)item).getDataMap();
			key = getMappedKey(key, mapping);
			if (map.containsKey(key)) {
				return map.get(key);
			} else if (map.containsKey(key.toLowerCase())) {
				return map.get(key.toLowerCase());
			} else if (map.containsKey(key.toUpperCase())) {
				return map.get(key.toUpperCase());
			}
		}
		return null;
	}

	private String getMappedKey(String key, Object mapping) {
		String newKey = key;
		if (mapping instanceof Map) {			
			Map map = (Map)mapping;
			if (map.containsKey(key)) {
				newKey = (String)map.get(key);
			}
		}
		return newKey;
	}
	

	public static boolean isSetAction(String action) {
		return ((action != null) && (action.equals("set")));
	}
	
	/*public static boolean isMap(Object data) {
		return (data instanceof Map);
	}*/
	public static boolean isMapContainer(Object data) {
		if(data instanceof DataContainerGVO) {
			return (((DataContainerGVO) data).isMap()); 
		} 
		return false;
	}
	
	public static boolean isList(Object data) {
		return (data instanceof List);
	}
}
