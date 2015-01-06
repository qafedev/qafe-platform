/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package test.com.qualogy.qafe.business.integration.rdb;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.resource.query.SQLQuery;
import com.qualogy.qafe.bind.resource.query.Select;
import com.qualogy.qafe.bind.resource.query.Update;
import com.qualogy.qafe.business.resource.rdb.query.QueryToStringCreator;

public class StatementToStringCreatorTest extends TestCase{

	public void testSelectSQLEmpty(){
		new Select(null, null, null, null);
		//expected result no exceptions
	}

	public void testSelectSQLAsAttribute(){
		SQLQuery statement = new Select(null, null, "select * from A", null);
		String expectedSql = "select * from A";
		assertTrue(expectedSql.equals(QueryToStringCreator.toString(statement, null, null, null)));
	}

    /*
     */
	private String testToStringMethod(String sql, String[] inputMapping){
		SQLQuery statement = new Select(null, null, sql, null);
		return QueryToStringCreator.toString(statement, null, inputMapping, null);
	}
//	inputs will be used as placeholders only in case of sql attribute
	public void testSelectSQLAsAttributeUseIn(){
		SQLQuery statement = new Select(null, null, "select * from A", null);
		String expectedSql = "select * from A".toUpperCase();
		String[] ins = new String[]{"0", "1"};
		assertEquals(expectedSql.toLowerCase().toLowerCase(), QueryToStringCreator.toString(statement,null, ins, null).toLowerCase());
	}
	// outs will not change the sql
	public void testSelectSQLAsAttributeUseOut(){
		SQLQuery statement = new Select(null, null, "select * from A", null);
		String expectedSql = "select * from A".toUpperCase();
		Set outs = create(new String[]{"0", "1"});
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, outs).toLowerCase());
	}
	// ins/outs will not change the sql
	public void testSelectSQLAsAttributeUseInAndOut(){
		SQLQuery statement = new Select(null, null, "select 0,1 from A", null);
		String expectedSql = "select 0,1 from A".toUpperCase();
		String[] ins = new String[]{"0", "1"};
		Set outs = create(new String[]{"3", "4"});
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, ins, outs).toLowerCase());
	} 
	public void testSelectSQLAsText(){
		SQLQuery statement = new Select(null, "select * from A", null, null);
		String expectedSql = "select * from A";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}	
	public void testSelectSQLAsTextUseIn(){
		SQLQuery statement = new Select(null, "select * from A", null, null);
		String expectedSql = "select * from A";
		String[] ins = new String[]{"0", "1"};
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, ins, null).toLowerCase());
		
	} 
	// outs will not change the sql
	public void testSelectSQLAsTextUseOut(){
		SQLQuery statement = new Select(null, "select id from A", null, null);
		String expectedSql = "select id from A";
		Set outs = create(new String[]{"0", "1"});
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, outs).toLowerCase());
		
	} 
	// outs/ins will not change the sql
	public void testSelectSQLAsTextUseInAndOut(){
		SQLQuery statement = new Select(null, "select id from A", null, null);
		String expectedSql = "select id from A";
		String[] ins = new String[]{"0", "1"};
		Set outs = create(new String[]{"0", "1"});
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement, null,ins, outs).toLowerCase());
	} 
	//when table attribute is provided ins are used to form where 
	public void testSelectSQLUseInAndOutAndTableName(){
		SQLQuery statement = new Select(null, null, null, "A");
		String expectedSql = "select * from a where 0=:0  and 1=:1";
		String[] ins = new String[]{"0", "1"};
		Set outs = create(new String[]{"0", "1"});
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, ins, outs).toLowerCase());
	}
	
	public void testUpdateSimple(){
		SQLQuery statement = new Update(null, null, "HSD_EMPLOYEES");
		String[] ins = new String[]{"NAME"};
		Set outs = create(new String[]{});
		String expectedSql = "update HSD_EMPLOYEES set NAME=:NAME";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, ins, outs).toLowerCase());
	}
	
	public void testUpdateSimpleMultipleIns(){
		SQLQuery statement = new Update(null, null, "HSD_EMPLOYEES");
		String[] ins = new String[]{"NAME","LAST_NAME"};
		Set outs = create(new String[]{});
		String expectedSql = "update HSD_EMPLOYEES set NAME=:NAME, LAST_NAME=:LAST_NAME";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, ins, outs).toLowerCase());
	}
	
	public void testSelectWithTable(){
		SQLQuery statement = new Select(null, null, null, "C");
		String expectedSql = "select * from C";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}
	
	// priority first is to sql attribute, then sql text then tablename
	public void testSelectWithSQLTextAttrubuteTableTogether(){
		SQLQuery statement = new Select(null, "select * from A", "select * from B", "C");
		String expectedSql = "select * from B";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}
	
	public void testSelectWithEmptySQLTextEmptyAttrubuteTableTogether(){
		SQLQuery statement = new Select(null, "", "", "C");
		String expectedSql = "select * from C";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}
	
	public void testSelectWithSQLTextEmptyAttrubuteTableTogether(){
		SQLQuery statement = new Select(null, "select * from A", "", "C");
		String expectedSql = "select * from A";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}
	
	public void testSelectWithEmptySQLTextAttrubuteTableTogether(){
		SQLQuery statement = new Select(null, "select * from A", "", "C");
		String expectedSql = "select * from A";
		assertEquals(expectedSql.toLowerCase(), QueryToStringCreator.toString(statement,null, null, null).toLowerCase());
	}
	
	private Set create(String[] args){
		TreeSet set = new TreeSet();
		set.addAll(Arrays.asList(args));
		return set;
	}
	
}
