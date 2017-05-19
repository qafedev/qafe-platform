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
package com.qualogy.qafe.business.resource.rdb.query.call;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCOLLECTION;
import oracle.jdbc.oracore.OracleTypeUPT;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TypeDescriptor;

import com.qualogy.qafe.bind.commons.type.AdapterAttribute;
import com.qualogy.qafe.bind.commons.type.AdapterMapping;
import com.qualogy.qafe.bind.commons.type.AttributeMapping;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.integration.filter.page.Page;
import com.qualogy.qafe.business.integration.filter.page.ResultSetDataExtractor;
import com.qualogy.qafe.business.integration.rdb.MetaDataRowMapper;
import com.qualogy.qafe.commons.db.procedure.CallArgument;
import com.qualogy.qafe.commons.db.procedure.CallArguments;
import com.qualogy.qafe.core.datastore.DataMap;
import com.qualogy.qafe.core.datastore.DataStore;

/**
 * Oracle specific instance of the DBCall class.
 */
public class BaseCall extends DBCall {

    private static final Logger logger = Logger.getLogger(BaseCall.class.getName());

    private static final String TYPE_TABLE = "TABLE";

    private static final String TYPE_REF_CURSOR = "REF CURSOR";

    private static final String TYPE_REF = "REF";

    private static final String RETURN_NAME = "result";

    private boolean supportsNamedParameters = true;

    public BaseCall(boolean supportsNamedParameters) {
        this.supportsNamedParameters = supportsNamedParameters;
    }

    /**
     * could have called callStmt.getParameterMetaData(); per call but cheaper to do it ones
     */
    public CallableStatement prepareCall(Connection connection, Call call, Method method, Map inputParameters)
            throws SQLException {
        Connection conn = NativeConnectionHandler.getNativeConnection(connection);
        String sql = null;
        if (!call.isPrepared()) {
            if (call.isFunction()) {
                sql = createFunction(call);
            } else {
                sql = createProcedure(call);
            }
        } else {
            sql = call.getSql();
        }
        CallableStatement callStmt = conn.prepareCall(sql);
        CallArguments arguments = call.getArguments();
        if (arguments != null) {
            prepareOutputs(conn, callStmt, method, arguments.getAllResultableArgs());
            prepareInputs(conn, callStmt, method, arguments.getInArgs(), inputParameters);
        }
        return callStmt;
    }

    private void prepareOutputs(Connection conn, CallableStatement callStmt, Method method, List outputArgs)
            throws SQLException {
        if (outputArgs != null) {
            for (Object outputArg : outputArgs) {
                if (outputArg instanceof CallArgument) {
                    CallArgument argument = (CallArgument) outputArg;
                    registerOutput(conn, callStmt, argument, method.getOutput());
                }
            }
        }
    }

    private void registerOutput(Connection conn, CallableStatement callStmt, CallArgument argument,
            List<Out> outputs) throws SQLException {
        if (argument == null) {
            return;
        }
        AdapterMapping outputAdapter = getOutputAdapter(argument, outputs);
        int argumentIndex = argument.getSequence();
        int argumentType = argument.getSqlType();
        String argumentTypeName = getArgumentTypeName(outputAdapter, argument.getTypeName());
        if (isStructType(argument)) {
            argumentType = Types.STRUCT;
            callStmt.registerOutParameter(argumentIndex, argumentType, argumentTypeName);
        } else if (isArrayOfStructType(argument)) {
            argumentType = Types.ARRAY;
            callStmt.registerOutParameter(argumentIndex, argumentType, argumentTypeName);
        } else {
            callStmt.registerOutParameter(argumentIndex, argumentType);
        }
    }

    private void prepareInputs(Connection conn, CallableStatement callStmt, Method method, List inputArgs,
            Map inputValues) throws SQLException {
        if (inputArgs == null) {
            return;
        }
        Connection nativeConn = conn.getMetaData().getConnection();
        for (Object inputArg : inputArgs) {
            if (inputArg instanceof CallArgument) {
                CallArgument argument = (CallArgument) inputArg;

                int argumentIndex = argument.getSequence();
                String argumentTypeName = argument.getTypeName();
                int argumentType = argument.getSqlType();
                if (isStructType(argument)) {
                    argumentType = Types.STRUCT;
                } else if (isArrayOfStructType(argument)) {
                    argumentType = Types.ARRAY;
                }

                AdapterMapping inputAdapter = getInputAdapter(argument, method.getInput());

                Object argumentValue = null;
                String argumentName = argument.getArgumentName();
                if (inputValues.containsKey(argumentName)) {
                    Object inputValue = inputValues.get(argumentName);
                    argumentValue =
                        getArgumentValue(nativeConn, argumentName, argumentType, argumentTypeName,
                            inputValue, inputAdapter);
                } else {
                    String inputIndex = String.valueOf(argumentIndex - 1);
                    if (inputValues.containsKey(inputIndex)) {
                        Object inputValue = inputValues.get(inputIndex);
                        argumentValue =
                            getArgumentValue(nativeConn, inputIndex, argumentType, argumentTypeName,
                                inputValue, inputAdapter);
                    } else {
                        // TODO
                    }
                }
                callStmt.setObject(argumentIndex, argumentValue, argumentType);
            }
        }
    }

