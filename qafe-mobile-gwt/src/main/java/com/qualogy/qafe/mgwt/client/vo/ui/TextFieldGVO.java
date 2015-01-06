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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie
 */

public class TextFieldGVO extends EditableComponentGVO implements HasVisibleTextI, HasRequiredClass, HasRequiredValidationMessage {

	public static final String TYPE_TEXT = "text";
	public static final String TYPE_INTEGER = "int";
	public static final String REGEXP_TYPE_INTEGER_VALUE = "^[-]?[0-9]+$";
	public static final String TYPE_INTEGER_DEFAULT_MESSAGE = "not an integer value";
	public static final String TYPE_SIGNED_INTEGER = "signed_int";
	public static final String REGEXP_TYPE_SIGNED_INTEGER_VALUE = "[0-9]+$";
	public static final String TYPE_SIGNED_INTEGER_DEFAULT_MESSAGE = "not an signed integer value";
	public static final String TYPE_DOUBLE = "double";
	public static final String REGEXP_TYPE_DOUBLE_VALUE = "^[-]?[0-9]*[\\.]?[0-9]*$";
	public static final String TYPE_DOUBLE_DEFAULT_MESSAGE = "Only decimal value allowed";
	public static final String TYPE_CHARACTERS = "chars";
	public static final String REGEXP_TYPE_CHARACTERS_VALUE = "^[a-zA-Z]+$";
	public static final String TYPE_CHARACTERS_DEFAULT_MESSAGE = "Only characters allowed[aA-zZ]";
	public static final String TYPE_EMAIL = "email";
	public static final String REGEXP_TYPE_EMAIL_VALUE = "^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*\\s+<(\\w[-._\\w]*\\w@\\\\w[-._\\w]*\\w\\.\\w{2,4})>$|^(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})$";
	public static final String TYPE_EMAIL_DEFAULT_MESSAGE = "Not correct format for email";
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	public String orientation;
	public String displayname;
	private String format = DEFAULT_DATE_FORMAT;
	private String regExp;
	private Boolean required = Boolean.FALSE;
	private String requiredStyleClassName;
	private static final long serialVersionUID = 3537234314264151866L;
	public static final String TYPE_DATE = "date";
	public static final String DEFAULT_HEIGHT = "20px";
	public static final String DEFAULT_WIDTH = "100px";
	public static final String TYPE_SPINNER = "spinner";
	public static final int DEFAULT_MINIMUM = 0;
	public static final int DEFAULT_MAXIMUM = 20;
	protected String value;
	protected Integer minLength;
	protected Integer maxLength;
	protected String type = TYPE_TEXT;
	protected String minValue;
	protected String maxValue;
	private Boolean suggest = Boolean.FALSE;
	private Integer suggestCharacters;
	//private String styleClassName = "qafe_textfield";
	protected String validationMessage;
	protected String validationTitle;
	private String requiredValidationMessage;
	private String requiredValidationTitle;

	
	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public String getValidationTitle() {
		return validationTitle;
	}

	public void setValidationTitle(String validationTitle) {
		this.validationTitle = validationTitle;
	}

		
	public static final String REGEXPTYPE = "regexptype" ;
	
	
	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequiredStyleClassName() {
		return requiredStyleClassName;
	}

	public void setRequiredStyleClassName(String requiredStyleClassName) {
		this.requiredStyleClassName = requiredStyleClassName;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public void setSuggest(Boolean suggest) {
		this.suggest = suggest;

	}

	public Integer getSuggestCharacters() {
		return suggestCharacters;
	}

	public void setSuggestCharacters(Integer suggestCharacters) {
		this.suggestCharacters = suggestCharacters;
	}

	public Boolean getSuggest() {
		return suggest;
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}
	
	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/
	
	public static String getRegExp(String type) {
		String regExp = null;
		 if(TYPE_TEXT.equals(type)){
				//do nothing, default behaviour								
		} else if (TYPE_CHARACTERS.equals(type)){
			 regExp = REGEXP_TYPE_CHARACTERS_VALUE;			
		} else if(TYPE_DOUBLE.equals(type)){
			regExp = REGEXP_TYPE_DOUBLE_VALUE;						
		}  else if(TYPE_EMAIL.equals(type)){
			regExp = REGEXP_TYPE_EMAIL_VALUE;		
		} else if(TYPE_INTEGER.equals(type)){
			regExp = REGEXP_TYPE_INTEGER_VALUE;											
		} else if(TYPE_SIGNED_INTEGER.equals(type)){
			regExp = REGEXP_TYPE_SIGNED_INTEGER_VALUE;			
		}
		 return regExp;
	}
	
	public static String getRegExpMessage(String type) {
		String regExpMessage = null;
		 if(TYPE_TEXT.equals(type)){
				//do nothing, default behaviour								
		} else if (TYPE_CHARACTERS.equals(type)){
			regExpMessage = TYPE_CHARACTERS_DEFAULT_MESSAGE;			
		} else if(TYPE_DOUBLE.equals(type)){
			regExpMessage = TYPE_DOUBLE_DEFAULT_MESSAGE;						
		}  else if(TYPE_EMAIL.equals(type)){
			regExpMessage = TYPE_EMAIL_DEFAULT_MESSAGE;		
		} else if(TYPE_INTEGER.equals(type)){
			regExpMessage = TYPE_INTEGER_DEFAULT_MESSAGE;											
		} else if(TYPE_SIGNED_INTEGER.equals(type)){
			regExpMessage = TYPE_SIGNED_INTEGER_DEFAULT_MESSAGE;			
		}
		 return regExpMessage;
	}

	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}

	public void setRequiredValidationMessage(String requiredValidationMessage) {
		this.requiredValidationMessage = requiredValidationMessage;
	}

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}

	public void setRequiredValidationTitle(String requiredValidationTitle) {
		this.requiredValidationTitle = requiredValidationTitle;
	}
	
}
