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
package com.qualogy.qafe.gwt.client.util;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.UIObject;

public class QEffects {

	private QEffects(){}
	
	public static void fadeIn(final UIObject ui, final int delay,final int startIn){
		final Effects a = $(ui.getElement()).as(Effects.Effects);
		new Timer(){

			@Override
			public void run() {
				$(ui.getElement()).fadeIn(delay,new Function() {
					public void f(Element e) {
						//a.fadeIn();
					
					}
				});
				
			}
			
		}.schedule(startIn);
	}
	
	public static void fadeOut(final UIObject ui, final int delay,final int startIn, final boolean visibleAfterFadingOut){
		final Effects a = $(ui.getElement()).as(Effects.Effects);
		new Timer(){

			@Override
			public void run() {
				$(ui.getElement()).fadeOut(delay,new Function() {
					public void f(Element e) {
						//a.fadeOut();
						ui.setVisible(visibleAfterFadingOut);
					}
				});
				
			}
			
		}.schedule(startIn);
	}
}
