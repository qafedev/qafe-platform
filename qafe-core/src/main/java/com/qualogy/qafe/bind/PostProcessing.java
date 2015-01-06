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
package com.qualogy.qafe.bind;

import org.jibx.runtime.IUnmarshallingContext;

/**
 * This interface is needed, since postprocessing in our code
 * occurs without the Jibx Classes, so for non jibx processing,
 * these functions must also be called without any jibx related parameters.
 * (for example when manually creating the class)
 * @author rjankie
 *
 */
public interface PostProcessing {
	
	/**
	 * Method must be implemented to do processing of the object after
	 * parameters have been set. This method is used by Jibx if post-set is set to postset.
	 * Good practice is to call the performPostProcessing method of the object, that must be
	 * implemented following the contract of this interface.
	 * @param context
	 */
	void postset(IUnmarshallingContext context);
	
	/**
	 * Method must be implemented to do processing of the object after
	 * parameters have been set. This method will be called by the postset method
	 * configured in Jibx. For postprocessing without Jibx see PostProcessor. 
	 */	
	void performPostProcessing();

}
