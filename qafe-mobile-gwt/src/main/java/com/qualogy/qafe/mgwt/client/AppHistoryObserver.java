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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;
import com.googlecode.mgwt.ui.client.MGWT;
import com.qualogy.qafe.mgwt.client.activities.AboutPlace;
import com.qualogy.qafe.mgwt.client.activities.UIEntrySelectedEvent;
import com.qualogy.qafe.mgwt.client.activities.UIPlace;
import com.qualogy.qafe.mgwt.client.activities.UIEntrySelectedEvent.UIEntry;
import com.qualogy.qafe.mgwt.client.activities.animation.Animation;
import com.qualogy.qafe.mgwt.client.activities.animation.AnimationPlace;
import com.qualogy.qafe.mgwt.client.activities.animation.AnimationSelectedEvent;
import com.qualogy.qafe.mgwt.client.activities.animation.Animation.AnimationNames;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationDissolvePlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationFadePlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationFlipPlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationPopPlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationSlidePlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationSlideUpPlace;
import com.qualogy.qafe.mgwt.client.activities.animationdone.AnimationSwapPlace;
import com.qualogy.qafe.mgwt.client.activities.button.ButtonPlace;
import com.qualogy.qafe.mgwt.client.activities.buttonbar.ButtonBarPlace;
import com.qualogy.qafe.mgwt.client.activities.elements.ElementsPlace;
import com.qualogy.qafe.mgwt.client.activities.forms.FormsPlace;
import com.qualogy.qafe.mgwt.client.activities.popup.PopupPlace;
import com.qualogy.qafe.mgwt.client.activities.progressbar.ProgressBarPlace;
import com.qualogy.qafe.mgwt.client.activities.progressindicator.ProgressIndicatorPlace;
import com.qualogy.qafe.mgwt.client.activities.pulltorefresh.PullToRefreshPlace;
import com.qualogy.qafe.mgwt.client.activities.scrollwidget.ScrollWidgetPlace;
import com.qualogy.qafe.mgwt.client.activities.searchbox.SearchBoxPlace;
import com.qualogy.qafe.mgwt.client.activities.slider.SliderPlace;
import com.qualogy.qafe.mgwt.client.activities.tabbar.TabBarPlace;
import com.qualogy.qafe.mgwt.client.event.ActionEvent;
import com.qualogy.qafe.mgwt.client.event.ActionNames;
import com.qualogy.qafe.mgwt.client.places.HomePlace;

public class AppHistoryObserver implements HistoryObserver {

	@Override
	public void onPlaceChange(Place place, HistoryHandler handler) {

	}

	@Override
	public void onHistoryChanged(Place place, HistoryHandler handler) {

	}

	@Override
	public void onAppStarted(Place place, HistoryHandler historyHandler) {
		if (MGWT.getOsDetection().isPhone()) {
			onPhoneNav(place, historyHandler);
		} else {
			// tablet
			onTabletNav(place, historyHandler);

		}

	}

	@Override
	public HandlerRegistration bind(EventBus eventBus, final HistoryHandler historyHandler) {

		HandlerRegistration addHandler = eventBus.addHandler(AnimationSelectedEvent.getType(), new AnimationSelectedEvent.Handler() {

			@Override
			public void onAnimationSelected(AnimationSelectedEvent event) {

				Animation animation = event.getAnimation();

				AnimationNames animationName = animation.getAnimationName();

				Place place = null;

				switch (animationName) {
				case SLIDE:
					place = new AnimationSlidePlace();

					break;
				case SLIDE_UP:
					place = new AnimationSlideUpPlace();

					break;
				case DISSOLVE:
					place = new AnimationDissolvePlace();

					break;
				case FADE:
					place = new AnimationFadePlace();

					break;
				case FLIP:
					place = new AnimationFlipPlace();

					break;
				case POP:
					place = new AnimationPopPlace();

					break;
				case SWAP:
					place = new AnimationSwapPlace();

					break;

				default:
					// TODO log
					place = new AnimationSlidePlace();
					break;
				}

				if (MGWT.getOsDetection().isTablet()) {

					historyHandler.replaceCurrentPlace(place);
					historyHandler.goTo(place, true);
				} else {
					historyHandler.goTo(place);
				}

			}
		});
		HandlerRegistration register3 = UIEntrySelectedEvent.register(eventBus, new UIEntrySelectedEvent.Handler() {

			@Override
			public void onAnimationSelected(UIEntrySelectedEvent event) {

				UIEntry entry = event.getEntry();

				Place place = null;

				switch (entry) {
				case BUTTON_BAR:
					place = new ButtonBarPlace();
					break;
				case BUTTONS:
					place = new ButtonPlace();
					break;
				case ELEMENTS:
					place = new ElementsPlace();
					break;
				case FORMS:
					place = new FormsPlace();
					break;
				case POPUPS:
					place = new PopupPlace();
					break;
				case PROGRESS_BAR:
					place = new ProgressBarPlace();
					break;
				case PROGRESS_INDICATOR:
					place = new ProgressIndicatorPlace();
					break;
				case PULL_TO_REFRESH:
					place = new PullToRefreshPlace();
					break;
				case SCROLL_WIDGET:
					place = new ScrollWidgetPlace();
					break;
				case SEARCH_BOX:
					place = new SearchBoxPlace();
					break;
				case SLIDER:
					place = new SliderPlace();
					break;
				case TABBAR:
					place = new TabBarPlace();
					break;

				default:
					break;
				}

				if (MGWT.getOsDetection().isTablet()) {

					historyHandler.replaceCurrentPlace(place);
					historyHandler.goTo(place, true);
				} else {
					historyHandler.goTo(place);
				}

			}
		});

		HandlerRegistration register2 = ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler() {

			@Override
			public void onAction(ActionEvent event) {

				History.back();

			}
		});

