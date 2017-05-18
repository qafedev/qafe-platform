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
package com.qualogy.qafe.bind.core.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.io.FileLocation;

public class Messages implements PostProcessing, Serializable{

	private static final long serialVersionUID = 5761422854041490379L;

	protected List<Bundle> bindBundles;
	protected List<FileLocation> bundleFileLocations;
	
	private Map<String, Bundle> bundles = new HashMap<String, Bundle>();

	protected List<Bundle> getBindBundles() {
		return bindBundles;
	}

	public List<FileLocation> getBundleFileLocations() {
		return bundleFileLocations;
	}
	
	public void put(Bundle bundle){
		if(bundle==null)
			throw new IllegalArgumentException("bundle cannot be null");
		
		bundles.put(Bundle.DEFAULT_BUNDLE_ID, bundle);
	}
	
	public String get(String bundleId, String key, String locale){
		if(StringUtils.isBlank(bundleId)){
			bundleId = Bundle.DEFAULT_BUNDLE_ID;
		}
		Bundle bundle = (Bundle)bundles.get(bundleId);
		String message = (bundle!=null) ? bundle.get(key, locale) : null;
		
		return message;
	}
	
	private void merge(List<Bundle> otherBindBundles){
		if(otherBindBundles!=null){
			for (Bundle bindbundle : otherBindBundles) {
				String id = StringUtils.isEmpty(bindbundle.getId())?Bundle.DEFAULT_BUNDLE_ID:bindbundle.getId();
				
				if(bundles.containsKey(id)){
					Bundle bundle = (Bundle)bundles.get(id);
					bindbundle.putAll(bundle.getMessages());
				}
				bundles.put(id, bindbundle);
			}
		}
	}
	
	/**
	 * merge all individual bundles to the map
	 */
	public void performPostProcessing() {
		merge(bindBundles);
	}
	
	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public void merge(Messages otherImpl) {
		if(otherImpl!=null){
			merge(((Messages)otherImpl).getBindBundles());
		}
	}

	public Map<String, Bundle> getBundles() {
		return bundles;
	}
}
