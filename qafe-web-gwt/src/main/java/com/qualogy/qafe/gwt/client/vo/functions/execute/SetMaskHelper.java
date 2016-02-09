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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;

public class SetMaskHelper {
	
	public static native void setMaskEnable(final String componentId, final boolean mask, final boolean usePosition) /*-{
   	var mask_postfix = "-disabled";
   	var escapedComponentId = componentId.replace(/\|/g, "\\|");
   	// for selecting items usind id we need to use #
   	escapedComponentId = '#' + escapedComponentId;
    
   	$wnd.jQuery(escapedComponentId + mask_postfix).remove(); 
   
   	if(!mask) {
   		var width = $wnd.jQuery(escapedComponentId).width();
		var height = $wnd.jQuery(escapedComponentId).height();
		var top = 0;
		var left = 0;
		if (usePosition) {
			top = $wnd.jQuery(escapedComponentId).position().top;
			left = $wnd.jQuery(escapedComponentId).position().left;
		}
		$el = $wnd.jQuery('<div></div>').insertBefore(escapedComponentId);
			$el.css('background-color', 'white');
			$el.css('position', 'absolute');
			$el.css('z-index', '2000');
			$el.css('width', width);
			$el.css('height', height);
			$el.css('top', top);
			$el.css('left', left);
			$el.css('opacity', .20);
			$el.attr('id', componentId + mask_postfix);
   	}   
		
	}-*/;
	
	public static void setMask(final String componentId, final String className, final boolean mask) {
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(componentId);
		if (uiObjects == null) {
			setMaskNative(componentId, className, mask);
			return;
		}
		for (UIObject uiObject : uiObjects) {
			uiObject.removeStyleName(RendererHelper.QAFE_GLASS_PANEL_STYLE);
			if (mask) {
				uiObject.addStyleName(RendererHelper.QAFE_GLASS_PANEL_STYLE);
			}
		}	
	}
	
	private static native void setMaskNative(final String componentId, final String className, final boolean mask) /*-{
	   	
   	var escapedComponentId = componentId.replace(/\|/g, "\\|");
   	// for selecting items using id we need to use #
   	escapedComponentId = '#' + escapedComponentId;
   	
    if($wnd.jQuery(escapedComponentId).attr('class') != undefined && $wnd.jQuery(escapedComponentId).hasClass(className)) {
    	$wnd.jQuery(escapedComponentId).removeClass(className);  //to remove the glass panel class
    }   
    	    	   	
    if(mask) {   		
		$el = $wnd.jQuery(escapedComponentId).addClass(className);
   	}  
   	
	}-*/;
	
	public static native void setMaskUsingClass(final String className, final boolean mask) /*-{
   	var mask_postfix = "-disabled";
   	// for selecting items using class we need to use .   	
    var escapedComponentId = '.' + "gwt-PopupPanel"
    
   	$wnd.jQuery(escapedComponentId).removeClass(className);  //to remove the glass panel class
   	
	if($wnd.jQuery(escapedComponentId).position() == null){		
     	return;
   	}    	   	
    if(!mask) {   		
		$el = $wnd.jQuery(escapedComponentId).addClass(className);
   	}  
   	
	}-*/;
}