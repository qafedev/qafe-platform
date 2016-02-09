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
package com.qualogy.qafe.gwt.client.ui.renderer;


import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;


public abstract class AbstractTextComponentRenderer extends AbstractComponentRenderer {

	@SuppressWarnings("deprecation")
	protected void handleTypeAttribute(UIObject uiObject, final String regExp, final String title,final String message) {
		if (uiObject!=null && uiObject instanceof TextBox){
			if (regExp!=null && regExp.trim().length()>0){
				TextBox textBox = (TextBox)uiObject;
				textBox.addBlurHandler(new BlurHandler(){

					public void onBlur(BlurEvent event) {
						String textValue=  ((TextBoxBase)event.getSource()).getText();
						if (textValue!=null && regExp!=null){											
							if (textValue.replaceFirst(regExp, "").length()>0){							
								if(title != null){
									ClientApplicationContext.getInstance().log(title, message,true);
								} else {
									ClientApplicationContext.getInstance().log("Validation error", message,true);
								}
								 
							}						
						}
//						String textValue=  ((TextBoxBase)event.getSource()).getText();
//						if (textValue!=null){											
//							if (textValue.replaceFirst(regExp, "").length()>0){							
//								ClientApplicationContext.getInstance().log(title, message,true);
//							}						
//						}					
					}});
				
			}		
		} else if (uiObject!=null && uiObject instanceof QSuggestBox){
			if (regExp!=null && regExp.trim().length()>0){
				QSuggestBox qSuggestBox = (QSuggestBox)uiObject;
				qSuggestBox.addFocusListener(new FocusListener() {
					
					public void onLostFocus(Widget sender) {
						String textValue =  ((QSuggestBox)sender).getText();
						if (textValue!=null){		
							if (textValue.replaceFirst(regExp, "").length()>0){							
								ClientApplicationContext.getInstance().log("Validation error", message,true);
							}					
						}
					}
					
					public void onFocus(Widget sender) {
					}
				});
			}
		}
	}

}
