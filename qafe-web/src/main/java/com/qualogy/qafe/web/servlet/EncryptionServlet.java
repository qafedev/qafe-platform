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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qualogy.qafe.web.EncryptionService;

public class EncryptionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String DATA = "data";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		 String data = request.getParameter(DATA);
		 try {
			 EncryptionService encryptionService = new EncryptionService();
			 String encryptedData = encryptionService.encrypt(data);
			 response.getWriter().write("Encrypted: " + encryptedData);
		} catch (Exception e) {
			response.getWriter().write(e.getMessage());
		}
	}
}