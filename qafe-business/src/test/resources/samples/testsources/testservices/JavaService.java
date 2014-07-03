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
package samples.testsources.testservices;

import samples.testsources.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaService {
	
	public class Out{
		public String actualserviceout = "actualserviceout";
	}
	
	public void getCar(){
		//
	}
	public void getCar(String car){
		//
	}
	
	public MyPerson validate(MyPerson person){
		return person;
	}
	
	public Map validate(Map person){
		return person;
	}
	
	public String printCar(Car car){
		if(car==null)
			throw new IllegalArgumentException("cannot printCar since car is null");
		return "jaja";
	}
	public void getCar(Car car, String jaja){
		//
	}
	
	public void throwIllegalArgument(){
		throw new IllegalArgumentException("method JvaService.throwIllegalArgument() called to throw illegalargument");
	}
	public void doNothing(){
		//method does nothing
	}
	
	public void printHello(){
		System.out.println("hello");
	}
	
	public boolean testNullParam(String param){
		return param==null;
	}
	public boolean testMethodWithPrimitive(int param){
		return param==1;
	}
	public boolean testMethodWithPrimitive(boolean param){
		return param;
	}
	
//	public boolean testMethodWithPrimitive(Integer param){
//		return param!=null && param.intValue()==1;
//	}
	public Out testParameter(String param){
		return new Out();
	}
	public String returnIn(String param){
		return param;
	}
	public Object returnInput(Object input){
		return input;
	}
	
	public List getListOf10Maps(){
		List list = new ArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(getMap());
		}
		return list;
	}
	/**
	 * @deprecated use getListOfStrings supplyng the amount
	 * @return
	 */
	public List getListOf10Strings(){
		List list = new ArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(getString()+i);
		}
		return list;
	}
	public Map getMap(){
		Map map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		
		Map internalmap = new HashMap();
		internalmap.put("keykey1.key1", "keykey1.value1value1");
		map.put("keykey1", internalmap);
		
		return map;
	}
	public String getString(){
		return "astring";
	}
	public String getNull(){
		return null;
	}
	
	
	private List getListOfStrings(int amount){
		List list = new ArrayList();
		for (int i = 0; i < amount; i++) {
			list.add("" + i + " > " + getString());
		}
		return list;
	}
	public List getListOfStrings(Integer amount){
		return getListOfStrings(amount.intValue());
	}
	public String[] getArrayOfStrings(Integer amount){
		int amnt = amount.intValue();
		return (String[])getListOfStrings(amnt).toArray(new String[amnt]);
	}
}
