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
package com.qualogy.qafe.business.action;

import java.util.List;
import java.util.ListIterator;

import com.qualogy.qafe.bind.commons.error.ErrorHandler;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.business.integration.adapter.PredefinedAdapterFactory;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.errorhandling.ErrorProcessor;
import com.qualogy.qafe.core.errorhandling.ErrorResult;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToProcessException;

/**
 * @author Marc van der Wurff
 */
public abstract class ItemExecuter {
	
	public static void execute(ApplicationContext context, DataIdentifier dataId, List<? extends Item> items) throws ExternalException, UnableToProcessException {
		if (items != null) {
			ListIterator<? extends Item> itrItem = items.listIterator();
			while (itrItem.hasNext()) {
				Item item = itrItem.next();
				if (item instanceof ErrorHandler) {
					continue;
				}
				try {
					ItemExecuter executer = ItemExecuterFactory.create(item);
					executer.execute(context, dataId, item);	
				} catch (ExternalException e) {
					ErrorResult errorResult = ErrorProcessor.processError(itrItem, item, context, dataId, e);
					boolean rethrow = (errorResult.getExternalException() != null);
					if (rethrow) {
						throw errorResult.getExternalException();
					} else if (errorResult.hasItems()) {
						execute(context, dataId, errorResult.getItems());
					}
				}
			}
		}
	}
	
	protected final void processIn(ApplicationContext context, DataIdentifier dataId, List inputParamList){
		processParameters(context, dataId, inputParamList);
	}
	
	protected final void processOut(ApplicationContext context, DataIdentifier dataId, List outputParamList){
		processParameters(context, dataId, outputParamList);
	}
	
	private void processParameters(ApplicationContext context, DataIdentifier dataId, List paramList){
		if (paramList != null) {
			for (int i=0; i< paramList.size(); i++) {
				Parameter parameter = (Parameter)paramList.get(i);
				Object value = ParameterValueHandler.get(context, dataId, parameter);
				if (PredefinedAdapterFactory.canObjectBeConverted(parameter.getType(), value)) {
					value = PredefinedAdapterFactory.create(parameter.getType()).convert(value);
				}
				DataStore.store(dataId, parameter.getName(), value);
			}
		}
	}
	
	/**
	 * Abstract method which an item must implement. Method to execute the item.
	 * @param dataId
	 * @param item
	 * @throws UnableToProcessException
	 */
	public abstract void execute(ApplicationContext context, DataIdentifier dataId, Item item) throws ExternalException;
}