		HandlerRegistration register = ActionEvent.register(eventBus, ActionNames.ANIMATION_END, new ActionEvent.Handler() {

			@Override
			public void onAction(ActionEvent event) {
				if (MGWT.getOsDetection().isPhone()) {
					History.back();
				} else {
					historyHandler.goTo(new AnimationPlace(), true);
				}

			}
		});

		HandlerRegistrationCollection col = new HandlerRegistrationCollection();
		col.addHandlerRegistration(register);
		col.addHandlerRegistration(register2);
		col.addHandlerRegistration(register3);
		col.addHandlerRegistration(addHandler);
		return col;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private void onPhoneNav(Place place, HistoryHandler historyHandler) {
		if (place instanceof AnimationDissolvePlace || place instanceof AnimationFadePlace || place instanceof AnimationFlipPlace || place instanceof AnimationPopPlace
				|| place instanceof AnimationSlidePlace || place instanceof AnimationSlideUpPlace || place instanceof AnimationSwapPlace) {

			historyHandler.replaceCurrentPlace(new HomePlace());

			historyHandler.pushPlace(new AnimationPlace());

		} else {
			if (place instanceof AboutPlace) {
				historyHandler.replaceCurrentPlace(new HomePlace());

			} else {
				if (place instanceof AnimationPlace) {
					historyHandler.replaceCurrentPlace(new HomePlace());
				} else {
					if (place instanceof UIPlace) {
						historyHandler.replaceCurrentPlace(new HomePlace());
					} else {
						if (place instanceof UIPlace) {
							historyHandler.replaceCurrentPlace(new HomePlace());
						} else {

							if (place instanceof ButtonBarPlace || place instanceof ButtonPlace || place instanceof ElementsPlace || place instanceof FormsPlace || place instanceof PopupPlace
									|| place instanceof ProgressBarPlace || place instanceof ProgressIndicatorPlace || place instanceof PullToRefreshPlace || place instanceof ScrollWidgetPlace
									|| place instanceof SearchBoxPlace || place instanceof SliderPlace || place instanceof TabBarPlace) {
								historyHandler.replaceCurrentPlace(new HomePlace());

								historyHandler.pushPlace(new UIPlace());
							}

						}
					}
				}
			}
		}
	}

	private void onTabletNav(Place place, HistoryHandler historyHandler) {
		if (place instanceof AnimationDissolvePlace || place instanceof AnimationFadePlace || place instanceof AnimationFlipPlace || place instanceof AnimationPopPlace
				|| place instanceof AnimationSlidePlace || place instanceof AnimationSlideUpPlace || place instanceof AnimationSwapPlace) {

			historyHandler.replaceCurrentPlace(new HomePlace());

		} else {
			if (place instanceof AboutPlace) {
				historyHandler.replaceCurrentPlace(new HomePlace());
			} else {
				if (place instanceof AnimationPlace) {
					historyHandler.replaceCurrentPlace(new HomePlace());
				} else {
					if (place instanceof UIPlace) {
						historyHandler.replaceCurrentPlace(new HomePlace());
					} else {
						if (place instanceof UIPlace) {
							historyHandler.replaceCurrentPlace(new HomePlace());
						} else {

							if (place instanceof ButtonBarPlace || place instanceof ButtonPlace || place instanceof ElementsPlace || place instanceof FormsPlace || place instanceof PopupPlace
									|| place instanceof ProgressBarPlace || place instanceof ProgressIndicatorPlace || place instanceof PullToRefreshPlace || place instanceof ScrollWidgetPlace
									|| place instanceof SearchBoxPlace || place instanceof SliderPlace || place instanceof TabBarPlace) {
								historyHandler.replaceCurrentPlace(new HomePlace());

							}

						}
					}
				}
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity
}
