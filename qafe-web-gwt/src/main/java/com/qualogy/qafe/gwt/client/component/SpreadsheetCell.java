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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;

public class SpreadsheetCell extends Composite implements HasText, Focusable, HasEditable, HasValueChangeHandlers, HasClickHandlers, HasDoubleClickHandlers {

	private static enum Modes {
		EDIT, DISPLAY;
	}

	public final static String EMPTY_VALUE = "&nbsp;";
	public final static Modes EDIT = Modes.EDIT;
	public final static Modes DISPLAY = Modes.DISPLAY;
	
	private Modes mode = DISPLAY;
	private boolean isEditable;
	private Panel panel = new FlowPanel();
	private FocusLabel label = new FocusLabel();
	private TextBox textBox = new TextBox();
	private FocusLabel emptylabel = new FocusLabel(EMPTY_VALUE);	
	private DataContainerGVO rowValue = null;
	private ColumnDefinition<DataContainerGVO, String> columnDefinition = null;
	private int column;
	private HasDataGridMethods parentContainer;
	private int row;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public HasDataGridMethods getParentContainer() {
		return parentContainer;
	}

	public SpreadsheetCell(String text, HasDataGridMethods parent, DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDefinition, DataGridColumnGVO column) {
		this.rowValue = rowValue;
		this.columnDefinition = columnDefinition;
		this.parentContainer = parent;
		this.isEditable = !Boolean.FALSE.equals(column.getEditable());
		init(text);
	}

	protected void init(String text) {
		textBox.setVisible(false);
		textBox.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				toDisplayMode();
				label.setFocus(true);				
			}
		});
		textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				//log("textBox: keyboardListener: onKeyDown", null);
				if (mode == EDIT) {
					switch (event.getNativeKeyCode()) {
					case KeyCodes.KEY_ENTER:
						//log("textBox: keyboardListener: onKeyDown: KEY_ENTER", null);
						textBox.cancelKey();
						toDisplayMode();
						break;
					default:
						;
					}
				}
			}
		});
		
		addListeners(emptylabel);
		addListeners(label);
		
		panel.add(emptylabel);
		panel.add(label);
		panel.add(textBox);
		initWidget(panel);

		setText(text);
		setStylePrimaryName("QAFESpreadSheetCell");
		sinkEvents(Event.ONCLICK | Event.FOCUSEVENTS | Event.KEYEVENTS | Event.ONCHANGE);
	}
	
	protected void toEditMode() {
		if (isEditable) {
			//log("editMode", null);
			mode = EDIT;
			textBox.setText(label.getText());
			emptylabel.setVisible(false);
			label.setVisible(false);
			textBox.setVisible(true);
			textBox.setFocus(true);
		}
	}

	protected void toDisplayMode() {
		//log("displayMode", null);
		mode = DISPLAY;
		boolean valueChanged = (textBox.getText() != null) && !textBox.getText().equals(label.getText());

		label.setText(textBox.getText());
		textBox.setVisible(false);
		label.setVisible(true);
		// When data becomes empty the trigrrering of onClick on column is not happening, this is added to avoid that.
		if (label.getText().equals("")) {			
			label.setVisible(false);
			emptylabel.setVisible(true);
		}
		if (valueChanged) {
			//String newValue = label.getText().equals(EMPTY_VALUE) ? "" : label.getText();
			doDataChange(label.getText(), true);
		}
	}
	
	public void addListeners(FocusLabel focusLabel) {
		focusLabel.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				setFocusStyle(true);
			}
		});
		focusLabel.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if (mode == DISPLAY) {
					// label is loosing focus to text box
					setFocusStyle(false);
				}
			}
		});
		focusLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toEditMode();
			}
		});
		focusLabel.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (mode == DISPLAY) {
					switch (event.getCharCode()) {
					case KeyCodes.KEY_TAB:
						break;
					default:
						toEditMode();
					}
				}
			}
		});
	}
	
	protected void doDataChange(String newValue, boolean changedByUser) {
		if (getParentContainer() != null) {
			getParentContainer().setModified(columnDefinition, this, rowValue, new DataContainerGVO(newValue), changedByUser);
		}
	}

	public void setFocusStyle(boolean focused) {
		//log("setFocusStyle: " + focused, null);
		if (focused) {
			addStyleDependentName("focused");
		} else {
			removeStyleDependentName("focused");
		}
	}

	public boolean isDisplayMode(){
		return mode==DISPLAY;
	}
	
	public boolean isEditMode(){
		return mode==EDIT;
	}
	
	public Modes getMode() {
		return mode;
	}

	@Override
	public String toString() {
		return "EditableLable(" + label.getText() + ")";
	}

	protected void log(String msg, Exception e) {
		GWT.log(toString() + ": " + msg, e);
	}

	/*
	 * HasText - send to label
	 */

	public String getText() {
		return label.getText();
	}

	public void setText(String text) {
		emptylabel.setVisible((text == null) || (text.length() == 0));
		label.setVisible(!emptylabel.isVisible());
		label.setText(text);
		doDataChange(getText(), false);
	}

	/*
	 * HasFocus - send to label
	 */

	public int getTabIndex() {
		return label.getTabIndex();
	}

	public void setAccessKey(char key) {
		label.setAccessKey(key);
	}

	public void setFocus(boolean focused) {
		label.setFocus(focused);
	}

	public void setTabIndex(int index) {
		label.setTabIndex(index);
	}

	/*
	 * Event pumping
	 */
	/*@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			//clickListeners.fireClick(this);
			break;
		case Event.ONBLUR:
		case Event.ONFOCUS:
			//focusListeners.fireFocusEvent(this, event);
			break;
		case Event.ONKEYDOWN:
		case Event.ONKEYUP:
		case Event.ONKEYPRESS:
			//keyboardListeners.fireKeyboardEvent(this, event);
			break;
		case Event.ONCHANGE:
			//fireEvent(ValueChangeEvent.getType());//.fireChange(this);
			break;
		default:
			break;
		}
	}*/

	@Override
	public void setStylePrimaryName(String style) {
		textBox.setStylePrimaryName(style);
		label.setStylePrimaryName(style);
		super.setStylePrimaryName(style);
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		return null;// addDomHandler(handler,ValueChangeEvent.getType() );
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}
	
	public HTML getLabel() {
		return label.getWrappedLabel();
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	public void setRowValue(DataContainerGVO rowValue) {
		this.rowValue = rowValue;
	}
}