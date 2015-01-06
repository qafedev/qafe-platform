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
package com.qualogy.qafe.bind.presentation.event.function;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;


public class RegExpValidate extends BuiltInFunction implements PostProcessing{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1937627451028517090L;

	protected String regexp;
	
	protected String message;
	
	protected String type;
	
// see http://regexlib.com for more types
	
	private final String REGEXP_TYPE_INTEGER ="int";
	private final String REGEXP_TYPE_INTEGER_VALUE="^[0-9]+$";
	
	private final String REGEXP_TYPE_SIGNED_INTEGER ="signed_int";
	private final String REGEXP_TYPE_SIGNED_INTEGER_VALUE="^(\\+|-)?\\d+$";
	
	private final String REGEXP_TYPE_DOUBLE ="double";
	private final String REGEXP_TYPE_DOUBLE_VALUE="^([0-9]*|\\d*\\.\\d{1}?\\d*)$";
	
	private final String REGEXP_TYPE_CHARACTERS ="chars";
	private final String REGEXP_TYPE_CHARACTERS_VALUE="^[a-zA-Z]+$";

	private final String REGEXP_TYPE_EMAIL ="email";
	private final String REGEXP_TYPE_EMAIL_VALUE="^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*\\s+<(\\w[-._\\w]*\\w@\\\\w[-._\\w]*\\w\\.\\w{2,4})>$|^(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})$";

	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public void performPostProcessing() {
		if (getType()!=null && getType().length()>0){
			if (REGEXP_TYPE_CHARACTERS.equals(getType())){
				setRegexp(REGEXP_TYPE_CHARACTERS_VALUE);
			} else if (REGEXP_TYPE_DOUBLE.equals(getType())){
				setRegexp(REGEXP_TYPE_DOUBLE_VALUE);
			}  else if (REGEXP_TYPE_EMAIL.equals(getType())){
				setRegexp(REGEXP_TYPE_EMAIL_VALUE);
			}  else if (REGEXP_TYPE_INTEGER.equals(getType())){
				setRegexp(REGEXP_TYPE_INTEGER_VALUE);
			} else if (REGEXP_TYPE_SIGNED_INTEGER.equals(getType())){
				setRegexp(REGEXP_TYPE_SIGNED_INTEGER_VALUE);
			} else {
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"The type is not one of the following: chars/double/email/int/signed_int");
			}
		}
		
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
		
	}

	
	
	
	
}
