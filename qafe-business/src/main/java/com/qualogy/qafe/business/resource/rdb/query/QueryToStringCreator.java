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
package com.qualogy.qafe.business.resource.rdb.query;

import com.qualogy.qafe.bind.resource.query.Delete;
import com.qualogy.qafe.bind.resource.query.Insert;
import com.qualogy.qafe.bind.resource.query.SQLQuery;
import com.qualogy.qafe.bind.resource.query.Select;
import com.qualogy.qafe.bind.resource.query.Update;

import org.apache.commons.lang.StringUtils;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.*;


//TODO: create a query with field from, where order clause
//TODO: rebuild with string replacements instead of string appending
public class QueryToStringCreator {

	private static String[] operators = {"BETWEEN ", "NULL", "NOT ", "IN ", ">=", "!=", "<=", "<>", "^=", "!=", ">", "<", "="};
    private static String[] operatorsCanBeUsedInLikeClause = { "%", "*", "_" };
    private static String DYNAMIC_VARS_PREFIX = "$SQL";

    /**
     * - sql as text goes first on sql as attribute
     * - sql attribute  goes first on table property
     * - if tableProperty is supplied services in and out params will be used
     *
     * @param query
     * @param inputMapping
     * @param outputMapping
     * @return
     */
    public static String toString(SQLQuery query, MapSqlParameterSource namedParameters, String[] inputMapping, Set outputMapping) {
        String sql = null;
        sql = (StringUtils.isNotBlank(query.getSqlAsAttribute())) ? query.getSqlAsAttribute() : query.getSqlAsText();

        if (StringUtils.isBlank(sql)) {
        	if (StringUtils.isNotBlank(query.getTable())) {
                sql = enrichSQLAroundTableProp(sql, query);

                if (query instanceof Update) {
                    sql = enrichUpdateWithParams(sql, query, namedParameters, inputMapping);
                } else if (query instanceof Insert) {
                    sql = enrichSQLWithInParams(sql, query, namedParameters, inputMapping);
                } else if (query instanceof Select) {
                    sql = enrichSQLWithInParams(sql, query, namedParameters, inputMapping);
                } else if (query instanceof Delete) {
                    sql = enrichSQLWithInParams(sql, query, namedParameters, inputMapping);
                }

                if ((sql != null) && !sql.toLowerCase().startsWith(query.getKeyword())) {
                    sql = query.getKeyword() + " " + sql;
                }
            }
        }
    	sql = manageDynamicQueryReplacement(namedParameters, sql);
        return sql;
    }

	private static String manageDynamicQueryReplacement(MapSqlParameterSource namedParameters, String sql) {
		if(namedParameters == null || namedParameters.getValues() == null) {
			return sql;
		}
		Iterator keySet = namedParameters.getValues().keySet().iterator();
    	while(keySet.hasNext()){
    		String key = (String) keySet.next();
    		if(key.startsWith(DYNAMIC_VARS_PREFIX) && sql.contains (key)) {
    			sql = sql.replace(key, (String) namedParameters.getValues().get(key));
    		}
    	}
		return sql;
	}

