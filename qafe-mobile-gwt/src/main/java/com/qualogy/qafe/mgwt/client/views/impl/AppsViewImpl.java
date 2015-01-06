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
package com.qualogy.qafe.mgwt.client.views.impl;

import java.util.List;

import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.HasCellSelectedHandler;
import com.qualogy.qafe.mgwt.client.BasicCell;
import com.qualogy.qafe.mgwt.client.activities.home.Topic;
import com.qualogy.qafe.mgwt.client.views.AppsView;

public class AppsViewImpl extends AbstractViewImpl implements AppsView {

	private CellList<Topic> cellList;

	public AppsViewImpl() {
		cellList = new CellList<Topic>(new BasicCell<Topic>() {
			@Override
			public String getDisplayString(Topic model) {
				return model.getName();
			}

			@Override
			public boolean canBeSelected(Topic model) {
				return true;
			}
		});
		cellList.setRound(true);
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidget(cellList);
		scrollPanel.setScrollingEnabledX(false);
		mainPanel.add(scrollPanel);
	}
	
	@Override
	public HasCellSelectedHandler getCellSelectedHandler() {
		return cellList;
	}

	@Override
	public void setTopics(List<Topic> models) {
		cellList.render(models);
	}
}
