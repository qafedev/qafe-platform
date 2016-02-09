/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;
import com.qualogy.qafe.mgwt.client.activities.home.Topic;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.main.UIModel;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.places.AppPlace;
import com.qualogy.qafe.mgwt.client.places.AppsPlace;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.AppView;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class AppActivity extends AbstractActivity {

	private Map<Integer,AbstractPlace> places = new HashMap<Integer,AbstractPlace>();
	private int selectedIndex;
	
	public AppActivity(ClientFactory clientFactory, AbstractPlace place) {
		super(clientFactory, place);
	}
	
	@Override
	public AbstractView getView() {
		return getClientFactory().getAppView();
	}

	@Override
	protected void bind(AbstractView view) {
		if (view instanceof AppView) {
			final AppView appView = (AppView)view;
			addHandlerRegistration(appView.getCellSelectedHandler().addCellSelectedHandler(new CellSelectedHandler() {
				@Override
				public void onCellSelected(CellSelectedEvent event) {
					int index = event.getIndex();
					appView.setSelectedIndex(selectedIndex, false);
					appView.setSelectedIndex(index, true);
					selectedIndex = index;
							
					AbstractPlace place = places.get(index);
					if (place instanceof WindowPlace) {
						WindowPlace windowPlace = (WindowPlace)place;
						openWindow(windowPlace);
					}
				}
			}));

			addHandlerRegistration(appView.getBackButton().addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					goTo(new AppsPlace());
				}
			}));
		}	
	}

	@Override
	protected void init(AbstractView view) {
		if (view instanceof AppView) {
			AppView appView = (AppView)view;
			String appId = getPlace().getContext();
			String appTitle = null;
			UIGVO appGVO = ActivityHelper.getApp(appId, this);
			if (appGVO != null) {
				appTitle = appGVO.getTitle();
			}
			
			appView.setTitle(appTitle);
			// TODO make constant 
			appView.setBackButtonText("Back");
			appView.setTopics(createTopics());
		}
	}

	private List<Topic> createTopics() {
		ArrayList<Topic> topicList = new ArrayList<Topic>();
		UIModel uiModel = getClientFactory().getUIModel();
		if (uiModel != null) {
			UIGVO[] vos = uiModel.getVos();
			if (vos != null) {
				String appId = getPlace().getContext();
				for (int i=0; i<vos.length; i++) {
					UIGVO uiGVO = vos[i];
					if (appId.equals(uiGVO.getAppId())) {
						WindowGVO[] windows = uiGVO.getWindows();
						if (windows != null) {
							int index = 0;
							for (WindowGVO windowGVO : windows) {
								if (!windowGVO.getIsparent()) {
									continue;
								}
								String windowId = windowGVO.getId();
								String windowTitle = windowGVO.getTitle();
								places.put(index, new WindowPlace(windowId, appId));
								Topic topic = new Topic(windowTitle, 1);
								topicList.add(topic);
								index++;
							}
						}
						break;
					}
				}
			}
		}
		return topicList;
	}

	private void openWindow(WindowPlace place) {
		String appId = place.getContext();
		UIGVO uiGVO = ActivityHelper.getApp(appId, this);
		if (uiGVO == null) {
			return;
		}
		String windowId = place.getId();
		WindowGVO windowGVO = ActivityHelper.getWindow(windowId, appId, this);
		String listenerType = QAMLConstants.EVENT_ONCLICK;
		EventListenerGVO eventGVO = uiGVO.getEventMap().get(windowId);
		ActivityHelper.handleEvent(windowGVO, null, eventGVO, null, listenerType, eventGVO.getInputvariablesList(), null, windowId, appId, this);
	}
	
	@Override
	public AppPlace getPlace() {
		return (AppPlace)super.getPlace();
	}
}
