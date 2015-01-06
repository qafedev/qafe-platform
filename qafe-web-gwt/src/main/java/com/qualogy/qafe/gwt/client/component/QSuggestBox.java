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

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class QSuggestBox extends SuggestBox implements HasDataModel, HasEditable {

	private QSuggestOracle qoracle=null;
	private QMultiWordSuggestion currentSuggestion=null;
	private int suggestCharactersLength=1;

	
	public QSuggestBox(QSuggestOracle oracle) {
		super(oracle);
		init(oracle);
	}

	public QSuggestBox(QSuggestOracle oracle, TextBoxBase box) {
		super(oracle, box);
		init(oracle);
	}
	
	protected void init(QSuggestOracle oracle) {
		setOracle(oracle);
		setAnimationEnabled(true);
		setPopupStyleName("qafe_suggest_popup");
		addDefaultEventHandler();
	}
	
	public int getSuggestCharactersLength() {
		return suggestCharactersLength;
	}

	public void setSuggestCharactersLength(int suggestCharactersLength) {
		this.suggestCharactersLength = suggestCharactersLength;
	}

	public QMultiWordSuggestion getCurrentSuggestion() {
		return currentSuggestion;
	}

	public QSuggestOracle getOracle() {
		return qoracle;
	}

	public void setOracle(QSuggestOracle oracle) {
		this.qoracle = oracle;
	}

	private void addDefaultEventHandler(){
		addSelectionHandler(new SelectionHandler<Suggestion>(){

			public void onSelection(SelectionEvent<Suggestion> event) {
				 currentSuggestion = (QMultiWordSuggestion)event.getSelectedItem();				 	
			}});
	}
	
	public void addChangeHandler(ChangeHandler handler){
		getTextBox().addChangeHandler(handler);
	}
	
	public Widget getWidget(){
		return getWidget();
	}
	
	public void clearSuggestions(){
		if (getOracle()!=null){
			getOracle().clear();
		}
	}

	public Object getDataModel() {
		if (getCurrentSuggestion() != null) {
			return getCurrentSuggestion().getData();	
		}
		return null;
	}

	public boolean isEditable() {
		return !getTextBox().isReadOnly();
	}

	public void setEditable(boolean editable) {
		getTextBox().setReadOnly(!editable);
	}
}