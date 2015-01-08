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
package com.qualogy.qafe.business;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.action.ItemExecuter;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.transaction.TransActionRolledBackException;
import com.qualogy.qafe.business.transaction.TransactionManagerBuilder;
import com.qualogy.qafe.business.transaction.TransactionTemplateBuilder;
import com.qualogy.qafe.core.application.DestoryFailedException;
import com.qualogy.qafe.core.application.InitializationFailedException;
import com.qualogy.qafe.core.conflictdetection.UpdateConflictException;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.BusinessManager;
import com.qualogy.qafe.core.framework.business.UnableToManageException;
import com.qualogy.qafe.core.framework.business.UnableToProcessException;

/**
 * implementation of the businessprocessor interface.
 * 
 * @author Marc van der Wurff
 * 
 */
public class BusinessActionManagerImpl implements BusinessManager {

    private static final Logger LOG = Logger.getLogger(BusinessActionManagerImpl.class.getName());

    public void manage(final ApplicationContext context, final DataIdentifier dataId,
            final BusinessAction action) throws ExternalException {
        manage(context, dataId, action, false);
    }

    public void manage(final ApplicationContext context, final DataIdentifier dataId,
            final BusinessAction action, final boolean nestedAction) throws ExternalException {
        try {
            validate(context, dataId, action, nestedAction);
            doManage(context, dataId, action, nestedAction);
        } catch (ExternalException e) {
            throw e;
        } catch (UpdateConflictException e) {
            throw e;
        } catch (Exception e) {
            String message = e.getMessage();
            throw new UnableToManageException(message, e);
        }
    }

    private void doManage(final ApplicationContext context, final DataIdentifier dataId,
            final BusinessAction action, final boolean nestedAction) throws ExternalException {

        try {
            final PlatformTransactionManager transactionManager =
                TransactionManagerBuilder.build(context, action);

            if (transactionManager == null) {
                internaldoManage(context, dataId, action, nestedAction);
            } else {
                TransactionBehaviour behaviour = action.getTransactionBehaviour();
                TransactionTemplate transactionTemplate =
                    TransactionTemplateBuilder.build(transactionManager, behaviour);

                transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            LOG.info("proceeding with action[" + action.getId() + "] within transaction");
                            internaldoManage(context, dataId, action, nestedAction);
                        } catch (ExternalException e) {
                            throw new TransActionRolledBackException(e);
                        } catch (RuntimeException e) {
                            throw new TransActionRolledBackException(e);
                        }
                    }
                });
            }
        } finally {
            // if(logger.isDebugEnabled())
            // logger.debug("after transaction -> " + DataStore.toLogString(id));
        }
        // TODO: after rollback datastore?
    }

    private void validate(final ApplicationContext context, final DataIdentifier dataId,
            final BusinessAction action, final boolean nestedAction) {
        if (action == null) {
            throw new IllegalArgumentException(
                    "Business Action is null, possible cause is that the business-action 'ref' does not match with any existing business action in the business tier.");
        }

        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        if (dataId == null) {
            throw new IllegalArgumentException("dataId is null");
        }

        if (!nestedAction && action.isPrivate()) {
            throw new IllegalArgumentException(
                    "Cannot access private business action ["
                            + action.getId()
                            + "] from outside business tier. Either change the visibility of this action or wrap the action in a public action.");
        }
    }

    private void internaldoManage(final ApplicationContext context, final DataIdentifier dataId,
            final BusinessAction action, final boolean nestedAction) throws ExternalException {

        try {
            ItemExecuter.execute(context, dataId, action.getBusinessActionItems());
        } catch (UnableToProcessException e) {
            final String header =
                "Stack, error occured:\n" + "BusinessAction: [" + action != null ? action.getId() : null
                        + "] \n" + "Exception: " + e.getMessage() + "\n" + DataStore.toLogString(dataId);
            StackRecorder.addHeader(dataId, header);
            LOG.warning(StackRecorder.toLogString(dataId));
        }

        if (LOG.isLoggable(Level.INFO)) {
            String message = "Succesfully performed: " + action.getId() + "\n";
            message += DataStore.toLogString(dataId);
            LOG.info(message);
        }

        LOG.fine("Application: " + (context != null ? context.getName() : "") + ", BusinessAction: "
                + (action != null ? action.getId() : ""));

    }

    public void destroy(ApplicationContext context) throws DestoryFailedException {
        ResourcePool.getInstance().destroy(context);
    }

    public void init(ApplicationContext context) throws InitializationFailedException {
        ResourcePool.getInstance().init(context);
    }

    public void refresh(ApplicationContext context) throws InitializationFailedException {
        ResourcePool.getInstance().init(context);
    }
}
