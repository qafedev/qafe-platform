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
package test.com.qualogy.qafe.bind.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;

public class PresentationTierTest {
	
	String testWindowId = "testWindowId";
	String firstEventId = "firstEvent";
	String appId = "processEventsTestApp";
	
	ApplicationContext context = null;
	Window window =null;
	
	@Before
	public void init(){
		
		ApplicationContextLoader.load("samples/" + "application-config.xml");		
		context = ApplicationCluster.getInstance().getApplicationContext(appId);
		window  = (Window) context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(testWindowId);			
		
	}
	
	@Test
	public void eventWithOneComponentReference(){
		String firstEventId = "eventWithOneComponentReference";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 1, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithTwoComponentReference(){
		String firstEventId = "eventWithTwoComponentReference";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 2, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("myComponentId2");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInSet(){
		String firstEventId = "eventWithComponentReferenceInSet";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 1, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInDialog(){
		String firstEventId = "eventWithComponentReferenceInDialog";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 1, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInIf(){
		String firstEventId = "eventWithComponentReferenceInIf";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 2, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("myComponentId2");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	
	@Test
	public void eventWithComponentReferenceInSwitch(){
		String firstEventId = "eventWithComponentReferenceInSwitch";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 2, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("myComponentId2");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInBusinessActionRef(){
		String firstEventId = "eventWithComponentReferenceInBusinessActionRef";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 1, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInIteration(){
		String firstEventId = "eventWithComponentReferenceInIteration";	
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 2, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("myComponentId2");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithComponentReferenceInNestedIf(){
		String firstEventId = "eventWithComponentReferenceInNestedIf";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 4, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("myComponentId2");
		expectedReferences.add("myComponentId3");
		expectedReferences.add("myComponentId4");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithOneEventRef(){
		String firstEventId = "eventWithOneEventRef";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 3, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("ref1InFirstEvent");
		expectedReferences.add("myComponentId1");
		expectedReferences.add("ref2InFirstEvent");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithMultipleCallToSameEventRef(){
		String firstEventId = "eventWithMultipleCallToSameEventRef";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 3, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("ref1InFirstEvent");
		expectedReferences.add("myComponentId1");
		expectedReferences.add("ref2InFirstEvent");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventWithMultipleCallsToSameEventRefAndAnotherEvent(){
		String firstEventId = "eventWithMultipleCallsToSameEventRefAndAnotherEvent";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 3, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("ref1InFirstEvent");		
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	/*@Test
	public void eventWithEventRefCallingAnotherEvent(){
		String firstEventId = "eventWithEventRefCallingAnotherEvent";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 3, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("myComponentId1");
		expectedReferences.add("ref1InFirstEvent");		
		expectedReferences.add("myComponentId1");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}*/
	
	@Test
	public void eventWithMultipleIndirectCallToSameEvent(){
		String firstEventId = "eventWithMultipleIndirectCallToSameEvent";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 4, event.getInput().size());
		List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("ref1InFirstEvent");
		expectedReferences.add("ref1InSecondEvent");
		expectedReferences.add("ref1InThirdEvent");
		expectedReferences.add("ref1InCommonEvent");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));
	}
	
	@Test
	public void eventReplicatingJiraIssue(){
		String firstEventId = "eventReplicatingJiraIssue";
		Event event = window.getEventsMap().get(firstEventId);
		assertEquals("Input count mismatch", 21, event.getInput().size());
		/*List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("ref1InFirstEvent");
		expectedReferences.add("ref1InSecondEvent");
		expectedReferences.add("ref1InThirdEvent");
		expectedReferences.add("ref1InCommonEvent");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));*/
	}
	
	@Test
	public void eventWithRecursiveCall(){
		String firstEventId = "eventWithRecursiveCall";
		Event event = window.getEventsMap().get(firstEventId);
		assertNull(event.getInput());
		/*List<String> expectedReferences = new ArrayList<String>();
		expectedReferences.add("ref1InFirstEvent");
		expectedReferences.add("ref1InSecondEvent");
		expectedReferences.add("ref1InThirdEvent");
		expectedReferences.add("ref1InCommonEvent");
		assertTrue("One or more expected component references not found", checkNameReference(event.getInput(), expectedReferences));*/
	}
	
	
	private boolean checkNameReference(List<InputVariable> inputs, List<String> expectedReferences) {
		boolean allReferencesFound = true;
		for(String expectedReference: expectedReferences) {
			boolean found = false;
			for(InputVariable inputVariable : inputs) {
				if (expectedReference.equals(inputVariable.getReference())){
					found = true;
					break;
				}
			}
			if(!found) {
				allReferencesFound = false;
				break;
			}
		}				
		return allReferencesFound;
	}
	
	

}
