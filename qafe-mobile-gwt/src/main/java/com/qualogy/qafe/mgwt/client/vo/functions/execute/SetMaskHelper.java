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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

public class SetMaskHelper {
	 
	
	public static native void setMaskEnable(final String componentId, final boolean mask) /*-{
   	var mask_postfix = "-disabled";
   	var escapedComponentId = componentId.replace(/\|/g, "\\|");
   	// for selecting items usind id we need to use #
   	escapedComponentId = '#' + escapedComponentId;
    
   	$wnd.jQuery(escapedComponentId + mask_postfix).remove(); 
   
   	if(!mask) {
   		var width = $wnd.jQuery(escapedComponentId).width();
		var height = $wnd.jQuery(escapedComponentId).height();
		var top = $wnd.jQuery(escapedComponentId).position().top;
		var left = $wnd.jQuery(escapedComponentId).position().left;
		$el = $wnd.jQuery('<div></div>').appendTo(escapedComponentId);
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
	
	
}

