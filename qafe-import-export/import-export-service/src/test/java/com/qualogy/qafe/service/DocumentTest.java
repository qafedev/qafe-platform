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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import com.qualogy.qafe.service.domain.DocumentOutput;
import com.qualogy.qafe.service.domain.DocumentParameter;

public class DocumentTest extends TestCase{

	DocumentService dus = new DocumentServiceImpl();

	public void testSimpleDocumentXLS(){		
		dus.processExcelUpload(getDocumentParameter("files/sample-excel.xls"));
	}
	
 
	public void testSimpleDocumentXLSWithoutHeader(){
		DocumentParameter dp = getDocumentParameter("files/sample-excel.xls");
		dp.setFirstFieldHeader(false);
		dus.processExcelUpload(dp);
	}

	public void testSimpleDocumentXLSWithoutHeaderAndPits(){
		DocumentParameter dp = getDocumentParameter("files/sample-excel-pits.xls");
		dp.setFirstFieldHeader(true);
		DocumentOutput docOutput = dus.processExcelUpload(dp);
		String[][] refData = createReferenceData4SampleExcelPits();
		validate(docOutput, refData);
	}
	
	public void testSimpleDocumentCSV(){
		DocumentParameter dp = getDocumentParameter("files/sample-csv.xls");
		dp.setFirstFieldHeader(false);
		dp.setDelimiter("\t");
		dus.processExcelUpload(dp);
	}
	
	public void testSimpleDocumentCSV3(){
		DocumentParameter dp = getDocumentParameter("files/sample-csv.xls");
		dp.setFirstFieldHeader(true);
		dp.setDelimiter("\t");
		dus.processExcelUpload(dp);
	}

	public void testSimpleDocumentCSV2(){
		DocumentParameter dp = getDocumentParameter("files/sample-excelplain.csv");
		dp.setFirstFieldHeader(false);
		dp.setDelimiter(";");
		dus.processExcelUpload(dp);
	}
	
	public void testSimpleDocumentXLS2007(){
		dus.processExcelUpload(getDocumentParameter("files/sample-excel2007.xlsx"));
	}
	
	public void testSimpleDocumentXLS2007WithoutHeader(){
		DocumentParameter dp = getDocumentParameter("files/sample-excel2007.xlsx");
		dp.setFirstFieldHeader(false);
		dus.processExcelUpload(dp);
	}
	
	private void validate(DocumentOutput docOutput, String[][] refData) {
		List<Map<String,String>> data = docOutput.getData();
		for (int i=0; i<data.size(); i++) {
			Map<String,String> row = data.get(i);
			int colIndex = 0;
			Iterator<String> itr = row.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				String value = row.get(key);
				String refValue = refData[i][colIndex];
				if (refValue == null) {
					assertNull(value);	
				} else {
					assertTrue(refValue.equalsIgnoreCase(value));
				}
				colIndex++;
			}
		}
	}
	
	private String[][] createReferenceData4SampleExcelPits() {
		String[][] refData = new String[6][3];
		refData[0][0] = "beta release begin september";
		refData[0][1] = "target is nu begin december. On going";
		refData[0][2] = "01-dec-2008";
	
		refData[1][0] = "QAFE Release 2";
		refData[1][1] = null;
		refData[1][2] = "Mei 2009";
		
		refData[2][0] = "QAFE Release 3";
		refData[2][1] = null;
		refData[2][2] = "Sept-2009";
		
		refData[3][0] = "QAFE Release 4";
		refData[3][1] = null;
		refData[3][2] = "Dec 2009";
		
		refData[4][0] = "Dynamic WebService Creation for External parties";
		refData[4][1] = "Analyse moet nog beginnen";
		refData[4][2] = "Sept-2009";
		
		refData[5][0] = "Geavanceerde tooling";
		refData[5][1] = "Volgende generatie tooling voor QAFE";
		refData[5][2] = "Nov 2009";
		return refData;
	}
	
	private byte[] readFile(String fileName){
		File f = new File(fileName);
		try {
			return FileUtils.readFileToByteArray(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	DocumentParameter getDocumentParameter(String file){
		byte[] bytes = readFile(file);
		DocumentParameter dp = new DocumentParameter();
		dp.setData(bytes);
		return dp;
	}
}
