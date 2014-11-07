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
package com.qualogy.qafe.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.qualogy.qafe.service.domain.DocumentExportInput;

public class DocumentExporter {

	public static void exportCVS(DocumentExportInput input, ByteArrayOutputStream bout) {
		if (input.getData() != null) {
			if (bout != null) {
				StringBuffer buffer = new StringBuffer();
				
				List<String> headerColumns = input.getColumnSequence();
				if (headerColumns == null) {
					headerColumns = getHeaderColumns(input.getData());
				}
				if (headerColumns != null) {
					if (input.isGenerateColumnHeader()) {
						for (int i=0; i<headerColumns.size(); i++) {
							String columnHeader = headerColumns.get(i);
							buffer.append(columnHeader);
							buffer.append(',');
						}
						buffer.append("\n");
					}
					
					for (Map<String, Object> map : input.getData()) {
						for (String key : headerColumns) {
							if (map.containsKey(key)) {
								buffer.append(map.get(key));
								buffer.append(',');
							}
						}
						buffer.append("\n");
					}
				}

				try {
					bout.write(buffer.toString().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void exportExcel(DocumentExportInput input, ByteArrayOutputStream bout) {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("Exported data");
		if (input.getData() != null) {
			if (bout != null) {
				short index = 0;
				
				List<String> headerColumns = input.getColumnSequence();
				if (headerColumns == null) {
					headerColumns = getHeaderColumns(input.getData());
				}
				if (headerColumns != null) {
					if (input.isGenerateColumnHeader()) {
						Row row = sheet.createRow(index++);
						for (int i=0; i<headerColumns.size(); i++) {
							String columnHeader = headerColumns.get(i);
							Cell cell = row.createCell(i);
							cell.setCellValue(columnHeader);
						}	
					}
					
					for (Map<String, Object> map : input.getData()) {
						Row row = sheet.createRow(index++);
						for (int i=0; i<headerColumns.size(); i++) {
							String key = headerColumns.get(i);
							Object value = map.get(key);
							Cell cell = row.createCell(i);
							setCellValue(cell, value);
						}
					}
				}
			}
		}

		try {

			wb.write(bout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void exportPDF(DocumentExportInput input, ByteArrayOutputStream bout) {
		exportPDFOrRTF(input, bout, true);
	}

	public static void exportPDFOrRTF(DocumentExportInput input, ByteArrayOutputStream bout, boolean pdf) {
		ByteArrayOutputStream xmlBout = new ByteArrayOutputStream();
		exportXML(input, xmlBout);

		ByteArrayInputStream xml = new ByteArrayInputStream(xmlBout.toByteArray());

		// configure fopFactory as desired
		FopFactory fopFactory = FopFactory.newInstance();
		fopFactory.setStrictValidation(false);

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// configure foUserAgent as desired

		// Setup output
		OutputStream out = new BufferedOutputStream(bout);

		// Construct fop with desired output format
		Fop fop;
		try {
			if (pdf){
				fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
			} else {
				fop = fopFactory.newFop(MimeConstants.MIME_RTF, foUserAgent, out);
			}

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();

			InputStream inputStream = DocumentExporter.class.getResourceAsStream("/exportPDF.xsl");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int len;
			while((len = inputStream.read(buffer)) >= 0){
				outputStream.write(buffer, 0, len);
			}
			outputStream.close();			
			byte[] xslByte = outputStream.toByteArray();
			
			String xslStringData = new String(xslByte);
			/*if (input.getHeader()!=null && input.getHeader().length()>0){
				xslStringData = xslStringData.replace("##TITLE##",input.getHeader());
			}*/
			
			InputStream xsltStream = new ByteArrayInputStream(xslStringData.getBytes());
			Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

			// Set the value of a <param> in the stylesheet
			transformer.setParameter("versionParam", "2.0");

			// Setup input for XSLT transformation
			Source src = new StreamSource(xml);

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			
			transformer.setParameter("generateColumnHeader", Boolean.toString(input.isGenerateColumnHeader()));
			
			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
			out.close();
			xsltStream.close();

		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Deprecated
	public static void exportRTF(DocumentExportInput input, ByteArrayOutputStream bout) {
		exportPDFOrRTF(input, bout, false);
	}

	public static void exportXML(DocumentExportInput input, ByteArrayOutputStream bout) {
		if (input.getData() != null) {
			if (bout != null) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				buffer.append("<RESULTS>\n");
								
				List<String> headerColumns = input.getColumnSequence();
				if (headerColumns == null) {
					headerColumns = getHeaderColumns(input.getData());
				}
				if (headerColumns != null) {
					for (Map<String, Object> map : input.getData()) {
						buffer.append("<ROW>\n");
						for (int i=0; i<headerColumns.size(); i++) {
							String key = headerColumns.get(i);
							Object value = map.get(key);
							buffer.append("<COLUMN ID=\"" + key + "\">");
							buffer.append("<![CDATA[");
							buffer.append(value != null ? value.toString().trim() : "");
							buffer.append("]]>");
							buffer.append("</COLUMN>\n");
						}
						buffer.append("</ROW>\n");
					}	
				}
				buffer.append("</RESULTS>\n");
				try {
					bout.write(buffer.toString().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/*public static void exportXMLForFop(DocumentExportInput input, ByteArrayOutputStream bout) {
		if (input.getData() != null) {
			if (bout != null) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				buffer.append("<RESULTS>\n");
				for (Map<String, Object> map : input.getData()) {
					buffer.append("<ROW>\n");
					for (String key : map.keySet()) {
						buffer.append("<" + key + ">");
						buffer.append("<![CDATA[");
						buffer.append(map.get(key) != null ? map.get(key).toString().trim() : "");
						buffer.append("]]>");
						buffer.append("</" + key + ">\n");
					}
					buffer.append("</ROW>\n");
				}
				buffer.append("</RESULTS>\n");
				try {
					bout.write(buffer.toString().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}*/
	
	
	protected static List<String> getHeaderColumns(Object data) {
		List<String> headerColumns = null;
		if (data instanceof Map) {
			Map map = (Map)data;
			headerColumns = new ArrayList<String>();
			Iterator<String> itr = map.keySet().iterator();
			while(itr.hasNext()) {
				String field = itr.next();
				headerColumns.add(field);
			}
		} else if (data instanceof List) {
			List list = (List)data;
			if (!list.isEmpty()) {
				Object entry = list.get(0);
				headerColumns = getHeaderColumns(entry);
			}
		}
		return headerColumns;
	}
	
	protected static void setCellValue(Cell cell, Object value) {
		if (cell == null) {
			return;
		}
		if (value instanceof Boolean) {
			cell.setCellValue((Boolean)value);		
		} else if (value instanceof Number) {
			cell.setCellValue(((Number)value).doubleValue());
		} else if (value instanceof Date) {
			cell.setCellValue(((Date)value));
		} else if (value instanceof Calendar) {
			cell.setCellValue(((Calendar)value));
		} else if (value != null) {
			cell.setCellValue(value.toString());
		} else {
			cell.setCellValue("");
			cell.setCellType(Cell.CELL_TYPE_BLANK);
		}
	}
}