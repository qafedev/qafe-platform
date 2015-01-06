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
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

/**
 * An iterator that serves as the data source for TableOracle requests.
 */
public class QTableModel extends MutableTableModel<DataContainerGVO> {

	public static final String KEY_WORD_SORT_ON_COLUMN = "$SORT_COLUMN";
	public static final String KEY_WORD_SORT_ORDER = "$SORT_ORDER";
	public static final String KEY_WORD_SORT_ORDER_ASC = "asc";
	public static final String KEY_WORD_SORT_ORDER_DESC = "desc";
	
	public final static String KEY_WORD_PAGESIZE = "$PAGESIZE";
	public final static String KEY_WORD_PAGE_NUMBER = "$OFFSET";
	
	
	/**
	 * 
	 */
	private Callback<DataContainerGVO> currentCallback = null;
	
	private Request currentRequest = null;


	/**
	 * which button is responsible for the sending part!
	 */
	private UIObject sender;

	private QPagingScrollTable source;
    
    private String sortColumn = null;
    private String sortOrder = null;
    
	public QTableModel(){
		// create request and response yourself
		currentRequest=  new Request(0,50);//,new TableModelHelper.ColumnSortList());
		currentCallback = new Callback<DataContainerGVO>(){

			public void onFailure(Throwable caught) {
				ClientApplicationContext.getInstance().log("Setting datagrid values failed", caught);
				
			}

			public void onRowsReady(Request request, Response<DataContainerGVO> response) {
				source.setData(request, response);
			}};
	}
    
	@Override
	public void requestRows(final Request request, final Callback<DataContainerGVO> callback) {
    	requestRows(request, callback, null);
	}
	
	public void requestRows(final Request request, final Callback<DataContainerGVO> callback, final ResultHandler resultHandler) {
		currentRequest = request;
		currentCallback = callback;
		
        final int col = request.getColumnSortList().getPrimaryColumn();
        final boolean ascending = request.getColumnSortList().isPrimaryAscending();
        
    	boolean canCreateCallback = false;
    	String listenerType = null;
    	Map<String,Object> internalVars = null;

    	boolean isSorting = (col > -1);
    	boolean isPaging = false;
    	boolean isDelete = false;
    	boolean isAdd = false;
    	boolean isSave = false;
    	boolean isCancel = false;
    	boolean isRefresh = false;
    	
    	if (sender != null) {
    		isPaging = isPagingControl(sender.getStyleName());
    		isDelete = isDeleteControl(sender.getStyleName());
    		isAdd = isAddControl(sender.getStyleName());
    		isSave = isSaveControl(sender.getStyleName());
    		isCancel = isCancelControl(sender.getStyleName());
    		isRefresh = isRefreshControl(sender.getStyleName());
    	}
    	
    	if (isPaging || isDelete || isAdd || isSave || isCancel || isRefresh) {
    		canCreateCallback = true;
    		listenerType = "onclick";
    		if(source.isScrollEvent()) {
    			sender = source;
    			listenerType = "onscroll-bottom";
    			source.setScrollEvent(false); // setting to default value
    		}
    	} else if (isSorting) {
    		canCreateCallback = true;
    		sender = source;
			listenerType = "onfetchdata";
			sortColumn = lookupColumnName(col);
			sortOrder = ascending ? KEY_WORD_SORT_ORDER_ASC : KEY_WORD_SORT_ORDER_DESC;
    	}
		internalVars = createInternalVars();
	    	
    	if (canCreateCallback) {
    		// registering result handler to do the post event body actions.
    		registerResultHandler(sender, listenerType, resultHandler);
    		EventListenerGVO eventListenerGVO = lookupEvent(sender, listenerType);
    		List<InputVariableGVO> inputVars = (eventListenerGVO != null) ? eventListenerGVO.getInputvariablesList() : new ArrayList<InputVariableGVO>();
    		CallbackHandler.createCallBack(sender, listenerType, eventListenerGVO, inputVars, internalVars,null);
    		
    		// When paging the sender is set, but when sorting the sender is not set
    		// so "refreshing" the sender by setting it to null
    		setSender(null);
    	}		
	}
	
	private void registerResultHandler(UIObject sender, String listenerType, ResultHandler resultHandler) {
		if ((sender != null) && (resultHandler != null)) {
			String senderId = DOM.getElementAttribute(sender.getElement(), "id");
    		ClientApplicationContext.getInstance().addResultHandler(senderId, listenerType, resultHandler);		
		}
	}
	
