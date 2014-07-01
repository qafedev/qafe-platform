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

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class Strings {
	public static final int DEFAULT_TRIM_SIZE = 35;
	public final static Logger logger = Logger.getLogger(Strings.class.getName());
	
	public static boolean isEmptyString(String value) {
        if ((value == null) || "".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }
	
	public static String getTrimmedString(String name) {
		return getTrimmedString(name, DEFAULT_TRIM_SIZE);
	}
	
	public static String getTrimmedString(String name, int trimSize) {
        if (name.length() > trimSize) {
            return name.substring(0, trimSize).concat("..");
        }
        return name;
    }
	
	public static long createChecksum(String uniqueString) {
		long value = 0;
		try {
			byte buffer[] = uniqueString.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			CheckedInputStream cis = new CheckedInputStream(bais, new Adler32());
			byte readBuffer[] = new byte[buffer.length];
			while (cis.read(readBuffer) >= 0) {
				value = cis.getChecksum().getValue();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Checksum can't be created : ", e);
		}
		return value;
	}
}
