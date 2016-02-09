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
/**
 *
 */
package com.qualogy.qafe.bind.presentation.component;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;


/**
 * @author rjankie
 */

public  class TextField extends EditableComponent implements PostProcessing, HasVisibleText, HasRequiredProperty{

	public final static String ORIENTATION_UPDOWN="updown";
	public final static String ORIENTATION_LEFTRIGHT="leftright";

	public static final String TYPE_TEXT="text";
	public static final String TYPE_DATE="date";

	public static final String TYPE_INTEGER ="int";
	public static final String REGEXP_TYPE_INTEGER_VALUE="^[-]?[0-9]+$";
	public static final String TYPE_INTEGER_DEFAULT_MESSAGE="not an integer value";

	public static final String TYPE_SIGNED_INTEGER ="signed_int";
	public static final String REGEXP_TYPE_SIGNED_INTEGER_VALUE="[0-9]+$";
	public static final String TYPE_SIGNED_INTEGER_DEFAULT_MESSAGE="not an signed integer value";

	public static final String TYPE_DOUBLE ="double";
	public static final String REGEXP_TYPE_DOUBLE_VALUE = "^[-]?[0-9]*[\\.]?[0-9]*$"; //"^([0-9]*|\\d*\\.\\d{1}?\\d*)$";
	public static final String TYPE_DOUBLE_DEFAULT_MESSAGE="Only decimal value allowed";

	public static final String TYPE_CHARACTERS ="chars";
	public static final String REGEXP_TYPE_CHARACTERS_VALUE="^[a-zA-Z]+$";
	public static final String TYPE_CHARACTERS_DEFAULT_MESSAGE="Only characters allowed[aA-zZ]";

	public static final String TYPE_EMAIL ="email";
	public static final String REGEXP_TYPE_EMAIL_VALUE="^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*\\s+<(\\w[-._\\w]*\\w@\\\\w[-._\\w]*\\w\\.\\w{2,4})>$|^(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})$";
	public static final String TYPE_EMAIL_DEFAULT_MESSAGE="Not correct format for email";

	public static final String TYPE_SPINNER = "spinner";
	public static final int DEFAULT_MINIMUM = 0;
	public static final int DEFAULT_MAXIMUM = 20;

	public static final String DEFAULT_DATE_FORMAT="dd/MM/yyyy";
	private static final long serialVersionUID = 3537234314264151866L;

	protected String displayname;
	protected String orientation = ORIENTATION_LEFTRIGHT;
	protected String type = TYPE_TEXT;
	protected String format=DEFAULT_DATE_FORMAT;
	protected String regExp;
	protected String validationMessage;
	protected String validationMessageKey;
	protected String validationTitle;
	protected String validationTitleKey;
	protected Boolean suggest = Boolean.FALSE;
	protected Boolean required = Boolean.FALSE;
	protected String requiredStyleClassName;
	protected String minValue = DEFAULT_MINIMUM+"";
	protected String maxValue = DEFAULT_MAXIMUM+"";
	protected Integer suggestCharacters = 3;
	protected String value ="";
	protected Integer minLength;
	protected Integer maxLength;
	protected String messageKey;
	private String requiredValidationTitle;
	private String requiredValidationTitleKey;
	private String requiredValidationMessage;
	private String requiredValidationMessageKey;

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}
	public String getRequiredValidationTitleKey() {
		return requiredValidationTitleKey;
	}
	public void setRequiredValidationTitleKey(String requiredValidationTitleKey) {
		this.requiredValidationTitleKey = requiredValidationTitleKey;
	}
	public String getRequiredValidationMessageKey() {
		return requiredValidationMessageKey;
	}
	public void setRequiredValidationMessageKey(String requiredValidationMessageKey) {
		this.requiredValidationMessageKey = requiredValidationMessageKey;
	}
	public void setRequiredValidationTitle(String requiredValidationTitle) {
		this.requiredValidationTitle = requiredValidationTitle;
	}
	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}
	public void setRequiredValidationMessage(String requiredValidationMessage) {
		this.requiredValidationMessage = requiredValidationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	public String getValidationMessageKey() {
		return validationMessageKey;
	}
	public void setValidationMessageKey(String validationMessageKey) {
		this.validationMessageKey = validationMessageKey;
	}
	public String getValidationTitle() {
		return validationTitle;
	}
	public void setValidationTitle(String validationTitle) {
		this.validationTitle = validationTitle;
	}
	public String getValidationTitleKey() {
		return validationTitleKey;
	}
	public void setValidationTitleKey(String validationTitleKey) {
		this.validationTitleKey = validationTitleKey;
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

	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Boolean getSuggest() {
		return suggest;
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

	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	/**
	 * @return
	 */
	public Integer getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return
	 */
	public Integer getMinLength() {
		return minLength;
	}
	/**
	 * @param minLength
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	// CHECKSTYLE.OFF: CyclomaticComplexity
	public void performPostProcessing() {
		if (getOrientation()!=null && getOrientation().length()>0) {
			if (!ORIENTATION_LEFTRIGHT.equals(getOrientation()) &&
				!ORIENTATION_UPDOWN.equals(getOrientation()) ){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"invalid value for orientation in textfield. Setting to" + ORIENTATION_LEFTRIGHT);
				setOrientation(ORIENTATION_LEFTRIGHT);
			}

		} else {
			setOrientation(ORIENTATION_LEFTRIGHT);
		}

		if(getValidationTitle() == null) {
			setValidationTitle("Validation Error");
		}

		 if(TYPE_TEXT.equals(getType())){
				//do nothing, default behaviour
		} else if (TYPE_CHARACTERS.equals(getType())){
			 setRegExp(REGEXP_TYPE_CHARACTERS_VALUE);
			 if(validationMessage == null) {
				 setValidationMessage(TYPE_CHARACTERS_DEFAULT_MESSAGE);
			 }

		} else if(TYPE_DOUBLE.equals(getType())){
			 setRegExp(REGEXP_TYPE_DOUBLE_VALUE);
			 if(validationMessage == null) {
				 setValidationMessage(TYPE_DOUBLE_DEFAULT_MESSAGE);
			 }

		}  else if(TYPE_EMAIL.equals(getType())){
			 setRegExp(REGEXP_TYPE_EMAIL_VALUE);
			 if(validationMessage == null) {
				 setValidationMessage(TYPE_EMAIL_DEFAULT_MESSAGE);
			 }

		} else if(TYPE_INTEGER.equals(getType())){
			 setRegExp(REGEXP_TYPE_INTEGER_VALUE);
			 if(validationMessage == null) {
				 setValidationMessage(TYPE_INTEGER_DEFAULT_MESSAGE);
			 }

		} else if(TYPE_SIGNED_INTEGER.equals(getType())){
			 setRegExp(REGEXP_TYPE_SIGNED_INTEGER_VALUE);
			 if(validationMessage == null) {
				 setValidationMessage(TYPE_SIGNED_INTEGER_DEFAULT_MESSAGE);
			 }
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();

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

	public String getRequiredStyleClassName() {
		return requiredStyleClassName;
	}
	public void setRequiredStyleClassName(String requiredStyleClassName) {
		this.requiredStyleClassName = requiredStyleClassName;
	}

	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
