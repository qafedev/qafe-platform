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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;



/**
 * @author rjankie This is the LayoutProperties class which holds the properties
 *         of the layout manager.
 * @deprecated
 */

public class LayoutPropertiesGVO implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4697646041915144690L;

	/**
	 * 
	 */
	protected Integer x = new Integer(0);

	/**
	 * 
	 */
	protected Integer y = new Integer(0);

	/**
	 * 
	 */
	protected Integer width = new Integer(0);

	/**
	 * 
	 */
	protected Integer height = new Integer(0);

	/**
	 * 
	 */
	protected String fill;

	/**
	 * 
	 */
	protected String align;

	/**
	 * 
	 */
	protected String valign;


	/**
	 * 
	 */
	public final static String FILL_HORIZONTAL = "HORIZONTAL";

	/**
	 * 
	 */
	public final static String FILL_VERTICAL = "VERTICAL";

	/**
	 * 
	 */
	public final static String FILL_NONE = "NONE";

	/**
	 * 
	 */
	public final static String ALIGN_CENTER = "CENTER";

	/**
	 * 
	 */
	public final static String ALIGN_LEFT = "LEFT";

	/**
	 * 
	 */
	public final static String ALIGN_RIGHT = "RIGHT";

	/**
	 * 
	 */
	public final static String VALIGN_CENTER = "CENTER";

	/**
	 * 
	 */
	public final static String VALIGN_TOP = "TOP";

	/**
	 * 
	 */
	public final static String VALIGN_BOTTOM = "BOTTOM";

	/**
	 * The id the of Layout
	 */
	private String id;

	/**
	 * Used when rendering to WebApplications for the CSS properties.
	 */
	private String style;
	
	/**
	 * 
	 */
	private String styleClass;
	/**
	 * @return the unique id of the layoutmanager
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id of the LayoutManager
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getValign() {
		return valign;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}



}