    /**
     *
     * @param sql
     * @param query
     * @param namedParameters
     * @param inputMapping
     * @return
     */
    // CHECKSTYLE.OFF: CyclomaticComplexity
    private static String enrichUpdateWithParams(String sql, SQLQuery query, MapSqlParameterSource namedParameters, String[] inputMapping) {
        //filter
        List<String> whereParams = new ArrayList<String>();
        boolean containsWhereClauseIdentifier = false;
        String whereAttribute = ((Update) query).getWhere();

        if (whereAttribute != null) {
            for (String str : inputMapping) {
                if (whereAttribute.contains(str)) {
                    whereParams.add(str);
                }
            }
        }

        if (whereAttribute == null) {
            whereParams = ((Update) query).getMetaData().getPrimaryKeys();
        }

        for (int i = 0; (i < inputMapping.length) && !containsWhereClauseIdentifier; i++) {
            for (Iterator iter = whereParams.iterator(); iter.hasNext() && !containsWhereClauseIdentifier;) {
                String whereParam = iter.next().toString();
                if (inputMapping[i].equalsIgnoreCase(whereParam)) {
                    containsWhereClauseIdentifier = true;
                    inputMapping[i] = whereParam; // Setting the primary key and the key in String[] with the same case.
                }
            }
        }
        String[] setParams = new String[0];
        if (containsWhereClauseIdentifier) {
            setParams = new String[inputMapping.length - whereParams.size()];
        } else {
            setParams = new String[inputMapping.length];
        }

        if (setParams.length == 0) {
            throw new IllegalArgumentException("Update statement has no set[" + setParams.length + "] parameters which are required, when using automatic update statement generation table metadata is used for retrieving primary keys to update on");
        }

        // check the input if it contains a where clause parameter:
        int setParamsCounter = 0;
        for (int i = 0; i < inputMapping.length; i++) {
            boolean isPK = false;
            for (Iterator iter = whereParams.iterator(); iter.hasNext();) {
                String whereParam = iter.next().toString();
                if (inputMapping[i].equalsIgnoreCase(whereParam)) {
                    isPK = true;
                    break;
                }
            }
            if (!isPK) {
                setParams[setParamsCounter] = inputMapping[i];
                setParamsCounter++;
            }
        }

        //set clause
        for (int i = 0; i < setParams.length; i++) {
            if (i > 0) {
                sql += ", ";
            }

            sql += (setParams[i] + "=:" + setParams[i]);
        }

        //where clause
        if (containsWhereClauseIdentifier) {
            sql = enrichSQLWithInParams(sql, query, namedParameters, (String[]) whereParams.toArray(new String[whereParams.size()]));
        }
        return sql;
    }
    // CHECKSTYLE.ON: CyclomaticComplexity

    /**
     *
     * @param sql
     * @param statement
     * @return
     */
    private static String enrichSQLAroundTableProp(String sql, SQLQuery statement) {
        String table = statement.getTable();
        if (statement instanceof Insert) {
            sql = "into " + table;
        } else if (statement instanceof Select) {
            sql = "* from " + table;
        } else if (statement instanceof Delete) {
            sql = "from " + table;
        } else if (statement instanceof Update) {
            sql = table + " set ";
        } else {
            throw new IllegalArgumentException("unsupported table option for statement type [" + statement.getKeyword() + "]");
        }
        return sql;
    }

