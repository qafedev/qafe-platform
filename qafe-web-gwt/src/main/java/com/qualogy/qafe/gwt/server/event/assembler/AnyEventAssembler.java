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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.error.ErrorHandler;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyle;
import com.qualogy.qafe.bind.presentation.event.function.Clear;
import com.qualogy.qafe.bind.presentation.event.function.ClosePanel;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.bind.presentation.event.function.Copy;
import com.qualogy.qafe.bind.presentation.event.function.EventRef;
import com.qualogy.qafe.bind.presentation.event.function.Focus;
import com.qualogy.qafe.bind.presentation.event.function.LocalDelete;
import com.qualogy.qafe.bind.presentation.event.function.LocalStore;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.bind.presentation.event.function.RegExpValidate;
import com.qualogy.qafe.bind.presentation.event.function.Return;
import com.qualogy.qafe.bind.presentation.event.function.CallScript;
import com.qualogy.qafe.bind.presentation.event.function.SetPanel;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.ShowPanel;
import com.qualogy.qafe.bind.presentation.event.function.Toggle;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;

// TODO: rename to EventAssembler when the existing one is renamed or removed
public class AnyEventAssembler {

    private static final Map<Class<?>, EventItemAssembler> ASSEMBLER_MAP =
        new HashMap<Class<?>, EventItemAssembler>();
    static {
        ASSEMBLER_MAP.put(OpenWindow.class, new OpenWindowAssembler());
        ASSEMBLER_MAP.put(LocalStore.class, new LocalStoreAssembler());
        ASSEMBLER_MAP.put(LocalDelete.class, new StoreClearAssembler());
        ASSEMBLER_MAP.put(BusinessActionRef.class, new BusinessActionRefAssembler());
        ASSEMBLER_MAP.put(GenericDialog.class, new GenericDialogAssembler());
        ASSEMBLER_MAP.put(SetValue.class, new SetValueAssembler());
        ASSEMBLER_MAP.put(SetPanel.class, new SetPanelAssembler());
        ASSEMBLER_MAP.put(ShowPanel.class, new ShowPanelAssembler());
        ASSEMBLER_MAP.put(ClosePanel.class, new ClosePanelAssembler());
        ASSEMBLER_MAP.put(Toggle.class, new ToggleAssembler());
        ASSEMBLER_MAP.put(LogFunction.class, new LogAssembler());
        ASSEMBLER_MAP.put(CloseWindow.class, new CloseWindowAssembler());
        ASSEMBLER_MAP.put(EventRef.class, new EventRefAssembler());
        ASSEMBLER_MAP.put(Focus.class, new FocusAssembler());
		ASSEMBLER_MAP.put(IfStatement.class, new IfAssembler());
        ASSEMBLER_MAP.put(Clear.class, new ClearAssembler());
        ASSEMBLER_MAP.put(Return.class, new ReturnAssembler());
        ASSEMBLER_MAP.put(Copy.class, new CopyAssembler());
        ASSEMBLER_MAP.put(ChangeStyle.class, new ChangeStyleAssembler());
        ASSEMBLER_MAP.put(SetProperty.class, new SetPropertyAssembler());
        ASSEMBLER_MAP.put(ErrorHandler.class, new ErrorHandlerAssembler());
        ASSEMBLER_MAP.put(SetProperty.class, new SetPropertyAssembler());
        ASSEMBLER_MAP.put(Iteration.class, new IterationAssembler());
        ASSEMBLER_MAP.put(SwitchStatement.class, new SwitchAssembler());
        ASSEMBLER_MAP.put(RegExpValidate.class, new RegExpValidateAssembler());
        ASSEMBLER_MAP.put(CallScript.class, new CallScriptAssembler());
    }

    public static EventGVO assemble(final Event event, final ApplicationContext applicationContext) {
        if (event == null) {
            return null;
        }
        EventGVO eventGVO = new EventGVO();
        assembleAttributes(event, eventGVO);
        final List<EventItem> eventItems = event.getEventItems();
        if (eventItems != null) {
        	for (EventItem eventItem : eventItems) {
                final BuiltInFunctionGVO eventItemGVO = assemble(eventItem, event, applicationContext);                
                if (eventItemGVO != null) {
                    eventGVO.addEventItem(eventItemGVO);
                }
            }	
        }
        return eventGVO;
    }
    
    private static void assembleAttributes(Event event, EventGVO eventGVO) {
        eventGVO.setSourceId(event.getSourceId());
        eventGVO.setSourceName(event.getSourceName());
        eventGVO.setSourceValue(event.getSourceValue());
        eventGVO.setSourceListenerType(event.getSourceListenerType());
    }
    
    public static BuiltInFunctionGVO assemble(final EventItem eventItem, final Event event
    	, final ApplicationContext applicationContext) {    	
        if (eventItem == null) {
            return null;
        }
        final EventItemAssembler eventItemAssembler = ASSEMBLER_MAP.get(eventItem.getClass());
        if (eventItemAssembler == null) {
            return null;
        }
        return eventItemAssembler.assemble(eventItem, event, applicationContext);
    }
}
