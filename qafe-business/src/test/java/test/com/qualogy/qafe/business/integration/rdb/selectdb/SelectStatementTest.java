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
package test.com.qualogy.qafe.business.integration.rdb.selectdb;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectStatementTest extends BusinessActionTestCase {
    /**
     *
    
      CREATE TABLE QAFE_TEST_SELECTDB
    (        "EMP_ID" VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "EMP_NAME" VARCHAR2(100 BYTE),
    "DEP_ID" VARCHAR2(20 BYTE),
    "BIRTHDATE" DATE,
    "CHILDREN" NUMBER(10,0),
     CONSTRAINT "QAFE_TEST_SELECTDB_PK" PRIMARY KEY ("EMP_ID")
    )  ;
    
    
     */
    protected String[] getSetupActions() {
        return new String[] { "setUp" };
    }

    protected String[] getTearDownActions() {
        return new String[] { "tearDown" };
    }

    public String getAppContextDir() {
        return getDirBasedUponPackage();
    }

    /**
     *         With inputs: emp_id=E2 and dep_id="D1"
     *        <select id="" table="qafe_test_select"></select>
     *        select * from qafe_test_select where and emp_id='E2' and dep_id='D1'
     * @throws ExternalException
     */
    public void testTableWithInputAsValues() throws ExternalException {
        manage("insertTestDataSet1");
        manage("tableWithInputs");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
        assertEquals("E2", ((Map) data.get(0)).get("EMP_ID"));
        assertEquals("Hallo", ((Map) data.get(0)).get("EMP_NAME"));
        assertEquals("D1", ((Map) data.get(0)).get("DEP_ID"));
    }

    /*
     *  user input: >=0
     * select * from X where y >= 0
     */
    public void testSqlWithUserExtraFilledOperatorGreaterEqualTo()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorGreaterEqualTo");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 3);
    }

    /**
     *  user input: =0
     *  select * from X where y = 0
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorEqualTo()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorEqualTo");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    /**
     *  user input: >=0
     *  select * from X where y > 0
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorGreaterTo()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorGreaterTo");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 3);
    }

    /**
     * User input: '%z%'
     * select * from X where y like '%z%'
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingBothSidePercentage()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingBothSidePercentage");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 2);
    }

    /**
     * User input: NOT '%z%'
     * select * from X where y NOT like '%z%'
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingNOTBothSidePercentage()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNOTBothSidePercentage");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 2);
    }

    /**
     * User input: NOT '%z%'
     * select * from X where y like '_a___'
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingUnderscore ()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingUnderscore");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }


    /**
     * User input:  'z%'
     * select * from X where y like 'z%'
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingRightSidePercentage()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingRightSidePercentage");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    /**
     * User input: NOT 'z%'
     * select * from X where y NOT like 'z%'
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingNOTRightSidePercentage()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNOTRightSidePercentage");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 3);
    }

    /**
     * Inequality test:  !=, ^=, <>;
     * User input:  != 1 OR ^=1 OR <> 1
     *
     * select * from X where y ^= 5
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingInequality()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingInequality");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 3);
    }

    /**
     * User input: NULL
     *
    * select * from X where y is NULL
    *
    * @throws ExternalException
    */
    public void testSqlWithUserExtraFilledOperatorUsingNULL()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNULL");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    /**
     * User input: NOT NULL
     *
     * select * from X where y is NOT NULL
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingNOTNULL()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNOTNULL");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 3);
    }

    /**
     * User input: IN (1, 2, 3)
     *
     * select * from X where y IN (1, 2, 3)
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingIN()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingIN");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 2);
    }

   /**
    * User input: NOT IN
    *
    * select * from X where y NOT IN (1, 2, 3)
    *
    * @throws ExternalException
    */
    public void testSqlWithUserExtraFilledOperatorUsingNOTIN()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNOTIN");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNull("Expected record retrieved", data);
    }

    /**
     * User input: BETWEEN x and y
     *
     * select * from X where y BETWEEN 1 and 2
     *
     * @throws ExternalException
     */
    public void testSqlWithUserExtraFilledOperatorUsingBetween()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingBetween");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record NOT retrieved", data);
        assertTrue(data.size() == 2);
    }

   /**
    * User input: NOT BETWEEN x and y
    *
    * select * from X where y NOT BETWEEN 1 and 2
    *
    * @throws ExternalException
    */
    public void testSqlWithUserExtraFilledOperatorUsingNOTBetween()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("SqlWithUserExtraFilledOperatorUsingNOTBetween");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record NOT retrieved", data);
        assertTrue(data.size() == 1);
    }

    public void testTableWithInputsRef() throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "emp_Id", "E2");
        DataStore.store(dataId, "dep_Id", "D1");
        manage("tableWithInputsAsRef");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
        assertEquals("E2", ((Map) data.get(0)).get("EMP_ID"));
        assertEquals("Hallo", ((Map) data.get(0)).get("EMP_NAME"));
        assertEquals("D1", ((Map) data.get(0)).get("DEP_ID"));
    }

    public void testTableWithInputsInMap() throws ExternalException {
        manage("insertTestDataSet1");

        HashMap<String,Object> inputMap = new HashMap<String,Object>();
        inputMap.put("emp_Id", "E2");
        inputMap.put("dep_Id", "D1");
        DataStore.store(dataId, "inputMap", inputMap);
        manage("tableWithInputsAsMapRef");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
        assertEquals("E2", ((Map) data.get(0)).get("EMP_ID"));
        assertEquals("Hallo", ((Map) data.get(0)).get("EMP_NAME"));
        assertEquals("D1", ((Map) data.get(0)).get("DEP_ID"));
    }

    public void testTableWithInputDate() throws ExternalException {
        manage("insertTestDataSet1");

        HashMap<String,Object> inputMap = new HashMap<String,Object>();
        inputMap.put("birthdate", new Date("08/12/1982"));
        DataStore.store(dataId, "inputMap", inputMap);
        manage("tableWithInputsAsMapRef");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue("data size not matches:" + data.size(), data.size() == 1);
    }

    /**
     *
            <select id="" table="qafe_test_select" order="dep_id" sql="select EMP_ID from qafe_test_select"></select>
            select EMP_ID from qafe_test_select
     * @throws ExternalException
     * When table and sql attributes are mentioned ignore table attribute
     */
    public void testWithTableAndSQLTogether() throws ExternalException {
        manage("insertTestDataSet1");
        manage("withTableAndSQLTogether");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected records not retrieved", data);
        assertTrue(((Map) data.get(0)).containsKey("EMP_ID"));
        assertFalse(((Map) data.get(0)).containsKey("EMP_NAME"));
    }

    //With inputs: dep:id=12,emp_id=E1
    public void testWithTableAndSQLTogetherWithInputs()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("withTableAndSQLTogetherWithInputs");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 4);
    }

    //With inputs: dep:id=12,employeeId=E3
    //select EMP_ID from QAFE_TEST_SELECTDB where emp_id=:employeeId
    public void testWithTableAndSQLWithInputsPlaceholders()
        throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "employeeId", "E3");
        manage("withTableAndSQLWithInputsPlaceholders");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    //With one input matches multiple placeholders: dep:id=12,employeeId=E3
    //select EMP_ID from QAFE_TEST_SELECTDB where emp_id=:employeeId and dep_id=:employeeId
    public void testWithSQLWithMultiplePlaceholderMatch()
        throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "employeeId", "E3");
        manage("withSQLWithMultiplePlaceholderMatch");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNull("As the condition dont match no records should be retrieved here",
            data);
    }

    public void testWithSQLWhereContainingDate() throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "birthdate", new Date("08/10/1982"));
        manage("withSQLWhereContainingDate");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    public void testwithSQLTextWhereContainingDate() throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "birthdate", new Date("08/10/1982"));
        manage("withSQLTextWhereContainingDate");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    // priority goes to sql attribute when statement contains sql attribute and sql Text
    public void testwithSQLTextAndSQLAttributeTogether()
        throws ExternalException {
        manage("insertTestDataSet1");
        manage("withSQLTextAndSQLAttributeTogether");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 4);
        assertNotNull(((Map) data.get(0)).get("EMP_NAME"));
        assertNull(((Map) data.get(0)).get("EMP_ID"));
    }

    public void testSqlWithInputsPlaceholders() throws ExternalException {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "depId", "D2");
        DataStore.store(dataId, "empName", "ABC");
        manage("sqlWithInputsPlaceholders");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertTrue(data.size() == 1);
    }

    public void testOrderdById() throws Exception {
        manage("insertTestDataSet1");
        manage("getOrderdById");
        assertEquals(4, ((List) DataStore.getValue(dataId, "result")).size());
    }

    public void testGetByChildren() throws Exception {
        manage("insertTestDataSet1");
        DataStore.store(dataId, "children", new Integer(1));
        manage("getByChildren");

        ArrayList data = (ArrayList) DataStore.getValue(dataId, "result");
        assertNotNull("Expected record not retrieved", data);
        assertEquals("Hallo", ((Map) data.get(0)).get("emp_name"));
        assertEquals(1, data.size());
    }

    public void testGetByChildrenWhileIdNotSet() throws Exception {
        manage("insertTestDataSet1");
        DataStore.clear(dataId);
        manage("getByChildren");
        DataStore.toLogString(dataId);
        assertEquals(4, ((List) DataStore.getValue(dataId, "result")).size());
    }

    //TODO - sorting should be tested properly
    public void atestGetDataSorted() throws Exception {
        DataStore.store(dataId, "$SORT_COLUMN", "name");
        DataStore.store(dataId, "$SORT_ORDER", "desc");
        manage("getDataSorted");
        assertEquals(10, ((List) DataStore.getValue(dataId, "result")).size());

        //		assertEquals("9", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("id"));
        assertEquals("0",
            ((Map) DataStore.getValue(dataId, "result[9]")).get("id"));

        //		DataStore.getValue(dataId, "outcome[1].name");
    }
}
