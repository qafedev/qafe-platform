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
package com.qualogy.qafe.bind.core.pattern;

import org.apache.commons.lang.StringUtils;




public abstract class IdPattern implements Comparable{
	
	public IdPattern() {
		super();
	}

	public IdPattern(WildCardPattern idpattern) {
		this();
		this.idpattern = idpattern;
	}
	
	public IdPattern(String idpatternStr) {
		this(new WildCardPattern(idpatternStr));
	}
	
	protected WildCardPattern idpattern;
	
	/**
	 * returns 1 when this is not wildcarded or this wildcard contains more characters
	 * than the one given
	 * TODO: idpattern must be strip from wildcard symbols before comparison on length and when two wildcards
	 * are supplied, the pattern is 'always' less strong than one wildcard. 
	 */
	public int compareTo(Object other){
		
		if(!(other instanceof IdPattern))
			throw new IllegalArgumentException("cannot compareto ["+(other!=null?other.getClass():null)+"]");
		
		return this.idpattern.compareTo(((IdPattern)other).idpattern);
	}

	public boolean matches(String id) {
		return idpattern.matches(id);
	}
	
	public String toString(){
		return (idpattern!=null)?idpattern.toString():null;
	}
	
	public boolean isEmpty(){
		return StringUtils.isEmpty(toString());
	}

	
}
