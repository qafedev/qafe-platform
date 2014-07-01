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
package test.com.qualogy.qafe.core.application;



import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;
import com.qualogy.qafe.core.application.LoadFailedException;
import com.qualogy.qafe.core.application.NotLoadedException;

public class ApplicationContextLoaderTest extends TestCase {
	public final static String PACKAGE = "test.com.qualogy.genesis.core.application.";
	public final static String CLASS_NAME_INITIALIZER_A = PACKAGE + "InitializerA";
	public final static String CLASS_NAME_INITIALIZER_B = PACKAGE + "InitializerB";
	public final static String FILE_NAME_FRAMEWORK_FILE_1 = "samples/readertest/1.xml";
	public final static String FILE_NAME_FRAMEWORK_FILE_2 = "samples/readertest/2.xml";
	
	public final static String[] DEFAULT_FILE_PATHS = new String[]{FILE_NAME_FRAMEWORK_FILE_1, FILE_NAME_FRAMEWORK_FILE_2};  
	
	private String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	public void testLoadHappyDay(){
		
		ApplicationContext context = loadAppHappyDay();
		ApplicationMapping default_gf = context.getApplicationMapping();
		
		assertNotNull(default_gf.getPresentationTier().getView());
		assertNull(default_gf.getIntegrationTier());
	}
	
	private ApplicationContext loadAppHappyDay(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-happy-day.xml");
		
		ApplicationContext context = null;
		
		//expecting only the app happy day in the cluster
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			context = (ApplicationContext) iter.next();
		}
		
		return context;
	}
	
	public void testLoadWithMessages(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-with-messages.xml");
	}
	
//	public void testLoadWithEmptyMapping(){
//		try{
//			//has root pointing to non-exsting dir
//			ApplicationContextLoader.load(getSamplesDir() + "application-config-empty-mapping.xml");
//			fail("expecting an exception");
//		}catch(LoadFailedException e){
//		}
//		
//	}

	public void testLoadWithNoMapping(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-no-mapping.xml");
	}

	public void testNonExistingAppConfigLocation(){
		try{
			ApplicationContextLoader.load(getSamplesDir() + "jaja");
			fail("expecting an exception");
		}catch(LoadFailedException e){
		}
	}
	
	public void testLoadTransactionTest(){

		Map<String, String> BA_IDS = new HashMap<String, String>();
		BA_IDS.put("BA_MANAGED", "10");
		BA_IDS.put("NO_WILDCARD", "1"); 
		BA_IDS.put("WILDCARD2WILDCARD", "2");  
		BA_IDS.put("WILDCARD3", "3" );
		BA_IDS.put("4WILDCARD", "4" );
		
		ApplicationContextLoader.load(getSamplesDir() + "application-config-transactiontest.xml");
		
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			List<BusinessAction> businessActions = context.getApplicationMapping().getBusinessTier().getBusinessActions();
			for (Iterator<BusinessAction> iterator = businessActions.iterator(); iterator.hasNext();) {
				BusinessAction ba = (BusinessAction) iterator.next();
				String timeout = (String) BA_IDS.get(ba.getId());
				if(timeout!=null)
					assertEquals("id["+ba.getId()+"]", Integer.parseInt(timeout), ba.getTransactionBehaviour().getTimeout());
				else
					assertNull(ba.getTransactionBehaviour());
			}
		}
		
	}
	
	/**
	 * this test tests if the reader can leve a file out, which is in the assigned root directory
	 *
	 */
