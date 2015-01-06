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
package com.qualogy.qafe.mgwt.client.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.event.shared.Event;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.HasLongTapHandlers;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.qualogy.qafe.mgwt.client.component.DataMap;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.main.MainController;
import com.qualogy.qafe.mgwt.client.main.UIModel;
import com.qualogy.qafe.mgwt.client.ui.component.HasAttribute;
import com.qualogy.qafe.mgwt.client.ui.component.HasData;
import com.qualogy.qafe.mgwt.client.ui.events.CellItemSelectEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellItemSelectHandler;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeEvent;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasCellItemSelectHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasDataChangeHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasLoadHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasScrollBottomHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasSuggestionHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasTimerHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasUnloadHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.LoadEvent;
import com.qualogy.qafe.mgwt.client.ui.events.LoadHandler;
import com.qualogy.qafe.mgwt.client.ui.events.NotifyEvent;
import com.qualogy.qafe.mgwt.client.ui.events.NotifyHandler;
import com.qualogy.qafe.mgwt.client.ui.events.ResultEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomHandler;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionEvent;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionHandler;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionSelectEvent;
import com.qualogy.qafe.mgwt.client.ui.events.SuggestionSelectHandler;
import com.qualogy.qafe.mgwt.client.ui.events.TimerEvent;
import com.qualogy.qafe.mgwt.client.ui.events.TimerHandler;
import com.qualogy.qafe.mgwt.client.ui.events.TimerHandlerImpl;
import com.qualogy.qafe.mgwt.client.ui.events.UnloadEvent;
import com.qualogy.qafe.mgwt.client.ui.events.UnloadHandler;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.exception.RequiredFieldException;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;
import com.qualogy.qafe.mgwt.client.util.KeyBoardHelper;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataI;
import com.qualogy.qafe.mgwt.client.vo.data.GDataObject;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.execute.FunctionsExecutor;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.ClickEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.DoubleClickEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnChangeEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnEnterEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnExitEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnFocusEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyDownEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyPressEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnKeyUpEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnLoadEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseDownEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseMoveEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnMouseUpEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnScrollEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnTimerEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.OnUnLoadEventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.ParameterGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class ActivityHelper {

	private ActivityHelper() {
	}

	public static UIGVO getApp(String appId, AbstractActivity activity) {
		if (appId == null) {
			return null;
		}
		if (activity == null) {
			return null;
		}
		UIModel uiModel = activity.getClientFactory().getUIModel();
		if (uiModel != null) {
			UIGVO[] vos = uiModel.getVos();
			if (vos != null) {
				for (int i=0; i<vos.length; i++) {
					UIGVO uiGVO = vos[i];
					if (appId.equals(uiGVO.getAppId())) {
						return uiGVO;
					}
				}
			}
		}
		return null;
	}
	
	public static WindowGVO getWindow(String windowId, String context, AbstractActivity activity) {
		if (windowId == null) {
			return null;
		}
		UIGVO appGVO = getApp(context, activity);
		if (appGVO == null) {
			return null;
		}
		WindowGVO[] windows = appGVO.getWindows();
		if (windows != null) {
			for (WindowGVO windowGVO : windows) {
				if (windowId.equals(windowGVO.getId())) {
					return windowGVO;
				}
			}
		}
		return null;
	}
	
	public static boolean isNavigationPanelEnabled() {
		return MainController.getInstance().isNavigationPanelEnabled();
	}
	
	public static void invokeUIByUUID(String uuid, AsyncCallback<?> callbackHandler) {
		MainController.getInstance().invokeUIByUUID(uuid, callbackHandler);
	}
	
	public static void handleResult(ResultEvent event, AbstractActivity activity) {
		try {
			if (event != null) {
				Object result = event.getResult();
				if (result instanceof GDataObject) {
					GDataObject dataObject = (GDataObject)result;
					BuiltInFunctionGVO[] builtInFunctions = dataObject.getFunctions();
					if (builtInFunctions == null) {
						return;
					}
					for (int i=0; i<builtInFunctions.length; i++) {
						BuiltInFunctionGVO builtInFunctionGVO = builtInFunctions[i];
						builtInFunctionGVO.setSenderId(dataObject.getSenderId());
						builtInFunctionGVO.setListenerType(dataObject.getListenerType());
						FunctionsExecutor.getInstance().execute(builtInFunctionGVO, activity);
					}
				}
			}
		} finally {
			activity.setBusy(false);
		}
	}
	
	public static void registerEvents(ComponentGVO component, UIObject widget, String windowId, String context, AbstractActivity activity) {
		registerEvents(component, widget, null, windowId, context, activity);
	}
	
	// CHECKSTYLE.OFF: CyclomaticComplexity
	public static void registerEvents(ComponentGVO component, UIObject widget, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		if (widget == null) {
			return;
		}
		if (component == null) {
			return;
		}
		if (activity == null) {
			return;
		}
		EventListenerGVO[] events = component.getEvents();
		if (events == null) {
			return;
		}
		for (int i=0; i<events.length; i++) {
			HandlerRegistration handlerRegistration = null;
			EventListenerGVO eventGVO = events[i];
			if (eventGVO instanceof ClickEventListenerGVO) {
				if (isCellItemSelectEvent(component, eventGVO)) {
					if (widget instanceof HasCellItemSelectHandlers) {
						String cellItem = getCellItem(component, eventGVO);
						HasCellItemSelectHandlers hasCellItemSelectHandlers = (HasCellItemSelectHandlers)widget;
						CellItemSelectHandler cellItemSelectHandler = createCellItemSelectHandler(component, eventGVO, notifyHandler, windowId, context, activity);
						hasCellItemSelectHandlers.addCellItemSelectHandler(cellItem, cellItemSelectHandler);
					}
				} else if (widget instanceof HasSuggestionHandlers) {
					HasSuggestionHandlers hasSuggestionHandlers = (HasSuggestionHandlers)widget;
					SuggestionSelectHandler suggestionSelectHandler = createSuggestionSelectHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasSuggestionHandlers.addSuggestionSelectHandler(suggestionSelectHandler);
				} else {
					if (widget instanceof HasClickHandlers) {
						// TODO
						System.out.println("HasClickHandlers" + widget);
					}
					if (widget instanceof HasTapHandlers) {
						HasTapHandlers hasTapHandlers = (HasTapHandlers)widget;
						TapHandler tapHandler = createTapHandler(component, eventGVO, notifyHandler, windowId, context, activity);
						handlerRegistration = hasTapHandlers.addTapHandler(tapHandler);
					}	
				}
			} else if (eventGVO instanceof DoubleClickEventListenerGVO) {
				if (widget instanceof HasLongTapHandlers) {
					HasLongTapHandlers hasLongTapHandlers = (HasLongTapHandlers)widget;
					LongTapHandler longTapHandler = createLongTapHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasLongTapHandlers.addLongTapHandler(longTapHandler);
				}
			} else if (eventGVO instanceof OnChangeEventListenerGVO) {
				if (widget instanceof HasSuggestionHandlers) {
					HasSuggestionHandlers hasSuggestionHandlers = (HasSuggestionHandlers)widget;
					SuggestionHandler suggestionHandler = createSuggestionHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasSuggestionHandlers.addSuggestionHandler(suggestionHandler);
				} else if (widget instanceof HasDataChangeHandlers) {
					HasDataChangeHandlers hasDataChangeHandlers = (HasDataChangeHandlers)widget;
					DataChangeHandler dataChangeHandler = createDataChangeHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasDataChangeHandlers.addDataChangeHandler(dataChangeHandler);
				}
			} else if (eventGVO instanceof OnExitEventListenerGVO) {
				if (widget instanceof HasBlurHandlers) {
					HasBlurHandlers hasBlurHandlers = (HasBlurHandlers)widget;
					BlurHandler blurHandler = createBlurHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasBlurHandlers.addBlurHandler(blurHandler);
				} 
			} else if (eventGVO instanceof OnFocusEventListenerGVO) {
				if (widget instanceof HasFocusHandlers) {
					HasFocusHandlers hasFocusHandlers = (HasFocusHandlers)widget;
					FocusHandler focusHandler = createFocusHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasFocusHandlers.addFocusHandler(focusHandler);
				}
			} else if (eventGVO instanceof OnEnterEventListenerGVO) {
				if (widget instanceof HasAllKeyHandlers) {
					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers)widget;
					KeyDownHandler keyDownHandler = createKeyEnterHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasAllKeyHandlers.addKeyDownHandler(keyDownHandler);
				}
			} else if (eventGVO instanceof OnKeyPressEventListenerGVO) {
				if (widget instanceof HasAllKeyHandlers) {
					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers)widget;
					KeyPressHandler keyPressHandler = createKeyPressHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasAllKeyHandlers.addKeyPressHandler(keyPressHandler);
				}
			} else if (eventGVO instanceof OnKeyDownEventListenerGVO) {
				if (widget instanceof HasAllKeyHandlers) {
					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers)widget;
					KeyDownHandler keyDownHandler = createKeyDownHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasAllKeyHandlers.addKeyDownHandler(keyDownHandler);
				}
			} else if (eventGVO instanceof OnKeyUpEventListenerGVO) {
				if (widget instanceof HasAllKeyHandlers) {
					HasAllKeyHandlers hasAllKeyHandlers = (HasAllKeyHandlers)widget;
					KeyUpHandler keyUpHandler = createKeyUpHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasAllKeyHandlers.addKeyUpHandler(keyUpHandler);
				}
			} else if (eventGVO instanceof OnMouseDownEventListenerGVO) {
				if (widget instanceof HasTouchHandlers) {
					HasTouchHandlers hasTouchHandlers = (HasTouchHandlers)widget;
					TouchStartHandler touchStartHandler = createTouchStartHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasTouchHandlers.addTouchStartHandler(touchStartHandler);
				}
			} else if (eventGVO instanceof OnMouseMoveEventListenerGVO) {
				if (widget instanceof HasTouchHandlers) {
					HasTouchHandlers hasTouchHandlers = (HasTouchHandlers)widget;
					TouchMoveHandler touchMoveHandler = createTouchMoveHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasTouchHandlers.addTouchMoveHandler(touchMoveHandler);
				}
			} else if (eventGVO instanceof OnMouseUpEventListenerGVO) {
				if (widget instanceof HasTouchHandlers) {
					HasTouchHandlers hasTouchHandlers = (HasTouchHandlers)widget;
					TouchEndHandler touchEndHandler = createTouchEndHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasTouchHandlers.addTouchEndHandler(touchEndHandler);
				}
			} else if (eventGVO instanceof OnLoadEventListenerGVO) {
				if (widget instanceof HasLoadHandlers) {
					HasLoadHandlers hasLoadHandlers = (HasLoadHandlers)widget;
					LoadHandler loadHandler = createLoadHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasLoadHandlers.addLoadHandler(loadHandler);
				}
			} else if (eventGVO instanceof OnUnLoadEventListenerGVO) {
				if (widget instanceof HasUnloadHandlers) {
					HasUnloadHandlers hasUnloadHandlers = (HasUnloadHandlers)widget;
					UnloadHandler unloadHandler = createUnloadHandler(component, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasUnloadHandlers.addUnloadHandler(unloadHandler);
				}
			} else if (eventGVO instanceof OnTimerEventListenerGVO) {
				// The timer event is attached to a view only,
				// and a view can contain only one timer for each event
				AbstractView view = activity.getView();
				if (view instanceof HasTimerHandlers) {
					HasTimerHandlers hasTimerHandlers = (HasTimerHandlers)view;
					TimerHandler timerHandler = createTimerHandler(component, widget, eventGVO, notifyHandler, windowId, context, activity);
					handlerRegistration = hasTimerHandlers.addTimerHandler(timerHandler);
				}
			} else if (eventGVO instanceof OnScrollEventListenerGVO) {
				String listenerType = eventGVO.getEventListenerType();
				if (QAMLConstants.EVENT_ONSCROLL_BOTTOM.equals(listenerType)) {
					if (widget instanceof HasScrollBottomHandlers) {
						HasScrollBottomHandlers hasScrollBottomHandlers = (HasScrollBottomHandlers)widget;
						ScrollBottomHandler scrollBottomHandler = createScrollBottomHandler(component, eventGVO, notifyHandler, windowId, context, activity);
						handlerRegistration = hasScrollBottomHandlers.addScrollBottomHandler(scrollBottomHandler);
					}	
				}
			}
			if (handlerRegistration != null) {
				activity.addHandlerRegistration(handlerRegistration);	
			}
		}	
	}
	// CHECKSTYLE.ON: CyclomaticComplexity

	private static boolean isCellItemSelectEvent(ComponentGVO component, EventListenerGVO eventGVO) {
		if (component == null) {
			return false;
		}
		if (eventGVO == null) {
			return false;
		}
		String eventComponentId = eventGVO.getEventComponentId();
		if (eventComponentId == null) {
			return false;
		}
		String componentId = component.getId();
		return eventComponentId.startsWith(componentId + ".");
	}

	private static String getCellItem(ComponentGVO component, EventListenerGVO eventGVO) {
		if (component == null) {
			return null;
		}
		if (eventGVO == null) {
			return null;
		}
		String eventComponentId = eventGVO.getEventComponentId();
		if (eventComponentId == null) {
			return null;
		}
		String cellItem = null;
		String componentId = component.getId();
		String dot = ".";
		if (eventComponentId.startsWith(componentId + dot)) {
			int dotIndex = eventComponentId.indexOf(dot);
			cellItem = eventComponentId.substring(dotIndex + 1);
		}
		return cellItem;
	}
	
	private static CellItemSelectHandler createCellItemSelectHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new CellItemSelectHandler() {
			@Override
			public void onCellItemSelect(CellItemSelectEvent event) {
				UIObject widget = (UIObject)event.getSource();
				Object widgetValue = event.getModel();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, widgetValue, eventGVO, QAMLConstants.EVENT_ONCLICK, inputVariables, null, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static TapHandler createTapHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONCLICK, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static LongTapHandler createLongTapHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new LongTapHandler() {
			@Override
			public void onLongTap(LongTapEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONDBLCLICK, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static DataChangeHandler createDataChangeHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new DataChangeHandler() {
			@Override
			public void onDataChange(DataChangeEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONCHANGE, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static SuggestionHandler createSuggestionHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new SuggestionHandler() {
			@Override
			public void onSuggestion(SuggestionEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONCHANGE, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static SuggestionSelectHandler createSuggestionSelectHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new SuggestionSelectHandler() {
			@Override
			public void onSuggestionSelect(SuggestionSelectEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONCLICK, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static BlurHandler createBlurHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONEXIT, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static FocusHandler createFocusHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONFOCUS, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}

	private static KeyDownHandler createKeyEnterHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					UIObject widget = (UIObject)event.getSource();
					List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
					handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONENTER, inputVariables, notifyHandler, windowId, context, activity);
				}
			}
		};
	}
	
	private static KeyPressHandler createKeyPressHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (eventGVO.getParameterList() == null) {
					return;
				}
				Iterator<ParameterGVO> itrParameter = eventGVO.getParameterList().iterator();
				while (itrParameter.hasNext()) {
					ParameterGVO parameterGVO = itrParameter.next();
					if (parameterGVO == null) {
						continue;
					}
					if (KeyBoardHelper.isKeyInput(parameterGVO.getName(), parameterGVO.getValue(), Character.toString(event.getCharCode()))) {
						UIObject widget = (UIObject)event.getSource();
						List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
						handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONKEYPRESS, inputVariables, notifyHandler, windowId, context, activity);
						break;
					}
				}
			}
		};
	}
	
	private static KeyDownHandler createKeyDownHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				handleKeyInput(componentGVO, eventGVO, event, QAMLConstants.EVENT_ONKEYDOWN, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static KeyUpHandler createKeyUpHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				handleKeyInput(componentGVO, eventGVO, event, QAMLConstants.EVENT_ONKEYUP, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static TouchStartHandler createTouchStartHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new TouchStartHandler() {
			@Override
			public void onTouchStart(TouchStartEvent event) {
				handleTouchInput(componentGVO, eventGVO, event, QAMLConstants.EVENT_ONMOUSE_DOWN, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static TouchMoveHandler createTouchMoveHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				handleTouchInput(componentGVO, eventGVO, event, QAMLConstants.EVENT_ONMOUSE_MOVE, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static TouchEndHandler createTouchEndHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				handleTouchInput(componentGVO, eventGVO, event, QAMLConstants.EVENT_ONMOUSE_UP, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static LoadHandler createLoadHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONLOAD, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static UnloadHandler createUnloadHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new UnloadHandler() {
			@Override
			public void onUnload(UnloadEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONUNLOAD, inputVariables, notifyHandler, windowId, context, activity);				
			}
		};
	}
	
	private static TimerHandler createTimerHandler(final ComponentGVO componentGVO, final UIObject widget, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		int timeout = -1;
		int repeat = 0;
		if (eventGVO.getParameterList() != null) {
			Iterator<ParameterGVO> itrParameter = eventGVO.getParameterList().iterator();
			while (itrParameter.hasNext()) {
				ParameterGVO parameterGVO = itrParameter.next();
				if (parameterGVO == null) {
					continue;
				}
				String paramName = parameterGVO.getName();
				String paramValue = parameterGVO.getValue();
				if (QAMLConstants.PARAM_TIME_OUT.equals(paramName)) {
					timeout = QAMLUtil.toInteger(paramValue, timeout);
				} else if (QAMLConstants.PARAM_REPEAT.equals(paramName)) {
					repeat = QAMLUtil.toInteger(paramValue, repeat);
				}
			}
		}
		return new TimerHandlerImpl(eventGVO.getEventId(), timeout, repeat) {
			@Override
			public void onTimer(TimerEvent event) {
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONTIMER, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static ScrollBottomHandler createScrollBottomHandler(final ComponentGVO componentGVO, final EventListenerGVO eventGVO, final NotifyHandler notifyHandler, final String windowId, final String context, final AbstractActivity activity) {
		return new ScrollBottomHandler() {
			@Override
			public void onScrollBottom(ScrollBottomEvent event) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, QAMLConstants.EVENT_ONSCROLL_BOTTOM, inputVariables, notifyHandler, windowId, context, activity);
			}
		};
	}
	
	private static void handleKeyInput(ComponentGVO componentGVO, EventListenerGVO eventGVO, KeyEvent event, String listenerType, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		if (eventGVO.getParameterList() == null) {
			return;
		}
		Iterator<ParameterGVO> itrParameter = eventGVO.getParameterList().iterator();
		while (itrParameter.hasNext()) {
			ParameterGVO parameterGVO = itrParameter.next();
			if (parameterGVO == null) {
				continue;
			}
			if (KeyBoardHelper.isKeyInput(parameterGVO.getName(), parameterGVO.getValue(), event.getNativeEvent())) {
				UIObject widget = (UIObject)event.getSource();
				List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
				handleEvent(componentGVO, widget, eventGVO, event, listenerType, inputVariables, notifyHandler, windowId, context, activity);
				break;
			}
		}
	}

	private static void handleTouchInput(ComponentGVO componentGVO, EventListenerGVO eventGVO, TouchEvent event, String listenerType, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		UIObject widget = (UIObject)event.getSource();
		List<InputVariableGVO> inputVariables = eventGVO.getInputvariablesList();
		handleEvent(componentGVO, widget, eventGVO, event, listenerType, inputVariables, notifyHandler, windowId, context, activity);
	}
	
	public static void handleEvent(ComponentGVO component, UIObject widget, EventListenerGVO eventGVO, Event event, String listenerType, List<InputVariableGVO> inputVariables, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		Map<String,String> touchInput = getTouchInput(event);
		handleEvent(component, widget, eventGVO, listenerType, inputVariables, touchInput, notifyHandler, windowId, context, activity);
	}

	public static void handleEvent(ComponentGVO component, UIObject widget, EventListenerGVO eventGVO, String listenerType, List<InputVariableGVO> inputVariables, Map<String,String> touchInput, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		Object widgetValue = null;
		if (widget instanceof HasData) {
			widgetValue = ((HasData)widget).getData();
		}
		handleEvent(component, widget, widgetValue, eventGVO, listenerType, inputVariables, touchInput, notifyHandler, windowId, context, activity);
	}
	
	public static void handleEvent(ComponentGVO component, UIObject widget, Object widgetValue, EventListenerGVO eventGVO, String listenerType, List<InputVariableGVO> inputVariables, Map<String,String> touchInput, NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		try{
			notify(notifyHandler, widget, listenerType);
			String componentId = component.getId();
			if (componentId == null) {
				return;
			}
			String sender = ComponentRepository.getInstance().generateComponentKey(componentId, windowId, context);
			String componentName = component.getFieldName();
			EventDataGVO eventDataGVO = new EventDataGVO();
			eventDataGVO.setUuid(context);
			eventDataGVO.setEventId(eventGVO.getEventId());
			eventDataGVO.setSender(sender);
			eventDataGVO.setSenderName(componentName);
			eventDataGVO.setOriginalSenderId(sender);
			eventDataGVO.setListenerType(listenerType);
			eventDataGVO.setUserUID(ClientApplicationContext.getInstance().getAppUUID());
			eventDataGVO.setWindowSession(ClientApplicationContext.getInstance().getWindowSession());
			
			if (touchInput != null) {
				String posX = touchInput.get(EventDataI.MOUSE_X);
				String posY = touchInput.get(EventDataI.MOUSE_Y);
				eventDataGVO.setMouseCoordinates(posX, posY);
			}
			
			// TODO
			eventDataGVO.setInternalVariables(null);
			
			eventDataGVO.setParameters(ClientApplicationContext.getInstance().getParameters());
			eventDataGVO.setParent(windowId);
			enrichEventWithExtraInfo(eventDataGVO, component, widgetValue, eventGVO, listenerType, windowId, context);
			resolveInputVariables(eventDataGVO, component, widgetValue, inputVariables, windowId, context);
			MainController.getInstance().executeEvent(eventDataGVO, activity);
			activity.setBusy(true);
		} catch (RequiredFieldException e){
			handleRequiredFieldException(e);
		} catch (Exception e){
			ClientApplicationContext.getInstance().log("Error", e.getMessage(), true, true, null);
		}
	}
	
	public static void notify(NotifyHandler notifyHandler, UIObject source, String listenerType) {
		if (notifyHandler == null) {
			return;
		}
		NotifyEvent event = new NotifyEvent(source, listenerType);
		event.execute(notifyHandler);
	}
	
	private static void enrichEventWithExtraInfo(EventDataGVO eventDataGVO, ComponentGVO component, Object componentValue, EventListenerGVO eventGVO, String listenerType, String windowId, String context) {
		String componentId = component.getId();
		String componentName = component.getFieldName();
		String sourceId = eventGVO.getSourceId();
		if (!QAMLUtil.isEmpty(sourceId)) {
			eventDataGVO.setSourceId(sourceId);
			eventDataGVO.setSourceIdValue(componentId);
		}
		String sourceName = eventGVO.getSourceName();
		if (!QAMLUtil.isEmpty(sourceName)) {
			eventDataGVO.setSourceName(sourceName);
			eventDataGVO.setSourceNameValue(componentName);
		}
		String sourceValue = eventGVO.getSourceValue(); 
		if (!QAMLUtil.isEmpty(sourceValue)) {
			eventDataGVO.setSourceValue(sourceValue);
			if (componentValue instanceof String) {
				eventDataGVO.setSourceValueValue((String)componentValue);
			}
		}
		String sourceListenerType = eventGVO.getSourceListenerType();
		if (!QAMLUtil.isEmpty(sourceListenerType)) {
			eventDataGVO.setSourceListenerType(sourceListenerType);
			eventDataGVO.setSourceListenerTypeValue(listenerType);
		}
	}
	
	private static void resolveInputVariables(EventDataGVO eventDataGVO, ComponentGVO component, Object componentValue, List<InputVariableGVO> inputVariables, String windowId, String context) {
		if (inputVariables != null) {
			for (InputVariableGVO inputVariableGVO : inputVariables) {
				String value = "";
				DataContainerGVO dataContainerGVO = null;
				String reference = inputVariableGVO.getReference();
				if (hasAttribute(reference)) {
					value = getAttributeValue(reference, windowId, context);
				} else if (reference.contains(".$$")) {
					
				} else if (reference.equals(component.getId())) {
					if (componentValue instanceof DataMap) {
						dataContainerGVO = new DataContainerGVO((DataMap)componentValue);
					} else if (componentValue != null) {
						value = componentValue.toString();
					}
				} else {
					Object data = getValue(reference, windowId, context);
					if (data instanceof DataContainerGVO) {
						dataContainerGVO = (DataContainerGVO)data;
					} else if (data != null) {
						value = data.toString();
					} 
				}
				
				if ((reference != null) && reference.startsWith("||")) {
					// TODO
					// So this is a click from a table
//						reference = (inputVariableGVO.getReference().substring(senderId.lastIndexOf("||") + 2));
				}
				InputVariableGVO newInputVariableGVO = new InputVariableGVO(inputVariableGVO.getName(), reference, inputVariableGVO.getDefaultValue(), value, dataContainerGVO); 
				eventDataGVO.getInputVariables().add(newInputVariableGVO);
			}
		}
	}

	private static Object getValue(String reference, String windowId, String context) {
		Object value = null;
		
		// Get components based on the id
		String key = ComponentRepository.getInstance().generateComponentKey(reference, windowId, context);
		List<UIObject> widgets = ComponentRepository.getInstance().getComponentById(key);
		if (!QAMLUtil.isEmpty(widgets)) {
			// Expect to have only one component
			UIObject widget = widgets.get(0);
			if (widget instanceof HasData) {
				HasData hasData = (HasData)widget;
				// The method getDataModel is in most cases the same as getData,
				// but in some cases it returns the model of the selected data, depends on the component 
				// like a DropDown component it returns a map containing "id" and "value", instead of "id" only  
				value = hasData.getDataModel();
			}
		} else {
			// Determine whether the view has components,
			// if not stop processing because there are no components at all
			String viewKey = ComponentRepository.getInstance().generateViewKey(windowId, context);
			Map<UIObject,ComponentGVO> components = ComponentRepository.getInstance().getComponents(viewKey);
			if (components == null) {
				return null;
			}
			
			// If there is a child reference, like parentId.childId,
			// take the parent
			String[] references = reference.split("[.]");
			String mainReference = references[0];
			key = ComponentRepository.getInstance().generateComponentKey(mainReference, windowId, context);
			
			// Get components based on the name
			widgets = ComponentRepository.getInstance().getComponentByName(key);
			if (!QAMLUtil.isEmpty(widgets)) {
				value = getData(widgets, mainReference, windowId, context, components, true);
			} else {
				// Get components based on the group
				widgets = ComponentRepository.getInstance().getComponentByGroup(key);
				if (!QAMLUtil.isEmpty(widgets)) {
					value = getData(widgets, mainReference, windowId, context, components, false);
				}
			}
			
			// If possible, process child references
			if (value instanceof DataContainerGVO) {
				DataContainerGVO dataContainerGVO = (DataContainerGVO)value;
				for (int i=1; i<references.length; i++) {
					if ((dataContainerGVO != null) && (dataContainerGVO.getKind() == DataContainerGVO.KIND_MAP)) {
						String subReference = references[i];
						dataContainerGVO = dataContainerGVO.getDataMap().get(subReference);
					} else {
						dataContainerGVO = null;
						break;
					}
				}
				value = dataContainerGVO;
			}
		}
		if (!(value instanceof DataContainerGVO)) {
			value = createDataContainerGVO(value);	
		}
		return value;
	}
	
	private static DataContainerGVO createDataContainerGVO(Object value) {
		DataContainerGVO dataContainerGVO = DataMap.createDataContainerGVO(value);
		return dataContainerGVO;
		
		// Moved to DataMap
//		if (value instanceof DataContainerGVO) {
//			return (DataContainerGVO)value;	
//		} else if (value instanceof List) {
//			List<DataContainerGVO> dataContainers = new ArrayList<DataContainerGVO>();
//			List list = (List)value;
//			for (int i=0; i<list.size(); i++) {
//				Object itemValue = list.get(i);
//				DataContainerGVO dataContainerGVO = createDataContainerGVO(itemValue);
//				if (dataContainerGVO != null) {
//					dataContainers.add(dataContainerGVO);
//				}
//			}
//			DataContainerGVO dataContainerGVO = new DataContainerGVO();
//			dataContainerGVO.setListofDC(dataContainers);
//			dataContainerGVO.setKind(DataContainerGVO.KIND_COLLECTION);
//			return dataContainerGVO;	
//		} else if (value instanceof Map) {
//			DataMap dataMap = new DataMap();
//			Map map = (Map)value;
//			Iterator itrKey = map.keySet().iterator();
//			while (itrKey.hasNext()) {
//				Object key = itrKey.next();
//				Object keyValue = map.get(key);
//				DataContainerGVO dataContainerGVO = createDataContainerGVO(keyValue);
//				if (dataContainerGVO != null) {
//					dataMap.put((String)key, dataContainerGVO);	
//				}
//			}
//			DataContainerGVO dataContainerGVO = new DataContainerGVO();
//			dataContainerGVO.setDataMap(dataMap);
//			dataContainerGVO.setKind(DataContainerGVO.KIND_MAP);
//			return dataContainerGVO;	
//		} else if (value instanceof String) {
//			DataContainerGVO dataContainerGVO = new DataContainerGVO();
//			dataContainerGVO.setDataString((String)value);
//			dataContainerGVO.setKind(DataContainerGVO.KIND_STRING);
//			return dataContainerGVO;	
//		} else if (value instanceof Date) {
//			DataContainerGVO dataContainerGVO = new DataContainerGVO();
//			dataContainerGVO.setDataString(((Date)value).toString());
//			dataContainerGVO.setDateData((Date)value);
//			dataContainerGVO.setKind(DataContainerGVO.KIND_STRING);
//			dataContainerGVO.setStringDataType(DataContainerGVO.TYPE_DATE);
//			return dataContainerGVO;	
//		}
//		return null;
	}
	
	private static DataContainerGVO getData(Iterable<? extends UIObject> widget, String reference, String windowId, String context, Map<UIObject,ComponentGVO> components, boolean mainLevel) {
		if (widget == null) {
			return null;
		}
		DataContainerGVO dataContainerGVO = new DataContainerGVO();
		dataContainerGVO.setParameterName(reference);
		DataMap dataMap = new DataMap();
		dataContainerGVO.setDataMap(dataMap);
		dataContainerGVO.setKind(DataContainerGVO.KIND_MAP);
		Iterator<? extends UIObject> itrWidget = widget.iterator();
		while (itrWidget.hasNext()) {
			UIObject childWidget = itrWidget.next();
			String childReference = null;
			ComponentGVO childComponent = components.get(childWidget);
			if (childComponent != null) {
				 childReference = childComponent.getFieldName();
			}
			Object data = null;
			if (childWidget instanceof HasWidgets) {
				data = getData((HasWidgets)childWidget, childReference, windowId, context, components, false);
			} else if ((childReference != null) && (childWidget instanceof HasData)) {
				data = ((HasData)childWidget).getData();
			}
			DataContainerGVO childDataContainerGVO = createDataContainerGVO(data);
			if (childReference != null) {
				if (dataMap.containsKey(childReference)) {
					// Merging if possible
					DataContainerGVO dcGVO = dataMap.get(childReference);					
					if ((dcGVO != null) && dcGVO.isMap() && (childDataContainerGVO != null) && childDataContainerGVO.isMap()) {						
						childDataContainerGVO.getDataMap().putAll(dcGVO.getDataMap());
					}
				}
				dataMap.put(childReference, childDataContainerGVO);
			} else if (childDataContainerGVO != null) {
				dataMap.putAll(childDataContainerGVO.getDataMap());
			}
		}
		if (mainLevel) {
			dataContainerGVO = dataContainerGVO.getDataMap().get(reference);
		}
		return dataContainerGVO;
	}
	
	public static void setValue(SetValueGVO setValueGVO, String windowId, String context) {
		if (setValueGVO == null) {
			return;
		}
		DataContainerGVO dataContainerGVO = setValueGVO.getDataContainer();
		Object defaultData = setValueGVO.getValue();
		String action = setValueGVO.getAction();
		Map<String,String> mapping = setValueGVO.getMapping();
		
		// Get components based on the id
		List<UIObject> widgets = null;
		String key = null;
		BuiltInComponentGVO builtInComponentGVO = setValueGVO.getBuiltInComponentGVO();
		if (builtInComponentGVO != null) {
			key = builtInComponentGVO.getComponentIdUUID();
		}
		widgets = ComponentRepository.getInstance().getComponentById(key);
		if (!QAMLUtil.isEmpty(widgets)) {
			for (UIObject widget : widgets) {
				if (widget instanceof HasData) {
					HasData hasData = (HasData)widget;
					setData(hasData, dataContainerGVO, defaultData, action, mapping);
				}
			}
		} else {
			// Determine whether the view has components,
			// if not stop processing because there are no components at all
			String viewKey = ComponentRepository.getInstance().generateViewKey(windowId, context);
			Map<UIObject,ComponentGVO> components = ComponentRepository.getInstance().getComponents(viewKey);
			if (components == null) {
				return;
			}
			
			// Get components based on the name
			key = setValueGVO.getNamedComponentId();
			widgets = ComponentRepository.getInstance().getComponentByName(key);
			if (!QAMLUtil.isEmpty(widgets)) {
				for (UIObject widget : widgets) {
					if (widget instanceof HasWidgets) {
						HasWidgets hasWidgets = (HasWidgets)widget;
						setData(hasWidgets, dataContainerGVO, defaultData, action, mapping, components);
					} else if (widget instanceof HasData) {
						HasData hasData = (HasData)widget;
						setData(hasData, dataContainerGVO, defaultData, action, mapping);
					}
				}
			} else {
				// Get components based on the group
				key = setValueGVO.getGroup();
				widgets = ComponentRepository.getInstance().getComponentByGroup(key);
				setData(widgets, dataContainerGVO, defaultData, action, mapping, components);
			}
		}
	}
	
	public static void setValue(Iterable<? extends UIObject> widget, Object value, Object defaultData, String action, Map<String,String> mapping, Map<UIObject,ComponentGVO> components) {
		DataContainerGVO dataContainerGVO = createDataContainerGVO(value);
		setData(widget, dataContainerGVO, defaultData, action, mapping, components);
	}
	
	public static void setValue(HasData widget, Object value, Object defaultData, String action, Map<String,String> mapping) {
		DataContainerGVO dataContainerGVO = createDataContainerGVO(value);
		setData(widget, dataContainerGVO, defaultData, action, mapping);
	}
	
	private static void setData(Iterable<? extends UIObject> widget, DataContainerGVO dataContainerGVO, Object defaultData, String action, Map<String,String> mapping, Map<UIObject,ComponentGVO> components) {
		if (widget == null) {
			return;
		}
		if (dataContainerGVO == null) {
			return;
		}
		if (dataContainerGVO.getKind() != DataContainerGVO.KIND_MAP) {
			return;
		}
		DataMap dataMap = dataContainerGVO.getDataMap();
		if (dataMap == null) {
			return;
		}
		for (UIObject childWidget : widget) {
			String childReference = null;
			ComponentGVO childComponent = components.get(childWidget);
			if (childComponent == null) {
				// If childComponent is a container look inside
				if (childWidget instanceof HasWidgets) {
					HasWidgets hasWidgets = (HasWidgets)childWidget;
					setData(hasWidgets, dataContainerGVO, defaultData, action, mapping, components);
				}
				continue;
			}
			childReference = childComponent.getFieldName();
			DataContainerGVO childDataContainerGVO = dataMap.get(childReference);
			if (childWidget instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets)childWidget;
				setData(hasWidgets, childDataContainerGVO, defaultData, action, mapping, components);
			} else if ((childReference != null) && (childWidget instanceof HasData)) {
				HasData hasData = (HasData)childWidget;
				setData(hasData, childDataContainerGVO, defaultData, action, mapping);
			}
		}
	}
	
	private static void setData(HasData widget, DataContainerGVO dataContainerGVO, Object defaultData, String action, Map<String,String> mapping) {
		if (widget == null) {
			return;
		}
		Object newData = null;
		if (dataContainerGVO != null) {
			switch (dataContainerGVO.getKind()) {
				case DataContainerGVO.KIND_STRING:
				case DataContainerGVO.KIND_MAP: {
					Object oldData = widget.getModel();
					newData = resolveData(dataContainerGVO, oldData, action, mapping);
				} break;
				case DataContainerGVO.KIND_COLLECTION: {
					List list = new ArrayList();
					for (DataContainerGVO itemContainerGVO : dataContainerGVO.getListofDC()) {
						Object data = resolveData(itemContainerGVO, null, action, mapping);
						list.add(data);
					}
					Object oldData = null;
					if (SetValueGVO.ACTION_ADD.equals(action)) {
						oldData = widget.getModel();
					}
					if (oldData != null) {
						if (oldData instanceof List) {
							List oldList = (List)oldData;
							oldList.addAll(list);
							list = oldList;
						} else {
							list.add(0, oldData); 
						}
					}
					newData = list;
				} break;
				case DataContainerGVO.KIND_VALUE: {
					newData = defaultData;
				} break;
			}	
		}	
		widget.setData(newData);
	}
	
	private static Object resolveData(DataContainerGVO dataContainerGVO, Object oldData, String action, Map<String,String> mapping) {
		Object data = null;
		switch (dataContainerGVO.getKind()) {
			case DataContainerGVO.KIND_STRING: {
				data = dataContainerGVO.getDataString();
				if (dataContainerGVO.getStringDataType() == DataContainerGVO.TYPE_DATE) {
					data = dataContainerGVO.getDateData();
				}
			} break;
			case DataContainerGVO.KIND_MAP: {
				data = dataContainerGVO.getDataMap();
			} break;
		}
		Object newData = resolveData(data, oldData, action, mapping);
		return newData;
	}
	
	private static Object resolveData(Object data, Object oldData, String action, Map<String,String> mapping) {
		Object newData = resolveMapping(data, mapping);
		if (SetValueGVO.ACTION_ADD.equals(action)) {
			if (oldData instanceof Map) {
				List list = new ArrayList();
				list.add(oldData);
				newData = list;
			} else if (oldData instanceof List) {
				newData = oldData;
			}
			if (newData instanceof List) {
				if (!(data instanceof List)) {
					((List)newData).add(data);
				}
			} 
		}
		return newData;
	}
	
	private static Object resolveMapping(Object data, Map<String,String> mapping) {
		if ((mapping == null) || (mapping.size() == 0)) {
			return data;
		}
		if (data instanceof Map) {
			Map dataMap = (Map)data;
			Object newData = null;
			if (mapping.size() > 1) {
				newData = new HashMap();
			}
			Iterator<String> itrKey = mapping.keySet().iterator();
			while (itrKey.hasNext()) {
				String key = itrKey.next();
				String keyValue = mapping.get(key);
				Object value = dataMap.get(keyValue);
				if (value instanceof DataContainerGVO) {
					value = resolveData((DataContainerGVO)value, null, null, null);
				}
				if (newData instanceof Map) {
					((Map)newData).put(key, value);
				} else {
					newData = value;
				}
			}
			data = newData;
		}
		return data;
	}
	
	private static String getAttributeValue(String reference, String windowId, String context) {
		String value = "";
		if (reference == null) {
			return value;
		}
		int prefixPropertyIndex = reference.indexOf(QAMLConstants.PREFIX_PROPERTY);
		String component = reference.substring(0, prefixPropertyIndex);
		String attribute = reference.substring(prefixPropertyIndex + QAMLConstants.PREFIX_PROPERTY.length());
		String key = ComponentRepository.getInstance().generateComponentKey(component, windowId, context);
		List<UIObject> widgets = ComponentRepository.getInstance().getComponentById(key);
		if (widgets == null) {
			return value;
		}
		Object attributeValue = null;
		for (UIObject widget : widgets) {
			if (widget instanceof HasAttribute) {
				HasAttribute hasAttribute = (HasAttribute)widget;
				attributeValue = hasAttribute.getAttribute(attribute);
				break;
			}
		}
		if (attributeValue != null) {
			value = attributeValue.toString();
		}
		return value;
	}
	
	private static boolean hasAttribute(String reference) {
		if (reference == null) {
			return false;
		}
		return reference.indexOf(QAMLConstants.PREFIX_PROPERTY) > -1;
	}
	
	private static Map<String,String> getTouchInput(Event event) {
		Map<String,String> touchInput = null;
		if (event instanceof TouchEvent) {
			TouchEvent touchEvent = (TouchEvent)event;
			int posX = -1;
			int posY = -1;
			Object object = touchEvent.getTouches().get(0);
			if (object instanceof Touch) {
				Touch touch = (Touch)object;
				posX = touch.getPageX();
				posY = touch.getPageY();
			}
			touchInput = new HashMap<String,String>();
			touchInput.put(EventDataI.MOUSE_X, String.valueOf(posX));
			touchInput.put(EventDataI.MOUSE_Y, String.valueOf(posY));
		} else if (event instanceof TapEvent) {
			TapEvent tapEvent = (TapEvent)event;
			int posX = tapEvent.getStartX();
			int posY = tapEvent.getStartY();
			touchInput = new HashMap<String,String>();
			touchInput.put(EventDataI.MOUSE_X, String.valueOf(posX));
			touchInput.put(EventDataI.MOUSE_Y, String.valueOf(posY));
		}
		return touchInput;
	}
	
	private static void handleRequiredFieldException(RequiredFieldException e) {
		String title = e.getTitle();
		if (title == null){
			title = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_TITLE;
		}
		
		String message = e.getMessage();
		if (message == null){
			message = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_MESSAGE;
		}
		ClientApplicationContext.getInstance().log(title, message, true, true, null);
	}
}