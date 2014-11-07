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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.AbstractComponentRenderer.MessageBox;
import com.qualogy.qafe.gwt.client.util.QEffects;

public abstract class Utils {
	
	public static void fadeMessage(MessageBox messageBox, String message){
		fadeMessage(messageBox,message,3000,null,null);
		
	}	
	
	public static void fadeMessage(MessageBox messageBox, String message,Integer delay,String styleClass,String[][] styleProperties){
		if (messageBox != null){
			if (styleClass!=null){
				messageBox.addStyleName(styleClass);
			}
			RendererHelper.setStyleForElement(messageBox.getElement(), styleProperties);
			
			messageBox.getLabel().setText(message);
			QEffects.fadeIn(messageBox, 500, 10);
			new Timer(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
				}}.schedule(510);
			QEffects.fadeOut(messageBox,delay, 50, false);
		}
	}	
	public static void setWidgetPosition(Widget w, int left, int top) {
		Element h = w.getElement();
		DOM.setStyleAttribute(h, "position", "absolute");
		DOM.setStyleAttribute(h, "left", left + "px");
		DOM.setStyleAttribute(h, "top", top + "px");
	}
}
