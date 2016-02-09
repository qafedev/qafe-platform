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
package com.qualogy.qafe.bind.presentation.event.function.dialog;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Value;

public class GenericDialog extends MessageDialog {

    private static final long serialVersionUID = -6570201472308045671L;

    protected Parameter message;
    
    private Object messageData;
    
    public final static String TYPE_INFO="info";
    public final static String TYPE_ERROR="error";
    public final static String TYPE_ALERT="alert";
    
    
    protected String type = TYPE_INFO;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GenericDialog() {
        super();
    }
    
    public GenericDialog(String title, String message){
    	Parameter titleParameter = new Parameter();
    	Value titleValue =  new Value();
    	titleValue.setStaticValue(title);
    	titleParameter.setValue(titleValue);
    	setTitle(titleParameter);
    	
    	Parameter messageParameter = new Parameter();
    	Value messageValue =  new Value();
    	messageValue.setStaticValue(message);
    	messageParameter.setValue(messageValue);
    	setMessage(messageParameter);
    	
    }
    

    

	public Parameter getMessage() {
		return message;
	}

	public void setMessage(Parameter message) {
		this.message = message;
	}

	public Object getMessageData() {
		return messageData;
	}

	public void setMessageData(Object messageData) {
		this.messageData = messageData;
	}
	
	
	public void setMessageStringValue(String value){
		if (getMessage()==null){
			setMessage(new Parameter());
		}					
		
		if (getMessage().getValue()==null){
				getMessage().setValue(new Value());
		}
		getMessage().getValue().setStaticValue(value);
		
	}
	
	public void setTitleStringValue(String value){
		if (getTitle()==null){
			setTitle(new Parameter());
		}					
		
		if (getTitle().getValue()==null){
			getTitle().setValue(new Value());
		}
		getTitle().getValue().setStaticValue(value);
		
	}
}
