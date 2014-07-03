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
package com.qualogy.qafe.commons.db.procedure;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
/**
 * This class wraps one column from DatabaseMetaData.getProcedureColumns. The wrapped information 
 * represents one param of a stored procedure call.
 *  
 * The following documentation snippet is from DatabaseMetaData.getProcedureColumns:
 * <P>
 * Each row in the <code>ResultSet</code> is a parameter description or column
 * description with the following fields:
 * <OL>
 * <LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be
 * <code>null</code>)
 * <LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be
 * <code>null</code>)
 * <LI><B>PROCEDURE_NAME</B> String => procedure name
 * <LI><B>COLUMN_NAME</B> String => column/parameter name
 * <LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
 * <UL>
 * <LI> procedureColumnUnknown - nobody knows
 * <LI> procedureColumnIn - IN parameter
 * <LI> procedureColumnInOut - INOUT parameter
 * <LI> procedureColumnOut - OUT parameter
 * <LI> procedureColumnReturn - procedure return value
 * <LI> procedureColumnResult - result column in <code>ResultSet</code>
 * </UL>
 * <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
 * <LI><B>TYPE_NAME</B> String => SQL type name, for a UDT type the type name
 * is fully qualified
 * <LI><B>PRECISION</B> int => precision
 * <LI><B>LENGTH</B> int => length in bytes of data
 * <LI><B>SCALE</B> short => scale
 * <LI><B>RADIX</B> short => radix
 * <LI><B>NULLABLE</B> short => can it contain NULL.
 * <UL>
 * <LI> procedureNoNulls - does not allow NULL values
 * <LI> procedureNullable - allows NULL values
 * <LI> procedureNullableUnknown - nullability unknown
 * </UL>
 * <LI><B>REMARKS</B> String => comment describing parameter/column
 * </OL>
 * 
 * <P>
 * <B>Note:</B> Some databases may not return the column descriptions for a
 * procedure. Additional columns beyond REMARKS can be defined by the database.
 */
public class CallArgument {
	
	
	public static final int TYPE_OTHER = -10;

	/**
     * Indicates that type of the column is unknown.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_UNKNOWN = DatabaseMetaData.procedureColumnUnknown;

    /**
     * Indicates that the column stores IN parameters.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_IN = DatabaseMetaData.procedureColumnIn;
    
    /**
     * Indicates that type of the column is unknown.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_OUT = DatabaseMetaData.procedureColumnOut;
    
    /**
     * Indicates that the column stores INOUT parameters.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_INOUT = DatabaseMetaData.procedureColumnInOut;
    
    /**
     * Indicates that the column stores return values.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_RETURN = DatabaseMetaData.procedureColumnReturn;
   
    /**
     * Indicates that the column stores results.
     * A possible value for the column <code>COLUMN_TYPE</code> in the <code>ResultSet</code> 
     * returned by the method <code>getProcedureColumns</code>.
     * @see DatabaseMetaData.procedureColumnUnknown
     */
    public final static int ARGUMENT_TYPE_RESULT = DatabaseMetaData.procedureColumnResult;
    
    public final static String METAPROCEDURE_PROCEDURE_SCHEM = "PROCEDURE_SCHEM";
	/**
	 * name of the procedure
	 */
	public final static String METAPROCEDURE_PROCEDURE_NAME = "PROCEDURE_NAME";
	/**
	 * name of the param
	 * a String object representing the name pattern of the parameter names or return value names. 
	 * Java procedures have parameter names matching those defined in the CREATE PROCEDURE statement. 
	 * Use "%" to find all parameter names
	 */
	public final static String METAPROCEDURE_COLUMN_NAME = "COLUMN_NAME";
	/**
	 * short indicating what the row describes. 
	 * Always is DatabaseMetaData.procedureColumnIn for method parameters, unless the parameter is an array. 
	 * If so, it is DatabaseMetaData.procedureColumnInOut. 
	 * It always returns DatabaseMetaData.procedureColumnReturn for return values.
	 */
	public final static String METAPROCEDURE_COLUMN_TYPE = "COLUMN_TYPE";
	/**
	 * in java.sql.Types
	 */
	public final static String METAPROCEDURE_DATA_TYPE = "DATA_TYPE";
	/**
	 * f.i. VARCHAR2
	 */
	public final static String METAPROCEDURE_TYPE_NAME = "TYPE_NAME";
	/**
	 * always returns DatabaseMetaData.procedureNoNulls for primitive parameters and DatabaseMetaData.procedureNullable for object parameters
	 * 0, 1
	 */
	public final static String METAPROCEDURE_NULLABLE = "NULLABLE";
	/**
	 * starting at 1
	 */
	public final static String METAPROCEDURE_SEQUENCE = "SEQUENCE";
	/**
	 * i've only seen it with null?
	 */
	public final static String METAPROCEDURE_OVERLOAD = "OVERLOAD";
	
