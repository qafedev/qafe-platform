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
package com.qualogy.qafe.bind.commons.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.qualogy.qafe.bind.core.messages.PlaceHolder;

/**
 * A parameter represents any form of data used in a call.
 * 
 * A parameter typically has a name and ref (to a piece of data) or static value.
 * A parameter also could have a type supplied through its value property, if
 * not supplied its assumable the type for this parameter is the instanceof the data
 * the parameter represents. 
 * 
 * The ref is what the calling party has called it for the using party.
 * The name is what the using party needs for further processing.
 * 
 * If you would take a look at a possible implementation than an inbound parameter 
 * to f.i. a service could have a name supplied which points to the name used 
 * in the actual service. The ref will be used to retrieve data from the datastore 
 * to be used in that service (or if not supplied the name is assumed to be the ref).
 * Outbound, the ref points to the name the actual service uses for the piece of data
 * and the ref tells where it'll be stored in the datastore.
 * 
 * <code>
 * 		<in ref="servicein" name="actualservicein"/><!-- ref is how its stored in the datastore, name is how this service calls it -->
 *		<out ref="actualserviceout" name="serviceout"/><!-- in ref from actualservice, out name to the world -->
 * </code> 
 * 
 * @author 
 *
 */
public class Parameter implements Serializable, Inputable, Comparable<Parameter>, Cloneable {
    
	private static final long serialVersionUID = 73634993433835489L;

	
	public final static char ATTRIBUTE_DELIMITER ='@';
    /**
	 * key to delimit objects 
	 */
	public final static char OBJECT_DELIMITER = '.';
	
	/**
	 * identifier to identify the start of a reference call to one of a lists objects
	 */
	public final static String LIST_IDENTIFIER_START = "[";
	
	/**
	 * identifier to identify the end of a reference call to one of a lists objects
	 */
	public final static String LIST_IDENTIFIER_END = "]";


	public static final String ATTRIBUTE_SIZE = "size"; 
	
	/**
	 * name for this parameter
	 */
	protected String name;
	
	/**
	 * reference property holder to the data for this parameter
	 */
	protected Reference ref;
	
	/**
	 * object that holds the defaultvalue and type for this object
	 */
    protected Value value;
    
    /**
     * adapter holds variables for the posibility of adapting a value for this parameter 
     * to the actual type of this parameter
     */
    protected AdapterMapping adapter;
    
    /**
     * list of placeholder objects intended for replacement of placeholder data
     * within a value that concludes from getting this parameter from any system resource
     */
    protected List<PlaceHolder> placeHolders = null;
    
    /**
     * parameter that can hold an executable script expression
     */
    protected String expression;
    
    /**
     * boolean indicating if this parameter allows the value for this parameter may be null
     */
    protected boolean nullable;
    
    /**
     * variable which can be set (some paramaters don't take this attr by binding) to specify
     * if this parameter should be set inbound on a resource even when null.
     */
    protected Boolean useWhenNotSet;
    
    private Object data;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getUseWhenNotSet() {
		return useWhenNotSet;
	}

	public void setUseWhenNotSet(Boolean useWhenNotSet) {
		this.useWhenNotSet = useWhenNotSet;
	}
    
	public boolean isNullable() {
		return nullable;
	}

	public Parameter() {
        super();
    }

    public Parameter(String staticValue) {
        super();
        this.value = new Value(staticValue);
    }

    public Parameter(Parameter otherParam) {
    	this.adapter = otherParam.adapter;
    	this.expression = otherParam.expression;
    	this.name = otherParam.name;
    	this.nullable = otherParam.nullable;
    	this.placeHolders = otherParam.placeHolders;
    	if (otherParam.ref != null) {
    		this.ref = new Reference(otherParam.ref);
    	}	
    	if (otherParam.value != null) {
    		this.value = new Value(otherParam.value.getStaticValue());	
    	}
    }
    
    public Parameter(String name, Reference ref, Value value, AdapterMapping adapter) {
        super();
        this.name = name;
        this.ref = ref;
        this.value = value;
        this.adapter = adapter;
    }
    
