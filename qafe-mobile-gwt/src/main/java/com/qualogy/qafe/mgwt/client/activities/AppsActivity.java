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
import com.qualogy.qafe.mgwt.client.places.HomePlace;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.AppsView;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;

public class AppsActivity extends AbstractActivity {

	private Map<Integer,AbstractPlace> places = new HashMap<Integer,AbstractPlace>();
	
	public AppsActivity(ClientFactory clientFactory, AbstractPlace place) {
		super(clientFactory, place);
	}
	
	@Override
	public AbstractView getView() {
		return getClientFactory().getAppsView();
	}

	@Override
	protected void bind(AbstractView view) {
		if (view instanceof AppsView) {
			AppsView appsView = (AppsView)view;
			addHandlerRegistration(appsView.getCellSelectedHandler().addCellSelectedHandler(new CellSelectedHandler() {
				@Override
				public void onCellSelected(CellSelectedEvent event) {
					int index = event.getIndex();
					AbstractPlace place = places.get(index);
					if (place != null) {
						goTo(place);
					}
				}
			}));
			
			addHandlerRegistration(appsView.getBackButton().addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					goTo(new HomePlace());
				}
			}));
		}	
	}

	@Override
	protected void init(AbstractView view) {
		if (view instanceof AppsView) {
			AppsView appsView = (AppsView)view;
			// TODO make constants
			appsView.setTitle("QAFE Apps");
			appsView.setBackButtonText("Back");
			appsView.setTopics(createTopics());
		}
	}

	private List<Topic> createTopics() {
		ArrayList<Topic> topicList = new ArrayList<Topic>();
		UIModel uiModel = getClientFactory().getUIModel();
		if (uiModel != null) {
			UIGVO[] vos = uiModel.getVos();
			if (vos != null) {
				for (int i=0; i<vos.length; i++) {
					UIGVO uiGVO = vos[i];
					String appId = uiGVO.getAppId();
					String appTitle = uiGVO.getTitle();
					places.put(i, new AppPlace(appId));
					Topic topic = new Topic(appTitle, 1);
					topicList.add(topic);
				}
			}
		}
		return topicList;
	}
	
	@Override
	public AppsPlace getPlace() {
		return (AppsPlace)super.getPlace();
	}
}