    private Object getArgumentValue(Connection nativeConn, String argumentName, int argumentType,
            String argumentTypeName, Object inputValue, AdapterMapping inputAdapter) throws SQLException {
        Object argumentValue = inputValue;
        argumentTypeName = getArgumentTypeName(inputAdapter, argumentTypeName);
        Map<String, Object> objectAttributes =
            getObjectAttributes(nativeConn, argumentType, argumentTypeName);
        if (argumentType == Types.STRUCT) {
            Object[] struct = createStruct(objectAttributes, (Map) argumentValue, inputAdapter);
            argumentValue = nativeConn.createStruct(argumentTypeName, struct);
        } else if (argumentType == Types.ARRAY) {
            List<Object[]> listOfStruct = new ArrayList<Object[]>();
            if (argumentValue instanceof Iterable) {
                Iterator itr = ((Iterable) argumentValue).iterator();
                while (itr.hasNext()) {
                    Object obj = itr.next();
                    if (obj instanceof Map) {
                        Object[] struct = createStruct(objectAttributes, (Map) obj, inputAdapter);
                        if (struct != null) {
                            listOfStruct.add(struct);
                        }
                    }
                }
            }
            TypeDescriptor typeDescriptor = getTypeDescriptor(nativeConn, argumentType, argumentTypeName);
            if (typeDescriptor instanceof ArrayDescriptor) {
                argumentValue =
                    new ARRAY((ArrayDescriptor) typeDescriptor, nativeConn, listOfStruct.toArray());
            }
        } else {
            if (argumentValue instanceof Date) {
                argumentValue = new java.sql.Date(((Date) argumentValue).getTime());
            }
        }
        return argumentValue;
    }

    private AdapterMapping getInputAdapter(CallArgument argument, List<In> inputs) {
        for (In input : inputs) {
            String inputName = input.getName();
            if (isArgument(argument, inputName)) {
                return input.getAdapter();
            }
        }
        return null;
    }

    private AdapterMapping getOutputAdapter(CallArgument argument, List<Out> outputs) {
        for (Out output : outputs) {
            String outputName = output.getName();
            String referenceName = null;
            Reference reference = output.getRef();
            if (reference != null) {
                referenceName = reference.stringValueOf();
            }
            if (isArgument(argument, referenceName) || isArgument(argument, outputName)) {
                return output.getAdapter();
            }
        }
        return null;
    }

    private boolean isArgument(CallArgument argument, String paramName) {
        String argumentName = argument.getArgumentName();
        if ((argumentName != null) && argumentName.equals(paramName)) {
            return true;
        } else {
            int argumentIndex = argument.getSequence();
            if (String.valueOf(argumentIndex).equals(paramName)) {
                return true;
            }
        }
        return false;
    }

    private String getArgumentTypeName(AdapterMapping adapter, String argumentTypeName) {
        if (adapter == null) {
            return argumentTypeName;
        }
        return adapter.getAdaptName();
    }

    private Object[] createStruct(Map<String, Object> attributes, Map data, AdapterMapping adapter) {
        Object[] struct = new Object[attributes.size()];
        if (data != null) {
            int attributeIndex = 0;
            Iterator<String> itrAttribute = attributes.keySet().iterator();
            while (itrAttribute.hasNext()) {
                String attribute = itrAttribute.next();
                if ((adapter != null) && (adapter.adaptAll() == false)) {
                    attribute = getAdapterAttribute(attribute, adapter);
                }
                Object attributeValue = data.get(attribute);
                if (attributeValue instanceof Date) {
                    attributeValue = new java.sql.Date(((Date) attributeValue).getTime());
                } else if ("".equals(attributeValue)) {
                    // TODO
                    attributeValue = null;
                }
                struct[attributeIndex] = attributeValue;
                attributeIndex++;
            }
        }
        return struct;
    }

