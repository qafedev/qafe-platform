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
package com.qualogy.qafe.service;

import com.qualogy.qafe.service.domain.DocumentExportInput;
import com.qualogy.qafe.service.domain.DocumentExportOutput;
import com.qualogy.qafe.service.domain.DocumentOutput;
import com.qualogy.qafe.service.domain.DocumentParameter;


public interface DocumentService {
	public final static String EXPORT_CSV = "CSV";
	public final static String EXPORT_EXCEL = "Excel";
	public final static String EXPORT_XML = "XML";
	public final static String EXPORT_PDF = "PDF";
	public final static String EXPORT_RTF = "RTF";
	
	DocumentOutput processExcelUpload(DocumentParameter parameter);

	DocumentExportOutput export(DocumentExportInput input);
}