	protected String lookupColumnName(int columnIndex) {
		if (source.getTableDefinition() != null) {
			List<ColumnDefinition<DataContainerGVO,?>> columnDefinitionList = source.getTableDefinition().getVisibleColumnDefinitions();
			if ((columnDefinitionList != null) && (columnDefinitionList.size() > columnIndex)) {
				ColumnDefinition<DataContainerGVO,?> columnDefinition = columnDefinitionList.get(columnIndex);
				if (columnDefinition instanceof QColumnDefinition) {
					QColumnDefinition qColumnDefinition = (QColumnDefinition)columnDefinition;
					return qColumnDefinition.getField();
				}
			}	
		}
		return "";
	}
	
	protected EventListenerGVO lookupEvent(UIObject uiObject, String listenerType) {
		if (source.getSource() != null) {
			String uiObjectId = DOM.getElementAttribute(uiObject.getElement(), "id");
			if (uiObjectId != null) {
				int pipeIndex = uiObjectId.indexOf("|");
				if (pipeIndex > -1) {
					uiObjectId = uiObjectId.substring(0, pipeIndex);
					if (source.getSource().getEvents() != null) {
						for (EventListenerGVO eventGVO : source.getSource().getEvents()) {
							if ((eventGVO.getEventListenerType() != null) && eventGVO.getEventListenerType().equals(listenerType)
									&& (eventGVO.getEventComponentId() != null) && eventGVO.getEventComponentId().equals(uiObjectId)) {
								return eventGVO;
							}
						}	
					}
				}
			}
		}	
		return null;
	}
	
	protected boolean isDeleteControl(String senderName) {
		if (senderName != null) {
			return senderName.endsWith(QPagingScrollTableOperation.STYLE_DELETE);
		}	
		return false;
	}
	
	protected boolean isAddControl(String senderName) {
		if (senderName != null) {
			return senderName.endsWith(QPagingScrollTableOperation.STYLE_ADD);
		}	
		return false;
	}
	
	protected boolean isSaveControl(String senderName) {
		if (senderName != null) {
			return senderName.endsWith(QPagingScrollTableOperation.STYLE_SAVE);
		}	
		return false;
	}
	
	protected boolean isCancelControl(String senderName) {
		if (senderName != null) {
			return senderName.endsWith(QPagingScrollTableOperation.STYLE_CANCEL);
		}	
		return false;
	}
	
	protected boolean isRefreshControl(String senderName) {
		if (senderName != null) {
			return senderName.endsWith(QPagingScrollTableOperation.STYLE_REFRESH);
		}	
		return false;
	}
	
	protected boolean isPagingControl(String senderName) {
		if (senderName != null) {
			if (senderName.endsWith(QPagingOptions.STYLE_FIRSTPAGE)
					|| senderName.endsWith(QPagingOptions.STYLE_PREVIOUSPAGE)
					|| senderName.endsWith(QPagingOptions.STYLE_NEXTPAGE)
					|| senderName.endsWith(QPagingOptions.STYLE_LASTPAGE)) {
				return true;
			}	
		}
		return false;
	}
	
	protected Map<String,Object> createInternalVars() {
		Map<String,Object> internalVars = new HashMap<String,Object>();
		// sorting
		internalVars.put(KEY_WORD_SORT_ON_COLUMN, sortColumn);
		internalVars.put(KEY_WORD_SORT_ORDER, sortOrder);
		// paging
		internalVars.put(KEY_WORD_PAGESIZE, String.valueOf(source.getPageSize()));
		internalVars.put(KEY_WORD_PAGE_NUMBER, String.valueOf(source.getCurrentPage()));
		return internalVars;
	}
	
	public boolean isByPagingRequest(){
		return (currentRequest!=null && currentCallback!=null);
	}
	
	public void processData(List<DataContainerGVO> listOfDataMap){
		if (listOfDataMap!=null){
			if (currentRequest!=null && currentCallback!=null){
				setRowCount(listOfDataMap.size());
				SerializableResponse<DataContainerGVO> response = new SerializableResponse<DataContainerGVO>(listOfDataMap);
				
				currentCallback.onRowsReady(currentRequest, response);
			}
		}
	}
	
	@Override
	protected boolean onRowInserted(int beforeRow) {
		return true;
	}

	@Override
	protected boolean onRowRemoved(int row) {
		return true;
	}

	@Override
	protected boolean onSetRowValue(int row, DataContainerGVO rowValue) {
		return true;
	}

	public void setSender(UIObject sender) {
		this.sender = sender;
	}

	public void setSource(QPagingScrollTable pagingScrollTable) {
		this.source= pagingScrollTable;
	}
}
