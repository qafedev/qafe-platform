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
package com.qualogy.qafe.gwt.client.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SerializableWhitelist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Byte b;
	private Short s;
	private Integer i;
	private Long l;
	private Float f;
	private Double d;
	private Character c;
	private BigDecimal bd;
	private BigInteger bi;
	private Date date;
	private Boolean bool;
}