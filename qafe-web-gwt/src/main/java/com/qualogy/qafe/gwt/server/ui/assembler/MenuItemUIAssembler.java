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
package com.qualogy.qafe.gwt.server.ui.assembler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.MenuItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class MenuItemUIAssembler implements UIAssembler {

	public MenuItemUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof MenuItem) {
				MenuItem menuItem = (MenuItem)object;
				MenuItemGVO voTemp  = new MenuItemGVO();
				voTemp.setId(menuItem.getId());
				UIAssemblerHelper.copyFields(menuItem, currentWindow,voTemp,applicationMapping, context, ss);				
				
				voTemp.setShortcut(menuItem.getShortcut());
				// shortcut
				if (voTemp.getShortcut()!=null && voTemp.getShortcut().length()>0){
					String[] keys = StringUtils.split(voTemp.getShortcut(),'+');
					
					List<String> modifiers = Arrays.asList(keys);
					String key   = findKey(keys);
					
					voTemp.setModifiers((String[]) modifiers.toArray(new String[]{}));
					voTemp.setKey(key);
					
				}
				
				
				if (menuItem.getSubMenus()!=null){
					MenuItemGVO[] subMenus = new MenuItemGVO[menuItem.getSubMenus().size()];
					int index=0;
					for (MenuItem mi  : menuItem.getSubMenus()) {									
						subMenus[index] = (MenuItemGVO)ComponentUIAssembler.convert(mi, currentWindow,applicationMapping,context, ss);
						index++;
					}
					voTemp.setSubMenus(subMenus);
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	private String findKey(String[] keys) {
		if (keys!=null){
			return keys[keys.length-1]; // the last element is most likely the key !!
			
		} else{
			return null;
		}
	}

	

	public String getStaticStyleName() {
		return "menuitem";
	}
}
