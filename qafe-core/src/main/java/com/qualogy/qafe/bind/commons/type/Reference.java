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
package com.qualogy.qafe.bind.commons.type;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * - a reference to an object (from system/component/etc.) that will be used as a parameter in
 * - a reference to an object that can be used for selection statements
 * - a reference to an object that can be used later on  
 * @author 
 *
 */
public class Reference implements Serializable{
	
	private static final long serialVersionUID = 1896949434732452871L;
	
	protected String ref;
	
	/**
	 * identifier saying that this parameter points to a application local store value
	 */
	public final static String SOURCE_APP_LOCAL_STORE_ID = "user";
	/**
	 * identifier saying that this parameter points to a application global store value
	 */
	public final static String SOURCE_APP_GLOBAL_STORE_ID = "global";
	
	/**
	 * identifier saying that this parameter points to a component value
	 */
	public final static String SOURCE_COMPONENT_ID = "component";
	
	/**
	 * identifier saying that this parameter points to a datastore value
	 */
	public final static String SOURCE_DATASTORE_ID = "pipe";
	
	/**
	 * identifier saying that this parameter points to a message value
	 */
	public final static String SOURCE_MESSAGE_ID = "message";
	
	protected String source;
	
	public Reference(String ref) {
		this(ref, SOURCE_DATASTORE_ID);
	}
	
	public Reference(String ref, String source) {
		this.ref = ref;
		this.source = source;
	}
	
	public Reference() {
		super();
	}
	
	public Reference(Reference otherRef) {
		this.ref = otherRef.ref;
		this.source = otherRef.source;
	}
	/**
	 * Method to get the root for this reference. Examples:
	 * - root.key1.key2 = root
	 * - key1.key2      = key1
	 * - key2           = key2
	 * @return
	 */
	public final String getRootRef(){
		String key = this.ref;
		int dotMark = key.indexOf(Parameter.OBJECT_DELIMITER);
		return (dotMark>-1)?key.substring(0, dotMark):key;
	}
	
	/**
	 * Method to get the reference without the root. Examples:
	 * - root.key1.key2 = key1.key2
	 * - key1.key2      = key2
	 * - key2           = key2
	 * @return
	 */
	public final String getRefWithoutRoot(){
		String key = this.ref;
		int dotMark = key.indexOf(Parameter.OBJECT_DELIMITER);
		return (dotMark>-1)?key.substring(1+dotMark):key;
	}
	
	/**
	 * Method to get the root for this reference. Examples
	 * - root.key1.key2 = root
	 * - key1.key2      = key1
	 * - key2           = key2
	 * @return
	 */
	public final boolean hasRootRef(){
		return this.ref.indexOf(Parameter.OBJECT_DELIMITER)>-1;
	}
	
	/**
	 * method checks if this.getKey() represents a key to the root
	 * of the object (iow not pointing to a parameter within the object),
	 * denoted by null, blank or <code>AbstractParameter.OBJECT_DELIMITER</code>
	 * 
	 * f.i.
	 *  ref=""
	 *  ref="."
	 * 
	 * @return boolean - true if denotes root
	 */
	public final boolean denotesRoot(){
		return this.ref==null || (this.ref.length()==1 && this.ref.charAt(0)==Parameter.OBJECT_DELIMITER) || this.ref.length()==0; 
	}
	
	/**
	 * @deprected - ambiguous use stringValueOf
	 * do not change the body of this message (not just a logging method)
	 */
	public String toString(){
		return stringValueOf();
	}
	
	public String stringValueOf(){
		return ref;
	}
	
	public String toLogString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	public boolean isComponentReference() {
		return SOURCE_COMPONENT_ID.equals(source);
	}
	
	public boolean isMessageReference(){
		return  SOURCE_MESSAGE_ID.equals(source);////source!=null && source.startsWith(SOURCE_MESSAGE_ID);
	}
	
	public boolean isDataStoreReference(){
		return !(StringUtils.isBlank(source)) && SOURCE_DATASTORE_ID.equals(source);
	}
	
	public boolean isLocalStoreReference(){
		return SOURCE_APP_LOCAL_STORE_ID.equals(source);
	}
	
	public void setRef(String newref) {
		this.ref = newref;
	}
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Reference clone = new Reference();
		clone.ref  = this.ref;
		clone.source = this.source;		
		return clone;
	
	}

	public boolean isGlobalStoreReference() {
		return SOURCE_APP_GLOBAL_STORE_ID.equals(source);
	}
	
}
