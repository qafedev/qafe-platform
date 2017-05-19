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
package com.qualogy.qafe.webservice.servlet;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import com.qualogy.qafe.webservice.server.GenericWebServiceServer;

public abstract class GenericWebServiceServlet extends CXFNonSpringServlet {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(GenericWebServiceServlet.class);

	/**
	 * Retrieves interface- and implementation class-names out of web.xml context parameter prefixed with "webservice." to add them as 
	 * webservices to the the WebServiceServer
	 * 
	 * @param	servletConfig  The ServletConfig of the webapplication
	 * @see		WebServiceServer
	 */
	@Override
    public void loadBus(ServletConfig servletConfig) throws ServletException {
        super.loadBus(servletConfig);        
        
        // sets the busfactory needed to link the webapp url to the webservice
        System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME, "org.apache.cxf.bus.CXFBusFactory");
        
        Map<String, String> webservices = extractWebservices(servletConfig.getServletContext());
 

        Bus bus = this.getBus();
        BusFactory.setDefaultBus(bus);
        try {
        	GenericWebServiceServer webServiceServer = createNewWebServiceServer(bus);
//        	for(String serviceInterface : webservices.keySet()) {
//            	webServiceServer.addService(serviceInterface, webservices.get(serviceInterface));       		
//        	}
        	String webServiceInterface = webServiceServer.getInterface();
        	String webServiceImplementation = webServiceServer.getImplementation();
        	webServiceServer.addService(webServiceInterface, webServiceImplementation);
		} catch (Exception e) {
			logger.error("Error adding webservices", e);
			throw new ServletException("Error adding webservices", e);
		}
	}

	public abstract GenericWebServiceServer createNewWebServiceServer(Bus bus);

	protected Map<String, String> extractWebservices(ServletContext servletContext) {
		Map<String, String> webservices = new HashMap<String, String>();
        
        Enumeration<String> paramNames = (Enumeration<String>) servletContext.getInitParameterNames();
  
        while(paramNames.hasMoreElements()) {
        	String parameterName = paramNames.nextElement();
        	extractWebserviceParameter(webservices, parameterName, servletContext.getInitParameter(parameterName));
        }
		return webservices;
	}

	protected void extractWebserviceParameter(Map<String, String> webservices, String paramName, String paramValue) {
		if(isWebserviceParam(paramName)) {
			String webserviceInterface = getWebserviceInterface(paramValue);
			String webserviceImpl = getWebserviceImplementation(paramValue);

			webservices.put(webserviceInterface, webserviceImpl);
		}
	}

	protected boolean isWebserviceParam(String paramName) {
		return paramName.indexOf("webservice.") > -1;
	}


	protected String getWebserviceInterface(String paramValue) {
    	String webserviceInterface = paramValue.trim();
		if(hasImplementationDefined(paramValue)) {
			webserviceInterface = (paramValue.split(",")[0]).trim();
    	} 
		
		return webserviceInterface;
	}
	
	protected String getWebserviceImplementation(String paramValue) {
    	String webserviceImplementation = paramValue.trim() + "Impl";
		if(hasImplementationDefined(paramValue)) {
			webserviceImplementation = (paramValue.split(",")[1]).trim();
    	} 
		
		return webserviceImplementation;
	}
	
	protected boolean hasImplementationDefined(String paramValue) {
		return paramValue.indexOf(',') > -1;
	}

}
