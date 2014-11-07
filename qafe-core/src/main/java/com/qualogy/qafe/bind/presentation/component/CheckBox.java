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
/**
 * 
 */
package com.qualogy.qafe.bind.presentation.component;


/**
 * @author rjankie 
 */

public  class CheckBox extends EditableComponent implements HasVisibleText{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5618002118711850699L;
	/**
	 * 
	 */
	protected Boolean value;
	/**
	 * 
	 */
	protected String displayname;
	
	protected String messageKey;
	
	protected String checkedValue;
	
	protected String unCheckedValue;

	public  final static String[] UNCHECKED_VALUE_DOMAIN = new String[]{"0","false","off","null"};
	public  final static String[] CHECKED_VALUE_DOMAIN   = new String[]{"1","true","on"};
	
	private String displaynamePosition;
	
	public static Boolean getValue(String value){
		
		Boolean returnValue=null;
		for (int i=0;i<CHECKED_VALUE_DOMAIN.length && returnValue==null;i++){
			if (CHECKED_VALUE_DOMAIN[i].equals(value)){
				returnValue=Boolean.TRUE;
			}			
		}
		// so if there is no true found, just check if the domain for UNCHECKED VALUE is applicable otherwise return null
		if (returnValue==null){
			for (int i=0;i<UNCHECKED_VALUE_DOMAIN.length && returnValue==null;i++){
				if (UNCHECKED_VALUE_DOMAIN[i].equals(value)){
					returnValue=Boolean.FALSE;
				}			
			}	
		}
		return returnValue;
		
	}
	/**
	 * @return
	 */
	public Boolean getValue() {
		return value;
	}
	/**
	 * @param value
	 */
	public void setValue(Boolean value) {
		this.value = value;
	}

	
	public String getCheckedValue() {
		return checkedValue;
	}
	public void setCheckedValue(String checkedValue) {
		this.checkedValue = checkedValue;
	}
	public String getUnCheckedValue() {
		return unCheckedValue;
	}
	public void setUnCheckedValue(String unCheckedValue) {
		this.unCheckedValue = unCheckedValue;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public String getDisplaynamePosition() {
		return displaynamePosition;
	}
	public void setDisplaynamePosition(String displaynamePosition) {
		this.displaynamePosition = displaynamePosition;
	}
	
}
