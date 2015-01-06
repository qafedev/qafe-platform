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

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qualogy.qafe.service.DocumentService;
import com.qualogy.qafe.service.DocumentServiceImpl;
import com.qualogy.qafe.service.domain.DocumentExportInput;
import com.qualogy.qafe.service.domain.DocumentExportOutput;
import com.qualogy.qafe.web.util.DatagridStorageHelper;

public class DatagridDownloadServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(DatagridDownloadServlet.class.getName());

    public static final String FORM_PARAMETER_DELIMITER = "delimeter";

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        writeUploadInfo(request);
        String uuid = request.getParameter("uuid");
        if (uuid != null) {
            List<Map<String, Object>> list = DatagridStorageHelper.getData(uuid);
            String exportCode = DatagridStorageHelper.getExportType(uuid);
            String header = DatagridStorageHelper.getHeader(uuid);
            boolean generateColumnHeader = DatagridStorageHelper.isGenerateColumnHeader(uuid);
            DocumentExportInput input = new DocumentExportInput();
            // String exportPDFLocation =
            // ApplicationCluster.getInstance().getConfigurationItem("reports.export.default");
            // input.setConfigurationLocation(request.getSession().getServletContext().getRealPath("/WEB-INF/"));

            input.setData(list);
            input.setExportCode(exportCode);
            input.setHeader(header);
            input.setGenerateColumnHeader(generateColumnHeader);
            DocumentExportOutput de = documentService.export(input);
            if (de != null) {
                response.setHeader("Content-Disposition", "inline; filename=" + de.getFileName());
                response.setContentType(de.getMimeType());
                response.setContentLength(de.getBytes().length);
                response.getOutputStream().write(de.getBytes());

            }
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        super.init(config);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -5162541603503766940L;

}
