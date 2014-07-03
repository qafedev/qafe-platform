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

import com.google.gwt.widgetideas.client.SpinnerListener;
import com.google.gwt.widgetideas.client.ValueSpinner;

public class QValueSpinner extends ValueSpinner implements HasEditable {

	private long currentValue;
	
	public QValueSpinner(long value, int min, int max) {
		super(value, min, max);
		currentValue = value;
		getSpinner().addSpinnerListener(new SpinnerListener() {
			public void onSpinning(long value) {
				if (isEditable()) {
					currentValue = getSpinner().getValue();
					return;
				}
				undoChange();
			}
		});
	}
	
	private void undoChange() {
		getSpinner().setValue(currentValue, false);
		getTextBox().setText(formatValue(currentValue));
	}

	public boolean isEditable() {
		return !getTextBox().isReadOnly();
	}

	public void setEditable(boolean editable) {
		getTextBox().setReadOnly(!editable);
	}
}
