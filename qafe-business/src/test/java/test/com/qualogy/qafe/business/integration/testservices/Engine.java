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
// Source File Name:   Engine.java

package test.com.qualogy.qafe.business.integration.testservices;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Engine
{

    public Engine()
    {
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    public static Engine create(String part1, String part2)
    {
        Engine engine = new Engine();
        engine.part1 = part1;
        engine.part2 = part2;
        return engine;
    }

    protected String part1;
    protected String part2;
}
