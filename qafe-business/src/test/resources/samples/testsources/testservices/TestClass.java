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
package test.com.qualogy.qafe.business.integration.testservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestClass {
	public final static Logger logger = Logger.getLogger(TestClass.class.getName());
	public class TestClassObject{
		@SuppressWarnings("unused")
		private String string = "jaja";
		@SuppressWarnings("unused")
		private Integer integer = new Integer(1);
		//private boolean boolean = "jaja";
	}
	
	public void doNothing(){
		logger.info("doing nothing ;)");
	}
	
	public TestClassObject returnATestClassObject(){
		return new TestClassObject();
	}
}
