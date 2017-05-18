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

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.TextField;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.i18n.MessagesHandler;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.presentation.io.ApplicationClusterUtil;
import com.qualogy.qafe.web.util.SessionContainer;

public class TextFieldUIAssembler implements UIAssembler {

	public TextFieldUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof TextField) {
				TextField textField = (TextField)object;
				TextFieldGVO voTemp  = new TextFieldGVO();
				UIAssemblerHelper.copyFields(textField, currentWindow,voTemp,applicationMapping, context, ss);				
				voTemp.setValue(textField.getValue());	
				voTemp.setEditable(textField.getEditable());
				voTemp.setMaxLength(textField.getMaxLength());
				voTemp.setMinLength(textField.getMinLength());
				voTemp.setType(textField.getType());
				voTemp.setRequired(textField.getRequired());
				voTemp.setRequiredStyleClassName(textField.getRequiredStyleClassName());
				String format=  null;
				if (textField.getFormat()!=null ){  // actually it can never be null, since this is a default value from jibx + TextField
					if (!textField.getFormat().equals(TextField.DEFAULT_DATE_FORMAT)){ // so a specific format is set.
						format = textField.getFormat();
					}else { //so the format is the same as the default, check if the global format is defined. The global one is preferred.
						String globalFormat = ApplicationClusterUtil.getGlobalDateFormat();
						if (globalFormat!=null){
							format= globalFormat;
						}
					}
				}
				if (format!=null){
					voTemp.setFormat(format);
				} else {
					voTemp.setFormat(TextField.DEFAULT_DATE_FORMAT); //safety fallback!
				}
				voTemp.setOrientation(textField.getOrientation());
				// Regexp and message are set in the postset after reading the XML
				voTemp.setRegExp(textField.getRegExp());
				voTemp.setSuggest(textField.getSuggest());
				voTemp.setSuggestCharacters(textField.getSuggestCharacters());
				voTemp.setMinValue(textField.getMinValue());
				voTemp.setMaxValue(textField.getMaxValue());
				voTemp.setValidationMessage(textField.getValidationMessage());
				voTemp.setValidationTitle(textField.getValidationTitle());
				voTemp.setRequiredValidationMessage(textField.getRequiredValidationMessage());
				voTemp.setRequiredValidationTitle(textField.getRequiredValidationTitle());
				vo = resolveI18N(textField, voTemp, applicationMapping, ss);
			}
		}
		return vo;
	}
	
	private ComponentGVO resolveI18N(TextField textField, TextFieldGVO textFieldGVO, ApplicationMapping applicationMapping, SessionContainer sessionContainer) {
		if(sessionContainer != null && ApplicationCluster.getInstance().get(applicationMapping) != null) {
			DataIdentifier dataId = DataStore.register();
			try {
				DataStore.store(dataId, DataStore.KEY_LOCALE, sessionContainer.getLocale());
				if (textField.getValidationMessageKey() != null){
					textFieldGVO.setValidationMessage(MessagesHandler.getMessage(applicationMapping, dataId, textField.getValidationMessageKey(), textFieldGVO.getValidationMessage()));
				}
				if (textField.getValidationTitleKey() != null){
					textFieldGVO.setValidationTitle(MessagesHandler.getMessage(applicationMapping, dataId, textField.getValidationTitleKey(), textFieldGVO.getValidationTitle()));
				}
				if (textField.getRequiredValidationMessageKey() != null){
					textFieldGVO.setRequiredValidationMessage(MessagesHandler.getMessage(applicationMapping, dataId, textField.getRequiredValidationMessageKey(), textFieldGVO.getRequiredValidationMessage()));
				}
				if (textField.getRequiredValidationTitleKey() != null){
					textFieldGVO.setRequiredValidationTitle(MessagesHandler.getMessage(applicationMapping, dataId, textField.getRequiredValidationTitleKey(), textFieldGVO.getRequiredValidationTitle()));
				}
			} finally {
				DataStore.unregister(dataId);
			}
		}
		return textFieldGVO;
	}

	public String getStaticStyleName() {
		return "textfield";
	}
	
}