    /**
     *
     * @param sql
     * @param statement
     * @param namedParameters
     * @param inputMapping
     * @return
     */
    // CHECKSTYLE.OFF: CyclomaticComplexity
    private static String enrichSQLWithInParams(String sql, SQLQuery statement, MapSqlParameterSource namedParameters, String[] inputMapping) {
        if ((inputMapping != null) && (inputMapping.length > 0)) {
            if (statement instanceof Insert) {
                sql += " (";
                for (int i = 0; i < inputMapping.length; i++) {
                    if (i > 0) {
                        sql += ", ";
                    }
                    sql += inputMapping[i];
                }
                sql += ") values (";
                for (int i = 0; i < inputMapping.length; i++) {
                    if (i > 0) {
                        sql += ", ";
                    }
                    sql += (":" + inputMapping[i]);
                }
                sql += ")";
            } else if (statement instanceof Delete || statement instanceof Select || statement instanceof Update) {
                boolean first = true;
                if (sql.indexOf("where") < 0) {
                    sql += " where ";
                } else {
                    first = false;
                }

                // Added the extra where clause. Only for Delete and Update
                boolean whereAtrributeExist = false;
                if (statement instanceof Update) {
                    if (((Update) statement).getWhere() != null) {
                        sql += ((Update) statement).getWhere();
                        whereAtrributeExist = true;
                    }
                } else if (statement instanceof Delete) {
                    if (((Delete) statement).getWhere() != null) {
                        sql += ((Delete) statement).getWhere();
                        whereAtrributeExist = true;
                    }
                }

                if (!whereAtrributeExist) {
                    for (int i = 0; i < inputMapping.length; i++) {
                        if (namedParameters != null) {
                            String columnName = getColumnName(namedParameters, inputMapping[i]);
                            if (StringUtils.isNotBlank(columnName) && namedParameters.getValue(columnName) instanceof String) {
                                String userFilledValue = (String) namedParameters.getValue(columnName);
                                String userOperator = getUserFirstUsedOperator(userFilledValue);

                                if (StringUtils.isNotBlank(userFilledValue)) {
                                    if (StringUtils.isNotBlank(userOperator)) {
                                        boolean isReverseCondition = false;
                                        sql += generateConditionForUserFilledValue(isReverseCondition, userOperator, userFilledValue, columnName, namedParameters);
                                    } else {
                                        sql += generateEquality(columnName, columnName);
                                    }
                                }
                            } else {
                                sql += generateEquality(inputMapping[i], inputMapping[i]);
                            }
                        } else {
                            sql += generateEquality(inputMapping[i], inputMapping[i]);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("cannot enrich [" + statement.getKeyword() + "], unsupported action");
            }
        }
        sql = finalClean(sql);
        return sql;
    }
    // CHECKSTYLE.ON: CyclomaticComplexity

    private static String finalClean(String sql) {
        sql = removeFromEnd(sql, "AND");
        sql = removeFromEnd(sql, "where");
        return sql.trim();
    }

    /**
     *  Remove clause in case it is the last key in sql statement
     * @return
     */
    private static String removeFromEnd(String sql, String clause) {
        return StringUtils.removeEnd(sql.trim(), clause).concat(" ");
    }

    /**
     *
     * @param columnName
     * @param placeholder
     * @return
     */
    private static String generateEquality(String columnName, String placeholder) {
        String sql = (columnName + "=:" + placeholder);
        sql = removeFromEnd(sql, "AND");
        sql += " AND ";
        return sql;
    }

    /**
     *
     * @param namedParameters
     * @param inputMap
     * @return
     */
    private static String getColumnName(MapSqlParameterSource namedParameters, String inputMap) {
        String columnName = "";
        if (namedParameters.getValues().keySet().contains(inputMap)) {
            columnName = inputMap;
        } else if (namedParameters.getValues().keySet().contains(inputMap.toLowerCase())) {
            columnName = inputMap.toLowerCase();
        } else if (namedParameters.getValues().keySet().contains(inputMap.toUpperCase())) {
            columnName = inputMap.toUpperCase();
        }
        return columnName;
    }

    /**
     * @param userOperator
     * @param userFilledValue
     * @param columnName
     * @param namedParameters
     * @return
     */
    private static String generateConditionForUserFilledValue(boolean isReverseCondition, String userOperator, String userFilledValue, String columnName, MapSqlParameterSource namedParameters) {
        String sql = "";
        if (isLikeOperator(userOperator)) {
            sql = replaceUserOperatorInLikeSqlStatement(isReverseCondition, columnName, userFilledValue);
        } else if (userOperator.equals("NULL")) {
            sql = replaceUserOperatorInNULLSqlStatement(isReverseCondition, columnName);
        } else if (userOperator.equals("IN")) {
            sql = replaceUserOperatorInINSqlStatement(columnName, userFilledValue);
        } else if (userOperator.equals("NOT")) {
            sql = replaceUserOperatorInNOTSqlStatement(columnName, userFilledValue, namedParameters);
        } else if (userOperator.equals("BETWEEN")) {
            sql = replaceUserOperatorInBetweenSqlStatement(columnName, userFilledValue);
        } else {
            String userFilledValueWithoutOperator = StringUtils.remove(userFilledValue, userOperator);
            namedParameters.addValue(columnName, userFilledValueWithoutOperator);

            sql += (columnName + userOperator + ":" + columnName);
        }
        sql = removeFromEnd(sql, "AND");
        sql += " AND ";
        return sql;
    }

    /**
     *
     * @param columnName
     * @param userFilledValue
     * @return
     */
    private static String replaceUserOperatorInLikeSqlStatement(boolean isReverseCondition, String columnName, String userFilledValue) {
        String adjustUserFilledValue = userFilledValue.replace("*", "%");
        String sql = "";
        if (isReverseCondition) {
            String userFilledValueWithoutNot = getUserFilledValueWithoutFirstOperator(adjustUserFilledValue, "NOT").trim();
            sql = (columnName + " NOT LIKE '" + userFilledValueWithoutNot + "'");
        } else {
            sql = (columnName + " LIKE '" + adjustUserFilledValue + "'");
        }
        return sql;
    }

    /**
     *
     * @param isReverseCondition
     * @param columnName
     * @return
     */
    private static String replaceUserOperatorInNULLSqlStatement(boolean isReverseCondition, String columnName) {
        String sql = (columnName + " IS ");
        sql += (isReverseCondition ? " NOT NULL" : "NULL");
        return sql;
    }

    /**
     *
     * @param columnName
     * @param placeholder
     * @return
     */
    private static String replaceUserOperatorInINSqlStatement(String columnName, String placeholder) {
        return columnName + " " + placeholder;
    }

    /**
     *
     * @param columnName
     * @param userFilledValue
     * @return
     */
    private static String replaceUserOperatorInNOTSqlStatement(String columnName, String userFilledValue, MapSqlParameterSource namedParameters) {
        String userNextOperator = getUserNextOperator(userFilledValue, "NOT");
        String sql = "";
        if (StringUtils.isBlank(userNextOperator)) {
            sql = columnName + " " + userFilledValue;
        } else {
            boolean isReverseCondition = true;
            sql = generateConditionForUserFilledValue(isReverseCondition, userNextOperator, userFilledValue, columnName, namedParameters);
        }
        return sql;
    }

    /**
     *
     * @param userFilledValue
     * @param userFirstUsedOperator
     * @return
     */
    private static String getUserNextOperator(String userFilledValue, String userFirstUsedOperator) {
        String filledValueWithoutNotOperator = getUserFilledValueWithoutFirstOperator(userFilledValue, userFirstUsedOperator);
        String userNextOperator = getUserFirstUsedOperator(filledValueWithoutNotOperator);
        return userNextOperator;
    }

    /**
     *
     * @param userFilledValue
     * @param userFirstUsedOperator
     * @return
     */
    private static String getUserFilledValueWithoutFirstOperator(String userFilledValue, String userFirstUsedOperator) {
        String filledValueWithoutNotOperator = StringUtils.removeStart(userFilledValue, userFirstUsedOperator);

        //check if first operator has been removed from user input. Remove the
        //user operator by changing it to lowercase.
        if (filledValueWithoutNotOperator.length() == userFilledValue.length()) {
            filledValueWithoutNotOperator = StringUtils.removeStart(userFilledValue, userFirstUsedOperator.toLowerCase());
        }
        return filledValueWithoutNotOperator;
    }

    /**
     *
     * @param columnName
     * @param placeholder
     * @return
     */
    private static String replaceUserOperatorInBetweenSqlStatement(String columnName, String placeholder) {
        String sql = columnName + " " + placeholder;
        return sql;
    }

    /**
    *
    * @param userFilledValue
    * @return
    */
    private static String getUserFirstUsedOperator(String userFilledValue) {
        String userOperator = "";
        for (int i = 0; i < operators.length; i++) {
            if (userFilledValue.trim().toUpperCase().startsWith(operators[i])) {
                userOperator = operators[i].trim();
                break;
            }
        }

        if (StringUtils.isBlank(userOperator)) {
            for (int i = 0; i < operatorsCanBeUsedInLikeClause.length; i++) {
                if (userFilledValue.toUpperCase().contains(operatorsCanBeUsedInLikeClause[i])) {
                    userOperator = operatorsCanBeUsedInLikeClause[i];
                    break;
                }
            }
        }
        return userOperator;
    }

    /**
     *
     * @param userOperator
     * @return
     */
    private static boolean isLikeOperator(String userOperator) {
        boolean isLikeOperator = false;

        for (int i = 0; i < operatorsCanBeUsedInLikeClause.length; i++) {
            if (userOperator.toUpperCase().endsWith(operatorsCanBeUsedInLikeClause[i])) {
                isLikeOperator = true;
                break;
            }
        }
        return isLikeOperator;
    }
}
