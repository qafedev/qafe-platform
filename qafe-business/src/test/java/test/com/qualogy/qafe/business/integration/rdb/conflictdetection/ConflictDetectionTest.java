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
package test.com.qualogy.qafe.business.integration.rdb.conflictdetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Ignore;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.business.test.BusinessActionLookup;
import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.conflictdetection.UpdateConflictException;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;

@Ignore
public class ConflictDetectionTest extends BusinessActionTestCase {

	private String columnEmpId = "EMP_ID";
	private String columnEmpName = "EMP_NAME";

	/**
	 * 
	 CREATE TABLE QAFE_TEST_SELECTDB ( "EMP_ID" VARCHAR2(20 BYTE) NOT NULL
	 * ENABLE, "EMP_NAME" VARCHAR2(100 BYTE), "DEP_ID" VARCHAR2(20 BYTE),
	 * "BIRTHDATE" DATE, "CHILDREN" NUMBER(10,0), CONSTRAINT
	 * "QAFE_TEST_SELECTDB_PK" PRIMARY KEY ("EMP_ID") ) ;
	 */
	@Override
	protected String[] getSetupActions() {
		return new String[] { "setUp" };
	}

	@Override
	protected String[] getTearDownActions() {
		return new String[] {};
	}

	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}

	public void testConflictingUpdate1() throws ExternalException {
		conflictingUpdateTest("conflictSelect1", "conflictUpdate1");

	}

	public void testNonConflictingUpdate1() throws ExternalException {
		nonConflictingUpdateTest("conflictSelect1", "conflictUpdate1");

	}

	public void testConflictingUpdate2() throws ExternalException {
		conflictingUpdateTest("conflictSelect2", "conflictUpdate2");

	}

	public void testNonConflictingUpdate2() throws ExternalException {
		nonConflictingUpdateTest("conflictSelect2", "conflictUpdate2");

	}

	public void testConflictingUpdate3() throws ExternalException {
		conflictingUpdateTest("conflictSelect3", "conflictUpdate1");

	}

	public void testNonConflictingUpdate3() throws ExternalException {
		nonConflictingUpdateTest("conflictSelect3", "conflictUpdate1");

	}

	/**
	 * If the select statement contains 1 or more column aliases, the conflicting update will NOT be detected.
	 * This feature must be documented in the user manual.
	 */
	public void testConflictingUpdate4() throws ExternalException {
		columnEmpId = "EMPID";
		columnEmpName = "EMPNAME";
		DataIdentifier dataId1 = DataStore.register();
		DataIdentifier dataId2 = DataStore.register();

		List<Map<String, String>> data1 = readData("conflictSelect4", dataId1);
		List<Map<String, String>> data2 = readData("conflictSelect4", dataId2);

		data1.get(0).put(columnEmpName, "Friso");
		data2.get(0).put(columnEmpName, "Salar");

		updateData("conflictUpdate4", dataId1, data1);
		updateData("conflictUpdate4", dataId2, data2);
	}

	public void testNonConflictingUpdate4() throws ExternalException {
		columnEmpId = "EMPID";
		columnEmpName = "EMPNAME";
		nonConflictingUpdateTest("conflictSelect4", "conflictUpdate4");
	}

	/**
	 * A delete of a concurrently modified record will NOT be detected.
	 * This feature must be documented in the user manual.
	 */
	public void testConflictingDelete1() throws ExternalException {
		DataIdentifier dataId1 = DataStore.register();
		DataIdentifier dataId2 = DataStore.register();

		readData("conflictSelect1", dataId1);
		List<Map<String, String>> data2 = readData("conflictSelect1", dataId2);

		data2.get(0).put(columnEmpName, "Salar");

		updateData("conflictUpdate1", dataId2, data2);
		DataStore.store(dataId1, "pk", "E1");
		manage(dataId1, "conflictDelete1");
	}

	private void conflictingUpdateTest(String conflictSelect, String conflictUpdate) throws ExternalException {
		DataIdentifier dataId1 = DataStore.register();
		DataIdentifier dataId2 = DataStore.register();

		List<Map<String, String>> data1 = readData(conflictSelect, dataId1);
		List<Map<String, String>> data2 = readData(conflictSelect, dataId2);

		data1.get(0).put(columnEmpName, "Friso");
		data2.get(0).put(columnEmpName, "Salar");

		updateData(conflictUpdate, dataId1, data1);
		try {
			// test
			updateData(conflictUpdate, dataId2, data2);
			fail("exception expected");
		} catch (UpdateConflictException e) {
			Logger.getLogger(getClass().getName()).info(e.getMessage());
		}
	}

	private void nonConflictingUpdateTest(String conflictSelect, String conflictUpdate) throws ExternalException {
		DataIdentifier dataId1 = DataStore.register();
		DataIdentifier dataId2 = DataStore.register();

		List<Map<String, String>> data1 = readData(conflictSelect, dataId1);
		List<Map<String, String>> data2 = readData(conflictSelect, dataId2);

		data1.get(0).put(columnEmpName, "Friso");
		data2.get(1).put(columnEmpName, "Ravi");

		updateData(conflictUpdate, dataId1, data1.get(0));
		updateData(conflictUpdate, dataId2, data2.get(1));
	}

	private List<Map<String, String>> readData(String conflictSelect, DataIdentifier dataIdX) throws ExternalException {
		manage(dataIdX, conflictSelect);
		@SuppressWarnings("unchecked")
		List<Map<String, String>> data1 = (List<Map<String, String>>) DataStore.getValue(dataIdX, "result");
		Collections.sort(data1, createComparator(columnEmpId));
		return data1;
	}

	private void updateData(String conflictUpdate, DataIdentifier dataIdX, Map<String, String> dataX)
			throws ExternalException {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(dataX);
		updateData(conflictUpdate, dataIdX, list);
	}
	
	private void updateData(String conflictUpdate, DataIdentifier dataIdX, List<Map<String, String>> dataX)
			throws ExternalException {
		DataStore.store(dataIdX, "listRefVar", dataX);
		manage(dataIdX, conflictUpdate);
	}

	private void manage(DataIdentifier theDataId, String businessActionId) throws ExternalException {
		List<BusinessAction> actions = context.getApplicationMapping().getBusinessTier().getBusinessActions();
		BusinessAction ba = BusinessActionLookup.getBusinessActionForId(actions, businessActionId);
		context.getBusinessManager().manage(context, theDataId, ba);
	}

	private Comparator<Map<String, String>> createComparator(final String empIdColumn) {
		return new Comparator<Map<String, String>>() {
			@Override
			public int compare(Map<String, String> map1, Map<String, String> map2) {
				return map1.get(empIdColumn).compareTo(map2.get(empIdColumn));
			}
		};
	}

}
