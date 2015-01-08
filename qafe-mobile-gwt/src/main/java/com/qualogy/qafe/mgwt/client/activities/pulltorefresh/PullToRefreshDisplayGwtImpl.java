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
package com.qualogy.qafe.mgwt.client.activities.pulltorefresh;

import java.util.List;

import com.googlecode.mgwt.ui.client.widget.CellList;
//import com.googlecode.mgwt.ui.client.widget.PullToRefresh;
//import com.googlecode.mgwt.ui.client.widget.event.HasPullHandlers;
import com.qualogy.qafe.mgwt.client.BasicCell;
import com.qualogy.qafe.mgwt.client.DetailViewGwtImpl;
import com.qualogy.qafe.mgwt.client.activities.home.Topic;

public class PullToRefreshDisplayGwtImpl extends DetailViewGwtImpl implements PullToRefreshDisplay {

//	private PullToRefresh pullToRefresh;
	private CellList<Topic> cellList;

	public PullToRefreshDisplayGwtImpl() {
		main.remove(scrollPanel);

//		pullToRefresh = new PullToRefresh();
//
//		main.add(pullToRefresh);

		cellList = new CellList<Topic>(new BasicCell<Topic>() {

			@Override
			public String getDisplayString(Topic model) {
				return model.getName();
			}
		});

//		pullToRefresh.add(cellList);

	}

//	@Override
//	public HasPullHandlers getReload() {
//		return pullToRefresh;
//	}

	@Override
	public void render(List<Topic> topics) {
		cellList.render(topics);

	}

	@Override
	public void onLoadingSucceeded() {
//		pullToRefresh.onLoadingSucceeded();

	}

	@Override
	public void onLoadingFailed() {
//		pullToRefresh.onLoadingFailed();

	}

}
