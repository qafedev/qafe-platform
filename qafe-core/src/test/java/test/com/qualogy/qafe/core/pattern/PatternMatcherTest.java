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
package test.com.qualogy.qafe.core.pattern;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.core.pattern.ApplicationIdPattern;
import com.qualogy.qafe.core.id.Identifiable;
import com.qualogy.qafe.core.pattern.PatternMatcher;

public class PatternMatcherTest extends TestCase {
	
	public void testHappyDayExactMatch(){
		String toMatch = "";
		String pattern = "";
		assertTrue(filter(toMatch, pattern));
		toMatch = "*";
		pattern = "*";
		assertTrue(filter(toMatch, pattern));
		toMatch = "*X";
		pattern = "*X";
		assertTrue(filter(toMatch, pattern));
		toMatch = "X*X";
		pattern = "X*X";
		assertTrue(filter(toMatch, pattern));
		toMatch = "X*";
		pattern = "X*";
		assertTrue(filter(toMatch, pattern));
	}

	public void testHappyDayMatch(){
		String toMatch = "jaja";
		
		String pattern = "j*";
		assertTrue(filter(toMatch, pattern));
		pattern = "*a";
		assertTrue(filter(toMatch, pattern));
	}
	
	public void testNoMatch(){
		String toMatch = "+";
		String pattern = "-";
		assertFalse(filter(toMatch, pattern));
		toMatch = "a*";
		pattern = "*a";
		assertFalse(filter(toMatch, pattern));
	}
	
	private boolean filter(final String toMatch, final String pattern) {
		List<Identifiable> ids = new ArrayList<Identifiable>();
		ids.add(new Identifiable(){
			public String getStringValueOfId() {
				return toMatch;
			}
		});
	
		ids = PatternMatcher.filter(ids, new ApplicationIdPattern(pattern));
		return ids!=null && ids.size()==1;
	}
}
