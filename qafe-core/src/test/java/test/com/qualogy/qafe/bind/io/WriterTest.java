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
package test.com.qualogy.qafe.bind.io;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.domain.BusinessTier;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.io.Writer;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.bind.resource.query.SQLOnly;
import com.qualogy.qafe.bind.resource.query.SQLQuery;
import com.qualogy.qafe.bind.resource.query.Select;
import com.qualogy.qafe.bind.util.PostProcessor;

public class WriterTest extends TestCase{
	
	public void testWriteBusinessAction(){
		
		//simple business-action
		BusinessAction businessAction = new BusinessAction("deleteRecord");
		
		businessAction.setComment("this is a comment");
		
		BusinessTier tier = new BusinessTier();
		
		tier.add(businessAction);
		ApplicationMapping gf = ApplicationMapping.create(tier, null, null);
		PostProcessor.process(gf);
		
		//write business action
		new Writer().write(gf, "output", "application-write-result.xml");
	
		//validate result
		new Reader(ApplicationMapping.class).read("output/application-write-result.xml");
	}
	
	public void testWriteStatements(){
		SQLQuery query = new Select();
		query.setId("11");
		query.setTable("jaja");
		
		QueryContainer container = new QueryContainer();
		container.put(query);
		
		new Writer().write(container, "output", "statements-write-result.xml");
	}
	
	public void testWriteStatementsWithSqlText(){
		SQLQuery query = new SQLOnly();
		query.setId("stmt1");
		query.setTable("");
		query.setSqlAsText("select * from EMPLOYEE");
		
		QueryContainer container = new QueryContainer();
		container.put(query);
		
		new Writer().write(container, "output", "testWriteStatementsWithSqlText-write-result.xml");
	}	
}