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
package com.qualogy.qafe.business.integration.filter.sort;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qualogy.qafe.business.integration.filter.sort.comparators.BigDecimalComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.BigIntegerComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.BooleanComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.ByteComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.DateComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.DoubleComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.FloatComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.IntegerComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.LongComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.ShortComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.SortComparator;
import com.qualogy.qafe.business.integration.filter.sort.comparators.StringComparator;

public abstract class ComparatorFactory {
	
	private static final Map<Class<?>, Class<?>> COMPARATOR_MAP = new HashMap<Class<?>, Class<?>>();
	static {
		COMPARATOR_MAP.put(String.class, StringComparator.class);
		COMPARATOR_MAP.put(Boolean.class, BooleanComparator.class);
		COMPARATOR_MAP.put(Date.class, DateComparator.class);
		COMPARATOR_MAP.put(Byte.class, ByteComparator.class);
		COMPARATOR_MAP.put(Short.class, ShortComparator.class);
		COMPARATOR_MAP.put(Integer.class, IntegerComparator.class);
		COMPARATOR_MAP.put(Long.class, LongComparator.class);
		COMPARATOR_MAP.put(Float.class, FloatComparator.class);
		COMPARATOR_MAP.put(Double.class, DoubleComparator.class);
		COMPARATOR_MAP.put(BigInteger.class, BigIntegerComparator.class);
		COMPARATOR_MAP.put(BigDecimal.class, BigDecimalComparator.class);
	}	
	
    public static Comparator createComparator(String fieldName, Boolean sortAscending, Object value) {
    	SortComparator comparator = null;
    	Class<?> clazz = COMPARATOR_MAP.get(value.getClass());
		if (clazz != null){
			try {
				comparator = (SortComparator)clazz.newInstance();
				if (comparator != null) {
					comparator.setFieldName(fieldName);
					comparator.setSortAscending(sortAscending);	
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//    	if (value instanceof Number) {
//    		// Byte; Short; Integer; Long; AtomicInteger; AtomicLong; Float; Double; BigInteger; BigDecimal.
//    		if (value instanceof Byte) {
//    			comparator = new ByteComparator(fieldName, sortAscending);
//    		} else if (value instanceof Short) {
//    			
//    		} else if (value instanceof Integer) {
//    			comparator = new IntegerComparator(fieldName, sortAscending);
//    		} else {
//    			
//    		}
//    	} else if (value instanceof String) {
//    		comparator = new StringComparator(fieldName, sortAscending);
//    	} else if (value instanceof Date) {
//    		comparator = new DateComparator(fieldName, sortAscending);
//    	}
    	return comparator;
    }
}
