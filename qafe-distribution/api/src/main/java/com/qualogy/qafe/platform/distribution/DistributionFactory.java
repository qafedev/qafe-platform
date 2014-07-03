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
package com.qualogy.qafe.platform.distribution;

import com.qualogy.qafe.platform.distribution.api.DistributionManager;

public class DistributionFactory {
	
	private static final DistributionFactory instance = new DistributionFactory();
	
	private static final String DistributionManagerImpl = "com.qualogy.qafe.platform.distribution.impl.DistributionManager";
	private DistributionManager distributionManager;
	
	private DistributionFactory() {
		if (instance != null) {
			throw new IllegalStateException();
		}
		distributionManager = createDistributionManager();
	}
	
	public static DistributionFactory getInstance() {
		return instance;
	}
	
	public DistributionManager getDistributionManager() {
		return distributionManager;
	}
	
	private static DistributionManager createDistributionManager() {
		DistributionManager distributionManager = null;
		try {
			distributionManager = (DistributionManager) Class.forName(DistributionManagerImpl).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distributionManager;
	}
}
