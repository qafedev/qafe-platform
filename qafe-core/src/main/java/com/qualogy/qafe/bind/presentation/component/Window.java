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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.security.AuthenticationControlled;
import com.qualogy.qafe.bind.core.security.AuthenticationModule;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.style.Style;
import com.qualogy.qafe.bind.presentation.style.StyleSet;

public class Window extends Component implements PostProcessing, AuthenticationControlled, HasVisibleText {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3145439667526952273L;

	protected RootPanel rootpanel;
	/**
	 * 
	 */
	protected Boolean resizable = Boolean.TRUE;

	protected Boolean closable = Boolean.TRUE;

	protected Boolean minimizable = Boolean.TRUE;

	protected Boolean maximizable = Boolean.TRUE;

	protected Boolean draggable = Boolean.TRUE;

	protected Boolean isparent = Boolean.TRUE;

	protected List<Event> events;

	protected Map<String, Event> eventsMap = new HashMap<String, Event>(17);

	protected StyleSet styles;

	protected String displayname;

	protected String messageKey;

	protected String name;

	protected String css;

	protected String icon;

	protected Boolean inDock;
	
	protected Integer left;
	protected Integer top;
	protected String iconStyle;

	private AuthenticationModule module;
	private boolean authenticationRequired; // is authentication required for this app

	public Boolean getInDock() {
		return inDock;
	}

	public void setInDock(Boolean inDock) {
		this.inDock = inDock;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void add(Event event) {
		if (event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		if (events == null) {
			events = new ArrayList<Event>();
		}
		events.add(event);
	}

	public void addAll(List<Event> events) {
		if (events == null) {
			throw new IllegalArgumentException("event list cannot be null");
		}
		if (this.events == null) {
			this.events = new ArrayList<Event>();
		}
		this.events.addAll(events);
	}

	public void add(Style style) {
		if (style == null) {
			throw new IllegalArgumentException("style cannot be null");
		}
		if (styles == null) {
			styles = new StyleSet();
		}
		styles.add(style);
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RootPanel getRootPanel() {
		return rootpanel;
	}

	public void setRootPanel(RootPanel rootpanel) {
		this.rootpanel = rootpanel;
	}

	public StyleSet getStyles() {
		return styles;
	}

	public void setStyles(final StyleSet styles) {
		this.styles = styles;
	}

	public Boolean getResizable() {
		return resizable;
	}

	public void setResizable(Boolean resizable) {
		this.resizable = resizable;
	}

	public Boolean getIsparent() {
		return isparent;
	}

	public void setIsparent(Boolean isparent) {
		this.isparent = isparent;
	}

	public void performPostProcessing() {
		// Since events can be referenced from other events, the map for
		// convenience is set up after processing.
		if (events != null) {
			for (Event event : events) {
				if (eventsMap.containsKey(event.getId())) {
					String errorMessage = "Event with id '"	+ event.getId()
							+ "' is defined more than once in the local events for window [" + getId()
							+ "]. Id of an event should be unqiue within a window. Please correct this problem";
					Logger.getLogger(this.getClass().getName()).severe(errorMessage);
					throw new ValidationException(errorMessage);
				}
				eventsMap.put(event.getId(), event);
			}
		}
		Logger.getLogger(this.getClass().getName()).info("" + eventsMap.size() + "  local events found for window [" + getId() + "]");
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public Map<String, Event> getEventsMap() {
		return eventsMap;
	}

	public Boolean getDraggable() {
		return draggable;
	}

	public void setDraggable(Boolean draggable) {
		this.draggable = draggable;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public void setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
	}

	public Boolean getMinimizable() {
		return minimizable;
	}

	public void setMinimizable(Boolean minimizable) {
		this.minimizable = minimizable;
	}

	public Boolean getClosable() {
		return closable;
	}

	public void setClosable(Boolean closable) {
		this.closable = closable;
	}

	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	public boolean isAuthenticationRequired() {
		return authenticationRequired;
	}

	public void setAuthenticationRequired() {
		this.authenticationRequired = true;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public AuthenticationModule getModule() {
		return module;
	}

	public void setAuthenticationModule(AuthenticationModule module) {
		this.module = module;
	}

	public AuthenticationModule getAuthenticationModule() {
		return module;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}
