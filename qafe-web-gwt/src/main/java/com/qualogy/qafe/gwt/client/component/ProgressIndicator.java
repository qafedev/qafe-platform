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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.qualogy.qafe.gwt.client.images.QAFEGWTWebImageBundle;
import com.qualogy.qafe.gwt.client.util.QAMLUtil;
import com.qualogy.qafe.gwt.client.util.QEffects;

public class ProgressIndicator extends AbsolutePanel {

	public final static String DEFAULT_SIZE = "25";
	
	private QAFEGWTWebImageBundle qafeGWTWebImageBundle = (QAFEGWTWebImageBundle) GWT.create(QAFEGWTWebImageBundle.class);
    private HTML html = new HTML("<div class=\"spinner\">" +
						    		"<div class=\"bar1\"></div>" +
						    		"<div class=\"bar2\"></div>" +
						    		"<div class=\"bar3\"></div>" +
						    		"<div class=\"bar4\"></div>" +
						    		"<div class=\"bar5\"></div>" +
						    		"<div class=\"bar6\"></div>" +
						    		"<div class=\"bar7\"></div>" +
						    		"<div class=\"bar8\"></div>" +
						    	 "</div>");

	public ProgressIndicator() {
		add(html, 0, 0);
		setWidth(DEFAULT_SIZE);
		setHeight(DEFAULT_SIZE);
	    DOM.setStyleAttribute(getElement(), "zIndex", "9999");
	}

	protected QAFEGWTWebImageBundle getImageBundle() {
		return qafeGWTWebImageBundle;
	}
	
	public void start() {		
		QEffects.fadeIn(html, 500, 50);
	}

	public void stop() {
		QEffects.fadeOut(html, 500, 50, false);
	}
	
	public int getWidth(){
		return Integer.parseInt(DEFAULT_SIZE);
	}
	
	@Override
	public void setWidth(String width) {
		super.setWidth(QAMLUtil.unitPX(width));
	}
	
	public int getHeight(){
		return Integer.parseInt(DEFAULT_SIZE);
	}
	
	@Override
	public void setHeight(String height) {
		super.setHeight(QAMLUtil.unitPX(height));
	}
}