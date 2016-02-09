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
package com.qualogy.qafe.mgwt.server.ui.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.web.ContextLoaderHelper;
import com.qualogy.qafe.web.util.SessionContainer;


public class WindowUIAssembler implements UIAssembler{



	public ComponentGVO convert(Component component, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		WindowGVO vo = null;
		if (component!=null && component instanceof Window){
			Window window= (Window) component;
			vo = new WindowGVO();
//			vo.setStyleClass("qafe_rootpanel");
			if(context != null && context.getId() != null) {
				vo.setContext(context.getId().toString());
			}
			vo.setId(window.getId());
			vo.setTitle(window.getDisplayname());
		//	vo.setResizable(window.getResizable());
			vo.setRootPanel((RootPanelGVO)(ComponentUIAssembler.convert( window.getRootPanel(),currentWindow,applicationMapping,context, ss )));
			vo.getRootPanel().setParent(window.getId());
			vo.getRootPanel().updateToolbar();
			vo.setClosable(window.getClosable());
			vo.setDraggable(window.getDraggable());
			vo.setMaximizable(window.getMaximizable());
			vo.setMinimizable(window.getMinimizable());
			vo.setResizable(window.getResizable());
			vo.setIsparent(window.getIsparent());
			vo.getRootPanel().updateMenu();
			vo.setWidth(window.getWidth());
			vo.setHeight(window.getHeight());
			vo.setLeft(window.getLeft());
			vo.setTop(window.getTop());
			vo.setIcon(window.getIcon());
			vo.setLoadOnStartup(ContextLoaderHelper.loadOnStartup(window.getId(),context,ss));
			vo.setInDock(window.getInDock());
			if(window.getStyle() != null){
				vo.setStyleClass(window.getStyle());
			}
		}
		
		return vo;
	}

	public String getStaticStyleName() {
		return "window";
	}
}
