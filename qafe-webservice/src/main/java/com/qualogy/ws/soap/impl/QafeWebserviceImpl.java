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
package com.qualogy.ws.soap.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import javax.jws.WebService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.ws.businessaction.BusinessActionExecutor;
import com.qualogy.ws.businessaction.impl.BusinessActionExecutorImpl;
import com.qualogy.ws.exception.NotFoundException;
import com.qualogy.ws.soap.QafeWebservice;

@WebService(endpointInterface = "com.qualogy.ws.soap.QafeWebservice")
public class QafeWebserviceImpl implements QafeWebservice {

    private final Gson gson;
    private final BusinessActionExecutor businessActionExecutor = new BusinessActionExecutorImpl();
    final Type typeOf = new TypeToken<Map<String,Object>>(){}.getType();
    
    public QafeWebserviceImpl() {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        
        Properties externalProperties = new Properties();
        
        InputStream in = this.getClass().getResourceAsStream("/external.properties");
        try {
            externalProperties.load(in);
            if (externalProperties.containsKey(Configuration.DATE_FORMAT)) {
                dateFormat = externalProperties.getProperty(Configuration.DATE_FORMAT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        gson = new GsonBuilder().setDateFormat(dateFormat).create();
    }
    
	@Override
	public String executeBusinessAction(String appId, String businessActionId, String inputAsJson) throws NotFoundException {
	    Map<String, Object> inputParams = gson.fromJson(inputAsJson, typeOf);
	    Object output = null;
        try {
            output = businessActionExecutor.execute(appId, businessActionId, inputParams);
        } catch (NotFoundException e) {
            throw e;
        }
	    	    
		return gson.toJson(output);
	}

}
