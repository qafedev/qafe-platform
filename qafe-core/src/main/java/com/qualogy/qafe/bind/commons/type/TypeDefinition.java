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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holder for TypeDefinition attributes. A TypeDefnition describes a 
 * qafe systems domain type and is used by f.i. adapters to adapt
 * the world to the internal domain. 
 * //TODO: is this a necessary class and xml
 * @author 
 */
public class TypeDefinition implements Serializable {
    /**
	 * generated svuid 
	 */
	private static final long serialVersionUID = 1402583778982927632L;

	protected String id;
    
    /**
     * list of attributes for the type f.i. 
     * Car
     *  |
     *  - wheel
     *  - door
     *  ->> wheel and door would be attributes to type car and can be types themselves
     */
    protected List<TypeAttributeMapping> attributes;
    
    /**
     * typedefinition of this types parent
     */
    protected TypeDefinition parent;

    /**
     * boolean indicating if this type is abstract
     */
    protected boolean abstractType;

    public TypeDefinition() {}

    public TypeDefinition(String type) {
        this.id = type;
    }

    
    /**
     * The object will return true when:
     * 	- {@link #id} not is null
     * 	- the given object is instanceof TypeDefinition (and therefore not null)
     * 	- the given objects id is not null and equals(ignoring the case) to {@link #id}
     * @return boolean stating equal or not to the given according above stated rules
     */
    public boolean equals(Object obj) {
    	boolean equal = false;
        if(this.id!=null && obj instanceof TypeDefinition){
        	String otherId = ((TypeDefinition)obj).getId();
        	equal = otherId!=null && this.id.equalsIgnoreCase(otherId);
        }
    	return equal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

   
    public boolean hasParent() {
        return parent != null;
    }

    public boolean isAbstractType() {
        return abstractType;
    }

    public void setAbstractType(boolean abstractType) {
        this.abstractType = abstractType;
    }

    public List getAttributes() {
        return attributes;
    }

    /**
     * method to add a TypeAttributeMapping to a TypeAttributeMapping list. The list will be created when null
     * 
     * @param typeDefinition
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(TypeAttributeMapping typeAttributeMapping) {
    	if (typeAttributeMapping == null)
            throw new IllegalArgumentException("typeAttributeMapping cannot be null");
        if (attributes == null)
        	attributes = new ArrayList<TypeAttributeMapping>();

        attributes.add(typeAttributeMapping);
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypeDefinition getParent() {
        return parent;
    }

    public void setParent(TypeDefinition parent) {
        this.parent = parent;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
