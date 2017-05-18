/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.qualogy.qafe.mgwt.client.main.MainController;

public class QAFEMGwtEntryPoint implements EntryPoint {
	
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				Window.alert("uncaught: " + e.getMessage());
				String s = buildStackTrace(e, "RuntimeException:\n");
				Window.alert(s);
				e.printStackTrace();

			}
		});

		new Timer() {
			@Override
			public void run() {
				initialize();
			}
		}.schedule(1);
		alertSomeStuff();
	}
	
	private void initialize(){
		MainController.getInstance().initialize();
	}

	private native void alertSomeStuff()/*-{
//		$doc.addEventListener("scroll", (function() {
//			alert('scroll');
//		}), true);
	}-*/;

	private String buildStackTrace(Throwable t, String log) {
		if (t != null) {
			log += t.getClass().toString();
			log += t.getMessage();
			StackTraceElement[] stackTrace = t.getStackTrace();
			if (stackTrace != null) {
				StringBuffer trace = new StringBuffer();
				for (int i = 0; i < stackTrace.length; i++) {
					trace.append(stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + "(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber());
				}
				log += trace.toString();
			}
			Throwable cause = t.getCause();
			if (cause != null && cause != t) {
				log += buildStackTrace(cause, "CausedBy:\n");
			}
		}
		return log;
	}
}
