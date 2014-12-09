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
package com.qualogy.qafe.bind.presentation.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.presentation.layout.BorderLayout;
import com.qualogy.qafe.bind.presentation.layout.GridLayout;
import com.qualogy.qafe.bind.presentation.layout.Layout;


/**
 * @author rjankie
 * The RootPanel class is the container 
 */
public class RootPanel extends Panel implements PostProcessing{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2329991094591222688L;

	protected Toolbar toolbar ;
	
	public Toolbar getToolbar() {
		return toolbar;
	}

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * 
	 * @param context JibxContext which is not used.
	 */
	public void postset(IUnmarshallingContext context){
		performPostProcessing();
	}

	public void performPostProcessing() {
		
		Map<String, Component> ids = new HashMap<String, Component>(17);
		processComponent(this, ids);
		
	}

	private void checkChildren(Layout layout, Map<String, Component> ids) {
		if (layout!=null){
			if (layout instanceof GridLayout){
				GridLayout gridLayout = (GridLayout)layout;
				List<Element> elements = gridLayout.getElements();
				if (elements!=null){
					for (Element element : elements) {												
						processComponent(element.getComponent(),ids);
					}
				}
			} else if (layout instanceof BorderLayout){
				BorderLayout borderLayout = (BorderLayout)layout;
				processComponent(borderLayout.getNorth(),ids);
				processComponent(borderLayout.getSouth(),ids);
				processComponent(borderLayout.getEast(),ids);
				processComponent(borderLayout.getWest(),ids);
				processComponent(borderLayout.getCenter(),ids);
				
			}else {
				if ( layout.getComponents()!=null){
					for (Component component : layout.getComponents()) {										
						processComponent(component,ids);
					}
				}
				
			}
		}
		
	}

	private void processComponent(Component component, Map<String, Component> ids) {
		if (component!=null){
			if (component.getId()!=null  ){
				if (ids.containsKey(component.getId())){
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "RootPanel with id " + getId() +" has duplicate entry for " + component.getId()+". Please fix this");
					throw new RuntimeException("RootPanel with id " + getId() +" has duplicate entry for " + component.getId()+". Please fix this");
				} else {
					ids.put(component.getId(),component);
					if (component instanceof HasComponents){
						if (component instanceof HasLayout){
							HasLayout hasLayout = (HasLayout) component;
							checkChildren(hasLayout.getLayout(), ids);
						}
						else {
							HasComponents hasComponents = (HasComponents)component;
							List<?> children = hasComponents.getComponents();
							if (children !=null){
								Iterator<?> itr  = children.iterator();
								while(itr.hasNext()){
									Object object = itr.next();
									if (object instanceof Component){
										Component childComponent = (Component)object;
										processComponent(childComponent, ids);
										
									}
								}
							}
						}
					} 
				}
			}
		}
		
	}
}
