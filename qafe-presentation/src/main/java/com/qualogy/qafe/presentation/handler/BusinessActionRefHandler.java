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
package com.qualogy.qafe.presentation.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.presentation.BusinessActionItemDataObject;
import com.qualogy.qafe.presentation.builtins.BuiltinConvertor;
import com.qualogy.qafe.presentation.builtins.BuiltinConvertorImpl;
import com.qualogy.qafe.presentation.handler.executors.EventItemExecuteHelper;

/**
 * 
 * Handler for Business Action Event Items
 *
 */
public class BusinessActionRefHandler {

    public final Map<String, Object> execute(final BusinessActionItemDataObject businessActionItemDataObject,
            final ApplicationContext context, final DataIdentifier dataId) throws ExternalException {

        final BusinessAction businessAction =
            context.getApplicationMapping().getBusinessTier()
                .getBusinessAction(businessActionItemDataObject.getBusinessActionId());

        Map<String, Object> outputValues = new HashMap<String, Object>();

        try {
            final String sessionId = businessActionItemDataObject.getSessionId();
            final String windowId = businessActionItemDataObject.getWindowId();

            storeValues(businessActionItemDataObject.getInputValues(), dataId, windowId, sessionId);
            storeValues(businessActionItemDataObject.getInternalVariables(), dataId, windowId, sessionId);

            context.getBusinessManager().manage(context, dataId, businessAction);

            outputValues = collectOutputValues(businessActionItemDataObject, dataId);

        } catch (final ExternalException e) {
            throw e;
        }

        return outputValues;
    }

    private Map<String, Object> collectOutputValues(final BusinessActionItemDataObject businessActionItemDataObject,
            final DataIdentifier dataId) {
        Map<String, Object> outputValues = new HashMap<String, Object>();
        Map<String, String> outputVariables = businessActionItemDataObject.getOutputVariables();
        for(String outName : outputVariables.keySet()) {
            String outReference = outputVariables.get(outName);
            Object value = DataStore.getValue(dataId, outReference);
            outputValues.put(outName, value);
        }
        
        // For sending the updated internal variables back to the client-side
        collectInternalVariables(dataId, outputValues);
        
        collectBuiltInsFromBackend(dataId, outputValues);
        
        return outputValues;
    }

	private void collectInternalVariables(final DataIdentifier dataId,
			Map<String, Object> outputValues) {
		for (String keyword : DataStore.KEY_WORDS) {
        	Object value = DataStore.findValue(dataId, keyword);
        	if (value != null) {
        		outputValues.put(keyword, value);
        	}
        }
	}
	
	private void collectBuiltInsFromBackend(DataIdentifier dataId, Map<String, Object> outputValues) {
		Object builtInList = DataStore.findValue(dataId, DataStore.KEY_WORD_QAFE_BUILT_IN_LIST);
		if (builtInList == null || !(builtInList instanceof String)) {
			return;
		}
		BuiltinConvertor builtInConverter = new BuiltinConvertorImpl();
		List<BuiltInFunction> builtIns = builtInConverter.convert((String) builtInList);
		if (builtIns == null) {
			return;
		}
		outputValues.put(DataStore.KEY_WORD_QAFE_BUILT_IN_LIST, builtIns);
	}


    private void storeValues(final Map<String, Object> values, final DataIdentifier dataId
    		, final String windowId, final String sessionId) {
    	if (values == null) {
    		return;
    	}
    	for (String key : values.keySet()) {
            Object value = values.get(key);
            if (EventItemExecuteHelper.isLookupData(value)) {
                Parameter parameter = new Parameter();
                parameter.setName(key);
                value = EventItemExecuteHelper.resolveLookupData(dataId, windowId, sessionId, parameter, value);
            }
            DataStore.store(dataId, key, value);
        }
    }
}