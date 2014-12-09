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
package com.qualogy.qafe.gwt.client.ui.renderer.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;
import org.gwt.mosaic.ui.client.WindowPanel.WindowStateListener;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DataChangeHandler;
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.data.EventDataI;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnLoadEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnUnLoadEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class EventFactory {
	private EventFactory() {
	}

	public static SelectionHandler createTreeClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new SelectionHandler() {
			public void onSelection(SelectionEvent event) {
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONCLICK, ev, input);
			}
		};
	}

	public static MouseMoveHandler createOnMouseMoveListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONMOUSE_MOVE, ev, input, mouseInfo);
			}
		};
	}

	public static MouseUpHandler createOnMouseUpListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONMOUSE_UP, ev, input, mouseInfo);
			}
		};
	}

	public static MouseDownHandler createOnMouseDownListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONMOUSE_DOWN, ev, input, mouseInfo);
			}
		};
	}

	public static MouseOverHandler createOnMouseEnterListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONMOUSE_ENTER, ev, input, mouseInfo);
			}
		};
	}

	public static MouseOutHandler createOnMouseExitListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONMOUSE_EXIT, ev, input, mouseInfo);
			}
		};
	}

	public static Command createCommandListener(final UIObject sender, final EventListenerGVO event, final List<InputVariableGVO> input) {
		return new Command() {
			public void execute() {
				CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONCLICK, event, input);
			}
		};
	}

	public static ClickHandler createClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new ClickHandler() {
			public void onClick(ClickEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONCLICK, ev, input, mouseInfo);
			}
		};
	}
	
	public static DoubleClickHandler createDoubleClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
				Map<String,String> mouseInfo = getMouseInfo(event);
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONDBLCLICK, ev, input, mouseInfo);
			}
		};
	}

	public static FocusHandler createFocusListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new FocusHandler() {
			public void onFocus(FocusEvent event) {
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONFOCUS, ev, input);
			}
		};
	}

	public static BlurHandler createOnExitListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new BlurHandler() {
			public void onBlur(BlurEvent event) {
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONEXIT, ev, input);
			}
		};
	}

	public static KeyDownHandler createOnEnterListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONENTER, ev, input);
				}
			}
		};
	}

	public static KeyPressHandler createOnKeyPressListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (ev.getParameterList() != null) {
					Iterator<ParameterGVO> itr = ev.getParameterList().iterator();
					while (itr.hasNext()) {
						ParameterGVO parameter = itr.next();
						if (parameter != null) {
							if (KeyBoardHelper.isKeyInput(parameter.getName(), parameter.getValue(), Character.toString(event.getCharCode()))) {
								CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONKEYPRESS, ev, input);
							}
						}
					}
				}
			}
		};
	}

	public static KeyDownHandler createOnKeyDownListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				handleKeyInput(event.getSource(), event.getNativeEvent(), QAMLConstants.EVENT_ONKEYDOWN, ev, input);
			}
		};
	}

	public static KeyUpHandler createOnKeyUpListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				handleKeyInput(event.getSource(), event.getNativeEvent(), QAMLConstants.EVENT_ONKEYUP, ev, input);
			}
		};
	}

	public static NativeKeyDownHandler createOnNativeKeyDownListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new NativeKeyDownHandler() {
			public void onNativeKeyDown(NativeKeyDownEvent event) {
				handleKeyInput(event.getSource(), event.getNativeEvent(), QAMLConstants.EVENT_ONKEYDOWN, ev, input);
			}
		};
	}
	
	public static NativeKeyUpHandler createOnNativeKeyUpListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new NativeKeyUpHandler() {
			public void onNativeKeyUp(NativeKeyUpEvent event) {
				handleKeyInput(event.getSource(), event.getNativeEvent(), QAMLConstants.EVENT_ONKEYUP, ev, input);
			}
		};
	}

	public static ChangeHandler createOnChangeListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONCHANGE, ev, input);
			}
		};
	}

	public static void processOnLoadEvent(ComponentGVO windowGVO, Widget sender) {
		if (windowGVO != null) {
			if ((windowGVO instanceof WindowGVO) || (windowGVO instanceof RootPanelGVO)) {
				EventListenerGVO[] events = windowGVO.getEvents();
				if (events != null) {
					for (int i=0; i<events.length; i++) {
						if (events[i] instanceof OnLoadEventListenerGVO) {
							CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONLOAD, events[i], events[i].getInputvariablesList());
						}
					}
				}
			}
		}
	}

	public static void createMenuCloseEvent(final String uuid, final WindowPanel sender) {
		sender.addWindowClosingHandler(new ClosingHandler() {
			public void onWindowClosing(ClosingEvent event) {
				if (Window.confirm("Are you sure you want to close the application and all of its windows ?")) {
					MainFactoryActions.remove(uuid);
					ComponentRepository.getInstance().removeAllItemsForWindow(uuid, null);
					// ClientApplicationContext.getInstance().closeAllWindowsForUUID(uuid);
					// return true;
				} else {
					// return false;
				}
				Window.prompt("Are you sure you want to close the application and all of its windows ?", "X");
			}
		});
	}

	public static SelectionHandler<Integer> createTabPanelListener(final TabPanel tabPanel, final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				CallbackHandler.createCallBack(event.getSelectedItem(), QAMLConstants.EVENT_ONCLICK, ev, input);
			}
		};
	}

	public static void createWindowSizeEvent(final String uuid, final String window, final WindowPanel sender) {
		sender.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
				int width = event.getWidth();
				if (!("" + height).equals(Cookies.getCookie(uuid + "-height-" + window)) || !("" + width).equals(Cookies.getCookie(uuid + "-width-" + window))) {
					
					int absoluteLeft = sender.getAbsoluteLeft();
					int absoluteTop = sender.getAbsoluteTop();
					
					Cookies.setCookie(uuid + "-left" + "-" + window, String.valueOf(absoluteLeft));
					Cookies.setCookie(uuid + "-top" + "-" + window, String.valueOf(absoluteTop));
					sender.getWidget().setHeight(height + "");
					sender.getWidget().setWidth(width + "");

						if (sender.getWidget() != null && (sender.getWidget()) instanceof QRootPanel) {
							QRootPanel qRootPanel = (QRootPanel) (sender.getWidget());
							if (qRootPanel.getRootPanel() != null) {
								qRootPanel.getRootPanel().setHeight(height + "");
								qRootPanel.getRootPanel().setWidth(width + "");
							}
							ScrollPanel sp = (ScrollPanel) qRootPanel.getRootPanel();
							int menuHeight = 0;
							int toolbarHeight = 0;
							int extra = 1;
							if(qRootPanel.getMenuBar() != null && qRootPanel.getToolbar() == null){
								menuHeight = qRootPanel.getMenuBar().getOffsetHeight();
								extra = 10;
							}
							if(qRootPanel.getToolbar() != null && qRootPanel.getMenuBar() == null){
								toolbarHeight = qRootPanel.getToolbar().getOffsetHeight();
								extra = -1;
							}
							if(qRootPanel.getToolbar() != null && qRootPanel.getMenuBar() != null){
								extra = 58;
							}
							int headerHeight =  sender.getHeader().getOffsetHeight();
							sp.setHeight((height-menuHeight-toolbarHeight-headerHeight-extra)+"");
							sp.setWidth(width+"");
						}
						// It is not necessary to pack the window after changing
						// the
						// width and/or the height
						// sender.pack();
//					}
				}
			}
		});
	}

	/**
	 * This method is used to add a closing event handler to the WindowPanel.
	 * The panel is removed from the windows object & mapOfWindows object in the
	 * application context object.
	 * 
	 * @param ComponentGVO
	 *            windowGVO
	 * @param WindowPanel
	 *            sender
	 * @param String
	 *            uuid
	 * */
	public static void createDefaultCloseEvent(final ComponentGVO windowGVO, final WindowPanel sender, final String windowSession, final String uuid) {
		if (windowGVO != null) {
			if (windowGVO instanceof WindowGVO || windowGVO instanceof RootPanelGVO) {
				sender.addCloseHandler(new CloseHandler<PopupPanel>() {
					public void onClose(CloseEvent<PopupPanel> event) {
						ClientApplicationContext.getInstance().fireWindowClose(uuid, windowGVO.getId());
						ClientApplicationContext.getInstance().getWindows().remove(sender);
						ClientApplicationContext.getInstance().removeFromMapOfWindows(uuid, windowGVO.getId());
						if (!ClientApplicationContext.getInstance().isClientSideEventEnabled()) {
							MainFactoryActions.removeWindowsEventData(windowSession, windowGVO.getId());
						}
						manageGloballyStoredData(windowGVO, windowSession);
					}

					private void manageGloballyStoredData(final ComponentGVO windowGVO, final String windowSession) {
						boolean removeGloballyStoredData = true;
						List<WindowPanel> existingWindows = ClientApplicationContext.getInstance().getWindows();
						if(existingWindows.size() > 0){
							for(WindowPanel window: existingWindows){
								String existingWindowsAppContext = DOM.getElementAttribute(window.getElement(), "app-context");
								String closingWindowsAppContext = windowGVO.getContext();
								if(existingWindowsAppContext.equals(closingWindowsAppContext)) {
									removeGloballyStoredData = false;
									break;
								}
							}
						}
						if(removeGloballyStoredData) { // Will happen when last window is closed.
							// Removing data stored with target = "global".
							if (!ClientApplicationContext.getInstance().isClientSideEventEnabled()) {
								MainFactoryActions.removeGloballyStoredData(windowSession, windowGVO.getContext());
							}
						}
					}
				});
			}
		}
	}

	public static void createOnUnLoadEvent(ComponentGVO windowGVO, final WindowPanel sender) {
		if (windowGVO != null) {
			if (windowGVO instanceof WindowGVO || windowGVO instanceof RootPanelGVO) {
				EventListenerGVO[] events = windowGVO.getEvents();
				if (events != null) {
					for (int i = 0; i < events.length; i++) {
						if (events[i] instanceof OnUnLoadEventListenerGVO) {
						}
					}
				}
			}
		}
	}

	/**
	 * This method adds a state change listener to a window panel and is used to
	 * manage the minimizing and restoring of the window panel
	 * 
	 * @param WindowPanel
	 *            sender
	 * @param String
	 *            windowTitle
	 * @param String
	 *            uuId
	 * @param String
	 *            windowId
	 * */
	public static void createWindowStateChangeListener(final WindowPanel sender, final String windowTitle, final String uuId, final String windowId) {
		sender.addWindowStateListener(new WindowStateListener() {
			final Label label = new Label(windowTitle);

			// public void onWindowStateChange(final WindowPanel sender) {
			//			
			// }
			public void onWindowStateChange(final WindowPanel sender, WindowState oldWindowState, WindowState newWindowState) {
				if (ClientApplicationContext.getInstance().isMDI()) {
					if (sender.getWindowState().equals(WindowState.MINIMIZED)) {
						label.setTitle(windowTitle);
						label.setText(windowTitle + " |");
						label.setStyleName("qafe-minimizedPanelContent");
						label.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								sender.setWindowState(WindowState.NORMAL);
								sender.show();
								sender.toFront();
								ClientApplicationContext.getInstance().getHorizontalPanel().remove(label);
							}
						});
						ClientApplicationContext.getInstance().getHorizontalPanel().add(label);
					}
					sender.show();
				}
			}
		});
	}

	public static KeyDownHandler createSuggestionHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (event.getSource() instanceof QSuggestBox) {
						QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
						String expression = qSuggestBox.getText();
						if (expression != null) {
							if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
								qSuggestBox.clearSuggestions();
								CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONCHANGE, ev, input);
							}
						}
					}
				}
			}
		};
	}

	public static KeyUpHandler createSuggestionOnKeyUpHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getSource() instanceof QSuggestBox) {
					QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
					String expression = qSuggestBox.getText();
					if (expression != null) {
						if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
							qSuggestBox.clearSuggestions();
							CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONCHANGE, ev, input);
						}
					}
				}
			}
		};
	}

	public static ValueChangeHandler<String> createSuggestionChangeHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if (event.getSource() instanceof QSuggestBox) {
					QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
					String expression = qSuggestBox.getText();
					if (expression != null) {
						if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
							qSuggestBox.clearSuggestions();
							CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONCHANGE, ev, input);
						}
					}
				}
			}
		};
	}
	
	public static ValueChangeHandler<String> createOnValueChangeHandler(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent event) {
				CallbackHandler.createCallBack(event.getSource(), QAMLConstants.EVENT_ONCHANGE, ev, input);
			}
		};
	}
	
	public static DataChangeHandler createOnDataChangeHandler(final EventListenerGVO ev, final List<InputVariableGVO> input) {
		return new DataChangeHandler() {
			public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
				CallbackHandler.createCallBack(uiObject, QAMLConstants.EVENT_ONCHANGE, ev, input);
			}
		};
	}
	
	public static Timer createTimerListener(final UIObject sender, final EventListenerGVO event, final List<InputVariableGVO> input) {
		return new Timer() {
			public void run() { 
				CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONTIMER, event, input); 
			}
		};
	}

	public static SubmitCompleteHandler createSubmitCompleteHandler(final UIObject ui, final EventListenerGVO ev, final List<InputVariableGVO> inputVariables) {
		SubmitCompleteHandler submitCompleteHandler = new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String uuId = event.getResults();
				boolean success = false;
				if ((uuId != null) && (uuId.indexOf("=") > 0)) {
					uuId = uuId.substring(uuId.indexOf("=") + 1);
					success = true;
				}
				FormPanel fp = (FormPanel) ui;
				if (fp instanceof HasWidgets) {
					HasWidgets hasWidgets = (HasWidgets) fp;
					Iterator<Widget> itr = hasWidgets.iterator();
					while (itr.hasNext()) {
						Widget widget = itr.next();
						if (widget instanceof Grid) {
							Grid gridPanel = (Grid) widget;
							FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
							if (success) {
								DOM.setElementAttribute(fileUpload.getElement(), "fu-uuid", uuId);
								CallbackHandler.createCallBack(ui, QAMLConstants.EVENT_ONFINISH, ev, inputVariables);
							} else {
								Label fileNameLabel = new Label("Uploading unsuccessfull.");// (Hyperlink)
																							// gridPanel.getWidget(1,
																							// 0);
								fileNameLabel.setText("Uploading unsuccessfull.");
								fileNameLabel.setVisible(true);
								gridPanel.add(fileNameLabel);
							}
						}
					}
				}
			}
		};
		return submitCompleteHandler;
	}

	public static ChangeListener createLegacyOnChangeListener(final EventListenerGVO event, final List<InputVariableGVO> inputVariables) {
		return new ChangeListener(){
			public void onChange(Widget sender) {
				CallbackHandler.createCallBack(sender, QAMLConstants.EVENT_ONCHANGE, event, inputVariables);
			}
		}; 
	}

	private static Map<String,String> getMouseInfo(MouseEvent event) {
		Map<String,String> mouseInfo = new HashMap<String,String>();
		int posX = -1;
		int posY = -1;
		try {
			// In QTree class a SelectionEvent is translated to a ClickEvent,
			// so position X and Y are not present (nativeEvent is null, is used to get the clientX and clientY)
			posX = event.getClientX();
			posY = event.getClientY();			
		} catch (Exception e) {
			// Ignore
		}
		mouseInfo.put(EventDataI.MOUSE_X, String.valueOf(posX));
		mouseInfo.put(EventDataI.MOUSE_Y, String.valueOf(posY));
		return mouseInfo;
	}
	
	private static void handleKeyInput(final Object source, final NativeEvent event, final String listenerType, final EventListenerGVO eventGVO, final List<InputVariableGVO> input) {
		if (eventGVO.getParameterList() != null) {
			Iterator<ParameterGVO> itrParameterGVO = eventGVO.getParameterList().iterator();
			while (itrParameterGVO.hasNext()) {
				ParameterGVO parameterGVO = itrParameterGVO.next();
				if (parameterGVO != null) {
					if (KeyBoardHelper.isKeyInput(parameterGVO.getName(), parameterGVO.getValue(), event)) {
						CallbackHandler.createCallBack(source, listenerType, eventGVO, input);
					}
				}
			}
		}
	}
}