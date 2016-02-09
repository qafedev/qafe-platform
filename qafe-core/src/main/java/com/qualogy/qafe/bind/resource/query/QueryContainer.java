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
package com.qualogy.qafe.bind.resource.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.util.TrackSource;

/**
 * Temporary container for holding {@link Query} objects while binding
 * a statements xml file.
 *  
 * @author 
 *
 */
public class QueryContainer implements Serializable, PostProcessing {
	
	private static final long serialVersionUID = 8739641221398141843L;
	
	//root element in xml file
	public final static String ROOT_ELEMENT_NAME = "statements";
	
	private Map<String,Set<String>> duplicationOfQueries = new HashMap<String,Set<String>>();
	
	/**
	 * tmp holder for binding purposes
	 */
	protected List<Query> statements;
	/**
	 * actual holder objects for queries
	 * key => query id
	 * value => query
	 */
	private Map<String, Query> container = new HashMap<String, Query>();
	/**
	 * public constructor with no impl
	 */
	public QueryContainer() {
		super();
		statements = new ArrayList<Query>();
	}

	/**
	 * This method should be used to update an existing query in this container.
	 * @param query
	 */
	public void update(Query query) {
		String queryId = query.getId();
		if (container.containsKey(queryId)) {
			container.put(query.getId(), query);	
		}
		int queryIndex = -1;
		for (int i=0; i<statements.size(); i++) {
			Query existingQuery = statements.get(i);
			if (queryId.equals(existingQuery.getId())) {
				queryIndex = i;
				break;
			}
		}
		if (queryIndex > -1) {
			statements.set(queryIndex, query);
		}
	}
	
	/**
	 * This method should be used to put a query to this container.
	 * @param query
	 */
	public void put(Query query){
		addWhenDuplicated(query);
		container.put(query.getId(), query);
		statements.add(query);//for writting reasons
	}

    /**
	 * This method should be used to put a list of query to this container.
	 * @param queries
	 */
	public void putAll(List<Query> queries){
        for(Query query : queries){
            put(query);
        }
	}
	
//	private Map filter(Class filter){
//		Map filtered = new HashMap();
//		for (Iterator iter = this.container.values().iterator(); iter.hasNext();) {
//			Query query = (Query) iter.next();
//			if(filter.isInstance(query))
//				filtered.put(query.getId(), query);
//		}
//		return filtered;
//	}
	
	/**
	 * collects the queries in a map 
 	 */
	public void performPostProcessing() {
		Set<String> duplicateQueries = new TreeSet<String>();
		for (Iterator<Query> iter = statements.iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();
			String queryId = query.getId();
			if (container.containsKey(queryId)) {
				duplicateQueries.add(queryId);
			}
			container.put(queryId, query);
		}
		if (duplicateQueries.size() > 0) {
			String message = "Query duplication detected: " + duplicateQueries + ", id of an query should be unqiue within the statements file.";
			throw new ValidationException(message);
		}
	}

	public Collection<Query> values(){
		return container.values();
	}
	
	public Set<String> keySet(){
		return container.keySet();
	}
	
	public void postset(IUnmarshallingContext arg0) {
		performPostProcessing();
	}

	public Query get(String queryName) {
		return (Query)container.get(queryName);
	}

	public List<String> getDuplicateQueries() {
		if (duplicationOfQueries.isEmpty()) {
			return null;
		}
		String[] duplicateQueries = duplicationOfQueries.keySet().toArray(new String[]{});
		return Arrays.asList(duplicateQueries);
	}
	
	private void addWhenDuplicated(Query query) {
		String queryId = query.getId();
		if (container.containsKey(queryId)) {
			Set<String> duplicateQueries = duplicationOfQueries.get(queryId);
			if (duplicateQueries == null) {
				duplicateQueries = new TreeSet<String>();
				duplicationOfQueries.put(queryId, duplicateQueries);
				Query existingQuery = container.get(queryId);
				String filename = TrackSource.getSimpleDocumentName(existingQuery);
				if (filename != null) {
					duplicateQueries.add(filename);	
				}
			}
			String filename = TrackSource.getSimpleDocumentName(query);
			if (filename != null) {
				duplicateQueries.add(filename);	
			}
		}		
	}
}