	public final static String[] METAPROCEDURE_KEYS = {
		METAPROCEDURE_PROCEDURE_NAME,
		METAPROCEDURE_COLUMN_NAME,  
		METAPROCEDURE_COLUMN_TYPE,  
		METAPROCEDURE_DATA_TYPE,    
		METAPROCEDURE_TYPE_NAME,  
		METAPROCEDURE_NULLABLE,  
		METAPROCEDURE_SEQUENCE,  
		METAPROCEDURE_OVERLOAD
	};
	
	private Map wrapper = new HashMap();
	
	/**
	 * Method walks through resultset and sets the values on this. If the COLUMN_TYPE
	 * result equals unknown an exception is thrown 
	 * @param set
	 * @return
	 * @throws SQLException - when getObject fails or 'column_type' is unknown
	 */
	public static CallArgument create(ResultSet set) throws SQLException{
		CallArgument wrapper = create(set, -1);
		return wrapper;
	}
	
	/**
	 * Method walks through resultset and sets the values on this. If the COLUMN_TYPE
	 * result equals unknown an exception is thrown 
	 * @param set
	 * @param argumentIndex
	 * @return
	 * @throws SQLException - when getObject fails or 'column_type' is unknown
	 */
	public static CallArgument create(ResultSet set, int argumentIndex) throws SQLException{
		CallArgument wrapper = new CallArgument();
		for (int i = 0; i < METAPROCEDURE_KEYS.length; i++) {
			wrapper.put(METAPROCEDURE_KEYS[i], set.getObject(METAPROCEDURE_KEYS[i]));
			
			if(METAPROCEDURE_KEYS[i].equals(METAPROCEDURE_COLUMN_TYPE) && wrapper.getArgumentType()==ARGUMENT_TYPE_UNKNOWN)
				throw new SQLException("BUSINESS_EXCEPTION: cannot determine type (direction) of argument ["+wrapper.stringValue(METAPROCEDURE_COLUMN_NAME)+"]");
		}
		if (argumentIndex != -1) {
			wrapper.put(METAPROCEDURE_SEQUENCE, argumentIndex);	
		}
		
		/*ResultSetMetaData columns = set.getMetaData();
		for(int i =1; i<= set.getMetaData().getColumnCount(); i++) {
			System.out.println(columns.getColumnLabel(i) + " = " + set.getObject(columns.getColumnLabel(i)));
		}*/
		
		return wrapper;
	}
	
	/**
	 * method to put a value on the wrapper
	 */
	public void put(String key, Object value){
		wrapper.put(key, value);
	}
	public String stringValue(String key) {
		return (String)wrapper.get(key);
	}
	
	public int intValue(String key) {
		Object obj = wrapper.get(key);
		
		int val = -1;
		if(obj instanceof BigDecimal){
			val = ((BigDecimal)obj).intValue();
		}else{
			val = Integer.parseInt(obj.toString());
		}
		return val; 
	}
	
	public int getSqlType(){
		int columnType = intValue(CallArgument.METAPROCEDURE_DATA_TYPE);
		if(columnType==Types.OTHER)
			columnType = TYPE_OTHER;
		return columnType;
	}
	
	/**
	 * @return method returns true when argumentType (retrieved by calling 
	 * getArgumentType()) is either:
	 * 	COLUMN_TYPE_IN
	 * 	COLUMN_TYPE_INOUT
	 */
	public boolean isIn(){
		return getArgumentType()==ARGUMENT_TYPE_IN || getArgumentType()==ARGUMENT_TYPE_INOUT;
	}
	/**
	 * @return method returns true when argumentType (retrieved by calling 
	 * getArgumentType()) is either:
	 * 	COLUMN_TYPE_OUT
	 * 	COLUMN_TYPE_INOUT
	 */
	public boolean isOut(){
		return getArgumentType()==ARGUMENT_TYPE_OUT || getArgumentType()==ARGUMENT_TYPE_INOUT;
	}
	
	/**
	 * @return method returns true when argumentType (retrieved by calling 
	 * getArgumentType()) is either:
	 * 	COLUMN_TYPE_OUT
	 * 	COLUMN_TYPE_INOUT
	 * 	COLUMN_TYPE_RESULT
	 * 	COLUMN_TYPE_RETURN
	 */
	public boolean isResult(){
		return getArgumentType()==ARGUMENT_TYPE_RESULT || getArgumentType()==ARGUMENT_TYPE_RETURN;
	}
	/**
	 * method to get this type of argument, either
	 * in/out/inout/result/return/unknown
	 * @return int
	 */
	public int getArgumentType(){
		return intValue(METAPROCEDURE_COLUMN_TYPE);
	}
	
	public int getSequence(){
		return intValue(METAPROCEDURE_SEQUENCE);
	}
	
	public String getArgumentName(){
		return stringValue(METAPROCEDURE_COLUMN_NAME);
	}
	
	public String getTypeName() {
		return stringValue(METAPROCEDURE_TYPE_NAME);
	}
}
