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
package com.qualogy.qafe.core.id;

import java.util.Calendar;
import java.util.Random;

import org.doomdark.uuid.UUIDGenerator;
/**
 * reasonabaly unique identifier (not guarenteed), check the <code>java.util.Random</code> specification
 * to determine the uniqueness of this identiefier 
 * @author Marc van der Wurff
 *
 */
public class UniqueIdentifier implements Comparable{
	private static Random random = new Random(Calendar.getInstance().getTimeInMillis());
	
	public final static byte GENERATOR_JAVAUTIL_RANDOM = '0';  
	public final static byte GENERATOR_DOOMDARK_GENERATOR = '1';
	
	public final static byte DEFAULT_GENERATOR = GENERATOR_JAVAUTIL_RANDOM;
	
	private String uuid;
	
	protected UniqueIdentifier(){
		super();
	}
	
	protected UniqueIdentifier(String uuid){
		this.uuid = uuid;
	}
	/**
	 * datastore is in charge of generation
	 * @return
	 */
	public static UniqueIdentifier nextSeed(){
		return nextSeed(DEFAULT_GENERATOR);
	}
	public static UniqueIdentifier nextSeed(byte generator){
		String id = null;
		switch (generator) {
			case GENERATOR_DOOMDARK_GENERATOR:
				id = UUIDGenerator.getInstance().generateRandomBasedUUID().toString();
				break;
			case GENERATOR_JAVAUTIL_RANDOM:
				id = String.valueOf(random.nextLong());
				break;
			default:
				throw new UnsupportedGeneratorException(generator + " is an unsupported generator type");
		}
		return new UniqueIdentifier(id);
	}
	public String toString(){
		return uuid;
	}
	/**
	 * method compares the internal uuid (String) to given UniqueIdentifier objects internal string
	 */
	public int compareTo(Object o) {
		return  (uuid!=null) ? uuid.compareTo(o.toString()) : 0 ;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UniqueIdentifier other = (UniqueIdentifier) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid) || (other.uuid!=null && !uuid.toString().equals(other.uuid.toString())))
			return false;
		return true;
	}
}
