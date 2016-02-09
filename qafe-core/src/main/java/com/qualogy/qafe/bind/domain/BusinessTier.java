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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.commons.type.TypeDefinition;

/**
 * Holder for businesstier domain. Holds typedefinitions and businessactions
 * 
 * @author 
 */
public class BusinessTier implements Serializable, Tier, Validatable {

    private static final long serialVersionUID = 8200783129167836482L;

    protected List<BusinessAction> businessActions;
    protected List<TypeDefinition> typeDefinitions;

    /**
     * method validates businessactions. see businessaction for details.
     * 
     * @throws ValidationException
     */
    public void validate() throws ValidationException {
        if (businessActions != null) {
            for (Iterator<BusinessAction> iter = businessActions.iterator(); iter.hasNext();) {
            	BusinessAction action = (BusinessAction) iter.next();
                action.validate();
                action.enrich();
                
            }
        }
        
        if (typeDefinitions != null) {
        	for (Iterator<TypeDefinition> iter = typeDefinitions.iterator(); iter.hasNext();) {
				TypeDefinition typeDefinition = (TypeDefinition) iter.next();
				int i = 0;
				for (Iterator<TypeDefinition> innerIter = typeDefinitions.iterator(); i<2 && innerIter.hasNext();) {
					TypeDefinition otherTypeDefinition = (TypeDefinition) innerIter.next();
					if(typeDefinition.equals(otherTypeDefinition))
						i++;
				}
				if(i>1)
					throw new ValidationException("typeDefenitions on BusinessTier contains a duplicate TypeDefinition for typeid["+typeDefinition.getId()+"](in some case)");
			}
        }
    }

    /**
     * @return the businessActions
     */
    public List<BusinessAction> getBusinessActions() {
        return businessActions;
    }

    /**
     * @return the typeDefinitions
     */
    public List<TypeDefinition> getTypeDefinitions() {
        return typeDefinitions;
    }

    /**
     * Method to add a TypeDefinition to a TypeDefinition list. 
     * The list will be created when null.
     * The given typedefenition will only be added to the internal holder
     * when not already in the list, checked by {@link List.contains} and
     * the {@link TypeDefinition.equals} method.
     * @param typeDefinition
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(TypeDefinition typeDefinition) {
        if (typeDefinition == null)
            throw new IllegalArgumentException("typeDefinition cannot be null");
        
        if (typeDefinitions == null)
            typeDefinitions = new ArrayList<TypeDefinition>();

        if (!typeDefinitions.contains(typeDefinition))
            typeDefinitions.add(typeDefinition);
    }

    /**
     * method to add a BusinessAction to a BusinessAction list. The list will be created when null
     * 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(BusinessAction action) {
        if (action == null)
            throw new IllegalArgumentException("action cannot be null");
        if (businessActions == null)
            businessActions = new ArrayList<BusinessAction>();

        businessActions.add(action);
    }
    
    /**
     * method to add a BusinessAction to a BusinessAction list. The list will be created when null
     * 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void addAll(List<BusinessAction> actions) {
        if (actions == null)
            throw new IllegalArgumentException("action cannot be null");
        
        if (businessActions == null)
            businessActions = actions;
        else if(actions!=null)
        	businessActions.addAll(actions);
    }
    
    public void addAll(BusinessTier otherBusinessTier){
    	addAll(otherBusinessTier.getBusinessActions());
    	
    	if(typeDefinitions==null)
    		typeDefinitions = otherBusinessTier.getTypeDefinitions();
    	else if(otherBusinessTier.getTypeDefinitions()!=null)
    		typeDefinitions.addAll(otherBusinessTier.getTypeDefinitions());
    }
    /**
     * method to get a businessaction for given actionId
     * @param actionId - search criteria
     * @throws IllegalArgumentException when actionId is null or no businessaction
     * is found for actionId
     * @return businessaction
     */
    public BusinessAction getBusinessAction(String actionId){
    	if(actionId==null)
    		throw new IllegalArgumentException("actionId cannot be null");
    	
    	BusinessAction action  = null;
    	for (BusinessAction tmp : businessActions) {
			if(actionId.equals(tmp.getId())){
				action = tmp;
				break;
			}
		}
    	if(action==null)
    		throw new IllegalArgumentException("no businessaction found for id ["+actionId+"]");
    	
    	return action;
    }
}
