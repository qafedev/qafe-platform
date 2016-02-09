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

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.qualogy.qafe.mgwt.client.factory.async.UiByUUIDCallback;
import com.qualogy.qafe.mgwt.client.main.ClientFactory;
import com.qualogy.qafe.mgwt.client.main.QPlaceController;
import com.qualogy.qafe.mgwt.client.places.AbstractPlace;
import com.qualogy.qafe.mgwt.client.places.AppPlace;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.events.LoadEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ResultEvent;
import com.qualogy.qafe.mgwt.client.ui.events.UnloadEvent;
import com.qualogy.qafe.mgwt.client.ui.renderer.WindowRenderer;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.views.WindowView;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnUnLoadEventListenerGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class WindowActivity extends AbstractActivity {

	private WindowView busyView = null;
	private Boolean unload = null;
	private Boolean unloaded = false;
	
	public WindowActivity(ClientFactory clientFactory, AbstractPlace place) {
		super(clientFactory, place);
	}
	
	@Override
	public boolean hasView() {
		AbstractView view = getView();
		return (view != busyView);
	}
	
	@Override
	public WindowView getView() {
		String viewKey = generateViewKey();
		WindowView view = (WindowView)getClientFactory().getView(viewKey);
		if (view == null) {
			if (busyView == null) {
				busyView = getClientFactory().createWindowView();
				render();	
			}
			view = busyView;
		}
		return view;
	}
	
	private void render() {
		final String appId = getPlace().getContext();
		final String windowId = getPlace().getId();
		UiByUUIDCallback callbackHandler = new UiByUUIDCallback() {
			@Override
			public String getWindowId() {
				return windowId;
			}

			@Override
			public void onSuccess(Object result) {
				if (result instanceof UIGVO) {
					UIGVO uiGVO = (UIGVO)result;
					WindowGVO[] windows = uiGVO.getWindows();
					for (int i=0; i<windows.length; i++) {
						WindowGVO windowGVO = windows[i];
						if (windowId.equals(windowGVO.getId())) {
							Widget view = (Widget) new WindowRenderer().render(windowGVO, null, appId, windowId, appId, WindowActivity.this);
							if (view instanceof WindowView) {
								restart();
							}
							break;
						}
					}	
				}
				setBusy(false);
			}	

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				setBusy(false);
			}
		};
		String uuid = windowId + "|" + appId;
		ActivityHelper.invokeUIByUUID(uuid, callbackHandler);
		setBusy(true);
	}
	
	@Override
	protected void bind(AbstractView view) {
		if (view instanceof WindowView) {
			WindowView windowView = (WindowView)view;
			addHandlerRegistration(windowView.getBackButton().addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					goBack();
				}
			}));
			
			// Register events for this Window
			WindowPlace windowPlace = getPlace();
			String windowId = windowPlace.getId();
			String context = windowPlace.getContext();
			WindowGVO windowGVO = ActivityHelper.getWindow(windowId, context, this);
			ActivityHelper.registerEvents(windowGVO, (Widget)windowView, windowId, context, this);
			
			// Register events for components in this Window
			String viewKey = generateViewKey();
			Map<UIObject,ComponentGVO> components = getClientFactory().getComponents(viewKey);
			if (components == null) {
				return;
			}
			Iterator<UIObject> itrWidget = components.keySet().iterator();
			while (itrWidget.hasNext()) {
				UIObject widget = itrWidget.next();
				ComponentGVO componentGVO = components.get(widget);
				ActivityHelper.registerEvents(componentGVO, widget, windowId, context, this);
			}
		}
	}
	
	@Override
	protected void init(AbstractView view) {
		if (view instanceof WindowView) {
			WindowView windowView = (WindowView)view;
			WindowPlace windowPlace = getPlace();
			String windowId = windowPlace.getId();
			String context = windowPlace.getContext();
			String windowTitle = null;
			WindowGVO windowGVO = ActivityHelper.getWindow(windowId, context, this);
			if (windowGVO != null) {
				windowTitle = windowGVO.getTitle();
			}
			
			windowView.setTitle(windowTitle);
			boolean displayBackButton = (windowPlace.getFromPlace() != null);
			windowView.displayBackButton(displayBackButton);
			
			// TODO make constant 
			windowView.setBackButtonText("Back");
			
			// Trigger the load event
			if (busyView != windowView) {
				fireLoad();
			}
		}
	}

	private void fireLoad() {
		WindowView view = getView();
		LoadEvent event = new LoadEvent(view.asWidget());
		((Widget)view).fireEvent(event);
	}
	
	private void fireUnload() {
		WindowView view = getView();
		UnloadEvent event = new UnloadEvent(view.asWidget());
		((Widget)view).fireEvent(event);
	}
	
	private boolean hasUnload() {
		if (unload == null) {
			WindowPlace windowPlace = getPlace();
			String windowId = windowPlace.getId();
			String context = windowPlace.getContext();
			WindowGVO windowGVO = ActivityHelper.getWindow(windowId, context, this);
			EventListenerGVO[] events = windowGVO.getEvents();
			int numOnUnloadEvents = 0;
			for (int i=0; i<events.length; i++) {
				EventListenerGVO eventGVO = events[i];
				if (eventGVO instanceof OnUnLoadEventListenerGVO) {
					numOnUnloadEvents++;
				}
				
				// There is always one onUnload event which is used internally
				if (numOnUnloadEvents > 1) {
					unload = true;
					break;
				}
			}
			if (unload == null) {
				unload = false;
			}
		}
		return unload;
	}

	private boolean isUnloaded() {
		return unloaded;
	}
	
	@Override
	public String mayStop() {
		// Trigger the unload event
		if (!isUnloaded()) {
			fireUnload();			
		}
	
		// If there is an user-defined unload event and it is not completed,
		// return a non-nullable message in order to stay in this view
		if (hasUnload() && !isUnloaded()) {
			return "";
		}
		return super.mayStop();
	}
	
	@Override
	public void onResult(ResultEvent event) {
		super.onResult(event);
		if (hasUnload()) {
			String listenerType = event.getListenerType();
			if (QAMLConstants.EVENT_ONUNLOAD.equals(listenerType)) {
				unloaded = true;
				
				// The user-defined unload is completed, 
				// so go to another place can be continued
				((QPlaceController)getClientFactory().getPlaceController()).flush();
			}	
		}
	}
	
	@Override
	public void goBack() {
		AbstractPlace fromPlace = getPlace().getFromPlace();
		if (fromPlace == null) {
			String appId = getPlace().getContext();
			fromPlace = new AppPlace(appId);
		}
		goTo(fromPlace);
	}
	
	@Override
	public WindowPlace getPlace() {
		return (WindowPlace)super.getPlace();
	}

	public boolean isShowBackButton() {
		return ActivityHelper.isNavigationPanelEnabled();
	}
	
	public boolean isSubWindow() {
		AbstractPlace fromPlace = getPlace().getFromPlace();
		return (fromPlace instanceof WindowPlace);
	}
	
	private String generateViewKey() {
		String context = getPlace().getContext();
		String windowId = getPlace().getId();
		return getClientFactory().generateViewKey(windowId, context);
	}
}