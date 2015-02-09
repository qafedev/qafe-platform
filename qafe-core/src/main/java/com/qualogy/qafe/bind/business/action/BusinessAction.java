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
package com.qualogy.qafe.bind.business.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;
import com.qualogy.qafe.bind.domain.BindBase;
import com.qualogy.qafe.bind.integration.service.ServiceRef;

/**
 * Business logic action type
 * @author 
 * @author Gideon Liem
 *
 */
public class BusinessAction extends BindBase implements Validatable {

	private static final long serialVersionUID = -3413242264532864852L;

	public final static String ACTION_ACCESS_PUBLIC = "public";
	public final static String ACTION_ACCESS_PRIVATE = "private";
	
	protected String access;
	
	protected String description;
	protected String id;
	protected List<BusinessActionItem> businessActionItems;

	protected TransactionBehaviour transactionBehaviour = TransactionBehaviour.createDefault();
	
	public BusinessAction(String id) {
		super();
		this.id = id;
	}

	public BusinessAction() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<BusinessActionItem> getBusinessActionItems() {
		return businessActionItems;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * Method to validate a businessaction. this method validates if items
	 * on this businessaction set directly on the protected member contain
	 * an actual reference to a businessaction or service. This method is
	 * written for of uncontrolled behaviour of third-party tools
	 * @throws ValidationException - when item has no ref
	 */
	public void validate() throws ValidationException{
		if(businessActionItems!=null){	
			for(BusinessActionItem item : businessActionItems) {
				validate(item);
			}
		}
	}

	private void validate(BusinessActionItem item) throws ValidationException {
		if (item instanceof ServiceRef) {
			ServiceRef serviceRef = (ServiceRef)item;
			if (serviceRef.getRef() == null) {
				throw new ValidationException("Service [" + serviceRef.getRefId() + "] not found for business-action [" + id + "], check your business-action with a reference to a service and make sure the reference point to an existing service");
			}
			if (serviceRef.getMethod() == null) {
				String methodId = serviceRef.getMethodRef().getRef();
				throw new ValidationException("Method [" + methodId + "] not found in service [" + serviceRef.getRefId() + "] for business-action [" + id + "], check your business-action with a reference to a service and make sure the reference point to an existing method");
			}	
		} else if (item instanceof BusinessActionRef) {
			BusinessActionRef businessActionRef = (BusinessActionRef)item;
			if (businessActionRef.getRef() == null) {
				throw new ValidationException("Business Action [" + businessActionRef.getRefId() + "] not found for business-action [" + id + "], possible cause is that the business-action 'ref' does not match with any existing business action in the business tier");
			}
		}
	}
	
	public TransactionBehaviour getTransactionBehaviour() {
		return transactionBehaviour;
	}

	public void setTransactionBehaviour(TransactionBehaviour transactionBehaviour) {
		if(transactionBehaviour==null)
			throw new IllegalArgumentException("transactionBehaviour cannot be null");
		this.transactionBehaviour = transactionBehaviour;
	}
   
	/**
     * method to add a BusinessActionItem to a BusinessActionItem list. The list will be
     * created when null 
     * @param typeDefinition
     * @throws IllegalArgumentException - when object param passed is null or reference within the item is null
     * @throws ValidationException - when item to add has no ref to an actual instance
     */
    public void add(BusinessActionItem businessActionItem) {
    	if(businessActionItem==null)
    		throw new IllegalArgumentException("businessActionItem cannot be null");
    	if(businessActionItems==null)
    		businessActionItems = new ArrayList<BusinessActionItem>();
    	
    	try {
			validate(businessActionItem);
		} catch (ValidationException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
    	
    	businessActionItems.add(businessActionItem);
    }

    /*
     * @pre this.validate() has been called
     */
	public void enrich() {
		if(businessActionItems!=null){
			for(BusinessActionItem item : businessActionItems) {
				if (item instanceof ServiceRef) {
					((ServiceRef)item).enrich();
				}
			}
		}
	}

	public boolean isPrivate(){
		return this.access!=null && this.access.equals(ACTION_ACCESS_PRIVATE);
	}
	
	public String getDescription() {
		return description;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}
