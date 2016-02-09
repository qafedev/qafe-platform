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
package com.qualogy.qafe.bind.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Super class for application mapping bind classes
 * @author sshenoy
 */
public abstract class BindBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5741437811667314343L;

	/**
	 * description string on the tag
	 */
	protected String description;
	
	/**
	 * holder for Comment objects
	 */
	protected Comment comment = null;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setComment(String comment){
		if(comment==null)
			throw new IllegalArgumentException();
		
		this.comment = Comment.create(comment);
	}
	
	public String toString(){
		return ReflectionToStringBuilder.reflectionToString(this);
	}
}
