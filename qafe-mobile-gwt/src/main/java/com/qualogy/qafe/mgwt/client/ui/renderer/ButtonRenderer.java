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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QButton;
import com.qualogy.qafe.mgwt.client.vo.ui.ButtonGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public class ButtonRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof ButtonGVO) {
			ButtonGVO buttonGVO = (ButtonGVO)component;
			QButton button = new QButton(buttonGVO.getLabel());
			init(buttonGVO, button, owner, uuid, parent, context, activity);
			widget = button;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}

//	/**
//	 * Creates PushButton component with image.
//	 * Also associated context menu is specified.
//	 * @param menu
//	 * @param buttonGVO
//	 * @param uuid
//	 * @param parent
//	 * @param context
//	 * @return
//	 */
//	private PushButton createPushButton(MenuItemGVO menu, final ButtonGVO buttonGVO, final String uuid, final String parent, String context) {
//		PushButton pushButton = null;
//		if (menu != null) {
//			pushButton =  new PushButton() {
//				@Override
//				public void onBrowserEvent(Event event) {
//					if (event.getTypeInt() == Event.ONCONTEXTMENU) {
//						//DOM.eventPreventDefault(event);
//						//applyContextMenu(event, buttonGVO, uuid, parent);
//					}
//					super.onBrowserEvent(event);
//				}
//
//				@Override
//				protected void setElement(Element elem) {
//					super.setElement(elem);
//					sinkEvents(Event.ONCONTEXTMENU);
//				}
//			};
//		} else {
//			pushButton =  new PushButton();
//		}
//		RendererHelper.fillIn(buttonGVO, pushButton, uuid, parent, context);
//		/**
//		 * Following code manages width and height of pushbutton holding image.
//		 * Style attribute from the uiObject rendered as div is taken off.
//		 * In case of PushButton the uiObject is rendered as a div which holds html element of type text and image.
//		 * All styles are made in effect on the image. The text input remains empty.
//		 * Width & Height mentioned in style attribute of push button tag has higher priority followed value of width & height attributes available in button tag 
//		 * and the least priority goes to the properties mentioned in css style class.
//		 * If width and height is not mentioned at all by any means then size of the image is made effective as size of the push button.
//		 * */
//		pushButton.getElement().removeAttribute("style");
//		Image image = new Image(buttonGVO.getImageLocation());
//		String imageHeight = buttonGVO.getHeight() == null ? image.getHeight()+"" : buttonGVO.getHeight();
//		String imageWidth = buttonGVO.getWidth() == null ? image.getWidth()+"" : buttonGVO.getWidth();
//		image.setSize(imageWidth,imageHeight);
//		pushButton.setSize(imageWidth,imageHeight);
//		if(buttonGVO.getStyleClass() != null){
//			image.setStylePrimaryName(buttonGVO.getStyleClass());
//		} 
//		if(buttonGVO.getStyleProperties().length > 0) {
//			RendererHelper.setStyleForElement(image.getElement(), buttonGVO.getStyleProperties());
//		}
//		pushButton.getUpFace().setImage(image);
//		pushButton.getDownFace().setImage(image);
//		return pushButton;
//	}
//	
//
//	/**
//	 * Creates Button component with a label.
//	 * Also associated context menu is specified.
//	 * @param menu
//	 * @param buttonGVO
//	 * @param uuid
//	 * @param parent
//	 * @param context
//	 * @return
//	 */
//	private Button createButton(MenuItemGVO menu, final ButtonGVO buttonGVO, final String uuid, final String parent, String context) {
//		Button button = null;
//		if (menu != null) {
//			button =  new Button() {
//				@Override
//				public void onBrowserEvent(Event event) {
//					if (event.getTypeInt() == Event.ONCONTEXTMENU) {
////						DOM.eventPreventDefault(event);
////						applyContextMenu(event, buttonGVO, uuid, parent);
//					}
//					super.onBrowserEvent(event);
//				}
//
//				@Override
//				protected void setElement(Element elem) {
//					super.setElement(elem);
//					sinkEvents(Event.ONCONTEXTMENU);
//				}
//			};
//		} else {
//			button = new Button();
//		}
//		if(buttonGVO.getStyleClass() != null){
//			button.setStylePrimaryName(buttonGVO.getStyleClass());
//		}		
//		RendererHelper.fillIn(buttonGVO, button, uuid, parent, context);
//		
//		button.setText(buttonGVO.getLabel());
//		return button;
//	}
}
