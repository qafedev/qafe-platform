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
package test.com.qualogy.qafe.business.integration.rdb.update;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

@SuppressWarnings("deprecation")
public class RDBServiceProcessorUpdateTest extends BusinessActionTestCase {

	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
	
	protected String[] getSetupActions() {
		return new String[]{"deleteData", "insertData"};
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testSimpleUpdate() throws Exception {
		DataStore.store(dataId, "nameRefVar", "Sam");
		manage("testUpdateWithoutPK");
		manage("testSelectToVerify");
		assertEquals("Sam", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testUpdateWithPK() throws Exception {
		DataStore.store(dataId, "nameRefVar", "Michelle");
		DataStore.store(dataId, "idRefVar", new Integer(1));
		manage("testUpdateWithPK");
		manage("testSelectToVerify");
		assertEquals("Michelle", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testUpdateMultipleFieldsWithoutPK() throws Exception {
		DataStore.store(dataId, "nameRefVar", "Ethan");
		DataStore.store(dataId, "addressRefVar", "Amsterdam");
		DataStore.store(dataId, "salaryRefVar", 0);
		DataStore.store(dataId, "dateRefVar", new Date("01/14/2011"));
		manage("testUpdateMultipleFieldsWithoutPK");
		manage("testSelectToVerify");
		assertEquals("Ethan", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals("Amsterdam", DataStore.getValue(dataId, "result[0].ADDRESS"));
		assertTrue(DataStore.getValue(dataId, "result[0].ENTRY_DATE") instanceof Timestamp); // Verifying Object type.
		assertEquals("2011-01-14 00:00:00.0", DataStore.getValue(dataId, "result[0].ENTRY_DATE").toString()); // Verifying data value.
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testUpdateMultipleFieldsWithPK() throws Exception {
		DataStore.store(dataId, "idRefVar", 1);
		DataStore.store(dataId, "nameRefVar", "Ford");
		DataStore.store(dataId, "addressRefVar", "Den Haag");
		DataStore.store(dataId, "salaryRefVar", 4444);
		manage("testUpdateMultipleFieldsWithPK");
		manage("testSelectToVerify");
		assertEquals("Ford", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals("Den Haag", DataStore.getValue(dataId, "result[0].ADDRESS"));
		assertTrue(DataStore.getValue(dataId, "result[0].SALARY") instanceof BigDecimal);
		assertEquals(4444, Integer.parseInt(DataStore.getValue(dataId, "result[0].SALARY").toString()));
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testDateUpdateWithPK() throws Exception {
		DataStore.store(dataId, "idRefVar", 1);
		DataStore.store(dataId, "dateRefVar", new Date("02/27/2011"));
		manage("testDateUpdateWithPK");
		manage("testSelectToVerify");
		assertTrue(DataStore.getValue(dataId, "result[0].ENTRY_DATE") instanceof Timestamp); // Verifying Object type.
		assertEquals("2011-02-27 00:00:00.0", DataStore.getValue(dataId, "result[0].ENTRY_DATE").toString()); // Verifying data value.
	}
	
	/* Testing with -> <update id="updateWithSqlAttribute" sql="update QAFE_TEST_UPDATE set NAME='Bob' where ID=1"/> */
	public void testUpdateWithSQLAttribute() throws Exception{
		manage("testUpdateWithSQLAttribute");
		manage("testSelectToVerify");
		assertEquals("Bob", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAttributeAndInParams" sql="update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar, ENTRY_DATE=:dateVar where ID = :idVar"/> */
	public void testUpdateWithSqlAttributeAndInParams() throws Exception {
		DataStore.store(dataId, "idRefVar", 1);
		DataStore.store(dataId, "nameRefVar", "Eva");
		DataStore.store(dataId, "salaryRefVar", 5000);
		DataStore.store(dataId, "dateRefVar", new Date("03/08/2011"));
		manage("testUpdateWithSqlAttributeAndInParams");
		manage("testSelectToVerify");
		assertEquals("Eva", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAttributeAndInParams" sql="update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar, ENTRY_DATE=:dateVar where ID = :idVar"/> */
	public void testUpdateWithSqlAttributeAndInParamsAsMap() throws Exception {
		HashMap<String, Object> mapRefVar = new HashMap<String, Object>();
		// Key should be same as that of the reference variable used in the SQL statement.
		mapRefVar.put("idVar", new Integer(1));
		mapRefVar.put("nameVar", "Amy");
		mapRefVar.put("salaryVar", 8888);
		mapRefVar.put("dateVar", new Date("02/15/2011"));
		DataStore.store(dataId, "mapRefVar", mapRefVar);
		manage("testUpdateWithSqlAttributeAndInParamsAsMap");
		manage("testSelectToVerify");
		assertEquals("Amy", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testUpdateTagWithInParamsAsMap() throws Exception {
		HashMap<String, Object> mapRefVar = new HashMap<String, Object>();
		// Key should be same as that of the column name. case INSENSITIVE
		mapRefVar.put("id", new Integer(1));
		mapRefVar.put("name", "James Bond");
		mapRefVar.put("ADDRESS", "London");
		mapRefVar.put("salary", 007);
		mapRefVar.put("ENTRY_DATE", new Date("03/25/2011"));
		DataStore.store(dataId, "mapRefVar", mapRefVar);
		manage("testUpdateTagWithInParamsAsMap");
		manage("testSelectToVerify");
		assertEquals("James Bond", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAttributeAndInParams" sql="update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar, ENTRY_DATE=:dateVar where ID=:idVar"/> */
	public void testUpdateWithSqlAttributeAndInParamsAsCollection() throws Exception {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		// Keys of the Map should be same as that of the reference variable used in the SQL statement.
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("idVar", new Integer(1));
		hm1.put("nameVar", "Jack Sparrow");
		hm1.put("addressVar", "Black Pearl"); // addressVar not mapped in SQL statement and hence won't be updated
		hm1.put("salaryVar", 6666);
		hm1.put("dateVar", new Date("04/30/2011"));
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("idVar", new Integer(2));
		hm2.put("nameVar", "Peter Pan");
		hm2.put("salaryVar", 5432);
		hm2.put("dateVar", new Date("05/18/2010"));
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
		manage("testUpdateWithSqlAttributeAndInParamsAsCollection");
		manage("testSelectAllToVerify");
		assertEquals("Jack Sparrow", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals("Peter Pan", DataStore.getValue(dataId, "result[1].NAME"));
	}
	
	/* Testing with -> <update id="useUpdateTagWithTableName" table="QAFE_TEST_UPDATE"/> */
	public void testUpdateTagWithInParamsAsCollection() throws Exception {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		// Keys of the Map should be same as that of the column name. case INSENSITIVE
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("ID", new Integer(1));
		hm1.put("name", "Neo");
		hm1.put("address", "Nebuchadnezzar"); // addressVar not mapped in SQL statement and hence won't be updated
		hm1.put("SALARY", 6666);
		hm1.put("entry_date", new Date("04/30/2011"));
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("id", new Integer(2));
		hm2.put("NAME", "Agent Smith");
		hm2.put("SALARY", 5432);
		hm2.put("ENTRY_DATE", new Date("05/18/2010"));
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
		manage("testUpdateTagWithInParamsAsCollection");
		manage("testSelectAllToVerify");
		assertEquals("Neo", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals("Agent Smith", DataStore.getValue(dataId, "result[1].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAsText">update QAFE_TEST_UPDATE set NAME='Amanda', SALARY=8000 where ID=1</update> */
	public void testUpdateWithSqlAsText() throws Exception {
		manage("testUpdateWithSqlAsText");
		manage("testSelectToVerify");
		assertEquals("Amanda", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals(8000, Integer.parseInt(DataStore.getValue(dataId, "result[0].SALARY").toString()));
	}
	
	/* Testing with -> <update id="updateWithSqlAsTextAndInParams">update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar where ID=:idVar</update> */
	public void testUpdateWithSqlAsTextAndInParams() throws Exception {
		DataStore.store(dataId, "idRefVar", 1);
		DataStore.store(dataId, "nameRefVar", "Trinity");
		DataStore.store(dataId, "salaryRefVar", 0);
		manage("testUpdateWithSqlAsTextAndInParams");
		manage("testSelectToVerify");
		assertEquals("Trinity", DataStore.getValue(dataId, "result[0].NAME"));
		assertEquals(0, Integer.parseInt(DataStore.getValue(dataId, "result[0].SALARY").toString()));
	}
	
	/* Testing with -> <update id="updateWithSqlAsTextAndInParams">update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar where ID=:idVar</update> */
	public void testUpdateWithSqlAsTextAndInParamsAsMap() throws Exception {
		HashMap<String, Object> mapRefVar = new HashMap<String, Object>();
		// Key should be same as that of the variable name used in the SQL statment.
		mapRefVar.put("idVar", new Integer(1));
		mapRefVar.put("nameVar", "Al Bundy");
		mapRefVar.put("salaryVar", 10000);
		DataStore.store(dataId, "mapRefVar", mapRefVar);
		manage("testUpdateWithSqlAsTextAndInParamsAsMap");
		manage("testSelectToVerify");
		assertEquals("Al Bundy", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAsTextAndInParams">update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar where ID=:idVar</update> */
	public void testUpdateWithSqlAsTextAndInParamsAsCollection() throws Exception {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		// Keys of the Map should be same as that of the reference variable used in the SQL statement.
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("idVar", new Integer(3));
		hm1.put("nameVar", "Shrek");
		hm1.put("salaryVar", 7777);
		hm1.put("dateVar", new Date("04/30/2011"));
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("idVar", new Integer(4));
		hm2.put("nameVar", "Pheona");
		hm2.put("salaryVar", 2222);
		hm2.put("dateVar", new Date("05/18/2010"));
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
		manage("testUpdateWithSqlAsTextAndInParamsAsCollection");
		manage("testSelectAllToVerify");
		assertEquals("Shrek", DataStore.getValue(dataId, "result[2].NAME"));
		assertEquals("Pheona", DataStore.getValue(dataId, "result[3].NAME"));
	}
	
	/* Testing with - > <update id="updateWithAllAttributes" table="IGNORED" sql="update QAFE_TEST_UPDATE set ADDRESS=:addressVar where ID=:idVar" where="IGNORED"/> */
	public void testUpdateWithAllAttributes() throws Exception {
		DataStore.store(dataId, "idRefVar", new Integer(3));
		DataStore.store(dataId, "addressRefVar", "Melbourne");
		manage("testUpdateWithAllAttributes");
		manage("testSelectAllToVerify");
		assertEquals("Melbourne", DataStore.getValue(dataId, "result[2].ADDRESS"));
	}
	
	/* Testing with - > <update id="updateWithAllAttributes" table="IGNORED" sql="update QAFE_TEST_UPDATE set ADDRESS=:addressVar where ID=:idVar" where="IGNORED"/> */
	public void testUpdateWithAllAttributesAndInParamsAsMap() throws Exception {
		HashMap<String, Object> mapRefVar = new HashMap<String, Object>();
		mapRefVar.put("idVar", new Integer(1));
		mapRefVar.put("addressVar", "Middle Earth");
		DataStore.store(dataId, "mapRefVar", mapRefVar);
		manage("testUpdateWithAllAttributesAndInParamsAsMap");
		manage("testSelectToVerify");
		assertEquals("Middle Earth", DataStore.getValue(dataId, "result[0].ADDRESS"));
	}
	
	/* Testing with - > <update id="updateWithAllAttributes" table="IGNORED" sql="update QAFE_TEST_UPDATE set ADDRESS=:addressVar where ID=:idVar" where="IGNORED"/> */
	public void testUpdateWithAllAttributesAndInParamsAsCollection() throws Exception {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		// Keys of the Map should be same as that of the reference variable used in the SQL statement.
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("idVar", new Integer(3));
		hm1.put("addressVar", "Almeere");
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("idVar", new Integer(4));
		hm2.put("addressVar", "Arhnem");
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
		manage("testUpdateWithAllAttributesAndInParamsAsCollection");
		manage("testSelectAllToVerify");
		assertEquals("Almeere", DataStore.getValue(dataId, "result[2].ADDRESS"));
		assertEquals("Arhnem", DataStore.getValue(dataId, "result[3].ADDRESS"));
	}
	
	/* Testing with -> <update id="updateWithTableAndSqlAttribute" table="IGNORED" sql="update QAFE_TEST_UPDATE set ADDRESS=:addressVar, SALARY=:salaryVar where ID=:idVar"/> */
	public void testUpdateWithTableAndSqlAttribute() throws Exception {
		DataStore.store(dataId, "idRefVar", new Integer(1));
		DataStore.store(dataId, "addressRefVar", "Cairo");
		DataStore.store(dataId, "salaryRefVar", 4343);
		manage("testUpdateWithTableAndSqlAttribute");
		manage("testSelectAllToVerify");
		assertEquals("Cairo", DataStore.getValue(dataId, "result[0].ADDRESS"));
		assertEquals(4343, Integer.parseInt(DataStore.getValue(dataId, "result[0].SALARY").toString()));
	}
	
	/* Testing with -> <update id="updateWithSqlAndWhereAttribute" sql="update QAFE_TEST_UPDATE set NAME=:nameVar, ENTRY_DATE=:dateVar where ID=:idVar" where="IGNORED"/> */
	public void testUpdateWithSqlAndWhereAttribute() throws Exception {
		DataStore.store(dataId, "idRefVar", new Integer(2));
		DataStore.store(dataId, "nameRefVar", "Muller");
		DataStore.store(dataId, "dateRefVar", new Date("06/31/2012"));
		manage("testUpdateWithSqlAndWhereAttribute");
		manage("testSelectAllToVerify");
		assertEquals("Muller", DataStore.getValue(dataId, "result[1].NAME"));
	}
	
	/* Testing with -> <update id="updateWithSqlAttributeAndSqlText" sql="update QAFE_TEST_UPDATE set ENTRY_DATE=:dateVar where ID=:idVar">IGNORED</update> */
	public void testUpdateWithSqlAttributeAndSqlText() throws Exception {
		DataStore.store(dataId, "idRefVar", new Integer(2));
		DataStore.store(dataId, "dateRefVar", new Date("12/29/2012"));
		manage("testUpdateWithSqlAttributeAndSqlText");
		manage("testSelectAllToVerify");
		assertEquals("2012-12-29 00:00:00.0", DataStore.getValue(dataId, "result[1].ENTRY_DATE").toString()); // Verifying data value.
	}
	
	/* Testing with -> <update id="updateWithTableAndWhereAttribute" table="QAFE_TEST_UPDATE" where="ID > :ID and ADDRESS=:ADDRESS"/> */
	public void testUpdateWithTableAndWhereAttribute() throws Exception {
		DataStore.store(dataId, "idRefVar", 2); // In where condition
		DataStore.store(dataId, "nameRefVar", "Bennu");
		DataStore.store(dataId, "addressRefVar", "C3"); // In where condition
		DataStore.store(dataId, "emailRefVar", "z@z.com");
		manage("testUpdateWithTableAndWhereAttribute");
		manage("testSelectAllToVerify");
		assertEquals("z@z.com", DataStore.getValue(dataId, "result[2].EMAIL"));
	}
	
	/*Testing with -> <update id="updateWithSQLTextUsingCDATA"><![CDATA[update QAFE_TEST_UPDATE set NAME=:nameVar, SALARY=:salaryVar where ID > :minIdVar and ID < :maxIdVar]]></update>*/
	public void testUpdateWithSQLTextUsingCDATA() throws Exception {
		DataStore.store(dataId, "nameRefVar", "Jupiter"); 
		DataStore.store(dataId, "salaryRefVar", 400);
		DataStore.store(dataId, "minRefVar", 0); 
		DataStore.store(dataId, "maxRefVar", 2);
		manage("testUpdateWithSQLTextUsingCDATA");
		manage("testSelectToVerify");
		assertEquals("Jupiter", DataStore.getValue(dataId, "result[0].NAME"));
	}
	
	protected String[] getTearDownActions() {
		return new String[]{"deleteData"};
	}		
}