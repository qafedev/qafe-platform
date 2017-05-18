/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;
import com.qualogy.qafe.mgwt.client.activities.home.Topic;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.main.UIModel;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.places.AppsPlace;
import com.qualogy.qafe.mgwt.client.places.HomePlace;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.HomeView;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class HomeActivity extends AbstractActivity {

	private Map<Integer,AbstractPlace> places = new HashMap<Integer,AbstractPlace>();
	
	public HomeActivity(ClientFactory clientFactory, AbstractPlace place) {
		super(clientFactory, place);
	}
	
	@Override
	public AbstractView getView() {
		return getClientFactory().getHomeView();
	}
	
	@Override
	protected void bind(AbstractView view) {
		if (view instanceof HomeView) {
			HomeView homeView = (HomeView)view;
			addHandlerRegistration(homeView.getCellSelectedHandler().addCellSelectedHandler(new CellSelectedHandler() {
				@Override
				public void onCellSelected(CellSelectedEvent event) {
					int index = event.getIndex();
					AbstractPlace place = places.get(index);
					if (place != null) {
						goTo(place);
					}
				}
			}));
		}	
	}

	@Override
	protected void init(AbstractView view) {
		if (view instanceof HomeView) {
			HomeView homeView = (HomeView)view;
			// TODO make constants
			homeView.setTitle("QAFE Mobile");
			homeView.setTopics(createTopics());
		}
	}

	private List<Topic> createTopics() {
		ArrayList<Topic> topicList = new ArrayList<Topic>();
		UIModel uiModel = getClientFactory().getUIModel();
		if (uiModel == null) {
			return topicList;
		}
		UIGVO systemMenuApplication = uiModel.getSystemMenuApplication();
		if (systemMenuApplication == null) {
			return topicList;
		}
		MenuItemGVO systemMenu = systemMenuApplication.getMenus();
		if (systemMenu == null) {
			return topicList;
		}
		MenuItemGVO[] subMenus = systemMenu.getSubMenus();
		if (subMenus == null) {
			return topicList;
		}
		for (int i=0; i<subMenus.length; i++) {
			MenuItemGVO menuItem = subMenus[i];
			if (QAMLConstants.MENU_APPLICATIONS.equals(menuItem.getId())) {
				places.put(i, new AppsPlace());
			}
			Topic topic = new Topic(menuItem.getDisplayname(), 1);
			topicList.add(topic);
		}
		return topicList;
	}
	
	@Override
	public HomePlace getPlace() {
		return (HomePlace)super.getPlace();
	}
}