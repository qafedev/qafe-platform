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
package com.qualogy.qafe.mgwt.client.activities.pulltorefresh;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
//import com.googlecode.mgwt.ui.client.widget.event.PullReleasedEvent;
//import com.googlecode.mgwt.ui.client.widget.event.PullReleasedHandler;
import com.qualogy.qafe.mgwt.client.DetailActivity;
import com.qualogy.qafe.mgwt.client.activities.home.Topic;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;

public class PullToRefreshActivity extends DetailActivity {

	private final ClientFactory clientFactory;

	private int counter;
	private List<Topic> list = new LinkedList<Topic>();

	public PullToRefreshActivity(ClientFactory clientFactory) {
		super(clientFactory.getPullToRefreshDisplay(), "nav");
		this.clientFactory = clientFactory;

		list = new LinkedList<Topic>();
		while (counter < 5) {
			list.add(new Topic("Topic " + (counter + 1), counter));
			counter++;
		}

	}

	private boolean failed = false;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		super.start(panel, eventBus);

		final PullToRefreshDisplay display = clientFactory.getPullToRefreshDisplay();

		display.getHeader().setText("Pulldown to Refresh");

		display.getMainButtonText().setText("Nav");
		display.getBackbuttonText().setText("UI");
		display.getHeader().setText("PullToRefresh");

//		addHandlerRegistration(display.getReload().addPullReleasedHandler(new PullReleasedHandler() {
//
//			@Override
//			public void onPullReleased(PullReleasedEvent event) {
//
//				new Timer() {
//
//					@Override
//					public void run() {
//						if (failed) {
//							display.onLoadingFailed();
//						} else {
//							for (int i = 0; i < 5; i++) {
//								list.add(new Topic("Topic " + (counter + 1), counter));
//								counter++;
//							}
//							display.render(list);
//							display.onLoadingSucceeded();
//						}
//						failed = !failed;
//
//					}
//				}.schedule(1000);
//
//			}
//		}));

		display.render(list);

		panel.setWidget(display);
	}
}
