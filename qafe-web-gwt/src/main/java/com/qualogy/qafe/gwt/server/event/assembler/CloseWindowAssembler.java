package com.qualogy.qafe.gwt.server.event.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class CloseWindowAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        CloseWindowGVO eventItemGVO = null;
        if (eventItem instanceof CloseWindow) {
            eventItemGVO = new CloseWindowGVO();
            assembleAttributes(eventItemGVO, (CloseWindow)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(CloseWindowGVO eventItemGVO, CloseWindow eventItem, Event event, ApplicationContext applicationContext) {
    	ParameterGVO windowGVO = assembleParameter(eventItem.getWindow());
    	eventItemGVO.setWindowGVO(windowGVO);
    }
}
