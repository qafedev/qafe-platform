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
package com.qualogy.qafe.gwt.server.ui.assembler;

import java.util.Iterator;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Tree;
import com.qualogy.qafe.bind.presentation.component.TreeItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TreeGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class TreeUIAssembler implements UIAssembler {

	public TreeUIAssembler() {
	}

	public ComponentGVO convert(Component object,Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof Tree) {
				Tree tree = (Tree)object;
				TreeGVO voTemp  = new TreeGVO();
					UIAssemblerHelper.copyFields(tree, currentWindow,voTemp,applicationMapping, context, ss);		
				
				if (tree.getChildren()!=null){
					TreeItemGVO[] items = new TreeItemGVO[tree.getChildren().size()];
					Iterator<TreeItem> itr = tree.getChildren().iterator();
					int index=0;
					while (itr.hasNext()){
						TreeItem item = (TreeItem) itr.next();
						items[index] = (TreeItemGVO)ComponentUIAssembler.convert(item, currentWindow,applicationMapping,context, ss);
						index++;
					}
					voTemp.setChildren(items);
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "tree";
	}
}
