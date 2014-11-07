/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
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
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.SetValueExecute;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class Tiles extends FlexTable {
	

	private List<UIObject> tileElements = new ArrayList<UIObject>();
	public List<UIObject> getTileElements() {
		return tileElements;
	}

	public void setTileElements(List<UIObject> tileElements) {
		this.tileElements = tileElements;
	}

	private ComponentGVO innerComponent;
	private int colCount;

	public ComponentGVO getInnerComponent() {
		return innerComponent;
	}

	public void setInnerComponent(ComponentGVO innerComponent) {
		this.innerComponent = innerComponent;
	}
	
	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public void fillDataFromMap(DataContainerGVO data, SetValueGVO setValueGVO){
		
		List<DataContainerGVO> temp = new ArrayList<DataContainerGVO>();
		temp.add(data);
		fillDataFromMapList(temp, setValueGVO);
	}
	/**
	 * This methods fills the widget values from the list of DataMap data type. 
	 * @param List<DataMap> listDataToFill
	 * @param SetValueGVO setValueGVO
	 */
	public void fillDataFromMapList(List<DataContainerGVO> listDataToFill, SetValueGVO setValueGVO){
		int index= tileElements.size();
		int row=tileElements.size()/getColCount();
		int col=tileElements.size()%getColCount();
		
		for (DataContainerGVO object : listDataToFill) {
			String parentComponent = DOM.getElementAttribute(getElement(), "pc");
			UIObject child = AnyComponentRenderer.getInstance().render(getInnerComponent(), getInnerComponent().getUuid(),parentComponent, getInnerComponent().getContext());
			tileElements.add(child);
			assignIndex(child,index);
			RendererHelper.storeComponent(getInnerComponent(), getInnerComponent().getUuid(), child, parentComponent, getInnerComponent().getContext() + QAMLConstants.TOKEN_INDEXING + index + QAMLConstants.TOKEN_INDEXING);
			RendererHelper.addName(getInnerComponent(), child, getInnerComponent().getUuid(),parentComponent, getInnerComponent().getContext()+ QAMLConstants.TOKEN_INDEXING + index + QAMLConstants.TOKEN_INDEXING);
			
			this.setWidget(row, col,(Widget)child);
			if(col == (getColCount()-1)) {
				row++;
				col = 0;
			} else if(col<(getColCount()-1)){
				col++;
			}
				
			if (child instanceof HasWidgets) {
				fillWidget(child, object, setValueGVO);
			}
			index++;
		}
	}

	

	/**
	 * This methods fills the widget values from the list of String data type.
	 * @param List<String> listOfString
	 * @param SetValueGVO setValueGVO
	 */
	public void fillDataFromStringList(List<String> listOfString, SetValueGVO setValueGVO) {
		int row = 1;
		int col = 1;
		int colCount = getColCount();
		for (String object : listOfString) {
			UIObject child = AnyComponentRenderer.getInstance().render(this.getInnerComponent(), this.getInnerComponent().getUuid(), DOM.getElementAttribute(this.getElement(), "pc"), this.getInnerComponent().getContext());
			this.setWidget(row, col,(Widget)child);
			if(col == colCount) {
				row++;
				col = 1;
			}
			else if(col<colCount)
				col++;
			if (child instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) child;
				Iterator<Widget> itr = hasWidgets.iterator();
				while (itr.hasNext()) {
					Widget widget = itr.next();
					if (RendererHelper.isNamedComponent(widget)) {
						DataContainerGVO valueDTC = new DataContainerGVO(object);
						SetValueExecute.processValue(widget, object, setValueGVO,valueDTC);
					}
				}
			}
			else if (child instanceof Widget){
				DataContainerGVO valueDTC = new DataContainerGVO(object);
				SetValueExecute.processValue(child, object, setValueGVO,valueDTC);
			}
			
		}
	}
	
	/**
	 * This method assigns the value to the UIObject with data given in the Object paramter.
	 * @param UIObject uiObject
	 * @param Object valueToFill
	 * @param SetValueGVO setValueGVO
	 */
	private void fillWidget(UIObject uiObject, Object valueToFill, SetValueGVO setValueGVO){
		if (uiObject instanceof HasWidgets){
			HasWidgets hasWidgets = (HasWidgets) uiObject;
			Iterator<Widget> itr = hasWidgets.iterator();
			while (itr.hasNext()) {
				Widget widget = itr.next();
				if (RendererHelper.isNamedComponent(widget)) {
					String name = RendererHelper.getNamedComponentName(widget);
					if(((DataContainerGVO)valueToFill).isMap()){
						//DataMap val = (DataMap)valueToFill;
						DataMap val = ((DataContainerGVO)valueToFill).getDataMap();
						DataContainerGVO data = val.get(name); 
						if (data!=null){
							Object dataToSet;
							if(data != null && data.getKind() == DataContainerGVO.KIND_COLLECTION || data.getKind() == DataContainerGVO.KIND_MAP){
								dataToSet = data.getListofDC();
							}else {
								dataToSet = DataContainerGVO.createType(data);
								dataToSet = dataToSet == null ? null : dataToSet.toString();
							}
							SetValueExecute.processValue(widget, dataToSet, setValueGVO,data);
						}
					}
					else if(((DataContainerGVO)valueToFill).getKind() == DataContainerGVO.KIND_COLLECTION){
						//List val = (List)valueToFill;
						List val = ((DataContainerGVO)valueToFill).getListofDC();
						for(int i=0;i<val.size();i++){
							if(val.get(i)!=null){
								fillWidget((UIObject)widget, val.get(i), setValueGVO);
							}
						}
					}
					else{
						if(valueToFill.toString()!=null){
							DataContainerGVO valueDTC = new DataContainerGVO( valueToFill.toString());
							SetValueExecute.processValue(widget, valueToFill.toString(), setValueGVO,valueDTC);
						}
					}
					
				}
				if (widget instanceof HasWidgets) {
					HasWidgets innerHasWidget = (HasWidgets) widget;
					fillWidget((UIObject)innerHasWidget, valueToFill, setValueGVO);
				}
			}
		}
	}
	
	/**
	 * This method assigns index for the elements in the FlexTable.
	 * Assigning index enables tracking events on specific element.
	 * @param UIObject child
	 */
	private void assignIndex(UIObject child,int index) {
		String tileId = DOM.getElementAttribute((Element)child.getElement(), "id");
		String tempTileId = "";
		if(tileId !=null && tileId.length()>0){
			tempTileId = tileId + QAMLConstants.TOKEN_INDEXING + index + QAMLConstants.TOKEN_INDEXING;
			
		
			DOM.setElementAttribute((Element)child.getElement(), "id", tempTileId);
			ComponentRepository.getInstance().putComponent(tempTileId, child);
		}
		if (child instanceof HasWidgets) {
			HasWidgets hasWidgets = (HasWidgets) child;
			Iterator<Widget> itr = hasWidgets.iterator();
			while (itr.hasNext()) {
				Widget widget = itr.next();
				if(widget instanceof HasWidgets){
					assignIndex(widget,index);
				} 
				String eleId = DOM.getElementAttribute((Element)widget.getElement(), "id");
				String tempStr = "";
				if(eleId !=null && eleId.length()>0){
					int exist = eleId.indexOf(QAMLConstants.TOKEN_INDEXING);
					if(exist < 0){
						tempStr = QAMLConstants.TOKEN_INDEXING + index + QAMLConstants.TOKEN_INDEXING + eleId;
						DOM.setElementAttribute((Element)widget.getElement(), "id", tempStr);
						ComponentRepository.getInstance().remove(eleId);
						ComponentRepository.getInstance().putComponent(tempStr, widget);
					}
				}
			}
		}
		ComponentRepository.getInstance().remove(tileId);
	}
}
