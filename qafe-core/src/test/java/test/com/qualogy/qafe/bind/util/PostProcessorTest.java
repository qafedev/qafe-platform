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
package test.com.qualogy.qafe.bind.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.domain.PresentationTier;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.bind.util.PostProcessor;

public class PostProcessorTest extends TestCase {
	public void testPostProcessorEventHappyDay(){
		List<EventItem> items = new ArrayList<EventItem>();
		for (int i = 0; i < 10; i++) {
			SetProperty setProperty = new SetProperty();
			setProperty.setProperty("enabled");
			Value v = new Value();
			v.setStaticValue("false");
			Parameter p = new Parameter();			
			p.setValue(v);
			setProperty.setParameter(p);
			items.add(setProperty);
		}
		Event event = new Event( "id", items);
		assertEquals(event.getEventItems().size(),10);
			
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		PresentationTier presentationTier = PresentationTier.create(events, null, null);
		
		ApplicationMapping gf = ApplicationMapping.create(presentationTier, null, null, null);
		PostProcessor.process(gf);
		event = (Event)gf.getPresentationTier().getEvents().get(0);
	}
}
