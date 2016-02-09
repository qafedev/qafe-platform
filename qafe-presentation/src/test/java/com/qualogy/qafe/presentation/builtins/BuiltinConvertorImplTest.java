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
package com.qualogy.qafe.presentation.builtins;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;

public class BuiltinConvertorImplTest {

	@Test
	public void convert() {		
		String gsonData = "set-property={\"property\":\"visible\",\"parameter\":{\"value\":{\"staticValue\":\"false\"},\"nullable\":false},\"dataObject\":\"false\",\"components\":[{\"componentId\":\"item1\"}]}";
		BuiltinConvertor builtInConverter = new BuiltinConvertorImpl();
		List<BuiltInFunction> builtIn = builtInConverter.convert(gsonData);
		assertNotNull(builtIn);
	}
	
	@Test
	public void test() {
		System.out.println("test");
		Gson gson = new Gson();
		SetProperty set = new SetProperty();
		set.setProperty("visible");
		List<Component> components = new ArrayList<Component>();
		Component component = new Component();
		component.setComponentId("item1");
		components.add(component);
		set.setComponents(components);
		Parameter parameter = new Parameter();
		Value value = new Value("false");
		parameter.setValue(value);
		set.setParameter(parameter);
		set.setDataObject("false");

		String gsonData = gson.toJson(set);
		System.out.println("visinle false" + gsonData);
	}
	
	@Test
	public void testA() {
		System.out.println("test2");
		Gson gson = new Gson();
		SetProperty set = new SetProperty();
		set.setProperty("visible");
		List<Component> components = new ArrayList<Component>();
		Component component = new Component();
		component.setComponentName("CN_APPL_DETLT");
		components.add(component);
		set.setComponents(components);
		Parameter parameter = new Parameter();
		Value value = new Value("false");
		parameter.setValue(value);
		set.setParameter(parameter);
		set.setDataObject("false");

		String gsonData = gson.toJson(set);
		System.out.println("visible CN_APPL_DETLT" + gsonData);
	}
	
	@Test
	public void testFromGsonString() {
		String gsonData = "{\"property\":\"visible\",\"parameter\":{\"value\":{\"staticValue\":\"false\"},\"nullable\":false},\"dataObject\":\"false\",\"components\":[{\"componentId\":\"item1\"}]}";
		Gson gson = new Gson();
		SetProperty set = gson.fromJson(gsonData, SetProperty.class);
		System.out.println(set.toString());
	}

}