//	public void testLoadWithMappingTwoSameFiles(){
//		//has root pointing to non-exsting dir
//		//TODO: nice exception handling on id bug
//		try{
//			ApplicationContextLoader.load(getSamplesDir() + "application-two-configs.xml");
//			fail("expecting to throw on duplicate");
//		}catch(Exception e){
//			
//		}
//	}
	
	public void testLoadWithoutAppsDefined(){
/*
	<application name="app-4a" root=""/>
	<application name="app-4b"/>
	<application name="app-6a" root="samples/contextnoapps">
		<application-mapping resource="1.xml"/>
	</application>
	<application name="app-6b" root="samples/contextnoapps" />
	<application name="app-6c" root="">
		<application-mapping resource="samples/contextnoapps/1.xml"/>
	</application>
	<application name="app-6d" root="">
		<application-mapping resource="samples/contextnoapps"/>
	</application>
	<application name="app-6e" root="/workspace_qpd/genesis-core/test/samples/contextnoapps">
		<application-mapping resource="1.xml"/>
	</application>
	<application name="app-6f" root="">
		<application-mapping resource="/workspace_qpd/genesis-core/test/samples/contextnoapps/1.xml"/>
	</application>
	<application name="app-6g" root="/workspace_qpd/genesis-core/test/samples/contextnoapps" />
*/
		ApplicationContextLoader.load(getSamplesDir() + "application-config-no-apps-defined.xml");
		assertNotNull(ApplicationCluster.getInstance().iterator());
		Map<String, String> expectedDirs = new HashMap<String, String>();
		expectedDirs.put("app-4a", new File(getSamplesDir()).getAbsolutePath());		
		expectedDirs.put("app-4b", new File(getSamplesDir()).getAbsolutePath());
		expectedDirs.put("app-6b", new File("samples/contextnoapps").getAbsolutePath());
//		expectedDirs.put("app-6c", new File(getSamplesDir()).getAbsolutePath());
//		expectedDirs.put("app-6d", new File(getSamplesDir()).getAbsolutePath());
//		expectedDirs.put("app-6g", new File("samples/contextnoapps").getAbsolutePath());
		expectedDirs.put("app-7", new File(getSamplesDir()).getAbsolutePath());
		
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			String name = context.getName();
			if(!expectedDirs.containsKey(name))
				continue;
			assertEquals(name, expectedDirs.get(name), new File(context.getRoot()).getAbsolutePath());
			ApplicationMapping gf = context.getApplicationMapping();
			assertNotNull(gf);
			assertNotNull(gf.getPresentationTier());
			assertNotNull(gf.getPresentationTier().getView());
			assertNotNull(gf.getPresentationTier().getView().getWindows());
			assertNotNull(gf.getPresentationTier().getView().getWindows().get(0));
			assertEquals(((Window)gf.getPresentationTier().getView().getWindows().get(0)).getDisplayname(), "succes");
		}
	}
	
	public void testDuplicateKey(){
		String fileName = "application-config-duplicate-key.xml";
		try{
			ApplicationContextLoader.load(getPath(fileName));
		}catch(BindException e){
			
		}
		
	}
	
	private String getPath(String fileName){
		File dir = new File(getSamplesDir());
		String path = dir.getPath() + File.separator;
		return path + fileName;
	}
	public void testLoadWithoutRoot(){
		File dir = new File(getSamplesDir());
		ApplicationContextLoader.load(getPath("application-config-no-root.xml"));
		assertNotNull(ApplicationCluster.getInstance().iterator());
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			if(!context.getId().startsWith("app-no-root"))
				continue;
			String expected = dir.getAbsolutePath() + File.separator;
			assertEquals(expected, context.getRoot());
		}
	}
	
//	public void testLoadNoMappingLocationSet(){
//		try{
//			ApplicationContextLoader.load(getPath("application-config-no-mappinglocation.xml"));
//			fail("expecting loadfailedexception");
//		}catch(LoadFailedException e){
//			
//		}
//		
//	}
	
	
	public void testReLoadOneContextHappyDay(){
		ApplicationContext context = loadAppHappyDay();
		context = ApplicationContextLoader.reload(context.getId());

		ApplicationMapping default_gf = context.getApplicationMapping();
		
		assertNotNull(default_gf.getPresentationTier().getView());
		assertNull(default_gf.getIntegrationTier());
	}

	public void testReLoadAllHappyDay(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-no-apps-defined.xml");
		
		Set<String> ids = new HashSet<String>();
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			ids.add(context.getId().stringValueOf());
		}
		
		ApplicationContextLoader.reload();
		
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			assertTrue(ids.contains(context.getId().stringValueOf()));
			ids.remove(context.getId().stringValueOf());
		}
		
		assertEquals(0, ids.size());
		
		ApplicationContext contextHappyDay = loadAppHappyDay();
		
		ApplicationContextLoader.reload();
		
		ApplicationCluster.getInstance().get(contextHappyDay.getId());
		
		ApplicationMapping default_gf = contextHappyDay.getApplicationMapping();
		
		assertNotNull(default_gf.getPresentationTier().getView());
		assertNull(default_gf.getIntegrationTier());
	}
	
	public void testUnLoadForIdHappyDay(){
		ApplicationContext context = loadAppHappyDay();
		ApplicationContextLoader.unload(context.getId());
		
		try{
			ApplicationCluster.getInstance().get(context.getId());
			fail("expected the context to be unloaded");
		}catch(NotLoadedException e){
			
		}

	}
	
	public void testUnLoadAllHappyDay(){
		ApplicationContext context = loadAppHappyDay();
		ApplicationContextLoader.unload();

		try{
			ApplicationCluster.getInstance().get(context.getId());
			fail("expected the context to be unloaded");
		}catch(NotLoadedException e){
		}

	}

	protected void setUp() throws Exception {
		super.setUp();
		
		ApplicationContextLoader.unload();
	}
}

