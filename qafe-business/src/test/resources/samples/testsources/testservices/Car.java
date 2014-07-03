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

import org.apache.commons.lang.builder.ToStringBuilder;

public class Car extends Vehicle{
	protected String model;
	@SuppressWarnings("unused")
	private static String name;
	public Engine engine;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	@SuppressWarnings("static-access")
	public static Car create(String name, String model, String enginePart1, String enginePart2){
		Car car = new Car();
		car.model = model;
		car.name = name;
		car.engine = Engine.create(enginePart1, enginePart2);
		return car;
	}
}
