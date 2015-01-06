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
package com.qualogy.qafe.mgwt.client.ui.renderer.events;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO;

public class EventFactory {

	private EventFactory() {
	}

//	public static SelectionHandler createTreeClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new SelectionHandler() {
//			public void onSelection(SelectionEvent event) {
//				CallbackHandler.createCallBack(event.getSource(), "onclick", ev, input);
//
//			}
//
//		};
//	}
//
//	public static MouseMoveHandler createOnMouseMoveListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new MouseMoveHandler() {
//
//			public void onMouseMove(MouseMoveEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onmouse-move", ev, input, mouseInfo);
//				
//			}
//		};
//	}
//
//	public static MouseUpHandler createOnMouseUpListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new MouseUpHandler() {
//
//			public void onMouseUp(MouseUpEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onmouse-up", ev, input, mouseInfo);
//
//			}
//		};
//	}
//
//	public static MouseDownHandler createOnMouseDownListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new MouseDownHandler() {
//
//			public void onMouseDown(MouseDownEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onmouse-down", ev, input, mouseInfo);
//
//			}
//
//		};
//	}
//
//	public static MouseOverHandler createOnMouseEnterListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new MouseOverHandler() {
//
//			public void onMouseOver(MouseOverEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onmouse-enter", ev, input, mouseInfo);
//
//			}
//
//		};
//	}
//
//	public static MouseOutHandler createOnMouseExitListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new MouseOutHandler() {
//
//			public void onMouseOut(MouseOutEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onmouse-exit", ev, input, mouseInfo);
//
//			}
//
//		};
//	}
//
//	public static Command createCommandListener(final UIObject sender, final EventListenerGVO event, final List<InputVariableGVO> input) {
//		return new Command() {
//
//			public void execute() {
//				CallbackHandler.createCallBack(sender, "onclick", event, input);
//
//			}
//
//		};
//	}
//
//	public static ClickHandler createClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "onclick", ev, input, mouseInfo);
//				
//			}
//		};
//
//	}
//	public static DoubleClickHandler createDoubleClickListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new DoubleClickHandler() {
//
//			public void onDoubleClick(DoubleClickEvent event) {
//				Map<String,String> mouseInfo = getMouseInfo(event);
//				CallbackHandler.createCallBack(event.getSource(), "ondblclick", ev, input, mouseInfo);
//				
//			}
//		};
//
//	}
//
//	public static FocusHandler createFocusListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new FocusHandler() {
//
//			public void onFocus(FocusEvent event) {
//				CallbackHandler.createCallBack(event.getSource(), "onfocus", ev, input);
//
//			}
//
//		};
//	}
//
//	public static BlurHandler createOnExitListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new BlurHandler() {
//			public void onBlur(BlurEvent event) {
//				CallbackHandler.createCallBack(event.getSource(), "onexit", ev, input);
//
//			}
//		};
//	}
//
//	public static KeyDownHandler createOnEnterListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new KeyDownHandler() {
//			public void onKeyDown(KeyDownEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					CallbackHandler.createCallBack(event.getSource(), "onenter", ev, input);
//				}
//
//			}
//		};
//
//	}
//
//	// public static KeyboardListener createOnEnterListener(final
//	// EventListenerGVO event, final List<InputVariableGVO> input) {
//	// return new KeyboardListenerAdapter() {
//	//
//	// public void onKeyPress(Widget sender, char keyCode, int modifiers) {
//	// if (keyCode == KEY_ENTER) {
//	//				
//	// }
//	//
//	// }
//	//
//	// };
//	// }
//
//	public static KeyPressHandler createOnKeyPressListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new KeyPressHandler() {
//
//			public void onKeyPress(KeyPressEvent event) {
//				if (ev.getParameterList() != null) {
//					Iterator<ParameterGVO> itr = ev.getParameterList().iterator();
//					while (itr.hasNext()) {
//						ParameterGVO parameter = itr.next();
//						if (parameter != null) {
//							// Check if the name of the parameter is "keys"
//							if ("keys".equals(parameter.getName())) {
//								if (Character.toString(event.getCharCode()).equals(parameter.getValue())) {
//									CallbackHandler.createCallBack(event.getSource(), "onkeypress", ev, input);
//								}
//							}
//						}
//					}
//				}
//
//			}
//
//		};
//	}
//
//	public static KeyDownHandler createOnKeyDownListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new KeyDownHandler() {
//
//			public void onKeyDown(KeyDownEvent event) {
//				if (ev.getParameterList() != null) {
//					Iterator<ParameterGVO> itr = ev.getParameterList().iterator();
//					while (itr.hasNext()) {
//						ParameterGVO parameter = itr.next();
//						if (parameter != null) {
//							if (KeyBoardHelper.isKeyInput(parameter.getName(), parameter.getValue(), event.getNativeKeyCode())) {
//								CallbackHandler.createCallBack(event.getSource(), "onkeydown", ev, input);
//							}
//						}
//					}
//				}
//			}
//		};
//	}
//
//	public static KeyUpHandler createOnKeyUpListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new KeyUpHandler() {
//			public void onKeyUp(KeyUpEvent event) {
//				if (ev.getParameterList() != null) {
//					Iterator<ParameterGVO> itr = ev.getParameterList().iterator();
//					while (itr.hasNext()) {
//						ParameterGVO parameter = itr.next();
//						if (parameter != null) {
//							if (KeyBoardHelper.isKeyInput(parameter.getName(), parameter.getValue(), event.getNativeKeyCode())) {
//								CallbackHandler.createCallBack(event.getSource(), "onkeyup", ev, input);
//							}
//						}
//					}
//				}
//
//			}
//		};
//	}
//
//	public static ChangeHandler createOnChangeListener(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new ChangeHandler() {
//
//			public void onChange(ChangeEvent event) {
//				CallbackHandler.createCallBack(event.getSource(), "onchange", ev, input);
//
//			}
//		};
//	}
//
//	public static void processOnLoadEvent(ComponentGVO windowGVO, Widget sender) {
//		if (windowGVO != null) {
//			if (windowGVO instanceof WindowGVO || windowGVO instanceof RootPanelGVO) {
//				EventListenerGVO[] events = windowGVO.getEvents();
//				if (events != null) {
//					for (int i = 0; i < events.length; i++) {
//						if (events[i] instanceof OnLoadEventListenerGVO) {
//							CallbackHandler.createCallBack(sender, "onload", events[i], events[i].getInputvariablesList());
//						}
//					}
//				}
//			}
//		}
//
//	}
//
//	
//
//	
//	
//
//	public static SelectionHandler<Integer> createTabPanelListener(final TabPanel tabPanel, final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new SelectionHandler<Integer>() {
//
//			public void onSelection(SelectionEvent<Integer> event) {
//
//				CallbackHandler.createCallBack(event.getSelectedItem(), "onclick", ev, input);
//
//			}
//
//		};
//	}
//
//	
//
//	
//	
//	public static KeyDownHandler createSuggestionHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
//
//		return new KeyDownHandler() {
//
//			public void onKeyDown(KeyDownEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
////					if (event.getSource() instanceof QSuggestBox) {
////						QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
////						String expression = qSuggestBox.getText();
////						if (expression != null) {
////							if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
////								qSuggestBox.clearSuggestions();
////								CallbackHandler.createCallBack(sender, "onchange", ev, input);
////							}
////
////						}
////					}
//				}
//			}
//
//		};
//
//	}
//
//	public static KeyUpHandler createSuggestionOnKeyUpHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
//		return new KeyUpHandler() {
//
//			public void onKeyUp(KeyUpEvent event) {
////				if (event.getSource() instanceof QSuggestBox) {
////					QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
////					String expression = qSuggestBox.getText();
////					if (expression != null) {
////						if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
////							qSuggestBox.clearSuggestions();
////							CallbackHandler.createCallBack(sender, "onchange", ev, input);
////						}
////
////					}
////				}
//
//			}
//
//		};
//	}
//
//	public static ValueChangeHandler<String> createSuggestionChangeHandler(final UIObject sender, final EventListenerGVO ev, final List<InputVariableGVO> input) {
//
//		return new ValueChangeHandler<String>() {
//
//			public void onValueChange(ValueChangeEvent<String> event) {
////				if (event.getSource() instanceof QSuggestBox) {
////					QSuggestBox qSuggestBox = (QSuggestBox) event.getSource();
////					String expression = qSuggestBox.getText();
////					if (expression != null) {
////						if (expression.length() >= qSuggestBox.getSuggestCharactersLength()) {
////							qSuggestBox.clearSuggestions();
////							CallbackHandler.createCallBack(sender, "onchange", ev, input);
////						}
////
////					}
////				}
//
//			}
//
//		};
//
//	}
//	
//	public static ValueChangeHandler<String> createOnValueChangeHandler(final EventListenerGVO ev, final List<InputVariableGVO> input) {
//
//		return new ValueChangeHandler<String>() {
//
//			public void onValueChange(ValueChangeEvent event) {
//				CallbackHandler.createCallBack(event.getSource(), "onchange", ev, input);
//			}
//
//		};
//
//	}
//	
////	public static DataChangeHandler createOnDataChangeHandler(final EventListenerGVO ev, final List<InputVariableGVO> input) {
////
////		return new DataChangeHandler() {
////			
////			public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
////				CallbackHandler.createCallBack(uiObject, "onchange", ev, input);
////			}
////
////		};
////
////	}
//	
	public static Timer createTimerListener(final UIObject sender, final EventListenerGVO event, final List<InputVariableGVO> input) {
		return new Timer() {
			public void run() { 
				CallbackHandler.createCallBack(sender, "ontimer", event, input); 
			}
		};
	}
//
//	public static SubmitCompleteHandler createSubmitCompleteHandler(final UIObject ui, final EventListenerGVO ev, final List<InputVariableGVO> inputVariables) {
//
//		SubmitCompleteHandler submitCompleteHandler = new SubmitCompleteHandler() {
//			public void onSubmitComplete(SubmitCompleteEvent event) {
//				String uuId = event.getResults();
//				boolean success = false;
//				if (uuId != null && uuId.indexOf("=") > 0) {
//					uuId = uuId.substring(uuId.indexOf("=") + 1);
//					success = true;
//				}
//				FormPanel fp = (FormPanel) ui;
//				if (fp instanceof HasWidgets) {
//					HasWidgets hasWidgets = (HasWidgets) fp;
//					Iterator<Widget> itr = hasWidgets.iterator();
//					while (itr.hasNext()) {
//						Widget widget = itr.next();
//						if (widget instanceof Grid) {
//							Grid gridPanel = (Grid) widget;
//							FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
//							if (success) {
//								DOM.setElementAttribute(fileUpload.getElement(), "fu-uuid", uuId);
//								CallbackHandler.createCallBack(ui, "onfinish", ev, inputVariables);
//							} else {
//								Label fileNameLabel = new Label("Uploading unsuccessfull.");// (Hyperlink)
//																							// gridPanel.getWidget(1,
//																							// 0);
//								fileNameLabel.setText("Uploading unsuccessfull.");
//								fileNameLabel.setVisible(true);
//								gridPanel.add(fileNameLabel);
//							}
//						}
//					}
//				}
//			}
//		};
//		return submitCompleteHandler;
//	}
//
//	public static ChangeListener createLegacyOnChangeListener(final 
//			EventListenerGVO event, final List<InputVariableGVO> inputVariables) {
//		return new ChangeListener(){
//
//			public void onChange(Widget sender) {
//				CallbackHandler.createCallBack(sender, "onchange", event, inputVariables);
//				
//			}}; 
//		
//	}
//
//	private static Map<String,String> getMouseInfo(MouseEvent event) {
//		Map<String,String> mouseInfo = new HashMap<String,String>();
//		int posX = -1;
//		int posY = -1;
//		try {
//			// In QTree class a SelectionEvent is translated to a ClickEvent,
//			// so position X and Y are not present (nativeEvent is null, is used to get the clientX and clientY)
//			posX = event.getClientX();
//			posY = event.getClientY();			
//		} catch (Exception e) {
//			// Ignore
//		}
//		mouseInfo.put(EventDataI.MOUSE_X, String.valueOf(posX));
//		mouseInfo.put(EventDataI.MOUSE_Y, String.valueOf(posY));
//		return mouseInfo;
//	}
}
