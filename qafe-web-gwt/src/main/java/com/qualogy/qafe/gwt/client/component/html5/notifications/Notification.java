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

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class Notification {
	/**
	 * Detector for browser support of Desktop Notification.
	 */
	private static class NotificationSupportDetector {
		private final boolean isNotificationSupported = detectNotificationSupport();
		
		public boolean isNotificationSupported() {
			return this.isNotificationSupported;
		}
		
		private native boolean detectNotificationSupport() /*-{
			if (!$wnd.webkitNotifications) {
				return false;
			} else {
				return true;
			}
		}-*/;
	}
	
	/**
	 * Detector for browser support of Desktop Notification.
	 */
	@SuppressWarnings("unused")
	private static class NotificationSupportDetectorNo extends NotificationSupportDetector {
		@Override
		public boolean isNotificationSupported() {
			return false;
		}
	}
	
	private static final NotificationImpl impl = GWT.create(NotificationImpl.class);
	
	private static int permission = -1;

	private static NotificationSupportDetector supportDetectorImpl;

	private static Notification notification;
	
	/**
	 * Check current status of notification is allowed or not
	 * 
	 * @return true if user allow to use notification
	 */
	public static boolean isNotificationAllowed() {
		checkPermission();
		return permission == NotificationImpl.PERMISSION_ALLOWED;
	}
	
	/**
	 * Check current status of notification is set or not
	 * 
	 * @return true if user doesn't set permission (never choose 'Allow' or 'Deny')
	 */
	public static boolean isNotificationNotAllowed() {
		checkPermission();
		return permission == NotificationImpl.PERMISSION_NOT_ALLOWED;
	}
	
	/**
	 * Check current status of notification is denied or not
	 * 
	 * @return true if user deny to use notification
	 */
	public static boolean isNotificationDenied() {
		checkPermission();
		return permission == NotificationImpl.PERMISSION_DENIED;
	}
	
	/**
	 * Get current status of notification permission
	 * @return
	 */
	public static int checkPermission() {
		if (permission == -1) {
			permission = impl.checkPermission();
		}
		return permission;
	}
	
	public static void requestPermission() {
		impl.requestPermission(null);
	}
	
	public static void requestPermission(AsyncCallback<Void> callback) {
		impl.requestPermission(callback);
	}
	
	public static boolean isSupported() {
		return getNotificationSupportDetector().isNotificationSupported();
	}
	
	private static NotificationSupportDetector getNotificationSupportDetector() {
		if (supportDetectorImpl == null) {
			supportDetectorImpl = GWT.create(NotificationSupportDetector.class);
		}
		return supportDetectorImpl;
	}
	
	public static Notification createIfSupported(String contentUrl) {
		if (isSupported()) {
			impl.createNotification(contentUrl);
		}
		return null;
	}
	
	public static Notification createIfSupported(String contentUrl,String title, String message) {
		if (isSupported()) {
			impl.createNotification(contentUrl, title, message);
		}
		return null;
	}

	private String contentUrl;
	private String iconUrl;
	private String title;
	private String body;
	
	private Notification(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	private Notification(String iconUrl, String title, String body) {
		this.contentUrl = null;
		this.iconUrl = iconUrl;
		this.title = title;
		this.body = body;
	}

}
