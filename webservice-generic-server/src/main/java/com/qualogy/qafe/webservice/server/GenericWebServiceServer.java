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
package com.qualogy.qafe.webservice.server;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.frontend.ServerFactoryBean;

public abstract  class GenericWebServiceServer {

	private static String baseAddress = "/";
	private static final Log logger = LogFactory.getLog(GenericWebServiceServer.class);
	private Bus bus;

    public GenericWebServiceServer(Bus bus) {
    	this.bus = bus;
    }

    public GenericWebServiceServer() {}
    
	/**
	 * Creates a webservice endpoint for an interface/implementation pair based on their fullnames<p>
	 * Classes and Interfaces to be used as webservice should be made available in the pom.xml
	 * 
	 * @param	serviceInterface  		The interface fullname of the webservice to be added
	 * @param	serviceImplementation	The implementation class fullname of the webservice to be added
	 * @throws Exception 
	 * @see		WebServiceServlet, ServerFactoryBean
	 */
    public void addService(String serviceInterface, String serviceImplementation) throws Exception
    {
    	String serviceName = extractServiceName(serviceInterface);
        ServerFactoryBean svrFactory = new ServerFactoryBean();
    	try {
    		if(bus != null) {
    			svrFactory.setBus(bus);
    		}
			svrFactory.setServiceClass(Class.forName(serviceInterface));
			svrFactory.setAddress(baseAddress + serviceName);
			svrFactory.setServiceBean(Class.forName(serviceImplementation).newInstance());
	        svrFactory.getServiceFactory().setDataBinding(new AegisDatabinding());
	        svrFactory.create();
	        System.out.println(svrFactory.getServiceFactory().getEndpointInfo().getAddress());

		} catch (Exception e) {
			logger.error("Error adding webservices with interface : " + serviceInterface  + " and implementation : " + serviceImplementation, e);
			throw e;
		} 
    }

	protected String extractServiceName(String serviceInterface) {
		return serviceInterface.substring(serviceInterface.lastIndexOf('.') + 1);
	}
    
	/**
	 * Return the FQN for the interface class
	 * @return
	 */
	public abstract String getInterface();
	
	/**
	 * Return the FQN for the implementation class of the @getInterface() method 
	 * @return
	 */
			
	public abstract String getImplementation();
	
	protected void setBaseAddress(String baseAddress) {
		this.baseAddress = baseAddress; 
	}
}