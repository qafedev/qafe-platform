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
package com.qualogy.qafe.util;

import org.jasypt.util.text.BasicTextEncryptor;

public class QAFECrypt {

	private static final String KEY = "QAFE is started in 2007";
	
	private static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	
	static {
		textEncryptor.setPassword(KEY);
	}

	public static String encrypt(String data) {
		try {
			return textEncryptor.encrypt(data);	
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Encryption fails by: " + data, e);
		}
	}
	
	public static String decrypt(String data) {
		try {
			return textEncryptor.decrypt(data);	
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Decryption fails by: " + data + " - possible reason: The key used to encrypt the data is different", e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage: QAFECrypt <data>");
			return;
		}

		String data = args[0];
		String encrypted = QAFECrypt.encrypt(data);
		String decrypted = QAFECrypt.decrypt(encrypted);
		System.out.println("Encrypt: " + encrypted + " (" + decrypted + ")");
	}
}