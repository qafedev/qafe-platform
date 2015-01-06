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

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class QDatePicker extends DateBox implements HasEditable, HasRequiredValidationMessage {

	private Format sdf = new DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
	private String requiredValidationMessage;
	private String requiredValidationTitle;

	public QDatePicker(final String dateFormat) {
		super(new DatePickerWithYearSelector(), null, new DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));
		// setAnimationEnabled(true);
		if (dateFormat != null) {
			sdf = new DefaultFormat(DateTimeFormat.getFormat(dateFormat));
		}
		setFormat(sdf);
		addHandlers();
	}
	
	private void addHandlers() {
		// TODO: add comments
		getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				handleTextChange();
			}
		});
	}
	
	private void handleTextChange() {
		Date date = sdf.parse(this, getTextBox().getText(), true);
		super.setValue(date, true);	
	}
	
	@Override
	public void showDatePicker() {
		if (isEditable()) {
			super.showDatePicker();
		}
	}

	public boolean isEditable() {
		return !getTextBox().isReadOnly();
	}

	public void setEditable(boolean editable) {
		getTextBox().setReadOnly(!editable);
	}

	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}

	public void setRequiredValidationMessage(String message) {
		this.requiredValidationMessage = message;
	}

	public void setRequiredValidationTitle(String title) {
		this.requiredValidationTitle = title;
	}
}