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
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JavaService.java

package test.com.qualogy.qafe.business.integration.testservices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Referenced classes of package test.com.qualogy.qafe.business.integration.testservices:
//            MyPerson, Car

public class JavaService
{
    public class Out
    {

        public String actualserviceout;
     
        public Out()
        {
        	super();
            
            actualserviceout = "actualserviceout";
        }
    }


    public JavaService()
    {
    }

    public void getCar()
    {
    }

    public void getCar(String s)
    {
    }

    public MyPerson validate(MyPerson person)
    {
        return person;
    }

    public Map validate(Map person)
    {
        return person;
    }

    public String printCar(Car car)
    {
        if(car == null)
            throw new IllegalArgumentException("cannot printCar since car is null");
        else
            return "jaja";
    }

    public void getCar(Car car1, String s)
    {
    }

    public void throwIllegalArgument()
    {
        throw new IllegalArgumentException("method JvaService.throwIllegalArgument() called to throw illegalargument");
    }

    public void doNothing()
    {
    }

    public void printHello()
    {
        System.out.println("hello");
    }

    public boolean testNullParam(String param)
    {
        return param == null;
    }

    public boolean testMethodWithPrimitive(int param)
    {
        return param == 1;
    }

    public boolean testMethodWithPrimitive(boolean param)
    {
        return param;
    }

    public Out testParameter(String param)
    {
        return new Out();
    }

    public String returnIn(String param)
    {
        return param;
    }

    public Object returnInput(Object input)
    {
        return input;
    }

    public List getListOf10Maps()
    {
        List list = new ArrayList();
        for(int i = 0; i < 10; i++)
            list.add(getMap());

        return list;
    }

    /**
     * @deprecated Method getListOf10Strings is deprecated
     */

    public List getListOf10Strings()
    {
        List list = new ArrayList();
        for(int i = 0; i < 10; i++)
            list.add((new StringBuilder(String.valueOf(getString()))).append(i).toString());

        return list;
    }

    public Map getMap()
    {
        Map map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Map internalmap = new HashMap();
        internalmap.put("keykey1.key1", "keykey1.value1value1");
        map.put("keykey1", internalmap);
        return map;
    }

    public String getString()
    {
        return "astring";
    }

    public String getNull()
    {
        return null;
    }

    private List getListOfStrings(int amount)
    {
        List list = new ArrayList();
        for(int i = 0; i < amount; i++)
            list.add((new StringBuilder()).append(i).append(" > ").append(getString()).toString());

        return list;
    }

    public List getListOfStrings(Integer amount)
    {
        return getListOfStrings(amount.intValue());
    }

    public String[] getArrayOfStrings(Integer amount)
    {
        int amnt = amount.intValue();
        return (String[])getListOfStrings(amnt).toArray(new String[amnt]);
    }
    
    public List<Employee> getEmployees(){
		List<Employee> data = new ArrayList<Employee>();
		
		for (int i=0; i<21; i++) {
			String name= "Name" + i;
			String address= "Address" + i;
			Employee p = new Employee();
			p.setName(name);
			p.setAge(i);
			p.setAddress(address);
			p.setBirthday(new Date(2010, 8, i+1));
			p.setSalary((i*10.5f));
			p.setMale(i/2==0);
			data.add(p);	
		}
		return data;
	}
}
