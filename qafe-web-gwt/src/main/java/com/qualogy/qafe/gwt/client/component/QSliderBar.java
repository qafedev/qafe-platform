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

import com.google.gwt.widgetideas.client.SliderBar;

public class QSliderBar extends SliderBar{
	
	private	Integer tickLabels;  
	
	public QSliderBar(double minValue, double maxValue) {
		super(minValue, maxValue);
	}
	
	public Integer getTickLabels() {
		return tickLabels;
	}
	
	public void setTickLabels(Integer tickLabels) {
		this.tickLabels = tickLabels;
		refreshNumLabels();
	}
	
	public void setValue(Object value) {
		double newValue = getMinValue();
		if (value != null) {
			try {
				newValue = Double.parseDouble(value.toString());
			} catch(NumberFormatException e) {
				newValue = getCurrentValue();					
			}
		}
		setCurrentValue(newValue);
	}
	
	@Override
	public void setCurrentValue(double curValue) {
	    boolean fireEvent = (Double.compare(curValue, getCurrentValue()) != 0);
	    super.setCurrentValue(curValue, fireEvent);
	}
	
	@Override
	public void setMinValue(double minValue) {
		super.setMinValue(minValue);
		refreshNumLabels();
	}
	
	@Override
	public void setMaxValue(double maxValue) {
		super.setMaxValue(maxValue);
		refreshNumLabels();
	}
	
	@Override
	public void setStepSize(double stepSize) {
		super.setStepSize(stepSize);
		refreshNumLabels();
	}
	
	private void refreshNumLabels() {
		int numLabels = calculateNumLabels();
		if ((tickLabels != null) && (tickLabels >= 2)) {
			// The numLabels is 0-based, whereas the tickLabels is 1-based
			numLabels = tickLabels - 1;
		}
		if (Double.compare(numLabels, getNumLabels()) != 0) {
			setNumLabels(numLabels);	
		}
		refreshNumTicks();
	}
	
	private void refreshNumTicks() {
		int numTicks = calculateNumLabels();
		if (Double.compare(numTicks, getNumTicks()) != 0) {
			setNumTicks(numTicks);	
		}
	}
	
	private int calculateNumLabels() {
		double numLabels = (getMaxValue() - getMinValue()) / getStepSize();
		return (int)Math.round(numLabels);
	}
}
