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
package com.qualogy.qafe.web.servlet;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qualogy.qafe.web.UploadService;


public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
     * Receives the POST request. The request contains form fields,
     * including an uploaded file. 
     * 
     * @param request
     * @param response
     * @throws ServletException
     */
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		 UploadService uploadService = new UploadService();
		 String resultMessage = uploadService.uploadFile(request);
		 try {
			 PrintWriter output = response.getWriter();
			 response.setContentType("text/html");
			 output.print(resultMessage);
			 output.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Map paramMap = request.getParameterMap();
			String appUUID = (String) request.getParameter(UploadService.APP_UUID);
			String windowId = (String) request.getParameter(UploadService.APP_WINDOWID);
			UploadService uploadService = new UploadService();
			if (paramMap.containsKey(UploadService.ACTION_VIEW)) {
				String uploadUUID = (String) request.getParameter(UploadService.ACTION_VIEW);
				Map uploadedFile = uploadService.getUploadedFile(appUUID, windowId, uploadUUID);
				if (uploadedFile != null) {
					try {
						byte[] fileContent = (byte[]) uploadedFile.get(UploadService.FILE_CONTENT);
						String fileName = (String) uploadedFile.get(UploadService.FILE_NAME);
						String contentType = (String) uploadedFile.get(UploadService.FILE_MIME_TYPE);
						
						response.setContentType(contentType);
						response.setContentLength(fileContent.length);
						response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
						OutputStream out = response.getOutputStream();
						out.write(fileContent);
						out.flush();
						out.close();	
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					PrintWriter output = response.getWriter();
					output.print("File not found");
					output.close();
				}
			} else if (paramMap.containsKey(UploadService.ACTION_REMOVE)) {
				String uploadUUID = (String) request.getParameter(UploadService.ACTION_REMOVE);
				String resultMessage = uploadService.removeUploadedFile(appUUID, windowId, uploadUUID);
				PrintWriter output = response.getWriter();
				output.print(resultMessage);
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
