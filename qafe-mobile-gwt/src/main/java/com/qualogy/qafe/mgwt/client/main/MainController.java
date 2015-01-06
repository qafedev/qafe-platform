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
package com.qualogy.qafe.mgwt.client.main;

import java.util.Map;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort.DENSITY;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.TabletPortraitOverlay;
import com.googlecode.mgwt.ui.client.layout.MasterRegionHandler;
import com.googlecode.mgwt.ui.client.layout.OrientationRegionHandler;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;
import com.qualogy.qafe.mgwt.client.AppHistoryObserver;
import com.qualogy.qafe.mgwt.client.AppPlaceHistoryMapper;
import com.qualogy.qafe.mgwt.client.PhoneActivityMapper;
import com.qualogy.qafe.mgwt.client.PhoneAnimationMapper;
import com.qualogy.qafe.mgwt.client.TabletMainActivityMapper;
import com.qualogy.qafe.mgwt.client.TabletMainAnimationMapper;
import com.qualogy.qafe.mgwt.client.TabletNavActivityMapper;
import com.qualogy.qafe.mgwt.client.TabletNavAnimationMapper;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.css.AppBundle;
import com.qualogy.qafe.mgwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.places.AppPlace;
import com.qualogy.qafe.mgwt.client.places.HomePlace;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.mgwt.client.ui.events.ResultEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ResultHandler;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.data.GDataObject;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIVOCluster;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class MainController {
	
	private static MainController instance = null;
	
	private AsyncCallback<?> gvoCallbackHandler = null;
	private ClientFactory clientFactory = new ClientFactoryImpl();
	private ViewPort viewPort = null;
	
	public static MainController getInstance() {
		if (instance == null) {
			instance = new MainController();
		}
		return instance;
	}
	
	private MainController() {
		ensureInjectMGWTCSS();
	}
	
	public boolean isNavigationPanelEnabled() {
		UIModel uiModel = getClientFactory().getUIModel();
		if (uiModel != null) {
			UIGVO systemMenuGVO = uiModel.getSystemMenuApplication();
			if (systemMenuGVO == null) {
				return false;
			}
			if (systemMenuGVO.getMenus() == null) {
				return false;
			}
		}
		
		Map<String,String> globalConfigurations = ClientApplicationContext.getInstance().getGlobalConfigurations();
		if (globalConfigurations != null) {
			String navigationPanelEnabled = globalConfigurations.get(QAMLConstants.SYS_NAVIGATION_PANEL_ENABLED);
			if ((navigationPanelEnabled != null) && !Boolean.parseBoolean(navigationPanelEnabled)) {
				return false;
			}
		}
		
		return true;
	}
	
	public ClientFactory getClientFactory() {
		return clientFactory;
	}
	
	public RPCServiceAsync getRPCService() {
		return MainFactoryActions.getRPCService();
	}
	
	public void initialize() {
		ClientApplicationContext.getInstance().setBusy(true);
		getRPCService().getUISFromApplicationContext(ClientApplicationContext.getInstance().getParameters(), getGVOCallbackHandler());
	}

	public void invokeUIByUUID(String uuid, AsyncCallback<?> callbackHandler) {
		getRPCService().getUIByUUID(uuid, callbackHandler);
	}
	
	public void executeEvent(EventDataGVO eventDataGVO, AbstractActivity activity) {
		getRPCService().executeEvent(eventDataGVO, getEventCallbackHandler(activity));
	}
	
	private AsyncCallback<?> getEventCallbackHandler(final AbstractActivity activity) {
		return new AsyncCallback<Object>() {
			@Override
			public void onSuccess(Object result) {
				fireResult((GDataObject)result, activity);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO look in the local storage?
				System.out.println(caught);
				Object local = null;
				fireResult((GDataObject)local, activity);
			}
		};
	}

	private void fireResult(GDataObject resultObject, AbstractActivity activity) {
		if (resultObject == null) {
			return;
		}
		ResultHandler handler = activity.getResultHandler();
		if (handler == null) {
			return;
		}
		ResultEvent event = new ResultEvent(resultObject, resultObject.getListenerType());
		event.execute(handler);
	}
	
	private AsyncCallback<?> getGVOCallbackHandler() {
		if (gvoCallbackHandler == null) {
			gvoCallbackHandler = new AsyncCallback<Object>() {
				@Override
				public void onSuccess(Object result) {
					if (result != null) {
						try {
							ClientApplicationContext.getInstance().clearAll();
							UIVOCluster ui = (UIVOCluster)result;
							
							// Store the ExternalProperties map in the ClientApplicationContext
							Map<String,String> externalProperties = ui.getExternalProperties();
							if (externalProperties != null) {
								ClientApplicationContext.getInstance().setGlobalConfigurations(externalProperties);
							}
							
							ClientApplicationContext.getInstance().clearPrerendering();
							getClientFactory().setUIModel(ui);
							handleStyling(ui.getCss());
							start();
							ClientApplicationContext.getInstance().postInitialization(ui);
						} catch (Exception e) {
							ClientApplicationContext.getInstance().log("Initialization failed", " While processing mainfactory functions, something went wrong\n" + e.getMessage(), true, true, e);
						}
					}
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log("Initialization failed", "Error creating dynamic panel\n\n" + caught.getMessage(), true, true, caught);	
					ClientApplicationContext.getInstance().setBusy(false);
				}
			};
		}
		return gvoCallbackHandler;
	}
	
	private void handleStyling(String css) {
		if ((css == null) || (css.length() == 0)) {
			return;
		}
		if (isIE()) {
			addStyleSheet(css);
		} else {
			// Append custom css style as last thing in the Head element
			Element head = Document.get().getElementsByTagName("head").getItem(0);
			StyleElement styleElement = Document.get().createStyleElement();
			styleElement.setType("text/css");
			styleElement.setInnerText(css);
			head.appendChild(styleElement);
		}
	}

	private native boolean isIE() /*-{ 
		var ua = navigator.userAgent.toLowerCase(); 
		if ((ua.indexOf("msie 6.0") != -1) ||  (ua.indexOf("msie 7.0") != -1)) { 
			return true; 
		} 
		else {
			return false;
		}
	}-*/;

	private native void addStyleSheet(String css)/*-{ 
		var ss = $doc.createStyleSheet(); 
		ss.cssText = css; 
	}-*/;
	
	private void start() {
		build();
		goToStartPlace();
	}
	
	private void build() {
		// MGWTColorScheme.setBaseColor("#56a60D");
		// MGWTColorScheme.setFontColor("#eee");
		//
		// MGWTStyle.setTheme(new MGWTColorTheme());
		//
		// MGWTStyle.setDefaultBundle((MGWTClientBundle)
		// GWT.create(MGWTStandardBundle.class));
		// MGWTStyle.getDefaultClientBundle().getMainCss().ensureInjected();

		// MGWTStyle.setTheme(new CustomTheme());

		if (viewPort == null) {
			viewPort = new MGWTSettings.ViewPort();
			viewPort.setTargetDensity(DENSITY.MEDIUM);
			viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);

			MGWTSettings settings = new MGWTSettings();
			settings.setViewPort(viewPort);
			settings.setIconUrl("logo.png");
			settings.setAddGlosToIcon(true);
			settings.setFullscreen(true);
			settings.setPreventScrolling(true);
			MGWT.applySettings(settings);
			createDisplay();
		}
	}
	
	private void goToStartPlace() {
		UIModel uiModel = getClientFactory().getUIModel();
		if (uiModel != null) {
			UIGVO[] vos = uiModel.getVos();
			if (vos != null) {
				for (int i=0; i<vos.length; i++) {
					UIGVO uiGVO = vos[i];
					WindowGVO[] windows = uiGVO.getWindows();
					if (windows != null) {
						for (WindowGVO windowGVO : windows) {
							if (windowGVO.getLoadOnStartup()) {
								String appId = uiGVO.getAppId();
								String windowId = windowGVO.getId();
								go(new AppPlace(appId));
								go(new WindowPlace(windowId, appId));
								return;
							}
						}
					}
				}
			}
		}
		go(new HomePlace());
	}
	
	private void createDisplay() {
		if (MGWT.getOsDetection().isTablet() && isNavigationPanelEnabled()) {
			StyleInjector.inject(AppBundle.INSTANCE.css().getText());
			createTabletDisplay();
		} else {
			createPhoneDisplay();
		}
	}
	
	private void createPhoneDisplay() {
		AnimatableDisplay display = GWT.create(AnimatableDisplay.class);
		PhoneActivityMapper phoneActivityMapper = new PhoneActivityMapper(getClientFactory());
		PhoneAnimationMapper phoneAnimationMapper = new PhoneAnimationMapper();
		AnimatingActivityManager activityManager = new AnimatingActivityManager(phoneActivityMapper, phoneAnimationMapper, getClientFactory().getEventBus());
		activityManager.setDisplay(display);
		RootPanel.get().add(display);
	}
	
	private void createTabletDisplay() {
		AnimatableDisplay navDisplay = GWT.create(AnimatableDisplay.class);
		SimplePanel navPanel = new SimplePanel();
		navPanel.getElement().setId("nav");
		navPanel.getElement().addClassName("landscapeonly");
		TabletPortraitOverlay tabletPortraitOverlay = new TabletPortraitOverlay();
		OrientationRegionHandler orientationRegionHandler = new OrientationRegionHandler(navPanel, tabletPortraitOverlay, navDisplay);
		MasterRegionHandler masterRegionHandler = new MasterRegionHandler(getClientFactory().getEventBus(), "nav", tabletPortraitOverlay);
		ActivityMapper navActivityMapper = new TabletNavActivityMapper(getClientFactory());
		AnimationMapper navAnimationMapper = new TabletNavAnimationMapper();
		AnimatingActivityManager navActivityManager = new AnimatingActivityManager(navActivityMapper, navAnimationMapper, getClientFactory().getEventBus());
		navActivityManager.setDisplay(navDisplay);
		RootPanel.get().add(navPanel);

		AnimatableDisplay display = GWT.create(AnimatableDisplay.class);
		SimplePanel contentPanel = new SimplePanel();
		contentPanel.getElement().setId("main");
		TabletMainActivityMapper contentActivityMapper = new TabletMainActivityMapper(getClientFactory());
		AnimationMapper contentAnimationMapper = new TabletMainAnimationMapper();
		AnimatingActivityManager contentActivityManager = new AnimatingActivityManager(contentActivityMapper, contentAnimationMapper, getClientFactory().getEventBus());
		contentActivityManager.setDisplay(display);
		contentPanel.setWidget(display);
		RootPanel.get().add(contentPanel);
	}
	
	private void go(AbstractPlace place) {
		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		
		AppHistoryObserver historyObserver = new AppHistoryObserver();
		MGWTPlaceHistoryHandler historyHandler = new MGWTPlaceHistoryHandler(historyMapper, historyObserver);
		historyHandler.register(getClientFactory().getPlaceController(), getClientFactory().getEventBus(), place);
		historyHandler.handleCurrentHistory();
	}
	
	private void ensureInjectMGWTCSS() {
		// Make sure the mgwt css style is injected, so custom css style can be added later on
		MGWTClientBundle clientBundle = MGWTStyle.getTheme().getMGWTClientBundle();
		clientBundle.getButtonBarCss().ensureInjected();
		clientBundle.getButtonBarButtonCss().ensureInjected();
		clientBundle.getButtonBarCss().ensureInjected();
		clientBundle.getButtonCss().ensureInjected();
		clientBundle.getCarouselCss().ensureInjected();
		clientBundle.getCheckBoxCss().ensureInjected();
		clientBundle.getDialogCss().ensureInjected();
		clientBundle.getHeaderCss().ensureInjected();
		clientBundle.getInputCss().ensureInjected();
		clientBundle.getLayoutCss().ensureInjected();
		clientBundle.getListCss().ensureInjected();
		clientBundle.getMainCss().ensureInjected();
		clientBundle.getPanelCss().ensureInjected();
		clientBundle.getProgressBarCss().ensureInjected();
		clientBundle.getProgressIndicatorCss().ensureInjected();
		clientBundle.getPullToRefreshCss().ensureInjected();
		clientBundle.getScrollPanelCss().ensureInjected();
		clientBundle.getSearchBoxCss().ensureInjected();
		clientBundle.getSliderCss().ensureInjected();
		clientBundle.getTabBarCss().ensureInjected();
		clientBundle.getUtilCss().ensureInjected();
	}
}
