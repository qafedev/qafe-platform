/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.presentation.event.function.dialog;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;

public abstract class MessageDialog extends BuiltInFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5371975706617275211L;

	protected Parameter title;

	private Object titleData;
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	protected String width;
	
	protected String height;
	
	
    public MessageDialog() {
        super();
    }

   

    public MessageDialog( final Parameter title) {
      
        this.title = title;
    }

    public Parameter getTitle() {
		return title;
	}

	public void setTitle(Parameter title) {
		this.title = title;
	}

	public Object getTitleData() {
		return titleData;
	}

	public void setTitleData(Object titleData) {
		this.titleData = titleData;
	}
}
