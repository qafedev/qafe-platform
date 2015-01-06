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
package com.qualogy.qafe.commons.db.procedure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallArguments {
	private List args = null;
	
	public Iterator iterator(){
		return args.iterator();
	}
	
	public CallArguments(List args){
		this.args = args;
	}

	/**
	 * method to check if args list contains
	 * an arg with type result or return
	 * determined by the isResult() method
	 * of each individual arg
	 */
	public boolean containsResultArgument() {
		boolean contains = false;
		for (Iterator iter = iterator(); !contains && iter.hasNext();) {
			contains = ((CallArgument) iter.next()).isResult();
		}
		return contains;
	}

	private List filterOnType(int[] types){
		List args = new ArrayList();
		for (Iterator iter = iterator(); iter.hasNext();) {
			CallArgument callArgument = ((CallArgument) iter.next());
			for (int i = 0; i < types.length; i++) {
				if(callArgument.getArgumentType() == types[i]){
					args.add(callArgument);
					break;
				}
			}
		}
		return args;
	}
	

	public List getInArgs(){
		return filterOnType(new int[]{
				CallArgument.ARGUMENT_TYPE_IN, 
				CallArgument.ARGUMENT_TYPE_INOUT});
	}
	
	public int getInArgsCount(){
		return getInArgs().size();
	}

	public List getAllResultableArgs(){
		return filterOnType(new int[]{
				CallArgument.ARGUMENT_TYPE_RESULT, 
				CallArgument.ARGUMENT_TYPE_RETURN,
				CallArgument.ARGUMENT_TYPE_OUT,
				CallArgument.ARGUMENT_TYPE_INOUT});
	}
	
	public List getResultArgs(){
		return filterOnType(new int[]{
				CallArgument.ARGUMENT_TYPE_RESULT, 
				CallArgument.ARGUMENT_TYPE_RETURN});
	}
	
	public List getParameterArgs(){
		return filterOnType(new int[]{
				CallArgument.ARGUMENT_TYPE_IN,
				CallArgument.ARGUMENT_TYPE_OUT,
				CallArgument.ARGUMENT_TYPE_INOUT});
	}
	/**
	 * get all arguments with type out
	 * @return
	 */
	public List getOutArgs(){
		return filterOnType(new int[]{
				CallArgument.ARGUMENT_TYPE_OUT,
				CallArgument.ARGUMENT_TYPE_INOUT});
	}
	public int size(){
		return args.size();
	}
}
