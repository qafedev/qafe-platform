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
package com.qualogy.qafe.gwt.server.ui.assembler;

import java.util.HashMap;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.View;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.Listener;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ClickEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class ApplicatonUIAssembler {

	public static UIGVO convert(View object, ApplicationMapping applicationMapping, ApplicationContext context,SessionContainer sc) {
		UIGVO vo = null;
		if (object != null) {
			vo = new UIGVO();
			vo.setTitle(object.getTitle());
			if (context!=null){
				vo.setRootMenu(context.getRootMenu());
			}
			// vo.setParent(convert(object.getParent()));

			List<Window> windows = object.getWindows();
			if (windows != null) {
				int index = 0;
				WindowGVO[] wins = new WindowGVO[windows.size()];
				for (Window w : windows) {
					WindowGVO windowGVO = (WindowGVO)ComponentUIAssembler.convert(w, w, applicationMapping,context,sc);
					wins[index] = windowGVO;
					index++;
					
					resolveEventForOpenWindow(vo, windowGVO, applicationMapping);
				}
				vo.setWindows(wins);
			}

		}

		return vo;
	}
	
	protected static void resolveEventForOpenWindow(UIGVO uiGVO, WindowGVO windowGVO, ApplicationMapping applicationMapping) {
		if ((uiGVO != null) && (windowGVO != null) && (applicationMapping != null)) {
			if ((applicationMapping.getPresentationTier() != null) && (applicationMapping.getPresentationTier().getEvents() != null)) {
				for (Event event : applicationMapping.getPresentationTier().getEvents()) {
					String eventId = Event.createEventId(windowGVO.getId() + Listener.EVENT_ONCLICK);
					if (eventId.equals(event.getId())) {
						EventListenerGVO eventListenerGVO = new ClickEventListenerGVO(eventId, new HashMap<String,String>(), null, null);
						uiGVO.getEventMap().put(windowGVO.getId(), eventListenerGVO);
						break;
					}
				}
			}
		}
	}
}
