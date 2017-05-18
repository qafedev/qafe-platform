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
package com.qualogy.qafe.gwt.client.component.html5.notifications;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NotificationImpl {
	public static final int PERMISSION_ALLOWED = 0;
	public static final int PERMISSION_NOT_ALLOWED = 1;
	public static final int PERMISSION_DENIED = 2;
	private JavaScriptObject jsObject;

	protected NotificationImpl() {
		
	}
	
	public native int checkPermission() /*-{
		return $wnd.webkitNotifications.checkPermission();
	}-*/;
	
	public void requestPermission(AsyncCallback<Void> callback) {
		this.requestPermission(this, callback);
	}

	private native void requestPermission(NotificationImpl x, AsyncCallback<Void> callback) /*-{
		$wnd.webkitNotifications.requestPermission($entry(function() {
			x.@com.qualogy.qafe.gwt.client.component.html5.notifications.NotificationImpl::callbackRequestPermission(Lcom/google/gwt/user/client/rpc/AsyncCallback;)(callback);
		}));
	}-*/;

	private void callbackRequestPermission(AsyncCallback<Void> callback) {
		if (callback != null) {
			callback.onSuccess(null);
		}
	}

	public void createNotification(String iconUrl, String title, String body) {
		this.jsObject = null;
		this.jsObject = this.createJSNotification(iconUrl, title, body);
		show();
	}

	public void createNotification(String contentUrl) {
		this.jsObject = null;
		this.jsObject = this.createHtmlNotification(contentUrl);
		show();
	}
	
	private native JavaScriptObject createJSNotification(String iconUrl, String title, String body) /*-{
		return $wnd.webkitNotifications.createNotification(iconUrl, title, body);
	}-*/;
	
	private native JavaScriptObject createHtmlNotification(String contentUrl) /*-{
		return $wnd.webkitNotifications.createHTMLNotification(contentUrl);
	}-*/;
	
	public native void show() /*-{
		var obj = this.@com.qualogy.qafe.gwt.client.component.html5.notifications.NotificationImpl::jsObject;		
		obj.show();
	}-*/;
	
}
