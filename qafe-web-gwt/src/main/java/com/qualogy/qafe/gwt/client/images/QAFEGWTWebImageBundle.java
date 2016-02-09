/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface QAFEGWTWebImageBundle extends ImageBundle {
	@Resource("com/qualogy/qafe/gwt/client/images/waitanimation.gif")
	public AbstractImagePrototype waitIcon();

	@Resource("com/qualogy/qafe/gwt/client/images/waitanimation2.gif")
	public AbstractImagePrototype waitIcon2();

	@Resource("com/qualogy/qafe/gwt/client/images/waitanimation3.gif")
	public AbstractImagePrototype waitIcon3();

	@Resource("com/qualogy/qafe/gwt/client/images/dot_dark.png")
	public AbstractImagePrototype dotDark();

	@Resource("com/qualogy/qafe/gwt/client/images/dot_medium.png")
	public AbstractImagePrototype dotMedium();

	@Resource("com/qualogy/qafe/gwt/client/images/dot_light.png")
	public AbstractImagePrototype dotLight();

	@Resource("com/qualogy/qafe/gwt/client/images/favicon.ico.gif")
	public AbstractImagePrototype progressIcon();
}
