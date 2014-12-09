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
package com.qualogy.qafe.bind.core.application;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.business.transaction.GlobalTransactions;
import com.qualogy.qafe.bind.core.messages.HasMessage;
import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.core.security.AuthenticationControlled;
import com.qualogy.qafe.bind.core.security.AuthenticationModule;
import com.qualogy.qafe.bind.core.security.AuthorizationControlled;
import com.qualogy.qafe.bind.core.security.AuthorizationRule;
import com.qualogy.qafe.bind.core.security.SecuritySettings;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.util.InterfaceScanner;
import com.qualogy.qafe.core.application.ContainerManager;
import com.qualogy.qafe.core.application.LoadFailedException;
import com.qualogy.qafe.core.framework.business.BusinessManager;
import com.qualogy.qafe.core.framework.presentation.EventHandler;
import com.qualogy.qafe.core.script.ScriptEngineManager;

/**
 * 
 * @author
 * 
 */
public class ApplicationContext implements Serializable, PostProcessing, AuthenticationControlled,
        AuthorizationControlled {

    private static final Logger LOG = Logger.getLogger(ApplicationContext.class.getName());

    private static final long serialVersionUID = 6623946831871605896L;

    /**
     * Private datamember to keep track of load errors. This is especially interesting when a big
     * application-config, with more than one application fails. In the previous implementation when an error
     * occured (it doesn't matter which of the applications) all the applications would not load also. This is
     * quite tedious and not very friendly. So if an application failes to load, the rootCause message will be
     * stored in this variable.
     */
    private Throwable loadException;

    public Throwable getLoadException() {
        return loadException;
    }

    public final static String DEFAULT_APP_CONFIG_FILE_NAME = "application-config.xml";

    // /////////// context info ///////////////////
    protected String root;

    protected String name;// non unique

    protected ApplicationIdentifier id;// unique user defined id

    protected int order;

    protected String rootMenu;

    public String getRootMenu() {
        return rootMenu;
    }

    public void setRootMenu(String rootMenu) {
        this.rootMenu = rootMenu;
    }

    // /////////// messages ///////////////////
    /**
     * Bind variable for application messages
     */
    protected Messages messages;

    // /////////// container managers ///////////////////
    private EventHandler eventManager;

    private BusinessManager businessManager;

    // /////////// mappings ///////////////////
    /**
     * the final product of a merge between interpreted mappingFileLocations and assignedMappings, the public
     * getApplicationMapping on this class will return this merged mapping.
     */
    private ApplicationMapping mergedApplicationMapping;

    /**
     * List for application mapping file locations (path)
     */
    protected List<FileLocation> mappingFileLocations;

    public List<FileLocation> getMappingFileLocations() {
        return mappingFileLocations;
    }

    /**
     * List for concrete/assigned ApplicationMappings
     */
    protected List<ApplicationMapping> concreteApplicationMappings;

    // /////////// configuration ///////////////////
    /**
     * holder for configuration items
     */
    protected Configuration applicationConfiguration;

    /**
	 * 
	 */
    protected SecuritySettings securitySettings;

    /**
     * authorization rule controlling this object
     */
    private AuthorizationRule controllingAuthorizationRule;

    private boolean authenticationRequired;// is authentication required for this application

    /**
     * String which can contain the filepath to the application context file this application context origins
     * from
     */
    private URI originAppConfigFileLocation;

    protected GlobalTransactions globalTransactions;

    /**
     * boolean property can be used to set auto-scan-root property true saying automatic scan root directory
     * if no application-mapping is set. (false means don't scan (default))
     */
    protected boolean autoScanRoot;

    /**
     * boolean indicating to scan sub directories as well, only picked up when autoScanRoot is true
     */
    protected boolean recursiveScan;

    private AuthenticationModule module;

    private List<String> warningMessages = new ArrayList<String>();

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    /**
     * private default constructor
     */
    public ApplicationContext() {
        super();
    }

    /**
     * convinience method to get a configuration item from the configuration returns null if item not found
     * for key or config is null
     * 
     * @return
     */
    public String getConfigurationItem(String key) {
        return applicationConfiguration != null ? applicationConfiguration.get(key) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFileLocation(FileLocation fileLocation) {
        if (mappingFileLocations == null)
            mappingFileLocations = new ArrayList<FileLocation>();
        mappingFileLocations.add(fileLocation);
    }

    public String getRoot() {
        return root;
    }

    /**
     * method called after binding of the context file. method takes care of merging and binding the
     * application-mappings
     */
    public void performPostProcessing() {
        try {
            ApplicationContextPostProcessHelper.postProcess(this);
        } catch (LoadFailedException e) {
            LOG.log(Level.WARNING, "ApplicationContext [id=" + getId() + ",name=" + getName()
                    + "] failed to load", e);
            handleLoadFailure(ExceptionUtils.getRootCause(e));
        }
    }

    public void postset(IUnmarshallingContext context) {
        if (this.root == null)
            this.root = context.getDocumentName();

        performPostProcessing();
    }

    public ApplicationMapping getApplicationMapping() {
        return mergedApplicationMapping;
    }

    public BusinessManager getBusinessManager() {
        return this.businessManager;
    }

    public EventHandler getEventHandler() {
        return this.eventManager;
    }

    public ApplicationIdentifier getId() {
        return id;
    }

    public void add(ContainerManager containerManager) {
        if (containerManager instanceof EventHandler)
            this.eventManager = (EventHandler) containerManager;
        if (containerManager instanceof BusinessManager)
            this.businessManager = (BusinessManager) containerManager;
    }

    public URI getOriginAppConfigFileLocation() {
        return originAppConfigFileLocation;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public void destroy() {
        if (this.eventManager != null)
            this.eventManager.destroy(this);
        if (this.businessManager != null)
            this.businessManager.destroy(this);

        ScriptEngineManager.getEngine(this).destroy();
    }

    public void init() {
        if (this.eventManager != null)
            this.eventManager.init(this);
        if (this.businessManager != null)
            this.businessManager.init(this);
        fetchMessages(HasMessage.class);

        ScriptEngineManager.getEngine(this).init();
    }

    public void setOriginAppConfigFileLocation(URI originAppConfigFileLocation) {
        this.originAppConfigFileLocation = originAppConfigFileLocation;
    }

    public void setId(ApplicationIdentifier id) {
        this.id = id;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public AuthorizationRule getControllingAuthorizationRule() {
        return controllingAuthorizationRule;
    }

    public void setControllingAuthorizationRule(AuthorizationRule controllingAuthorizationRule) {
        this.controllingAuthorizationRule = controllingAuthorizationRule;
    }

    public void setAuthenticationRequired() {
        this.authenticationRequired = true;
    }

    public SecuritySettings getSecuritySettings() {
        return securitySettings;
    }

    public void setSecuritySettings(SecuritySettings securitySettings) {
        this.securitySettings = securitySettings;
    }

    public void setAuthenticationModule(AuthenticationModule module) {
        this.module = module;
    }

    public AuthenticationModule getAuthenticationModule() {
        return this.module;
    }

    public boolean isAuthorizationControlled() {
        return getControllingAuthorizationRule() != null;
    }

    public void setApplicationMapping(ApplicationMapping mapping) {
        this.mergedApplicationMapping = mapping;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Method to setup default settings for the ApplicationContext. This method will not overwrite existing
     * settings, but will fill the ones that are null.
     */
    public void setDefaults() {
        if (this.applicationConfiguration == null)
            this.applicationConfiguration = ConfigurationDefaults.getInstance().getAppConfiguration();
        else {
            this.applicationConfiguration.fillInTheBlanks(ConfigurationDefaults.getInstance()
                .getAppConfiguration());
            this.applicationConfiguration.fillInTheBlanks(ConfigurationDefaults.getInstance()
                .getGlobalConfiguration());
        }
    }

    public void handleLoadFailure(Throwable loadException) {
        handleLoadFailure(loadException, false);
    }

    public void handleLoadFailure(Throwable loadException, boolean loadAnyway) {
        // Set the load failure exception
        if (loadException.getMessage() != null && loadException.getMessage().equals("Premature end of file.")) {
            this.loadException = new Throwable("QAML file is empty/invalid. " + loadException.getMessage());
        } else {
            this.loadException = loadException;
        }

        if (!loadAnyway) {
            // "Unload" the application-mapping of this context,
            // the context will remain in the cluster, so when rendering it knows which one is failing
            setApplicationMapping(null);
        }
    }

    private void fetchMessages(Class<HasMessage> hasMessage) {
        List<HasMessage> hasMessages = InterfaceScanner.scan(this, HasMessage.class, "com.qualogy");
        if (hasMessages != null) {
            for (HasMessage hasMsg : hasMessages) {
                if (hasMsg.getMessages() != null) {
                    for (String msg : hasMsg.getMessages()) {
                        warningMessages.add(msg);
                    }
                }
            }
        }
    }
}
