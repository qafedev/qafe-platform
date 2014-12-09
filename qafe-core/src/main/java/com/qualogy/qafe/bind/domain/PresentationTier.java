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
package com.qualogy.qafe.bind.domain;

/**
 * $Rev::               $:  Revision of last commit
 $Author::            $:  Author of last commit
 $Date::              $:  Date of last commit
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.IfResult;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SwitchResult;
import com.qualogy.qafe.bind.core.statement.SwitchResults;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.component.View;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.bind.presentation.event.function.EventRef;
import com.qualogy.qafe.bind.presentation.style.Style;
import com.qualogy.qafe.bind.presentation.style.StyleSet;

public class PresentationTier implements Serializable, PostProcessing, Tier {

	private static final long serialVersionUID = 8021402983709812487L;

	protected List<Event> events;
	protected StyleSet styles;
	protected View view;
	protected Map<String, Event> eventsMap = new HashMap<String, Event>(17);

	public PresentationTier(List<Event> events, StyleSet styles, View screens) {
		this.events = events;
		this.styles = styles;
		this.view = screens;
	}

	public PresentationTier() {
	}

	public static PresentationTier create(List<Event> events, StyleSet styles, View screens) {
		return new PresentationTier(events, styles, screens);
	}

	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 *
	 * @param events
	 *            the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	/**
	 * @return the styles
	 */
	public StyleSet getStyles() {
		return styles;
	}

	public void validate() throws ValidationException {
	}

	/**
	 * method to add a Event to a Event list. The list will be created when null
	 *
	 * @param action
	 * @throws IllegalArgumentException - when object param passed is null
	 */
	public void add(Event event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		if (events == null) {
			events = new ArrayList<Event>();
		}
		events.add(event);
	}

	/**
	 * method to add a Style to a Style list. The list will be created when null
	 *
	 * @param action
	 * @throws IllegalArgumentException - when object param passed is null
	 */
	public void add(Style style) {
		if (style == null) {
			throw new IllegalArgumentException("Style cannot be null");
		}
		if (styles == null) {
			styles = new StyleSet();
		}
		styles.add(style);
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public void performPostProcessing() {
		// Since events can be referenced from other events, the map for
		// convenience is set up after processing.
		if (events != null) {
			for (Event event : events) {
				if (eventsMap.containsKey(event.getId())) {
					String errorMessage = "Event with id '" + event.getId()
						+ "' is defined more than once in the global events. "
						+ "Id of an event should be unqiue within the global events. Please correct this problem";
					Logger.getLogger(this.getClass().getName()).severe(errorMessage);
					throw new ValidationException(errorMessage);
				}
				eventsMap.put(event.getId(), event);
			}
		}
		Logger.getLogger(this.getClass().getName()).info("" + eventsMap.size() + "  global events found");

		// Id of a global event should be unique within the application context
		checkIfEventsExistInWindows();

		// Populate the inputVariables when events are referenced.
		// All the post-set methods on events are executed.
		if (getView() != null) {
			if (getView().getWindows() != null) {
				for (Window window : getView().getWindows()) {
					processEvents(window);
				}
			}
		}
		processEvents(getEvents(), null);
	}

	private void checkIfEventsExistInWindows() {
		if ((getView() != null) && (getView().getWindows() != null)) {
			List<Window> windowList = getView().getWindows();
			for (Window window : windowList) {
				if (window.getEvents() != null) {
					for (Event event : window.getEvents()) {
						if (eventsMap.containsKey(event.getId())) {
							String errorMessage = "Event with id '" + event.getId()
								+ "' is defined in the local events for window [" + window.getId()
								+ "] which also exists within the global events. Please correct this problem";
							Logger.getLogger(this.getClass().getName()).severe(errorMessage);
							throw new ValidationException(errorMessage);
						}
					}
				}
			}
		}
	}

	private void processEvents(Window w) {
		if (w == null) {
			return;
		}
		processEvents(w.getEvents(), w);
	}

	private void processEvents(List<Event> events, Window w) {
		if (events == null) {
			return;
		}
		for (Event event : events) {
			processEvent(event, w);
		}
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private void processEvent(Event event, Window w) {
		if ((event == null) || (event.getEventItems() == null)) {
			return;
		}
		Set<String> processedEvents = new HashSet<String>();
		Queue<Item> queue = new LinkedList<Item>();
		for (Item eventItem : event.getEventItems()) {
			queue.offer(eventItem);
		}
		while (!queue.isEmpty()) {
			Item eventItem = queue.poll();
			if (eventItem instanceof EventRef) {
				EventRef eventRef = (EventRef)eventItem;
				String subEventId = eventRef.getEvent();

				if (processedEvents.contains(subEventId)) {
					continue;
				}
				processedEvents.add(subEventId);

				//TODO: As a Qafe Developer I want to find recursive events in application loading so that I will get warning about potential issue in the code.

				Event subEvent = getEventById(subEventId, w);
				if (subEvent == null) {
					continue;
				}
				if (subEvent.getInput() != null) {
					event.addAllParameters(subEvent.getInput());
				}
				if (subEvent.getEventItems() != null) {
					for (Item subEventItem : subEvent.getEventItems()) {
						queue.offer(subEventItem);
					}
				}
			} else if (eventItem instanceof ControlStatement) {
				ControlStatement controlStatement = (ControlStatement)eventItem;
				List<Item> resultItems = getResultItems(controlStatement);
				if (resultItems != null) {
					for (Item resultItem : resultItems) {
						queue.offer(resultItem);
					}
				}

				if (controlStatement instanceof Iteration) {
					Iteration iteration = (Iteration)controlStatement;
					Reference reference = iteration.getReference();
					if ((reference != null) && reference.isComponentReference()) {
						InputVariable inputVariable = new InputVariable(reference.stringValueOf(), reference.stringValueOf(), null);
						List<InputVariable> inputVariables = new ArrayList<InputVariable>();
						inputVariables.add(inputVariable);
						event.addAllParameters(inputVariables);
					}
				}
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private Event getEventById(String eventId, Window w) {
		Event event = null;
		Map<String,Event> eventsMap = null;
		if (w != null) {
			eventsMap = w.getEventsMap();
			if (eventsMap != null) {
				event = eventsMap.get(eventId);
			}
		}
		if (event == null) {
			eventsMap = getEventsMap();
			if (eventsMap != null) {
				event = eventsMap.get(eventId);
			}
		}
		return event;
	}

	private List<Item> getResultItems(ControlStatement controlStatement) {
		List<Item> resultItems = new ArrayList<Item>();
		if (controlStatement instanceof Iteration) {
			Iteration iteration = (Iteration)controlStatement;
			Iterator<ResultItem> itrResultItem = iteration.resultItemsIterator();
			addToResultItems(itrResultItem, resultItems);
		} else if (controlStatement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement)controlStatement;
			List<IfResult> results = ifStatement.getResults();
			if (results != null) {
				for (IfResult result : results) {
					if (result.getResultItems() == null) {
						continue;
					}
					Iterator<ResultItem> itrResultItem = result.getResultItems().iterator();
					addToResultItems(itrResultItem, resultItems);
				}
			}
		} else if (controlStatement instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement)controlStatement;
			SwitchResults switchResults = switchStatement.getResults();
			if (switchResults != null) {
				List<SwitchResult> results = switchResults.getResults();
				if (results != null) {
					for (SwitchResult result : results) {
						if (result.getResultItems() == null) {
							continue;
						}
						Iterator<ResultItem> itrResultItem = result.getResultItems().iterator();
						addToResultItems(itrResultItem, resultItems);
					}
				}
				List<ResultItem> defaultResult = switchResults.getDefaultResult();
				if (defaultResult != null) {
					Iterator<ResultItem> itrResultItem = defaultResult.iterator();
					addToResultItems(itrResultItem, resultItems);
				}
			}
		}
		return resultItems;
	}

	private void addToResultItems(Iterator<ResultItem> itrResultItem, List<Item> resultItems) {
		if (itrResultItem == null) {
			return;
		}
		if (resultItems == null) {
			return;
		}
		while (itrResultItem.hasNext()) {
			ResultItem resultItem = itrResultItem.next();
			resultItems.add(resultItem);
		}
	}

	public void addAll(PresentationTier otherPresentationTier) {
		// view
		if (view == null || view.getWindows() == null) {
			view = otherPresentationTier.getView();
		} else {
			if (otherPresentationTier.getView() != null) {
				getView().addAll(otherPresentationTier.getView());
			}
		}

		// events
		if (events == null) {
			events = new ArrayList<Event>();
		}
		if (otherPresentationTier.getEvents() != null) {
			events.addAll(otherPresentationTier.getEvents());
		}

		// styles
		if (styles == null) {
			styles = new StyleSet();
		}
		if (otherPresentationTier.getStyles() != null) {
			styles.addAll(otherPresentationTier.getStyles());
		}
	}

	public Map<String, Event> getEventsMap() {
		return eventsMap;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public void setStyles(StyleSet styles) {
		this.styles = styles;
	}
}