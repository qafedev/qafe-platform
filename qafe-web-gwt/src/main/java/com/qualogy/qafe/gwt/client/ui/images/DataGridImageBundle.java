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
package com.qualogy.qafe.gwt.client.ui.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface DataGridImageBundle extends ImageBundle {
	
	 
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/add.png")
	public AbstractImagePrototype addIcon();
	
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/delete.png")
	public AbstractImagePrototype deleteIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/edit.png")
	public AbstractImagePrototype editIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/new.png")
	public AbstractImagePrototype newIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/next_message.png")
	public AbstractImagePrototype lastIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/next.png")
	public AbstractImagePrototype nextIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/previous_message.png")
	public AbstractImagePrototype firstIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/previous.png")
	public AbstractImagePrototype previousIcon();
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/refresh.png")
	public AbstractImagePrototype refreshIcon();
	
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/undo.png")
	public AbstractImagePrototype undoIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/table.png")
	public AbstractImagePrototype exportIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/check.png")
	public AbstractImagePrototype saveIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/navigateBack.png")
	public AbstractImagePrototype backwardsIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/navigateForward.png")
	public AbstractImagePrototype forwardIcon();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/ico_file_csv.png")
	public AbstractImagePrototype  iconExportCSV();

	@Resource ("com/qualogy/qafe/gwt/client/ui/images/ico_file_excel.png")
	public AbstractImagePrototype  iconExportExcel();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/ico_file_pdf.png")
	public AbstractImagePrototype  iconExportPDF();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/ico_file_rtf.png")
	public AbstractImagePrototype  iconExportRTF();
	
	@Resource ("com/qualogy/qafe/gwt/client/ui/images/ico_file_xml.png")
	public AbstractImagePrototype  iconExportXML();
	
}
