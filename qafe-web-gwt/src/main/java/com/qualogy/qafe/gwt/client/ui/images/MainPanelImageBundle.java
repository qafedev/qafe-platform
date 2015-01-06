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
package com.qualogy.qafe.gwt.client.ui.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface MainPanelImageBundle extends ImageBundle {
	
	 
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/qafegrr.jpg")
	public AbstractImagePrototype grr();

	@Resource ("com/qualogy/qafe/gwt/client/ui/images/qafergr.jpg")
	public AbstractImagePrototype rgr();
	
	
}