    private String getAdapterAttribute(String attribute, AdapterMapping adapter) {
        String adapterAttribute = null;
        List<AdapterAttribute> adapterAttributes = adapter.getAdapterAttributes();
        if (adapterAttributes == null) {
            return adapterAttribute;
        }
        for (AdapterAttribute adapterAttr : adapterAttributes) {
            if (adapterAttr instanceof AttributeMapping) {
                AttributeMapping attributeMapping = (AttributeMapping) adapterAttr;
                if (attributeMapping.getName().equals(attribute)) {
                    Reference reference = attributeMapping.getRef();
                    if (reference != null) {
                        adapterAttribute = reference.stringValueOf();
                    }
                    break;
                }
            }
        }
        if (adapterAttribute == null) {
            AdapterMapping parentAdapter = adapter.getParent();
            if (parentAdapter != null) {
                adapterAttribute = getAdapterAttribute(attribute, parentAdapter);
            }
        }
        return adapterAttribute;
    }

    private Map<String, Object> getObjectAttributes(Connection nativeConn, int dataType, String dataTypeName)
            throws SQLException {
        Map<String, Object> objectAttributes = new LinkedHashMap<String, Object>();
        TypeDescriptor typeDescriptor = getTypeDescriptor(nativeConn, dataType, dataTypeName);
        if (typeDescriptor != null) {
            OracleTypeADT oracleTypeADT = (OracleTypeADT) typeDescriptor.getPickler();
            if (oracleTypeADT instanceof OracleTypeCOLLECTION) {
                OracleTypeCOLLECTION oracleTypeCOLLECTION = (OracleTypeCOLLECTION) oracleTypeADT;
                OracleTypeUPT oracleTypeUPT = (OracleTypeUPT) oracleTypeCOLLECTION.getElementType();
                oracleTypeADT = (OracleTypeADT) oracleTypeUPT.getRealType();
            }
            int numAttributes = oracleTypeADT.getNumAttrs();
            for (int i = 0; i < numAttributes; i++) {
                int attributeIndex = i + 1;
                String attributeName = oracleTypeADT.getAttributeName(attributeIndex);
                Object attributeType = oracleTypeADT.getAttributeType(attributeIndex);
                objectAttributes.put(attributeName, attributeType);
            }
        }
        return objectAttributes;
    }

    private TypeDescriptor getTypeDescriptor(Connection nativeConn, int dataType, String dataTypeName)
            throws SQLException {
        if (dataType == Types.STRUCT) {
            StructDescriptor structDescriptor = StructDescriptor.createDescriptor(dataTypeName, nativeConn);
            return structDescriptor;
        }
        if (dataType == Types.ARRAY) {
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(dataTypeName, nativeConn);
            return arrayDescriptor;
        }
        return null;
    }

    private boolean isStructType(CallArgument argument) {
        // TYPE_NAME for the Object type in database comes like KPMG.LNE_TYP (i.e. SCHEMA.OBJECTNAME),
        // from this we cannot identify if it is object type or not, but in other types like Refcursor or
        // Table or REF we are getting the type name.
        // So If the argument type name is not in the list we know that it is a Object type in database which
        // is equivalent to Java type STRUCT
        if (argument == null) {
            return false;
        }
        if (argument.getSqlType() != CallArgument.TYPE_OTHER) {
            return false;
        }
        String argumentTypeName = argument.getTypeName();
        if (TYPE_REF.equals(argumentTypeName)) {
            return false;
        }
        if (TYPE_REF_CURSOR.equals(argumentTypeName)) {
            return false;
        }
        if (TYPE_TABLE.equals(argumentTypeName)) {
            return false;
        }
        return true;
    }

    private boolean isArrayOfStructType(CallArgument argument) {
        if (argument == null) {
            return false;
        }
        if (argument.getSqlType() != CallArgument.TYPE_OTHER) {
            return false;
        }
        String argumentTypeName = argument.getTypeName();
        if (TYPE_TABLE.equals(argumentTypeName)) {
            return true;
        }
        return false;
    }

    protected String createProcedure(Call call) {
        String sql = "{" + CALLABLESTATEMENT_KEYWORD + " " + call.getCallName();
        int numArgs = call.getArguments().size();
        if (numArgs > 0) {
            sql += "(";
            for (int i = 0; i < numArgs; i++) {
                sql += (i != (numArgs - 1)) ? "?," : "?";
            }
            sql += ")";
        }
        sql += "}";
        return sql;
    }

