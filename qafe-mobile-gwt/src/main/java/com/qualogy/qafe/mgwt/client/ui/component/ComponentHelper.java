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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeEvent;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomEvent;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.exception.RequiredFieldException;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequired;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequiredValidationMessage;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public abstract class ComponentHelper {

	public static final String STYLE_DISABLED			= "qafe_item_disabled";
	public static final String STYLE_SHOWPANEL_CLOSE	= "qafe_close_on_showPanel";
	
	private static TouchWidgetImpl touchWidgetImpl = GWT.create(TouchWidgetImpl.class);
	
	public static HandlerRegistration addDataChangeHandler(Widget widget, DataChangeHandler handler) {
		return widget.addHandler(handler, DataChangeEvent.getType());
	}
	
	public static void handleDataChange(Widget widget, Object newData, Object oldData) {
		if (isDataChanged(newData, oldData)) {
			fireDataChange(widget, newData, oldData);
		}
	}
	
	public static boolean isDataChanged(Object newData, Object oldData) {
		return (oldData != newData) && (oldData == null || !oldData.equals(newData));
	}
	
	public static void fireDataChange(Widget widget, Object newData, Object oldData) {
		DataChangeEvent event = new DataChangeEvent(widget, newData, oldData);
		widget.fireEvent(event);
	}
	
	public static HandlerRegistration addTouchStartHandler(Widget widget, TouchStartHandler handler) {
		return touchWidgetImpl.addTouchStartHandler(widget, handler);
	}

	public static HandlerRegistration addTouchMoveHandler(Widget widget, TouchMoveHandler handler) {
		return touchWidgetImpl.addTouchMoveHandler(widget, handler);
	}

	public static HandlerRegistration addTouchCancelHandler(Widget widget, TouchCancelHandler handler) {
		return touchWidgetImpl.addTouchCancelHandler(widget, handler);
	}

	public static HandlerRegistration addTouchEndHandler(Widget widget, TouchEndHandler handler) {
		return touchWidgetImpl.addTouchEndHandler(widget, handler);
	}
	
	public static HandlerRegistration addTouchHandler(Widget widget, TouchHandler handler) {
		HandlerRegistrationCollection handlerRegistrationCollection = new HandlerRegistrationCollection();
		handlerRegistrationCollection.addHandlerRegistration(addTouchCancelHandler(widget, handler));
		handlerRegistrationCollection.addHandlerRegistration(addTouchStartHandler(widget, handler));
		handlerRegistrationCollection.addHandlerRegistration(addTouchEndHandler(widget, handler));
		handlerRegistrationCollection.addHandlerRegistration(addTouchMoveHandler(widget, handler));
		return handlerRegistrationCollection;
	}

	public static void setStyle(UIObject widget, String styleClass, String[][] inlineStyles) {
		List<UIObject> styleWidgets = getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			setStyle(styleWidget.getElement(), styleClass, inlineStyles);
		}
	}
	
	public static void setStyle(UIObject widget, String attribute, String value) {
		List<UIObject> styleWidgets = getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			setStyle(styleWidget.getElement(), attribute, value);
		}
	}
	
	public static void setStyle(UIObject widget, String style) {
		List<UIObject> styleWidgets = getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			setStyle(styleWidget.getElement(), style);
		}
	}
	
	public static void addStyle(UIObject widget, String style) {
		List<UIObject> styleWidgets = getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			addStyle(styleWidget.getElement(), style);
		}
	}
	
	public static void removeStyle(UIObject widget, String style) {
		List<UIObject> styleWidgets = getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			removeStyle(styleWidget.getElement(), style);
		}
	}
	
	public static void setStyle(Element element, String styleClass, String[][] inlineStyles) {
		if (element == null) {
			return;
		}
		if (styleClass != null) {
			if (!element.getClassName().contains(styleClass)) {
				addStyle(element, styleClass);
			}
		}		
		if (inlineStyles != null) {
			for (int i=0; i<inlineStyles.length; i++) {
				if (inlineStyles[i].length == 2) {
					String attribute = inlineStyles[i][0];
					String value = inlineStyles[i][1];
					setStyle(element, attribute, value);
				}
			}
		}
	}
	
	public static void setStyle(Element element, String attribute, String value) {
		if (element == null) {
			return;
		}
		attribute = QAMLUtil.camelize(attribute);
		DOM.setStyleAttribute(element, attribute, value);
	}
	
	public static void setStyle(Element element, String style) {
		if (element == null) {
			return;
		}
		element.setClassName(style);
	}
	
	public static void addStyle(Element element, String style) {
		if (element == null) {
			return;
		}
		element.addClassName(style);
	}
	
	public static void removeStyle(Element element, String style) {
		if (element == null) {
			return;
		}
		element.removeClassName(style);
	}
	
	public static List<UIObject> getStyleWidgets(UIObject widget) {
		List<UIObject> styleWidgets = new ArrayList<UIObject>();
		if (widget != null) {
			if (widget instanceof HasStyle) {
				HasStyle hasStyle = (HasStyle)widget;
				styleWidgets.addAll(hasStyle.getStyleWidgets());
			} else {
				styleWidgets.add(widget);
			}
		}
		return styleWidgets;
	}
	
	public static boolean isNonCharKeyPressed(KeyEvent event, char charCode) {
		if (event.isAnyModifierKeyDown()) {
			return true;
		}
		return (charCode == 0);
	}
	
	public static void handleKeyConstraints(HasConstraints widget, String value, KeyEvent event, char charCode) {
		if (widget == null) {
			return;
		}
		if (isNonCharKeyPressed(event, charCode)) {
			return;
		}
		List<String> constraints = widget.getKeyConstraints();
		if ((constraints == null) || (constraints.size() == 0)) {
			return;
		}
		String newValue = value + charCode;
		if (newValue == null) {
			return;
		}
		for (String constraint : constraints) {
			if (newValue.matches(constraint)) {
				continue;
			}
			event.preventDefault();
			return;
		}
	}
	
	public static HandlerRegistration registerScroll(Widget widget) {
		if (widget == null) {
			return null;
		}
		final Widget source = widget;
		HandlerRegistration handlerRegistration = null;
		while (widget.getParent() != null) {
			widget = widget.getParent();
			if (widget instanceof ScrollPanel) {
				ScrollPanel scrollPanel = (ScrollPanel)widget;
				handlerRegistration = (HandlerRegistration)scrollPanel.addScrollEndHandler(new ScrollEndEvent.Handler() {
					@Override
					public void onScrollEnd(ScrollEndEvent event) {
						if (isScrolledToBottom(source)) {
							fireScrollBottom(source);
						}
					}
				});
				break;
			}
		}
		return handlerRegistration;
	}
	
	public static void unregisterScroll(HandlerRegistration handlerRegistration) {
		if (handlerRegistration == null) {
			return;
		}
		handlerRegistration.removeHandler();
	}
	
	public static void refreshScroll(Widget widget) {
		// After setting data which results in a long list, 
		// the ScrollPanel needs to be refreshed for recalculating the size of the child to scroll,
		// otherwise the scrolling will not work properly, this is a workaround
		// see https://groups.google.com/group/mgwt/tree/browse_frm/month/2012-5/b47fc71b23b2b40e?rnum=201&_done=/group/mgwt/browse_frm/month/2012-5?scoring%3Dd%26start%3D0%26sa%3DN%26fwc%3D1%26&scoring=d&pli=1
		if (widget == null) {
			return;
		}
		while (widget.getParent() != null) {
			widget = widget.getParent();
			if (widget instanceof ScrollPanel) {
				ScrollPanel scrollPanel = (ScrollPanel)widget;
				scrollPanel.refresh();
				return;
			}
		}
	}

	public static boolean isScrolledToBottom(UIObject source) {
		Element element = source.getElement();
		int pageClientHeight = element.getOwnerDocument().getClientHeight();
		int clientHeight = element.getClientHeight();
		if (clientHeight > pageClientHeight) {
			int delta = clientHeight - pageClientHeight;
			
			// When scrolling down the absoluteTop will be negative
			int absoluteTop = element.getAbsoluteTop() * -1;
			if (absoluteTop >= delta) {
				return true;
			}
		}
		return false;
	}
	
	public static void fireScrollBottom(Widget widget) {
		ScrollBottomEvent event = new ScrollBottomEvent(widget);
		widget.fireEvent(event);
	}

	public static String toString(Object value, String defaultValue) {
		return QAMLUtil.toString(value, defaultValue);
	}

	public static Boolean toBoolean(Object value, Boolean defaultValue) {
		return QAMLUtil.toBoolean(value, defaultValue);
	}

	public static Boolean toBoolean(Object value, String value4True, String value4False, Boolean defaultValue) {
		return QAMLUtil.toBoolean(value, value4True, value4False, defaultValue);
	}

	public static Integer toInteger(Object value, Integer defaultValue) {
		return QAMLUtil.toInteger(value, defaultValue);
	}

	public static Double toDouble(Object value, Double defaultValue) {
		return QAMLUtil.toDouble(value, defaultValue);
	}
	
	public static Date toDate(Object value, Date defaultValue) {
		return QAMLUtil.toDate(value, defaultValue);
	}

	public static void checkRequired(HasRequired widget, Object data) {
		if (widget == null) {
			return;
		}
		boolean empty = (data == null) || data.toString().isEmpty();
		if (!(widget.getRequired() && empty)) {
			return;
		}
		String message = null;
		String title = null;
		if(widget instanceof HasRequiredValidationMessage) {
			HasRequiredValidationMessage hasRequiredValidationMessage = (HasRequiredValidationMessage) widget;
			message = hasRequiredValidationMessage.getRequiredValidationMessage();
			title = hasRequiredValidationMessage.getRequiredValidationTitle();
		}
		throw new RequiredFieldException(title, message);
	}
}