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
package com.qualogy.qafe.core.errorhandling;

import java.util.List;

import com.qualogy.qafe.bind.item.Item;

public class ErrorResult {
	
	private  List<Item>  items;
	private ExternalException externalException;
	
	public void setExternalException(ExternalException externalException) {
		this.externalException = externalException;
	}

	public ExternalException getExternalException() {
		return externalException;
	}

	public  List<Item>  getItems() {
		return items;
	}

	public boolean hasItems() {
		return items!=null && !items.isEmpty();
	}

	public void setItems( List<Item>  items) {
		this.items = items;
	}
	
	
}
