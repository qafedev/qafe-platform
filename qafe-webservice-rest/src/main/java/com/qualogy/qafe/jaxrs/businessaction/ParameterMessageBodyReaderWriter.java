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
package com.qualogy.qafe.jaxrs.businessaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.businessaction.impl.BusinessActionParameterBean;
import com.qualogy.qafe.core.application.ApplicationCluster;

/**
 * Due to the dynamic nature of business action parameters (number and type of input/output parameters are
 * unknown beforehand), this provider supports converting a json stream to a list of business action
 * parameters and vice versa.
 * 
 * @author sdahlberg
 * 
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class ParameterMessageBodyReaderWriter implements
        MessageBodyReader<List<BusinessActionParameterBean>>,
        MessageBodyWriter<List<BusinessActionParameterBean>> {

    private static final String UTF_8 = "UTF-8";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final Gson gson;

    public ParameterMessageBodyReaderWriter() {
        final String configDateFormat =
            ApplicationCluster.getInstance().getConfigurationItem(Configuration.WEB_SERVICE_FORMAT_DATE);

        final String dateFormat;
        if (configDateFormat == null) {
            dateFormat = DEFAULT_DATE_FORMAT;
        } else {
            dateFormat = configDateFormat;
        }

        gson = new GsonBuilder().setDateFormat(dateFormat).create();
    }

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
            final MediaType mediaType) {

        if (type != List.class && !(genericType instanceof ParameterizedType)) {
            return false;
        }

        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        if (actualTypeArguments.length == 0) {
            return false;
        }

        return actualTypeArguments[0] == BusinessActionParameterBean.class;
    }

    @Override
    public List<BusinessActionParameterBean> readFrom(final Class<List<BusinessActionParameterBean>> type,
            final Type genericType, final Annotation[] annotations, final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
            throws IOException {

        final List<Map<String, String>> fromJson = parseJson(entityStream);

        final List<BusinessActionParameterBean> inputParameters = toInputParameters(fromJson);

        return inputParameters;
    }

    @Override
    public long getSize(final List<BusinessActionParameterBean> parameters, final Class<?> type,
            final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations,
            final MediaType mediaType) {
        return isReadable(type, genericType, annotations, mediaType);
    }

    @Override
    public void writeTo(final List<BusinessActionParameterBean> parameters, final Class<?> type,
            final Type genericType, final Annotation[] annotations, final MediaType mediaType,
            final MultivaluedMap<String, Object> arg5, final OutputStream entityStream) throws IOException {

        final List<Map<String, Object>> jsonParameters = new ArrayList<Map<String, Object>>();

        for (final BusinessActionParameterBean parameterBean : parameters) {
            final Map<String, Object> keyValuePair = new HashMap<String, Object>();
            keyValuePair.put(parameterBean.getKey(), parameterBean.getValue());
            jsonParameters.add(keyValuePair);

        }

        final OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);

        try {
            gson.toJson(jsonParameters, writer);
        } finally {
            writer.close();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> parseJson(final InputStream inputStream) throws IOException {
        final InputStreamReader reader = new InputStreamReader(inputStream, UTF_8);
        try {
            // By default gson parses the Object into a list of maps of string to string
            return (List<Map<String, String>>) gson.fromJson(reader, Object.class);
        } finally {
            reader.close();
        }
    }

    private List<BusinessActionParameterBean> toInputParameters(final List<Map<String, String>> parsedInput) {

        final List<BusinessActionParameterBean> inputParameters =
            new ArrayList<BusinessActionParameterBean>(parsedInput.size());

        for (final Map<String, String> input : parsedInput) {
            final BusinessActionParameterBean inputParameterBean = new BusinessActionParameterBean();
            final Entry<String, String> next = input.entrySet().iterator().next();
            inputParameterBean.setKey(next.getKey());
            inputParameterBean.setValue(next.getValue());

            inputParameters.add(inputParameterBean);
        }

        return inputParameters;
    }
}
