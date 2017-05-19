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
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Car.java

package test.com.qualogy.qafe.business.integration.testservices;

import org.apache.commons.lang.builder.ToStringBuilder;

// Referenced classes of package test.com.qualogy.qafe.business.integration.testservices:
//            Vehicle, Engine

public class Car extends Vehicle
{

    public Car()
    {
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    public static Car create(String name, String model, String enginePart1, String enginePart2)
    {
        Car car = new Car();
        car.model = model;
        Car _tmp = car;
        name = name;
        car.engine = Engine.create(enginePart1, enginePart2);
        return car;
    }

    protected String model;
    private static String name;
    public Engine engine;
}
