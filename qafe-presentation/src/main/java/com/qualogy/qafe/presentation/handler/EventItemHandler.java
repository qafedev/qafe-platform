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
package com.qualogy.qafe.presentation.handler;

import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.presentation.BusinessActionItemDataObject;
import com.qualogy.qafe.presentation.EventItemDataObject;

public class EventItemHandler {

    private static EventItemHandler singleton = null;

    public static EventItemHandler getInstance() {
        if (singleton == null) {
            singleton = new EventItemHandler();
        }
        return singleton;
    }

    public Map<String, Object> execute(EventItemDataObject eventItemDataObject) throws ExternalException {
        final ApplicationIdentifier applicationIdentifier =
            new ApplicationIdentifier(eventItemDataObject.getApplicationId());
        final ApplicationContext context = ApplicationCluster.getInstance().get(applicationIdentifier);
        Map<String, Object> outputValues = null;
        final DataIdentifier dataId = DataStore.register();
        if (eventItemDataObject instanceof BusinessActionItemDataObject) {
            final BusinessActionRefHandler businessActionRefHandler = new BusinessActionRefHandler();
            BusinessActionItemDataObject businessActionItemDataObject =
                (BusinessActionItemDataObject) eventItemDataObject;
            outputValues = businessActionRefHandler.execute(businessActionItemDataObject, context, dataId);
        }

        return outputValues;
    }

}
