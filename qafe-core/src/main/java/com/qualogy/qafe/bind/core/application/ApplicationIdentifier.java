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
package com.qualogy.qafe.bind.core.application;

import java.io.Serializable;


public class ApplicationIdentifier implements Comparable<Object>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7211969367579764586L;
	protected String id;

	@SuppressWarnings("unused")
	private ApplicationIdentifier() {
		super();
	}
	
	public ApplicationIdentifier(String id) {
		super();
		this.id = id;
	}
	
	public String stringValueOf() {
		return id;
	}

	
	
	public int compareTo(Object o) {
		  return o instanceof ApplicationIdentifier 
		  				? id.compareTo(((ApplicationIdentifier)o).stringValueOf())
		  				: 0;
	}
	
	public String toString(){
		return stringValueOf();
	}
	
	public boolean startsWith(String prefix){
		return id!=null && id.startsWith(prefix);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicationIdentifier other = (ApplicationIdentifier) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
