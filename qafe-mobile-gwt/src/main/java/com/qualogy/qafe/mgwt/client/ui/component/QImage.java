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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.HasLongTapHandlers;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.googlecode.mgwt.ui.client.widget.touch.GestureUtility;

public class QImage extends Image implements HasTapHandlers, HasLongTapHandlers, HasTouchHandlers {

	private GestureUtility gestureUtility;
	
	public QImage() {
		gestureUtility = new GestureUtility(this);
	}
	
	@Override
	public HandlerRegistration addTapHandler(TapHandler handler) {
		gestureUtility.ensureTapRecognizer();
		return addHandler(handler, TapEvent.getType());
	}
	
	@Override
	public HandlerRegistration addLongTapHandler(LongTapHandler handler) {
		gestureUtility.ensureLongTapRecognizer();
		return addHandler(handler, LongTapEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return ComponentHelper.addTouchStartHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return ComponentHelper.addTouchMoveHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return ComponentHelper.addTouchCancelHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return ComponentHelper.addTouchEndHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchHandler(TouchHandler handler) {
		return ComponentHelper.addTouchHandler(this, handler);
	}
}
