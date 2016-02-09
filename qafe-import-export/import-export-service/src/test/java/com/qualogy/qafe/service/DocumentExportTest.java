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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.qualogy.qafe.service.domain.DocumentExportInput;

public class DocumentExportTest extends TestCase {

	
	public void testExportEmptyPDF(){
		DocumentExportInput dei = new DocumentExportInput();
		dei.setExportCode(DocumentService.EXPORT_PDF);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map m1 = new HashMap<String, Object>();
		m1.put("Name", "A");
		m1.put("Last Name", "1");
		Map m2 = new HashMap<String, Object>();
		m2.put("Name", "B");
		m2.put("Last Name", "2");
		Map m3 = new HashMap<String, Object>();
		m3.put("Name", "C");
		m3.put("Last Name", "3");
		
		list.add(m1);
		list.add(m2);
		list.add(m3);
		dei.setData(list);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DocumentExporter.exportPDF(dei, bout);
	}
	
	public void testExportEmptyXML(){
		DocumentExportInput dei = new DocumentExportInput();
		dei.setExportCode(DocumentService.EXPORT_XML);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map m1 = new HashMap<String, Object>();
		m1.put("Name", "A");
		m1.put("Last Name", "1");
		Map m2 = new HashMap<String, Object>();
		m2.put("Name", "B");
		m2.put("Last Name", "2");
		Map m3 = new HashMap<String, Object>();
		m3.put("Name", "C");
		m3.put("Last Name", "3");
		
		list.add(m1);
		list.add(m2);
		list.add(m3);
		
		dei.setData(list);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DocumentExporter.exportXML(dei, bout);
	}
	
}
