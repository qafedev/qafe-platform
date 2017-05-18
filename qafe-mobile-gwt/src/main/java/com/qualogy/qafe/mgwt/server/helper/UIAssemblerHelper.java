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
package com.qualogy.qafe.mgwt.server.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.EditableComponent;
import com.qualogy.qafe.bind.presentation.component.HasRequiredProperty;
import com.qualogy.qafe.bind.presentation.component.HasVisibleText;
import com.qualogy.qafe.bind.presentation.component.Label;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.style.Condition;
import com.qualogy.qafe.bind.presentation.style.ConditionalStyle;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.i18n.MessagesHandler;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ConditionGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ConditionalStyleRefGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.EditableComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequired;
import com.qualogy.qafe.mgwt.client.vo.ui.HasVisibleTextI;
import com.qualogy.qafe.mgwt.client.vo.ui.LabelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.mgwt.server.ui.assembler.ComponentUIAssembler;
import com.qualogy.qafe.mgwt.server.ui.assembler.ConditionUIAssembler;
import com.qualogy.qafe.mgwt.server.ui.assembler.ConditionalStyleUIAssembler;
import com.qualogy.qafe.util.StyleDomUtil;
import com.qualogy.qafe.web.util.SessionContainer;

public class UIAssemblerHelper {

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static void copyFields(Component component, Window currentWindow, ComponentGVO vo,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer sc){
		if ((component != null) && (vo != null)){

			vo.setId(component.getId());
			vo.setFieldName(component.getFieldName());
			vo.setGroup(component.getGroup());
			vo.setTooltip(component.getTooltip());
			vo.setMenu((MenuItemGVO)ComponentUIAssembler.convert(component.getMenu(), currentWindow,applicationMapping,context, sc));
			if (vo.getMenu()!=null){
				manageMenuId(vo.getMenu(), component.getId());
			}
			vo.setDisabled(component.getDisabled());
			vo.setVisible(component.getVisible());
			vo.setWidth(component.getWidth());
			vo.setHeight(component.getHeight());
			if (currentWindow != null) {
				vo.setWindow(currentWindow.getId());
			}
			if (context != null) {
				vo.setContext(context.getId().toString());
			}

			// Style
			vo.setStyleClass(component.getStyleClass());
			String[] properties = StringUtils.split(component.getStyle()==null ? "": component.getStyle(), ';');
			String[][] styleProperties = new String[properties.length][2];
			for (int i=0;i<properties.length;i++){
				styleProperties[i]= StringUtils.split(properties[i],':');
			}

			/*
			 * Modify the properties since this is DOM manipulation : font-size for css has to become fontSize for DOM
			 */
			for (int i=0;i<styleProperties.length;i++){
				styleProperties[i][0] =StyleDomUtil.initCapitalize(styleProperties[i][0]);
			}

			vo.setStyleProperties(styleProperties);

			if (component instanceof EditableComponent){
				if (vo instanceof EditableComponentGVO){
					EditableComponent editableComponent=  (EditableComponent)component;
					EditableComponentGVO editableComponentGVO = (EditableComponentGVO)vo;
					editableComponentGVO.setEditable(editableComponent.getEditable());

					ConditionalStyle conditionalStyle = editableComponent.getConditionalStyleRef();

					if(conditionalStyle != null) {
						editableComponentGVO.setConditionalStyleRef(ConditionalStyleUIAssembler.convert(conditionalStyle));
					}

				}
			}

			if (component instanceof HasVisibleText && vo instanceof HasVisibleTextI){
				HasVisibleText hasVisibleText = (HasVisibleText)component;
				HasVisibleTextI hasVisibleTextI = (HasVisibleTextI)vo;
				if (hasVisibleText.getMessageKey()!=null && hasVisibleText.getMessageKey().length()>0){
					if (ApplicationCluster.getInstance().get(applicationMapping)!=null){
						DataIdentifier dataId = DataStore.register();
						DataStore.store(dataId, DataStore.KEY_LOCALE,sc.getLocale());
						hasVisibleTextI.setDisplayname(MessagesHandler.getMessage(applicationMapping, dataId, hasVisibleText.getMessageKey(), hasVisibleText.getDisplayname()));
						DataStore.unregister(dataId);

					} else {
						hasVisibleTextI.setDisplayname(hasVisibleText.getDisplayname());
					}
				} else {
					hasVisibleTextI.setDisplayname(hasVisibleText.getDisplayname());
				}

			}

			if (component instanceof HasRequiredProperty && vo instanceof HasRequired){
				((HasRequired)vo).setRequired(((HasRequiredProperty)component).getRequired());
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void manageMenuId(MenuItemGVO menuItemGVO, String parentId){
		menuItemGVO.setParent(parentId);
		menuItemGVO.setId(parentId+"."+menuItemGVO.getId());
		if(menuItemGVO.getSubMenus()!=null){
			for(int i = 0; i< menuItemGVO.getSubMenus().length;i++){
				manageMenuId(menuItemGVO.getSubMenus()[i], parentId);
			}
		}
	}

}
