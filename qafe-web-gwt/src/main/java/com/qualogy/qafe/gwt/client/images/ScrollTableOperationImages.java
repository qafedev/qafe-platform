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
package com.qualogy.qafe.gwt.client.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface ScrollTableOperationImages extends ImageBundle {
	
	@Resource ("com/qualogy/qafe/gwt/client/images/new.png")
    AbstractImagePrototype scrolltableOperationAdd();

	@Resource ("com/qualogy/qafe/gwt/client/images/delete.png")
    AbstractImagePrototype scrolltableOperationDelete();

	@Resource ("com/qualogy/qafe/gwt/client/images/save.png")
    AbstractImagePrototype scrolltableOperationSave();

	@Resource ("com/qualogy/qafe/gwt/client/images/refresh.png")
    AbstractImagePrototype scrolltableOperationRefresh();

	@Resource ("com/qualogy/qafe/gwt/client/images/undo.png")
    AbstractImagePrototype scrolltableOperationCancel();

}
