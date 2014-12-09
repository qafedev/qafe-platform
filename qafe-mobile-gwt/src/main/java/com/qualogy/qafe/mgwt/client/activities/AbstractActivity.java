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
package com.qualogy.qafe.mgwt.client.activities;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.ui.events.ResultEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ResultHandler;
import com.qualogy.qafe.mgwt.client.views.AbstractView;

public abstract class AbstractActivity extends MGWTAbstractActivity implements ResultHandler {

	private ClientFactory clientFactory;
	private AbstractPlace place;
	private AcceptsOneWidget containerWidget;
	private EventBus eventBus;
	
	public AbstractActivity(ClientFactory clientFactory, AbstractPlace place) {
		this.clientFactory = clientFactory;
		this.place = place;
	}
	
	public ClientFactory getClientFactory() {
		return clientFactory;
	}
	
	public AbstractPlace getPlace() {
		return place;
	}
	
	public ResultHandler getResultHandler() {
		return this;
	}
	
	public void goTo(AbstractPlace place) {
		getClientFactory().getPlaceController().goTo(place);
	}
	
	public void goBack() {
	}
	
	public boolean hasView() {
		AbstractView view = getView();
		return (view != null);
	}
	
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		this.containerWidget = containerWidget;
		this.eventBus = eventBus;
		if (hasView()) {
			AbstractView view = getView();
			bind(view);	
			init(view);
			containerWidget.setWidget(view.asWidget());	
		}
	}

	@Override
	public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		super.addHandlerRegistration(handlerRegistration);
	}
	
	protected void restart() {
		if (containerWidget == null) {
			return;
		}
		if (eventBus == null) {
			return;
		}
		start(containerWidget, eventBus);
	}
	
	@Override
	public void onResult(ResultEvent event) {
		ActivityHelper.handleResult(event, this);
	}
	
	protected void setBusy(boolean busy) {
		if (hasView()) {
			AbstractView view = getView();
			view.setBusy(busy);
		}
	}
	
	public abstract AbstractView getView();
	protected abstract void bind(AbstractView view);
	protected abstract void init(AbstractView view);
}