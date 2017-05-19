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
package com.qualogy.qafe.mgwt.server.ui.assembler;

	
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Element;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.layout.AbsoluteLayout;
import com.qualogy.qafe.bind.presentation.layout.AutoLayout;
import com.qualogy.qafe.bind.presentation.layout.BorderLayout;
import com.qualogy.qafe.bind.presentation.layout.GridLayout;
import com.qualogy.qafe.bind.presentation.layout.HorizontalLayout;
import com.qualogy.qafe.bind.presentation.layout.Layout;
import com.qualogy.qafe.bind.presentation.layout.VerticalLayout;
import com.qualogy.qafe.mgwt.client.vo.layout.AbsoluteLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.AutoLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.GridLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.HorizontalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.LayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.layout.VerticalLayoutGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class LayoutUIAssembler {

	public static LayoutGVO convert(Layout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		LayoutGVO vo = null;
		if (object!=null){
		 if (object instanceof HorizontalLayout){
				vo = convert((HorizontalLayout)object,currentWindow,applicationMapping,context,sc);
			} else if (object instanceof VerticalLayout){
				vo = convert((VerticalLayout)object,currentWindow,applicationMapping,context,sc);
			} else if (object instanceof AbsoluteLayout){
				vo = convert((AbsoluteLayout)object,currentWindow,applicationMapping,context,sc);
			} else if (object instanceof GridLayout){
				vo = convert((GridLayout)object,currentWindow,applicationMapping,context,sc);
			} else if (object instanceof BorderLayout){
				vo = convert((BorderLayout)object,currentWindow,applicationMapping,context,sc);
			} else if (object instanceof AutoLayout){
				vo = convert((AutoLayout)object,currentWindow,applicationMapping,context,sc);
			}
		}		
		return vo;
	}
	
	
	
	private static BorderLayoutGVO convert(BorderLayout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		BorderLayoutGVO vo = null;
		if (object !=null){
			vo = new BorderLayoutGVO();
			if (object.getCenter()!=null){
				vo.setCenter(ComponentUIAssembler.convert(object.getCenter(), currentWindow,applicationMapping,context, sc));
			}
			if (object.getEast()!=null){
				vo.setEast(ComponentUIAssembler.convert(object.getEast(), currentWindow,applicationMapping,context, sc));
			}
			if (object.getNorth()!=null){
				vo.setNorth(ComponentUIAssembler.convert(object.getNorth(), currentWindow,applicationMapping,context, sc));
			}
			
			if (object.getSouth()!=null){
				vo.setSouth(ComponentUIAssembler.convert(object.getSouth(), currentWindow,applicationMapping,context, sc));
			}
			if (object.getWest()!=null){
				vo.setWest(ComponentUIAssembler.convert(object.getWest(), currentWindow,applicationMapping,context, sc));
			}
		}
		return  vo;
	}
	
	private static HorizontalLayoutGVO convert(HorizontalLayout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		HorizontalLayoutGVO vo = null;
		if (object !=null){
			vo = new HorizontalLayoutGVO();
			vo.setComponents(convertComponents(object.getComponents(), currentWindow, applicationMapping,context,sc));
		}
		return vo;
	}
	private static VerticalLayoutGVO convert(VerticalLayout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		VerticalLayoutGVO vo = null;
		if (object !=null){
			vo = new VerticalLayoutGVO();
			vo.setComponents(convertComponents(object.getComponents(), currentWindow, applicationMapping,context,sc));
		}
		return vo;
	}
	
	private static AutoLayoutGVO convert(AutoLayout object, Window currentWindow,ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		AutoLayoutGVO vo = null;
		if (object !=null){
			vo = new AutoLayoutGVO();
			vo.setCols(object.getCols());
			vo.setComponents(convertComponents(object.getComponents(), currentWindow, applicationMapping,context,sc));
		}
		return vo;
	}
	
	private static AbsoluteLayoutGVO convert(AbsoluteLayout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		AbsoluteLayoutGVO vo = null;
		if (object !=null){
			vo = new AbsoluteLayoutGVO();
			vo.setElements(convertElements(object.getElements(), currentWindow, applicationMapping,context,sc));
		}
		return vo;
	}
	
	private static GridLayoutGVO convert(GridLayout object, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		GridLayoutGVO vo = null;
		if (object !=null){
			vo = new GridLayoutGVO();
			vo.setElements(convertElements(object.getElements(), currentWindow, applicationMapping,context,sc));
			
		}
		return vo;
	}
	
	
	
	private static ComponentGVO[] convertComponents(List<Component> list, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
			ComponentGVO[] vos = null;
			if (list!=null){
				vos = new ComponentGVO[list.size()];
				int i=0;
				for (Component component : list) {								
					vos[i]= ComponentUIAssembler.convert(component, currentWindow,applicationMapping,context, sc);
					i++;
				}
			}
			return vos;
	}

	private static ElementGVO[] convertElements(List<Element> list, Window currentWindow, ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		ElementGVO[] vos = null;
		if (list!=null){
			vos = new ElementGVO[list.size()];
			int i=0;
			for (Element element : list) {
							
				vos[i]= ElementUIAssembler.convert(element,currentWindow,applicationMapping,context,sc);
				i++;
			}
		}
		return vos;
}
}
