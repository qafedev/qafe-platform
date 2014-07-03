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
package com.qualogy.qafe.gwt.server.event.assembler;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyle;
import com.qualogy.qafe.bind.presentation.event.function.Clear;
import com.qualogy.qafe.bind.presentation.event.function.ClosePanel;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.bind.presentation.event.function.Copy;
import com.qualogy.qafe.bind.presentation.event.function.Focus;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.bind.presentation.event.function.RegExpValidate;
import com.qualogy.qafe.bind.presentation.event.function.SetPanel;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.ShowPanel;
import com.qualogy.qafe.bind.presentation.event.function.Toggle;
import com.qualogy.qafe.bind.presentation.event.function.UpdateModel;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.security.SetRestriction;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.server.event.assembler.dialog.DialogEventRenderer;
import com.qualogy.qafe.web.util.SessionContainer;

@Deprecated
public class EventUIAssembler {
	public final static Logger logger = Logger.getLogger(EventUIAssembler.class.getName());

	private static final Map<Class<?>,EventAssembler> RENDERER_MAP			= new HashMap<Class<?>,EventAssembler>();
	static {
		RENDERER_MAP.put(ChangeStyle.class, new ChangeStyleEventRenderer());
		RENDERER_MAP.put(RegExpValidate.class, new RegExpValidateEventRenderer());
		RENDERER_MAP.put(SetValue.class, new SetValueBuiltInRenderer());
		RENDERER_MAP.put(SetPanel.class, new SetPanelBuiltInRenderer());
		RENDERER_MAP.put(Copy.class, new CopyEventRenderer());
		RENDERER_MAP.put(Focus.class, new FocusEventRenderer());
		RENDERER_MAP.put(Toggle.class, new ToggleEventRenderer());
		RENDERER_MAP.put(SetProperty.class, new SetPropertyEventRenderer());		
		RENDERER_MAP.put(Clear.class, new ClearEventRenderer());	
		RENDERER_MAP.put(GenericDialog.class, new DialogEventRenderer());
		RENDERER_MAP.put(OpenWindow.class, new OpenWindowEventRenderer());
		RENDERER_MAP.put(CloseWindow.class, new CloseWindowEventRenderer());
		RENDERER_MAP.put(LogFunction.class, new LogFunctionEventRenderer());
		RENDERER_MAP.put(SetRestriction.class, new SetRestrictionEventRenderer());
		RENDERER_MAP.put(ShowPanel.class, new ShowPanelBuiltInRenderer());
		RENDERER_MAP.put(ClosePanel.class, new ClosePanelBuiltInRenderer());
		
		RENDERER_MAP.put(UpdateModel.class, new UpdateModelBuiltInRenderer());
	}
	
	public static BuiltInFunctionGVO convert(EventItem object,EventDataGVO eventData, ApplicationContext context,SessionContainer sc){
		BuiltInFunctionGVO vo = null;
		if (object!=null){
			EventAssembler assembler =RENDERER_MAP.get(object.getClass());
			if (assembler!=null){
					vo = assembler.convert(object,eventData,context, sc);
				
			}else {
				logger.warning("EventUIAssembler:Unable to find renderer for class " + object.getClass().getName() );
				
			}
		}
		
		return vo;
	}
}
