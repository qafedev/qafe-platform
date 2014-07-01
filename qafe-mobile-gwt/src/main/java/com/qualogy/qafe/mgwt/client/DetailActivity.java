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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.event.ShowMasterEvent;
import com.qualogy.qafe.mgwt.client.event.ActionEvent;
import com.qualogy.qafe.mgwt.client.event.ActionNames;

public class DetailActivity extends MGWTAbstractActivity {

	private final DetailView detailView;
	private final String eventId;

	public DetailActivity(DetailView detailView, String eventId) {
		this.detailView = detailView;
		this.eventId = eventId;

	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		addHandlerRegistration(detailView.getMainButton().addTapHandler(new TapHandler() {

			@Override
			public void onTap(TapEvent event) {
				eventBus.fireEvent(new ShowMasterEvent(eventId));

			}
		}));

		addHandlerRegistration(detailView.getBackbutton().addTapHandler(new TapHandler() {

			@Override
			public void onTap(TapEvent event) {
				ActionEvent.fire(eventBus, ActionNames.BACK);

			}
		}));

	}

}