    public Parameter(final String name, final Value value) {
        super();
        this.name = name;
        this.value = value;
    }

    public Parameter(final String name, final Reference ref) {
        super();
        this.name = name;
        this.ref = ref;
    }

    public Parameter(final Reference ref) {
    	this(null, ref);
    }

    public Parameter(Reference reference, Value value) {
    	this(null, reference, value, null);
	}

	public String getName() {
        return name;
    }

	/**
	 * Method to get the root for this name. Examples:
	 * - root.key1.key2 = root
	 * - key1.key2      = key1
	 * - key2           = key2
	 * @return
	 */
	public final String getNameRoot(){
		String key = this.name;
		int dotMark = key.indexOf(Parameter.OBJECT_DELIMITER);
		return (dotMark>-1)?key.substring(0, dotMark):key;
	}
	
	//TODO: if one of the values is not a number
    public int compareTo(Parameter otherParameter) {
        int value1 = 0;
        int value2 = 0;

        try {
            value1 = Integer.parseInt(getName());
        }catch (NumberFormatException e) {
            // counted for, ordering is only for numbered parameters
        }
        try {
        	value2 = Integer.parseInt(otherParameter.getName());
        }catch (NumberFormatException e) {
            // counted for, ordering is only for numbered parameters
        }
        return value1 - value2;
    }

    public Reference getRef() {
        return ref;
    }

    public AdapterMapping getAdapter() {
        return adapter;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRef(Reference ref) {
        this.ref = ref;
    }

    public TypeDefinition getType() {
        return value != null ? value.getType() : null;
    }
    public String getOutputClass() {
    	return value != null ? value.getOutputClass() : null;
	}
    public void setType(String type) {
    	TypeDefinition def = new TypeDefinition(type);
    	if(value == null)
    		value = new Value();
    	value.setType(def);
    }
    
    /**
     * shallow clone
     */
    public Object clone() throws CloneNotSupportedException {
		Parameter clone = (Parameter)super.clone();
		if (this.adapter != null) {
			clone.adapter = this.adapter;	
		}
		if (this.name != null) {
			clone.name = new String(this.name);	
		}
		if (this.ref != null) {
			clone.ref = new Reference(this.ref);	
		}
		if (this.value != null) {
			clone.value = new Value(this.value.getStaticValue());	
		}
		if (this.expression != null) {
			clone.expression = new String(this.expression);	
		}
		clone.nullable = this.nullable;
		if (placeHolders != null) {
			clone.placeHolders = new ArrayList<PlaceHolder>();
		    Iterator<PlaceHolder> itr = placeHolders.iterator();
		    while (itr.hasNext()){
		    	Object o = itr.next();
		    	if (o instanceof PlaceHolder){
		    		PlaceHolder origPlaceHolder = (PlaceHolder)o;
		    		PlaceHolder clonePlaceHolder = (PlaceHolder)origPlaceHolder.clone();
		    		clone.placeHolders.add(clonePlaceHolder);
		    	}
		    }
		}
		return clone;
	}

	public boolean hasPlaceHolders() {
		return placeHolders!=null && placeHolders.size()>0;
	}

	public List<PlaceHolder> getPlaceHolders() {
		return placeHolders;
	}

	/**
     * method to add a placeHolder to a placeHolder list. The list will be
     * created when null 
     * @param placeHolder
     * @throws IllegalArgumentException - when object param passed is null or reference within the item is null
     */
    public void add(PlaceHolder placeHolder) {
    	if(placeHolder==null)
    		throw new IllegalArgumentException("placeHolder cannot be null");
    	if(placeHolders==null)
    		placeHolders = new ArrayList<PlaceHolder>();
    	
    	placeHolders.add(placeHolder);
    }
    
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void addAll(List<PlaceHolder> placeHolders) {
		if(placeHolders!=null){
			if(this.placeHolders==null)
				this.placeHolders = new ArrayList<PlaceHolder>();
			this.placeHolders.addAll(placeHolders);
		}
	}

}
