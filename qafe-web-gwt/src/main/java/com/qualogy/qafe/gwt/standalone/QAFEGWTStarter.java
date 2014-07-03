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
package com.qualogy.qafe.gwt.standalone;

import java.io.FileInputStream;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

public class QAFEGWTStarter {

	/**
	 * Start jetty
	 **/

	public void start(String filename) throws Exception {
		Server server = new Server();

		XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(filename));
		configuration.configure(server);
		server.start();
	}

	public static void main(String[] arg) throws Exception {
		new QAFEGWTStarter().start(arg[0]);
	}
}
