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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

public class QPagingScrollTableHelper {

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static ComponentGVO createGVO(Object object) {
		ComponentGVO component = null;

		if (object instanceof Boolean || object instanceof Character || object instanceof String || object instanceof Date || object instanceof Number) {
			TextFieldGVO textfield = new TextFieldGVO();
			component = textfield;
			textfield.setValue(object.toString());

			if (object instanceof Boolean || object instanceof String) {

			} else if (object instanceof Character) {
				textfield.setType(TextFieldGVO.TYPE_CHARACTERS);
				textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_CHARACTERS_VALUE);
				textfield.setValidationMessage(TextFieldGVO.TYPE_CHARACTERS_DEFAULT_MESSAGE);
			} else if (object instanceof Date) {
				textfield.setType(TextFieldGVO.TYPE_DATE);
				String format =TextFieldGVO.DEFAULT_DATE_FORMAT;
				if (ClientApplicationContext.getInstance().getGlobalDateFormat()!=null){
					format = ClientApplicationContext.getInstance().getGlobalDateFormat();
				}

				DateTimeFormat sdf = DateTimeFormat.getFormat(format);
				textfield.setFormat(format);
				textfield.setValue(sdf.format((Date) object));

			} else if (object instanceof Number) {
				if (object instanceof Byte) {

				} else if (object instanceof Double) {
					textfield.setType(TextFieldGVO.TYPE_DOUBLE);
					textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_DOUBLE_VALUE);
					textfield.setValidationMessage(TextFieldGVO.TYPE_DOUBLE_DEFAULT_MESSAGE);
				} else if (object instanceof Float) {
					textfield.setType(TextFieldGVO.TYPE_DOUBLE);
					textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_DOUBLE_VALUE);
					textfield.setValidationMessage(TextFieldGVO.TYPE_DOUBLE_DEFAULT_MESSAGE);
				} else if (object instanceof Integer) {
					textfield.setType(TextFieldGVO.TYPE_INTEGER);
					textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_INTEGER_VALUE);
					textfield.setValidationMessage(TextFieldGVO.TYPE_INTEGER_DEFAULT_MESSAGE);
				} else if (object instanceof Long) {
					textfield.setType(TextFieldGVO.TYPE_INTEGER);
					textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_INTEGER_VALUE);
					textfield.setValidationMessage(TextFieldGVO.TYPE_INTEGER_DEFAULT_MESSAGE);
				} else if (object instanceof Short) {
					textfield.setType(TextFieldGVO.TYPE_INTEGER);
					textfield.setRegExp(TextFieldGVO.REGEXP_TYPE_INTEGER_VALUE);
					textfield.setValidationMessage(TextFieldGVO.TYPE_INTEGER_DEFAULT_MESSAGE);
				}
			} else if (object instanceof Date){
				textfield.setType(TextFieldGVO.TYPE_DATE);


			}


		}

		return component;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity
}
