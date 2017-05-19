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
package com.qualogy.qafe.core.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.ReferencingItem;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.commons.type.ParameterFactory;
import com.qualogy.qafe.core.application.MappingError;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

@Deprecated
public class IterationExecuter extends ControlStatementExecuter {

	public final static Logger logger = Logger.getLogger(IterationExecuter.class.getName());
	
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, ControlStatement statement, String localStoreId) throws MappingError {
		Iteration iteration = (Iteration)statement;

		Object[] iteratable = null;
		if(iteration.getReference()!=null){
			iteratable = getReference(context, storeId, dataId, iteration.getReference());
		}
		
		int begin = iteration.getBegin()!=-1 ? iteration.getBegin() : 0; 
		if(begin < 0) {
			throw new MappingError("Cannot iterate starting at negative number");
		}
		
		int end = iteration.getEnd()!=-1 ? iteration.getEnd() : (iteratable!=null ? iteratable.length-1 : 0);
		if(iteratable != null && end > iteratable.length-1) {
			throw new MappingError("only " + iteratable.length + " items in collection with reference " + iteration.getReference() + " and end is set to " + end + " (starting at 0, this iteration will fail)");
		} 
		
		int increment = iteration.getIncrement()!=-1 ? iteration.getIncrement() : 1;
		if(increment < 1) {
			throw new MappingError("Cannot iterate increment by a number smaller than 1");
		}
		
		logger.fine("begining iteration with begin["+begin+"] end["+end+"] increment["+increment+"] iteratable["+iteratable+"]");
		
		List<ResultItem> results = new ArrayList<ResultItem>();
		for (int i = begin; i <= end; i += increment) {
			if(iteratable!=null && iteration.getVar()!=null){
				Iterator<ResultItem> resultIterator = iteration.resultItemsIterator();
				List<ResultItem> resultItems = getResultItems(resultIterator, iteration.getVarIndex(), i, iteration.getReference());
				if(iteration.getItemCount() < 1){
					iteration.setItemCount(resultItems.size());
				}
				results.addAll(resultItems);
			}
		}
		
		logger.fine("following items ready for result ["+results+"]");
		
		return results;
	}
	
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement controlStatement){
		return execute(context, storeId, id, controlStatement, null);
	}

	/**
	 * if tmpVarName is in a referencingitem it will be replaced with the reference
	 * to the actual-object[index]
	 * @param tmpVarName
	 * @param o
	 * @return
	 */
	
	private List<ResultItem> getResultItems(Iterator<ResultItem> resultItems, String tmpVarName, int index, Reference originalRef){
		List<ResultItem> results = new ArrayList<ResultItem>();
		if(resultItems!=null){
			while(resultItems.hasNext()){
				ResultItem result = (ResultItem) resultItems.next();
				if(result instanceof ReferencingItem){
					ReferencingItem item = null;
					try {
						item = (ReferencingItem)((ReferencingItem)result).clone();
					} catch (CloneNotSupportedException e) {
						//dus
					}
					item = updateItemRefs(item, tmpVarName, originalRef.getRootRef(), originalRef, index);
					result = (ResultItem)item;
				}
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * update the references set in the resultitems. the reference was a tmpvariable id
	 * and will be replaced with the actual object id. 
	 * 
	 * @param item
	 * @param tmpVarName
	 * @param original
	 * @param index
	 * @return
	 */
	private ReferencingItem updateItemRefs(ReferencingItem item, String tmpVarName, String originalName, Reference originalRef, int index){
		List<Parameter> parameters = item.getParameters();
		for (Iterator<Parameter> iter = parameters.iterator(); iter.hasNext();) {
			Parameter oldParam = (Parameter) iter.next();
			if(oldParam == null || oldParam.getRef() == null || oldParam.getRef().stringValueOf() == null) {
				continue;
			}
			if(oldParam.getRef().stringValueOf().contains("[${" + tmpVarName + "}]")){//in params
				Parameter newParam = ParameterFactory.create(oldParam);
				newParam.getRef().setRef(createNewRef(originalRef, oldParam, tmpVarName, index));
				item.replace(oldParam, newParam);
			}
			
			/*if(tmpVarName.equals(oldParam.getRef().getRootRef())){//in params
				Parameter newParam = ParameterFactory.create(oldParam);
				newParam.getRef().setRef(createNewRef(originalRef, oldParam, tmpVarName, index));
				item.replace(oldParam, newParam);
			}else if(tmpVarName.equals(oldParam.getNameRoot())){//out params
				Parameter newParam = new Parameter(oldParam);
				newParam.setName(createNewName(originalName, oldParam, tmpVarName, index));
				item.replace(oldParam, newParam);
			}*/
		}
		return item;
	}
	/**
	 *
	 * @param original
	 * @param oldParam
	 * @param tmpVarName
	 * @param index
	 * @return
	 */
	private String createNewRef(Reference original, Parameter oldParam, String tmpVarName, int index){
		String oldParamRef = oldParam.getRef().toString();
		//String newParamRef = StringUtils.replace(oldParamRef, tmpVarName, original.getRootRef() + "[" + index + "]");
		String newParamRef = StringUtils.replace(oldParamRef, "[${" + tmpVarName + "}]", "[" + index + "]");
		return newParamRef;
	}
	
	private String createNewName(String originalName, Parameter oldParam, String tmpVarName, int index){
		String oldParamName = oldParam.getName();
		String newParamName = StringUtils.replace(oldParamName, tmpVarName, originalName + "[" + index + "]");		
		return newParamName;
	}
	
	private Object[] getReference(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Reference reference){
		Object[] result = null;
		
		Object objList = ParameterValueHandler.get(context, storeId, dataId, reference);
		if(objList instanceof Object[])
			result = (Object[])objList;
		else if(objList instanceof Collection)
			result = ((Collection<?>)objList).toArray();
		else
			throw new IllegalArgumentException("reference ["+ reference.toLogString() +"] does not point to a list type and for that reason cannot be used as argument in iteration");
		
		return result;
	}
}
