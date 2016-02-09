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
package com.qualogy.qafe.mgwt.client.main;

import java.util.List;
import java.util.Map;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.event.shared.EventBus;
import com.qualogy.qafe.mgwt.client.activities.AboutView;
import com.qualogy.qafe.mgwt.client.activities.UIView;
import com.qualogy.qafe.mgwt.client.activities.animation.AnimationView;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationDoneView;
import com.qualogy.qafe.mgwt.client.activities.button.ButtonView;
import com.qualogy.qafe.mgwt.client.activities.buttonbar.ButtonBarView;
import com.qualogy.qafe.mgwt.client.activities.elements.ElementsView;
import com.qualogy.qafe.mgwt.client.activities.forms.FormsView;
import com.qualogy.qafe.mgwt.client.activities.menu.MenuView;
import com.qualogy.qafe.mgwt.client.activities.popup.PopupView;
import com.qualogy.qafe.mgwt.client.activities.progressbar.ProgressBarView;
import com.qualogy.qafe.mgwt.client.activities.progressindicator.ProgressIndicatorView;
import com.qualogy.qafe.mgwt.client.activities.pulltorefresh.PullToRefreshDisplay;
import com.qualogy.qafe.mgwt.client.activities.scrollwidget.ScrollWidgetView;
import com.qualogy.qafe.mgwt.client.activities.searchbox.SearchBoxView;
import com.qualogy.qafe.mgwt.client.activities.slider.SliderView;
import com.qualogy.qafe.mgwt.client.activities.tabbar.TabBarView;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.AppView;
import com.qualogy.qafe.mgwt.client.views.AppsView;
import com.qualogy.qafe.mgwt.client.views.HomeView;
import com.qualogy.qafe.mgwt.client.views.WindowView;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public interface ClientFactory {

	public EventBus getEventBus();
	public PlaceController getPlaceController();
	public HomeView getHomeView();
	public AppsView getAppsView();
	public AppView getAppView();
	public AbstractView getView(String viewKey);
	public WindowView createWindowView();
	public Map<UIObject,ComponentGVO> getComponents(String viewKey);
	public UIObject getOwner(String viewKey, String ownerId);
	public void addOwner(String viewKey, String ownerId, UIObject owner, ComponentGVO ownerGVO);
	public Map<UIObject,ComponentGVO> getComponents(String viewKey, String ownerId);
	public boolean removeComponents(String viewKey, String ownerId);
	public List<UIObject> getComponentById(String componentKey);
	public List<UIObject> getComponentByName(String componentKey);
	public List<UIObject> getComponentByGroup(String componentKey);
	public String generateViewKey(String windowId, String context);
	public String generateComponentKey(String component, String windowId, String context);
	public UIModel getUIModel();
	public void setUIModel(UIModel ui);

	
//	public ShowCaseListView getHomeView();

	public MenuView getMenuView();

	/**
	 * @return
	 */
	public UIView getUIView();

	public AboutView getAboutView();

	public AnimationView getAnimationView();

	public AnimationDoneView getAnimationDoneView();

	public ScrollWidgetView getScrollWidgetView();

	public ElementsView getElementsView();

	public ButtonBarView getButtonBarView();

	public SearchBoxView getSearchBoxView();

	public TabBarView getTabBarView();

	public ButtonView getButtonView();

	/**
	 * 
	 */
	public PopupView getPopupView();

	public ProgressBarView getProgressBarView();

	public SliderView getSliderView();

	public PullToRefreshDisplay getPullToRefreshDisplay();

	public ProgressIndicatorView getProgressIndicatorView();

	public FormsView getFormsView();
}