    protected String createFunction(Call call) {
        String sql = "{";
        int numOutputArgs = call.getArguments().getResultArgs().size();
        if (numOutputArgs > 0) {
            for (int i = 0; i < numOutputArgs; i++) {
                sql += (i != (numOutputArgs - 1)) ? "?," : "?";
            }
            sql += " = ";
        }
        sql += CALLABLESTATEMENT_KEYWORD + " " + call.getCallName();

        int numParamArgs = call.getArguments().getParameterArgs().size();
        if (numParamArgs > 0) {
            sql += "(";
            for (int i = 0; i < numParamArgs; i++) {
                sql += (i != (numParamArgs - 1)) ? "?," : "?";
            }
            sql += ")";
        }
        sql += "}";

        return sql;
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    public Map readResults(CallableStatement callStmt, Call call, Method method, Set outputs, Filters filters)
            throws SQLException {
        Map callResult = new HashMap();
        List outputArgs = call.getArguments().getAllResultableArgs();
        if (outputArgs != null) {
            for (Object outputArg : outputArgs) {
                if (outputArg instanceof CallArgument) {
                    CallArgument argument = (CallArgument) outputArg;
                    int argumentIndex = argument.getSequence();
                    String argumentName = argument.getArgumentName();

                    String outputName = null;
                    if (DataStore.KEY_WORD_QAFE_BUILT_IN_LIST.equalsIgnoreCase(argumentName)) {
                        outputName = DataStore.KEY_WORD_QAFE_BUILT_IN_LIST;
                    } else {
                        outputName = String.valueOf(argumentIndex - 1);
                        if (!outputs.contains(outputName)) {
                            if (argumentName != null) {
                                if (!outputs.contains(argumentName) || !supportsNamedParameters) {
                                    logger
                                        .info("parameter ["
                                                + outputName
                                                + "]["
                                                + argumentName
                                                + "] is ignored since its not in the outputMapping if this service");
                                    continue;
                                }
                                outputName = argumentName;
                            } else {
                                // Default name for a return argument of a function call,
                                // e.g: ? = call my_function(), ? <=> result
                                outputName = RETURN_NAME;
                            }
                        }
                    }

                    AdapterMapping outputAdapter = getOutputAdapter(argument, method.getOutput());

                    Object outputValue = null;
                    Object stmtResult = callStmt.getObject(argumentIndex);
                    if (stmtResult instanceof STRUCT) {
                        STRUCT objectStruct = (STRUCT) stmtResult;
                        outputValue = createDataMap(objectStruct, outputAdapter);
                    } else if (stmtResult instanceof ARRAY) {
                        ARRAY objectArray = (ARRAY) stmtResult;
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        Object array = objectArray.getArray();
                        if (array instanceof Object[]) {
                            Object[] arrayOfObject = (Object[]) array;
                            for (Object object : arrayOfObject) {
                                if (object instanceof STRUCT) {
                                    STRUCT objectStruct = (STRUCT) object;
                                    Map<String, Object> map = createDataMap(objectStruct, outputAdapter);
                                    if (map != null) {
                                        list.add(map);
                                    }
                                }
                            }
                        }
                        outputValue = list;
                    } else if (stmtResult instanceof ResultSet) {
                        ResultSet resultSet = (ResultSet) stmtResult;
                        List<Object> list = new ArrayList<Object>();
                        MetaDataRowMapper rowMapper = new MetaDataRowMapper();
                        if ((filters != null) && (filters.getPage() != null)) {
                            Page page = filters.getPage();
                            page = (Page) new ResultSetDataExtractor(page, rowMapper).extractData(resultSet);
                            list = page.getPageItems();
                        } else {
                            while (resultSet.next()) {
                                list.add(rowMapper.mapRow(resultSet, resultSet.getRow()));
                            }
                        }
                        outputValue = list;
                    } else {
                        outputValue = stmtResult;
                    }
                    callResult.put(outputName, outputValue);
                }
            }
        }
        return callResult;
    }

    // CHECKSTYLE.ON: CyclomaticComplexity

    private Map<String, Object> createDataMap(STRUCT objectStruct, AdapterMapping adapter)
            throws SQLException {
        Map<String, Object> map = null;
        if (objectStruct != null) {
            map = new DataMap<String, Object>();
            OracleTypeADT oracleTypeADT = (OracleTypeADT) objectStruct.getDescriptor().getPickler();
            int numAttributes = oracleTypeADT.getNumAttrs();
            Object[] attributeValues = objectStruct.getAttributes();
            for (int i = 0; i < numAttributes; i++) {
                int attributeIndex = i + 1;
                String attributeName = oracleTypeADT.getAttributeName(attributeIndex);
                if ((adapter != null) && (adapter.adaptAll() == false)) {
                    attributeName = getAdapterAttribute(attributeName, adapter);
                }
                if (attributeName != null) {
                    Object attributeValue = attributeValues[i];
                    map.put(attributeName, attributeValue);
                }
            }
        }
        return map;
    }
}
