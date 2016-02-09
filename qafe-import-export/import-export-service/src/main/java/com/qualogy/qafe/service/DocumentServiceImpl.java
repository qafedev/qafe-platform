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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;

import com.qualogy.qafe.core.datastore.DataToLogStringBuilder;
import com.qualogy.qafe.service.domain.DocumentExportInput;
import com.qualogy.qafe.service.domain.DocumentExportOutput;
import com.qualogy.qafe.service.domain.DocumentOutput;
import com.qualogy.qafe.service.domain.DocumentParameter;
import com.qualogy.qafe.util.UUIDHelper;

public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOG = Logger.getLogger(DocumentServiceImpl.class.getName());

    public static String DEFAULT_FIELD_NAME = "FIELD";

    public static int EMPTY_NUMCOLUMNS_TOLERANCE = 10;

    public DocumentOutput processExcelUpload(DocumentParameter parameter) {
        DocumentOutput out = null;
        try {
            out = handleExcel2003(parameter);
        } catch (OfficeXmlFileException e) {
            out = handleExcel2007(parameter);
        } catch (IOException e) {
            if (canHandleCSV(e)) {
                out = handleCSV(parameter);
            } else {
                LOG.log(Level.FINE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        // store the sequence of the columns,
        // this information will lose when calling through the webservice
        out.setColumnSequence(getSequence(out.getData()));
        return out;
    }

    private boolean canHandleCSV(Exception e) {
        if (e != null) {
            if (e.getMessage().contains("Invalid header signature;")) {
                return true;
            } else if (e.getMessage().contains("Unable to read entire header;")) {
                // When plain text files are smaller than 32 bytes,
                // an exception will be thrown in the HeaderBlockReader class (calling from the constructor of
                // POIFSFileSystem):
                // Exception: "Unable to read entire header; <number> bytes read; expected 32 bytes"
                return true;
            }
        }
        return false;
    }

    private DocumentOutput handleCSV(DocumentParameter parameter) {
        DocumentOutput out = null;
        String uuid = UUIDHelper.generateUUID();
        try {
            CSVReader reader = null;
            if (parameter.getDelimiter() != null) {
                char separator =
                    parameter.getDelimiter().length() > 0 ? parameter.getDelimiter().charAt(0) : ',';
                reader =
                    new CSVReader(new InputStreamReader(new ByteArrayInputStream(parameter.getData())),
                            separator);
            } else {
                reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(parameter.getData())));
            }
            List<String[]> sheetData = reader.readAll();
            out = handleCSVData(sheetData, parameter.isFirstFieldHeader());
        } catch (Exception e) {
            LOG.log(Level.FINE, e.getMessage(), e);
            out = new DocumentOutput();
        }
        out.setUuid(uuid);
        return out;
    }

    // private DocumentOutput handleCSV(DocumentParameter parameter) {
    // DocumentOutput out = new DocumentOutput();
    // String uuid = null;
    // CSVReader reader;
    // // Apparently a CSV file
    // if (parameter.getDelimiter() != null) {
    // char separator = parameter.getDelimiter().length() > 0 ? parameter.getDelimiter().charAt(0) : ',';
    // reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(parameter.getData())),
    // separator);
    // } else {
    // reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(parameter.getData())));
    // }
    // try {
    // List<String[]> entries = reader.readAll();
    // List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    //
    // List<String> keys = new ArrayList<String>();
    // if (parameter.isFirstFieldHeader()) {
    // if (entries.iterator().hasNext()) {
    // String[] row = entries.iterator().next();
    // for (int i = 0; i < row.length; i++) {
    // String cell = row[i];
    // if (cell != null && cell.trim().length() > 0) {
    // keys.add(cell);
    // }
    // }
    // }
    // } else { // we define fields for the data:
    // if (entries.iterator().hasNext()) {
    // String[] row = entries.iterator().next();
    // int index = 0;
    // for (int i = 0; i < row.length; i++) {
    // String cell = row[i];
    // if (cell != null && cell.trim().length() > 0) {
    // keys.add(cell.trim());
    // }
    // }
    // }
    // }
    //
    // boolean readFirstRow = false;
    // Iterator<String[]> rit = entries.iterator();
    // while (rit.hasNext()) {
    // String[] row = rit.next();
    // if (parameter.isFirstFieldHeader() && !readFirstRow) {
    // readFirstRow = true;
    //
    // } else {
    // int index = 0;
    // Map<String, String> dataMap = new TreeMap<String, String>();
    // boolean rowWithData = false;
    // int i = 0;
    // for (String key : keys) {
    // String cell = row[i];
    //
    // if (cell != null && cell.trim().length() > 0) {
    // rowWithData = true;
    //
    // }
    // dataMap.put(keys.get(index), cell != null ? cell.trim() : cell);
    // index++;
    // i++;
    // }
    //
    // if (rowWithData) {
    // data.add(dataMap);
    // }
    // }
    // }
    // uuid = UUIDHelper.generateUUID();
    // printData(data);
    // out.setData(data);
    // out.setUuid(uuid);
    //
    // } catch (IOException e1) {
    // logger.debug(e1.getMessage(), e1);
    // }
    // out.setUuid(uuid);
    // return out;
    //
    // }

    private DocumentOutput handleExcel2007(DocumentParameter parameter) {
        DocumentOutput out = null;
        String uuid = UUIDHelper.generateUUID();
        try {
            XSSFWorkbook workBook = new XSSFWorkbook(new ByteArrayInputStream(parameter.getData()));
            XSSFSheet sheet = workBook.getSheetAt(0);
            out = handleExcelData(sheet, parameter.isFirstFieldHeader());
        } catch (Exception e) {
            LOG.log(Level.FINE, e.getMessage(), e);
            out = new DocumentOutput();
        }
        out.setUuid(uuid);
        return out;
    }

    // private DocumentOutput handleExcel2007(DocumentParameter parameter) {
    // DocumentOutput out = new DocumentOutput();
    // String uuid = null;
    // XSSFWorkbook workBook;
    // try {
    // workBook = new XSSFWorkbook(new ByteArrayInputStream(parameter.getData()));
    // uuid = UUIDHelper.generateUUID();
    // XSSFSheet sheet = workBook.getSheetAt(0);
    // List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    //
    // List<String> keys = new ArrayList<String>();
    // if (parameter.isFirstFieldHeader()) {
    // if (sheet.rowIterator().hasNext()) {
    // Row row = sheet.rowIterator().next();
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // keys.add(getCellValue(cell));
    // }
    // }
    // }
    // } else { // we define fields for the data:
    // if (sheet.rowIterator().hasNext()) {
    // Row row = sheet.rowIterator().next();
    // int index = 0;
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // keys.add("FIELD" + index);
    // index++;
    // }
    // }
    // }
    // }
    //
    // boolean readFirstRow = false;
    // for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
    // Row row = rit.next();
    // if (parameter.isFirstFieldHeader() && !readFirstRow) {
    // readFirstRow = true;
    //
    // } else {
    // int index = 0;
    // Map<String, String> dataMap = new TreeMap<String, String>();
    // boolean rowWithData = false;
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // String value = getCellValue(cell);
    // if (value != null || value.length() > 0) {
    // rowWithData = true;
    // }
    // dataMap.put(keys.get(index), value);
    // index++;
    // }
    // }
    // if (rowWithData) {
    // data.add(dataMap);
    // }
    // }
    // }
    // printData(data);
    // out.setData(data);
    // out.setUuid(uuid);
    //
    // } catch (IOException e1) {
    // // TODO Auto-generated catch block
    // logger.debug(e1.getMessage(), e1);
    // }
    // out.setUuid(uuid);
    // return out;
    // }

    private DocumentOutput handleExcel2003(DocumentParameter parameter) throws IOException {
        DocumentOutput out = null;
        String uuid = UUIDHelper.generateUUID();
        POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(parameter.getData()));
        Workbook workbook = WorkbookFactory.create(fs);
        Sheet sheet = workbook.getSheetAt(0);
        out = handleExcelData(sheet, parameter.isFirstFieldHeader());
        out.setUuid(uuid);
        return out;
    }

    // private DocumentOutput handleExcel2003(DocumentParameter parameter) throws IOException {
    // DocumentOutput out = new DocumentOutput();
    // String uuid = null;
    // POIFSFileSystem fs;
    //
    // fs = new POIFSFileSystem(new ByteArrayInputStream(parameter.getData()));
    // uuid = UUIDHelper.generateUUID();
    // Workbook wb = WorkbookFactory.create(fs);
    // Sheet sheet = wb.getSheetAt(0);
    // List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    //
    // List<String> keys = new ArrayList<String>();
    // if (parameter.isFirstFieldHeader()) {
    // if (sheet.rowIterator().hasNext()) {
    // Row row = sheet.rowIterator().next();
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // keys.add(getCellValue(cell));
    // }
    // }
    // }
    // } else { // we define fields for the data:
    // if (sheet.rowIterator().hasNext()) {
    // Row row = sheet.rowIterator().next();
    // int index = 0;
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // keys.add("FIELD" + index);
    // index++;
    // }
    // }
    // }
    // }
    //
    // boolean readFirstRow = false;
    // for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
    // Row row = rit.next();
    // if (parameter.isFirstFieldHeader() && !readFirstRow) {
    // readFirstRow = true;
    //
    // } else {
    // int index = 0;
    // Map<String, String> dataMap = new TreeMap<String, String>();
    // boolean rowWithData = false;
    // for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
    // Cell cell = cit.next();
    // if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
    // String value = getCellValue(cell);
    // if (value != null && value.length() > 0) {
    // rowWithData = true;
    // }
    // dataMap.put(keys.get(index), value);
    // index++;
    // }
    // }
    // if (rowWithData) {
    // data.add(dataMap);
    // }
    // }
    // }
    // printData(data);
    // out.setData(data);
    // out.setUuid(uuid);
    // return out;
    //
    // }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    private DocumentOutput handleExcelData(Sheet sheetData, boolean hasRowHeader) {
        DocumentOutput docOutput = new DocumentOutput();

        // Determine the column names
        List<String> columnNameList = new ArrayList<String>();
        if (sheetData.rowIterator().hasNext()) {
            Row row = sheetData.rowIterator().next();
            int emptyColCountChain = 0;
            String colName = null;
            for (Iterator<Cell> itr = row.cellIterator(); itr.hasNext();) {
                Cell cell = itr.next();
                boolean cellHasData = (cell.getCellType() != Cell.CELL_TYPE_BLANK);
                if (hasRowHeader && cellHasData) {
                    colName = getCellValue(cell);
                } else {
                    colName = DEFAULT_FIELD_NAME + cell.getColumnIndex();
                }
                columnNameList.add(colName);

                if (cellHasData) {
                    emptyColCountChain = 0;
                } else {
                    emptyColCountChain++;
                }
                if (emptyColCountChain > EMPTY_NUMCOLUMNS_TOLERANCE) {
                    break;
                }
            }
        }

        // Get the data from sheet
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        boolean[] columnsHaveData = new boolean[columnNameList.size()];
        for (Iterator<Row> itr = sheetData.rowIterator(); itr.hasNext();) {
            Row row = itr.next();
            if (hasRowHeader && (row.getRowNum() == 0)) {
                continue;
            }
            Map<String, String> rowData = new LinkedHashMap<String, String>();
            boolean rowHasData = false;
            for (Iterator<Cell> itr2 = row.cellIterator(); itr2.hasNext();) {
                Cell cell = itr2.next();
                if (cell.getColumnIndex() < columnNameList.size()) {
                    String colName = columnNameList.get(cell.getColumnIndex());
                    String cellValue = null;
                    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                        cellValue = getCellValue(cell);
                    }
                    boolean cellHasData = ((cellValue != null) && (cellValue.length() > 0));
                    columnsHaveData[cell.getColumnIndex()] =
                        columnsHaveData[cell.getColumnIndex()] || cellHasData;
                    rowHasData = rowHasData || cellHasData;
                    rowData.put(colName, cellValue);
                } else {
                    break;
                }
            }
            if (rowHasData) {
                data.add(rowData);
            }
        }

        removeEmptyColumns(columnNameList, data, columnsHaveData);

        printData(data);
        docOutput.setData(data);
        return docOutput;
    }

    private DocumentOutput handleCSVData(List<String[]> sheetData, boolean hasRowHeader) {
        DocumentOutput docOutput = new DocumentOutput();

        // Determine the column names
        List<String> columnNameList = new ArrayList<String>();
        if (sheetData.iterator().hasNext()) {
            String[] row = sheetData.iterator().next();
            int emptyColCountChain = 0;
            String colName = null;
            for (int i = 0; i < row.length; i++) {
                String cell = row[i];
                boolean cellHasData = (cell != null) && (cell.trim().length() > 0);
                if (hasRowHeader && cellHasData) {
                    colName = cell;
                } else {
                    colName = DEFAULT_FIELD_NAME + i;
                }
                columnNameList.add(colName);

                if (cellHasData) {
                    emptyColCountChain = 0;
                } else {
                    emptyColCountChain++;
                }
                if (emptyColCountChain > EMPTY_NUMCOLUMNS_TOLERANCE) {
                    break;
                }
            }
        }

        // Get the data
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        boolean[] columnsHaveData = new boolean[columnNameList.size()];
        int rowIndex = 0;
        for (Iterator<String[]> itr = sheetData.iterator(); itr.hasNext();) {
            String[] row = itr.next();
            if (!hasRowHeader || (rowIndex != 0)) {
                Map<String, String> rowData = new LinkedHashMap<String, String>();
                boolean rowHasData = false;
                for (int i = 0; i < row.length; i++) {
                    if (i < columnNameList.size()) {
                        String colName = columnNameList.get(i);
                        String cell = row[i];
                        boolean cellHasData = (cell != null) && (cell.trim().length() > 0);
                        columnsHaveData[i] = columnsHaveData[i] || cellHasData;
                        rowHasData = rowHasData || cellHasData;
                        rowData.put(colName, cell);
                    } else {
                        break;
                    }
                }
                if (rowHasData) {
                    data.add(rowData);
                }
            }
            rowIndex++;
        }

        removeEmptyColumns(columnNameList, data, columnsHaveData);

        printData(data);
        docOutput.setData(data);
        return docOutput;
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity

    private void removeEmptyColumns(List<String> columnNameList, List<Map<String, String>> data,
            boolean[] columnsHaveData) {
        for (int i = 0; i < columnsHaveData.length; i++) {
            if (!columnsHaveData[i]) {
                String colName = columnNameList.get(i);
                for (int j = 0; j < data.size(); j++) {
                    Map<String, String> rowData = data.get(j);
                    rowData.remove(colName);
                }
            }
        }
    }

    private void printData(List<Map<String, String>> data) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> elements = new HashMap<String, Object>();
        elements.put("excel_import", data);
        builder.append("Structure of data:\n");
        DataToLogStringBuilder.build(elements, builder);
        LOG.info(builder.toString());
    }

    String getCellValue(Cell cell) {
        String value = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_STRING:
                value = cell.toString();
                break;
            // case Cell.CELL_TYPE_NUMERIC:
            // value = cell.getNumericCellValue() + "";
            // break;
            // case Cell.CELL_TYPE_STRING:
            // value = cell.getStringCellValue();
            // break;
            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_BLANK:
                ;
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_ERROR:
                value = cell.getErrorCellValue() + "";
                break;
        }
        return value != null ? value.trim() : value;
    }

    public DocumentExportOutput export(DocumentExportInput input) {
        DocumentExportOutput de = new DocumentExportOutput();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        de.setFileName("export-file-" + sdf.format(new Date()) + "."
                + getFileNameExtension(input.getExportCode()));
        de.setMimeType(getMimeType(input.getExportCode()));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        if (EXPORT_CSV.equals(input.getExportCode())) {
            DocumentExporter.exportCVS(input, bout);
        } else if (EXPORT_EXCEL.equals(input.getExportCode())) {
            DocumentExporter.exportExcel(input, bout);
        } else if (EXPORT_PDF.equals(input.getExportCode())) {
            DocumentExporter.exportPDF(input, bout);
        } else if (EXPORT_RTF.equals(input.getExportCode())) {
            DocumentExporter.exportRTF(input, bout);
        } else if (EXPORT_XML.equals(input.getExportCode())) {
            DocumentExporter.exportXML(input, bout);
        }
        de.setBytes(bout.toByteArray());
        return de;

    }

    private String getFileNameExtension(String exportCode) {
        String extension = "txt";
        if (EXPORT_CSV.equals(exportCode)) {
            extension = "txt";
        } else if (EXPORT_EXCEL.equals(exportCode)) {
            extension = "xls";
        } else if (EXPORT_PDF.equals(exportCode)) {
            extension = "pdf";
        } else if (EXPORT_RTF.equals(exportCode)) {
            extension = "rtf";
        } else if (EXPORT_XML.equals(exportCode)) {
            extension = "xml";
        }
        return extension;
    }

    private String getMimeType(String exportCode) {
        String mimeType = "application/octet-stream";
        if (EXPORT_CSV.equals(exportCode)) {

        } else if (EXPORT_EXCEL.equals(exportCode)) {

        } else if (EXPORT_PDF.equals(exportCode)) {

        } else if (EXPORT_RTF.equals(exportCode)) {

        } else if (EXPORT_XML.equals(exportCode)) {

        }
        return mimeType;
    }

    private List<String> getSequence(Object data) {
        List<String> sequence = null;
        if (data instanceof Map) {
            Map map = (Map) data;
            sequence = new ArrayList<String>();
            Iterator<String> itr = map.keySet().iterator();
            while (itr.hasNext()) {
                String field = itr.next();
                sequence.add(field);
            }
        } else if (data instanceof List) {
            List list = (List) data;
            if (!list.isEmpty()) {
                Object entry = list.get(0);
                sequence = getSequence(entry);
            }
        }
        return sequence;
    }
}
