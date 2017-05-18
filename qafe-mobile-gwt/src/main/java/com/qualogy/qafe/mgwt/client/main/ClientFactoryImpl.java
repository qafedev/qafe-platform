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
package com.qualogy.qafe.mgwt.client.main;

import java.util.List;
import java.util.Map;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.qualogy.qafe.mgwt.client.activities.AboutView;
import com.qualogy.qafe.mgwt.client.activities.AboutViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.ShowCaseListView;
import com.qualogy.qafe.mgwt.client.activities.UIView;
import com.qualogy.qafe.mgwt.client.activities.UIViewImpl;
import com.qualogy.qafe.mgwt.client.activities.animation.AnimationView;
import com.qualogy.qafe.mgwt.client.activities.animation.AnimationViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationDoneView;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationDoneViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.button.ButtonView;
import com.qualogy.qafe.mgwt.client.activities.button.ButtonViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.buttonbar.ButtonBarView;
import com.qualogy.qafe.mgwt.client.activities.buttonbar.ButtonBarViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.elements.ElementsView;
import com.qualogy.qafe.mgwt.client.activities.elements.ElementsViewImpl;
import com.qualogy.qafe.mgwt.client.activities.forms.FormsView;
import com.qualogy.qafe.mgwt.client.activities.forms.FormsViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.menu.MenuView;
import com.qualogy.qafe.mgwt.client.activities.popup.PopupView;
import com.qualogy.qafe.mgwt.client.activities.popup.PopupViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.progressbar.ProgressBarView;
import com.qualogy.qafe.mgwt.client.activities.progressbar.ProgressBarViewImpl;
import com.qualogy.qafe.mgwt.client.activities.progressindicator.ProgressIndicatorView;
import com.qualogy.qafe.mgwt.client.activities.progressindicator.ProgressIndicatorViewImpl;
import com.qualogy.qafe.mgwt.client.activities.pulltorefresh.PullToRefreshDisplay;
import com.qualogy.qafe.mgwt.client.activities.pulltorefresh.PullToRefreshDisplayGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.scrollwidget.ScrollWidgetView;
import com.qualogy.qafe.mgwt.client.activities.scrollwidget.ScrollWidgetViewImpl;
import com.qualogy.qafe.mgwt.client.activities.searchbox.SearchBoxView;
import com.qualogy.qafe.mgwt.client.activities.searchbox.SearchBoxViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.slider.SliderView;
import com.qualogy.qafe.mgwt.client.activities.slider.SliderViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.tabbar.TabBarView;
import com.qualogy.qafe.mgwt.client.activities.tabbar.TabBarViewGwtImpl;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.AppView;
import com.qualogy.qafe.mgwt.client.views.AppsView;
import com.qualogy.qafe.mgwt.client.views.HomeView;
import com.qualogy.qafe.mgwt.client.views.WindowView;
import com.qualogy.qafe.mgwt.client.views.impl.AppViewImpl;
import com.qualogy.qafe.mgwt.client.views.impl.AppsViewImpl;
import com.qualogy.qafe.mgwt.client.views.impl.HomeViewImpl;
import com.qualogy.qafe.mgwt.client.views.impl.WindowViewImpl;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public class ClientFactoryImpl implements ClientFactory {

	private EventBus eventBus;
	private PlaceController placeController;
	private HomeView homeView = null;
	private AppsView appsView = null;
	private AppView appView = null;
	
	private ShowCaseListView homeViewImpl;
	private UIView uiView;
	private AboutView aboutView;
	private AnimationView animationView;
	private AnimationDoneView animationDoneView;
	private ScrollWidgetView scrollWidgetView;
	private ElementsView elementsView;
	private ButtonBarViewGwtImpl footerPanelView;
	private SearchBoxViewGwtImpl searchBoxViewGwtImpl;
	private TabBarView tabBarView;
	private ButtonView buttonView;
	private PopupView popupView;
	private ProgressBarView progressBarView;

	private SliderView sliderView;
	private PullToRefreshDisplayGwtImpl pullToRefreshView;
	private ProgressIndicatorViewImpl progressIndicatorView;
	private FormsViewGwtImpl formsView;
	private UIModel ui;

	public ClientFactoryImpl() {
		eventBus = new SimpleEventBus();
		placeController = new QPlaceController(eventBus);
	}
	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}
	
	@Override
	public HomeView getHomeView() {
		if (homeView == null) {
			homeView = new HomeViewImpl();
		}
		return homeView;
	}
	
	@Override
	public AppsView getAppsView() {
		if (appsView == null) {
			appsView = new AppsViewImpl();	
		}
		return appsView;
	}

	@Override
	public AppView getAppView() {
		if (appView == null) {
			appView = new AppViewImpl();
		}
		return appView;
	}
	
	@Override
	public WindowView createWindowView() {
		return new WindowViewImpl();
	}
	
	@Override
	public AbstractView getView(String viewKey) {
		return ComponentRepository.getInstance().getView(viewKey);
	}
	
	@Override
	public Map<UIObject,ComponentGVO> getComponents(String viewKey) {
		return ComponentRepository.getInstance().getComponents(viewKey);
	}
	
	@Override
	public UIObject getOwner(String viewKey, String ownerId) {
		return ComponentRepository.getInstance().getOwner(viewKey, ownerId);
	}
	
	@Override
	public void addOwner(String viewKey, String ownerId, UIObject owner, ComponentGVO ownerGVO) {
		ComponentRepository.getInstance().addOwner(viewKey, ownerId, owner, ownerGVO);
	}
	
	@Override
	public Map<UIObject,ComponentGVO> getComponents(String viewKey, String ownerId) {
		return ComponentRepository.getInstance().getComponents(viewKey, ownerId);
	}
	
	@Override
	public boolean removeComponents(String viewKey, String ownerId) {
		return ComponentRepository.getInstance().removeComponents(viewKey, ownerId);
	}
	
	@Override
	public List<UIObject> getComponentById(String componentKey) {
		return ComponentRepository.getInstance().getComponentById(componentKey);
	}
	
	@Override
	public List<UIObject> getComponentByName(String componentKey) {
		return ComponentRepository.getInstance().getComponentByName(componentKey);
	}

	@Override
	public List<UIObject> getComponentByGroup(String componentKey) {
		return ComponentRepository.getInstance().getComponentByGroup(componentKey);
	}
	
	@Override
	public String generateViewKey(String windowId, String context) {
		return ComponentRepository.getInstance().generateViewKey(windowId, context);
	}

	@Override
	public String generateComponentKey(String component, String windowId, String context) {
		return ComponentRepository.getInstance().generateComponentKey(component, windowId, context);
	}
	
	@Override
	public UIModel getUIModel() {
		return ui;
	}

	@Override
	public void setUIModel(UIModel ui) {
		this.ui=  ui;
	}
	
	@Override
	public UIView getUIView() {
		if (uiView == null) {
			uiView = new UIViewImpl();
		}
		return uiView;
	}

	@Override
	public AboutView getAboutView() {
		if (aboutView == null) {
			aboutView = new AboutViewGwtImpl();
		}

		return aboutView;
	}

	@Override
	public AnimationView getAnimationView() {
		if (animationView == null) {
			animationView = new AnimationViewGwtImpl();
		}
		return animationView;
	}

	@Override
	public AnimationDoneView getAnimationDoneView() {
		if (animationDoneView == null) {
			animationDoneView = new AnimationDoneViewGwtImpl();
		}
		return animationDoneView;
	}

	@Override
	public ScrollWidgetView getScrollWidgetView() {
		if (scrollWidgetView == null) {
			scrollWidgetView = new ScrollWidgetViewImpl();
		}
		return scrollWidgetView;
	}

	@Override
	public ElementsView getElementsView() {
		if (elementsView == null) {
			elementsView = new ElementsViewImpl();
		}
		return elementsView;
	}

	@Override
	public ButtonBarView getButtonBarView() {
		if (footerPanelView == null) {
			footerPanelView = new ButtonBarViewGwtImpl();
		}
		return footerPanelView;
	}

	@Override
	public SearchBoxView getSearchBoxView() {
		if (searchBoxViewGwtImpl == null) {
			searchBoxViewGwtImpl = new SearchBoxViewGwtImpl();
		}
		return searchBoxViewGwtImpl;
	}

	@Override
	public TabBarView getTabBarView() {
		if (tabBarView == null) {
			tabBarView = new TabBarViewGwtImpl();
		}
		return tabBarView;
	}

	@Override
	public ButtonView getButtonView() {
		if (buttonView == null) {
			buttonView = new ButtonViewGwtImpl();
		}
		return buttonView;
	}

	@Override
	public PopupView getPopupView() {
		if (popupView == null) {
			popupView = new PopupViewGwtImpl();
		}
		return popupView;
	}

	@Override
	public ProgressBarView getProgressBarView() {
		if (progressBarView == null) {
			progressBarView = new ProgressBarViewImpl();
		}
		return progressBarView;
	}

	@Override
	public SliderView getSliderView() {
		if (sliderView == null) {
			sliderView = new SliderViewGwtImpl();
		}
		return sliderView;
	}

	@Override
	public PullToRefreshDisplay getPullToRefreshDisplay() {
		if (pullToRefreshView == null) {
			pullToRefreshView = new PullToRefreshDisplayGwtImpl();
		}
		return pullToRefreshView;
	}

	@Override
	public ProgressIndicatorView getProgressIndicatorView() {
		if (progressIndicatorView == null) {
			progressIndicatorView = new ProgressIndicatorViewImpl();
		}
		return progressIndicatorView;
	}

	@Override
	public FormsView getFormsView() {
		if (formsView == null) {
			formsView = new FormsViewGwtImpl();
		}
		return formsView;
	}

	@Override
	public MenuView getMenuView() {
		return null;
	}
}