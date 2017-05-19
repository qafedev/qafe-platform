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
package com.qualogy.qafe.gwt.client.ui.renderer.cell;



import java.util.List;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

/**
 * A {@link Cell} used to render text. Clicking on the cell causes its
 * {@link ValueUpdater} to be called.
 */
public class LinkCell extends AbstractSafeHtmlCell<String> {

  /**
   * Construct a new ClickableTextCell that will use a
   * {@link SimpleSafeHtmlRenderer}.
   */
 
  String context;
  String window;
  String component;
  String id;
  String uuid;
  String parent;
  String tooltip;
  DataGridColumnGVO column;
  public LinkCell() {
    this(SimpleSafeHtmlRenderer.getInstance());
  }

  public LinkCell(String uuid,String context, String window, String component, String id,String parent, String tooltip,final DataGridColumnGVO column) {
	    this();
	    this.uuid = uuid;
	    this.context = context;
	    this.window = window;
	    this.component = component;
	    this.id = id;
	    this.parent = parent;
	    this.tooltip = tooltip;
	    this.column = column;
	  }

  /**
   * Construct a new ClickableTextCell that will use a given
   * {@link SafeHtmlRenderer}.
   * 
   * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
   */
  public LinkCell(SafeHtmlRenderer<String> renderer) {
    super(renderer, "click", "keydown");
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, String value,
      NativeEvent event, ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    if ("click".equals(event.getType())) {
      onEnterKeyDown(context, parent, value, event, valueUpdater);
      if (column.getEvents()!=null){
    	  int size= column.getEvents().length;
    	  for (int i=0;i<size;i++){
    		  //CallbackHandler.createCallBack(uuid,this.context,window,component, id, this.parent,column.getEvents()[i].getEventListenerType(), column.getEvents()[i], column.getEvents()[i].getInputvariablesList());
    	  }
      }
    }
  }

  @Override
  protected void onEnterKeyDown(Context context, Element parent, String value,
      NativeEvent event, ValueUpdater<String> valueUpdater) {
    if (valueUpdater != null) {
      valueUpdater.update(value);
    }
  }

  @Override
  protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
	  sb.appendHtmlConstant("<div  w style=\"text-decoration:underline;width:"+column.getWidth()+";height:"+column.getHeight()+";\" tabindex=\"-1\" "+ addToolTip(tooltip)+" >");
	    if (value != null) {
	      sb.append(value);
	    }
	    sb.appendHtmlConstant("</div>");
	  
    
  }
  private String addToolTip(String tooltip){
	  if (tooltip!=null){
		  return "title=\""+tooltip+"\" ";
	  }else {
		  return "";
	  }
  }
}
