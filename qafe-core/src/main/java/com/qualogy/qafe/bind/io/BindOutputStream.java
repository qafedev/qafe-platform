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
package com.qualogy.qafe.bind.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * OutputStream for Jibx bindings, adds functionality to write
 * schemalocation
 * @author 
 */
public class BindOutputStream extends OutputStream{

	private OutputStream output;
	
	private ByteArrayOutputStream copyOutput;
	
	private String START_DELIMITER_part1 = "<";
	private String START_DELIMITER_part2 = " xmlns=\"http://qafe.com/schema\"";
	
	private String START_DELIMITER = null;
	private String SCHEMA_LOCATION = null;
	
	private StringBuffer buffer = null;

	private boolean encodingWritten = false;
	
	public BindOutputStream(OutputStream output, String rootElementName, String schemaLocation){
		this.output = output;
		this.buffer = new StringBuffer();
		this.copyOutput = new ByteArrayOutputStream();
		
		START_DELIMITER = START_DELIMITER_part1 + rootElementName + START_DELIMITER_part2;
		SCHEMA_LOCATION = schemaLocation;
	}

	public void close()throws IOException{
		if(output!=null)
			this.output.close();
	}

	public void write(int b) throws IOException {
		
		output.write(b);
		copyOutput.write(b);
		copyOutput.toString("UTF-8");
		
		if (!encodingWritten) {
			buffer.append((char)b);
			if(buffer.lastIndexOf(START_DELIMITER)>-1){
				byte[] _b = (" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
						"xsi:schemaLocation=\""+SCHEMA_LOCATION+"\"").getBytes();
				output.write(_b);
				copyOutput.write(_b);
				buffer = null;
				encodingWritten = true;
			}
		}
	}

	public byte[] toByteArray() {
		return copyOutput.toByteArray();
	}
}

