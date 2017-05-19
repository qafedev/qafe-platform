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
package com.qualogy.qafe.bind.resource;

import com.qualogy.qafe.util.QAFECrypt;

public class DriverManagerResource extends DatasourceBindResource{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 543088812648311863L;
	
	protected String url;
	protected String username;
	protected String password;
	protected boolean encryptedPassword;
	protected String driverClassName;
	
	public String getPassword() {
		return getPassword(true);
	}
	public String getPassword(boolean plain) {
		if (plain && isEncryptedPassword()) {
			return QAFECrypt.decrypt(password);
		}
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public boolean isEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(boolean encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}	
}