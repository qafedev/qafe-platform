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
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;

public class QSuggestOracle extends SuggestOracle {
	private List<QMultiWordSuggestion> suggestions = new ArrayList<QMultiWordSuggestion>();

	@Override
	public void requestSuggestions(Request request, Callback callback) {

		Response resp = new Response(suggestions);
		callback.onSuggestionsReady(request, resp);

	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(QMultiWordSuggestion o) {

		return suggestions.add(o);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(QMultiWordSuggestion o) {

		return suggestions.remove(o);
	}
	
	public void clear(){
		suggestions.clear();
	}

}
