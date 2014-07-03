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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.type.TypeDefinition;
import com.qualogy.qafe.bind.domain.BusinessTier;

public class ValidatorTest extends TestCase {

	public void testValidator(){
		
	}
	public void testBusinessTierValidation() throws Exception{
		BusinessTier tier = new BusinessTier();
		List<TypeDefinition> typedefs = new ArrayList<TypeDefinition>();
		typedefs.add(new TypeDefinition("STRING"));
		typedefs.add(new TypeDefinition("String"));
		typedefs.add(new TypeDefinition("STriNG"));
		typedefs.add(new TypeDefinition("string"));
		for (Iterator iter = typedefs.iterator(); iter.hasNext();) {
			tier.add((TypeDefinition) iter.next());
			
		}
		assertEquals(tier.getTypeDefinitions().size(), 1);
		tier.validate();
		
		//jibx kind of working
		Field field = BusinessTier.class.getDeclaredField("typeDefinitions");
		field.setAccessible(true);
		field.set(tier, typedefs);
		assertEquals(tier.getTypeDefinitions().size(), 4);
		
		try{
			tier.validate();
			fail("expecting to fail since we added under the same type id");
		}catch(ValidationException e){
			//expected result
		}
	}
}
