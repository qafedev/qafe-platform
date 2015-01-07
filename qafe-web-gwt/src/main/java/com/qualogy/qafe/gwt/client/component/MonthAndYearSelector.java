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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.MonthSelector;

public class MonthAndYearSelector extends MonthSelector {

	private static String BASE_NAME = "datePicker";
	private static String PREV_BUTTON = BASE_NAME + "PreviousButton";
	private static String NEXT_BUTTON = BASE_NAME + "NextButton";
	private static String PREV_BUTTON_YEAR = PREV_BUTTON + "Year";
	private static String NEXT_BUTTON_YEAR = NEXT_BUTTON + "Year";

	private PushButton backwards;
	private PushButton forwards;
	private PushButton backwardsYear;
	private PushButton forwardsYear;
	private Grid grid;
	private int previousYearColumn = 0;
	private int previousMonthColumn = 1;
	private int monthColumn = 2;
	private int nextMonthColumn = 3;
	private int nextYearColumn = 4;
	private CalendarModel model;
	private DatePickerWithYearSelector picker;

	public void setModel(CalendarModel model) {
		this.model = model;
	}

	public void setPicker(DatePickerWithYearSelector picker) {
		this.picker = picker;
	}

	@Override
	protected void refresh() {
		String formattedMonth = getModel().formatCurrentMonth();
		grid.setText(0, monthColumn, formattedMonth);
	}

	@Override
	protected void setup() {
		// Set up backwards.
		backwards = new PushButton();
		backwards.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMonths(-1);
			}
		});

		backwards.getUpFace().setHTML("&lsaquo;");
		backwards.setStyleName(PREV_BUTTON);

		forwards = new PushButton();
		forwards.getUpFace().setHTML("&rsaquo;");
		forwards.setStyleName(NEXT_BUTTON);
		forwards.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMonths(+1);
			}
		});

		// Set up backwards year
		backwardsYear = new PushButton();
		backwardsYear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMonths(-12);
			}
		});

		backwardsYear.getUpFace().setHTML("&laquo;");
		backwardsYear.setStyleName(PREV_BUTTON_YEAR);

		forwardsYear = new PushButton();
		forwardsYear.getUpFace().setHTML("&raquo;");
		forwardsYear.setStyleName(NEXT_BUTTON_YEAR);
		forwardsYear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMonths(+12);
			}
		});

		// Set up grid.
		grid = new Grid(1, 5);
		grid.setWidget(0, previousYearColumn, backwardsYear);
		grid.setWidget(0, previousMonthColumn, backwards);
		grid.setWidget(0, nextMonthColumn, forwards);
		grid.setWidget(0, nextYearColumn, forwardsYear);

		CellFormatter formatter = grid.getCellFormatter();
		formatter.setStyleName(0, monthColumn, BASE_NAME + "Month");
		formatter.setWidth(0, previousYearColumn, "1");
		formatter.setWidth(0, previousMonthColumn, "1");
		formatter.setWidth(0, monthColumn, "100%");
		formatter.setWidth(0, nextMonthColumn, "1");
		formatter.setWidth(0, nextYearColumn, "1");
		grid.setStyleName(BASE_NAME + "MonthSelector");
		initWidget(grid);
	}

	public void addMonths(int numMonths) {
		model.shiftCurrentMonth(numMonths);
		picker.refreshComponents();
	}

}