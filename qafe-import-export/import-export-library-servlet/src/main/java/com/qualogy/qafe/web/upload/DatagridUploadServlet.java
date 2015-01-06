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
package com.qualogy.qafe.web.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.service.DocumentService;
import com.qualogy.qafe.service.DocumentServiceImpl;
import com.qualogy.qafe.service.domain.DocumentOutput;
import com.qualogy.qafe.service.domain.DocumentParameter;
import com.qualogy.qafe.util.ExceptionHelper;

public class DatagridUploadServlet extends HttpServlet {

    private static final long serialVersionUID = -5162541603503766940L;

    private static final Logger LOG = Logger.getLogger(DatagridUploadServlet.class.getName());

    public static final String FORM_PARAMETER_DELIMITER = "delimiter";

    public static final String FORM_PARAMETER_ISFIRSTLINEHEADER = "isFirstLineHeader";

    private DocumentService documentService = new DocumentServiceImpl();

    private void writeLog(String text) {
        log(text);
        LOG.info(text);
    }

    @SuppressWarnings("unchecked")
    private void writeUploadInfo(HttpServletRequest request) {
        writeLog("Document Upload!");

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            Object name = headerNames.nextElement();
            if (name != null) {
                writeLog("Header - " + name + " : " + request.getHeader((String) name));
            }
        }

        writeLog("ServletRemoteAddr: " + request.getRemoteAddr());
        writeLog("Remote Host: " + request.getRemoteHost());
        writeLog("Remote User: " + request.getRemoteUser());
        writeLog("Protocol: " + request.getProtocol());
        writeLog("Server Name: " + request.getServerName());
        writeLog("Server Port: " + request.getServerPort());
        writeLog("Request URL: " + request.getRequestURL());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        byte[] filecontent = null;
        ServletFileUpload upload = new ServletFileUpload();
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        boolean isFirstLineHeader = false;
        String delimiter = ",";

        writeUploadInfo(request);
        log(request.getHeader("User-Agent"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            FileItemIterator fileItemIterator = upload.getItemIterator(request);
            while (fileItemIterator.hasNext()) {
                FileItemStream item = fileItemIterator.next();
                inputStream = item.openStream();
                // Read the file into a byte array.
                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len = 0;
                while (-1 != (len = inputStream.read(buffer))) {
                    outputStream.write(buffer, 0, len);
                }
                if (filecontent == null) {
                    filecontent = outputStream.toByteArray();
                }
                if (FORM_PARAMETER_DELIMITER.equals(item.getFieldName())) {
                    delimiter = outputStream.toString();
                } else if (FORM_PARAMETER_ISFIRSTLINEHEADER.equals(item.getFieldName())) {
                    if ("on".equals(outputStream.toString())) {
                        isFirstLineHeader = true;
                    }
                }
            }
            inputStream.close();
            outputStream.close();
        } catch (FileUploadException e) {
            ExceptionHelper.printStackTrace(e);
        } catch (RuntimeException e) {
            out.print("Conversion failed. Please check the file. Message :" + e.getMessage());
        }

        DocumentParameter dp = new DocumentParameter();
        dp.setDelimiter(delimiter);
        dp.setFirstFieldHeader(isFirstLineHeader);
        dp.setData(filecontent);
        try {
            DocumentOutput dout = documentService.processExcelUpload(dp);
            String uploadUUID = DataStore.KEY_LOOKUP_DATA + dout.getUuid();
            ApplicationLocalStore.getInstance().store(uploadUUID, uploadUUID, dout.getData());
            out.print("UUID=" + uploadUUID);
        } catch (Exception e) {
            out.print("Conversion failed. Please check the file (" + e.getMessage() + ")");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

}
