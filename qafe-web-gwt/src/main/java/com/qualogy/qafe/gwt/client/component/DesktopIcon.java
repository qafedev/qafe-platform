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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DesktopIcon extends VerticalPanel implements HasDoubleClickHandlers{

	public final static int HEIGHT=60;
	public final static int WIDTH=40;
	private Label titleLabel =null;
	public Label getTitleLabel() {
		return titleLabel;
	}

	private DesktopImage image=null; 
    public DesktopImage getImage() {
		return image;
	}

	public DesktopIcon(String url, String title) { 
        super(); 
        image = new DesktopImage();
        image.setUrl(url);
        image.setTitle(title);
        image.setHeight(WIDTH+"px");
        image.setWidth(WIDTH+"px");
        titleLabel= new Label(title);
        titleLabel.setHeight("10px");
        titleLabel.setStyleName("desktopicontitle"); 
       
       // DOM.setStyleAttribute(titleLabel.getElement(), "fontSize", "9pt");
       // DOM.setStyleAttribute(titleLabel.getElement(), "textOverflow", "ellipses");
        add(image);
        add(titleLabel);
        setWidth(WIDTH+"px");
        setHeight(HEIGHT+"px");
        setStyleName("desktopicon");
        
    } 

    private class DesktopImage extends Image implements HasDoubleClickHandlers{
    	
    	public HandlerRegistration addDoubleClickHandler(final DoubleClickHandler handler) { 
            return addDomHandler(handler, DoubleClickEvent.getType()); 
        }

		
    }

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return image.addDoubleClickHandler(handler);
	}
	
}
