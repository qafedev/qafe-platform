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

import java.util.Iterator;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.resource.query.QueryContainer;

public class StatementsReaderTest extends TestCase{
	
	private final static Logger log = Logger.getLogger(StatementsReaderTest.class.getName());
	
	public void testReadStatement(){
		QueryContainer qc = (QueryContainer)new Reader(QueryContainer.class).read("samples/statements/statements.xml");
		for (Iterator<String> iter = qc.keySet().iterator(); iter.hasNext();) {
			log.fine((String) iter.next());
			
		}
	}
	
	public void testReadContext(){
		ApplicationStack stack = (ApplicationStack)new Reader(ApplicationStack.class).read("samples/statements/application-config.xml");
		for (Iterator iter = stack.getApplicationsIterator(); iter.hasNext();) {
			iter.next();
			//ResourcePool.getInstance().init(context);
		}
	}
	
	public void testReadStatementsWithDuplicatedQueries(){
		String result = null;
		try {
			QueryContainer qc = (QueryContainer)new Reader(QueryContainer.class).read("samples/statements/statements-duplicate-queries.xml");
			result = "Expected duplicate queries";
		} catch (ValidationException e) {
			// Expected
		}
		assertNull(result);
	}
}