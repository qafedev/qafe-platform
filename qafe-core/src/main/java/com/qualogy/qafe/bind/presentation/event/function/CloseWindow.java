/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.presentation.event.function;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.commons.type.Parameter;



public class CloseWindow extends OpenWindow implements PostProcessing{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1684904663738266821L;

	public static String serializeWindow(Parameter w) {
	    StringBuffer buff = new StringBuffer();
	    if (w!=null){
	    	if (w.getRef()!=null ){
	    		buff.append(w.getRef().getRootRef());
	    	}
	    }
	    return buff.toString();
	}

	
}
