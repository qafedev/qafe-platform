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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.MSearchBox;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;
import com.qualogy.qafe.mgwt.client.BasicCell;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasSuggestionHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionEvent;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionHandler;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionSelectEvent;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionSelectHandler;

public class QTextFieldSuggestion extends LayoutPanel implements IsTextField, HasSuggestionHandlers {
	
	private static class QCell<Object> extends BasicCell<Object> {
		 @Override
         public String getDisplayString(Object model) {
                 return model.toString();
         }

         @Override
         public boolean canBeSelected(Object model) {
                 return false;
         }
	}
 
	private MSearchBox searchBox;
	private ScrollPanel scrollPanel;
	private CellList<Object> suggestionList;
	private List<Object> suggestions;
	private int suggestChars = 1;
	private String dataName;
	private Boolean required;
	private String requiredValidationMessage;
	private String requiredValidationTitle;
	
	public QTextFieldSuggestion(String text) {
		searchBox = new MSearchBox();
		searchBox.setPlaceHolder(text);
		suggestionList = new CellList<Object>(new QCell<Object>());
		scrollPanel = new ScrollPanel();
		scrollPanel.setScrollingEnabledX(false);
		scrollPanel.setWidget(suggestionList);
		add(searchBox);
		add(scrollPanel);
		initialize();
	}
	
	private void initialize() {
		searchBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// If the search key is pressed, force to execute the fireSuggestion method
				boolean force = (event.getNativeKeyCode() == KeyCodes.KEY_ENTER);
				handleSuggestion(force);
			}
		});
		suggestionList.addCellSelectedHandler(new CellSelectedHandler() {
			@Override
			public void onCellSelected(CellSelectedEvent event) {
				int selectedIndex = event.getIndex();
				handleSuggestionSelected(selectedIndex);
			}
		});
	}
	
	private void handleSuggestion(boolean force) {
		String text = searchBox.getText();
		if (force || ((text != null) && (text.length() >= suggestChars))) {
			fireSuggestion();
		}
	}
	
	private void fireSuggestion() {
		SuggestionEvent event = new SuggestionEvent(this);
		fireEvent(event);
	}
	
	private void handleSuggestionSelected(int selectedIndex) {
		Object suggestion = suggestions.get(selectedIndex);
		setData(suggestion);
		fireSuggestionSelected(suggestion);
	}
	
	private void fireSuggestionSelected(Object selection) {
		SuggestionSelectEvent event = new SuggestionSelectEvent(this, selection);
		fireEvent(event);
	}
	
	private void setSuggestions(List data) {
		suggestions = data;
		if (suggestions == null) {
			suggestions = new ArrayList<Object>();
		}
		suggestionList.render(suggestions);
		scrollPanel.refresh();
	}
	
	@Override
	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public HandlerRegistration addSuggestionHandler(SuggestionHandler handler) {
		return addHandler(handler, SuggestionEvent.getType());
	}

	@Override
	public HandlerRegistration addSuggestionSelectHandler(SuggestionSelectHandler handler) {
		return addHandler(handler, SuggestionSelectEvent.getType());
	}
	
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@Override
	public String getDataName() {
		return dataName;
	}
	
	@Override
	public void setData(Object data) {
		if (data instanceof List) {
			setSuggestions((List)data);
		} else {
			setSuggestions(null);
			String value = "";
			if (data instanceof String) {
				value = (String)data;	
			}
			Object oldData = getData();
			searchBox.setValue(value);
			Object newData = getData();
		}
	}

	@Override
	public Object getData() {
		Object data = searchBox.getValue();
		ComponentHelper.checkRequired(this, data);
		return data;
	}

	@Override
	public Object getDataModel() {
		return getData();
	}

	@Override
	public Object getModel() {
		return getData();
	}
	
	public int getSuggestChars() {
		return suggestChars;
	}

	public void setSuggestChars(int suggestChars) {
		this.suggestChars = suggestChars;
	}
	
	@Override
	public void setFocus(boolean focus) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDisplayname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayname(String displayname) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> getKeyConstraints() {
		return null;
	}

	@Override
	public List<UIObject> getStyleWidgets() {
		List<UIObject> styleWidgets = new ArrayList<UIObject>();
		styleWidgets.add(this);
		styleWidgets.add(searchBox);
		return styleWidgets;
	}
	
	@Override
	public Widget asWidget() {
		return searchBox;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}

	public void setRequiredValidationMessage(String requiredValidationMessage) {
		this.requiredValidationMessage = requiredValidationMessage;
	}

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}

	public void setRequiredValidationTitle(String requiredValidationTitle) {
		this.requiredValidationTitle = requiredValidationTitle;
	}
}