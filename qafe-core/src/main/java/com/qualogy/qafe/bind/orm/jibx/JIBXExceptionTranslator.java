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
package com.qualogy.qafe.bind.orm.jibx;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jibx.runtime.JiBXException;


public class JIBXExceptionTranslator {

	public final static String JIBX_LINE_INDICATION_START_MARK = "line "; 
	public final static String JIBX_LINE_INDICATION_END_MARK = ",";
	
	/**
	 * Method to get the line JIBX complains about in its error message
	 * Method resets the given inputstream to get a clear vision
	 * @param e
	 * @param in
	 * @return
	 * @throws BindException - when IOException occurs reading inputstream
	 */
	public static String getLine(JiBXException exception, InputStream in) throws BindException{
		String line = null;
		
		try {
			in.reset();
		}catch (IOException e) {
			throw new BindException("Getting the original line failed",e);
		}	
		if(exception.getMessage()!=null){
			String message = exception.getMessage();
			
			int start = message.indexOf(JIBX_LINE_INDICATION_START_MARK) + JIBX_LINE_INDICATION_START_MARK.length();
			if(start>-1){
				int end = message.indexOf(JIBX_LINE_INDICATION_END_MARK, start);
				
				if(end>-1){
					String lineNrStr = message.substring(start, end);
					if(NumberUtils.isNumber(lineNrStr)){
						int lineNr = Integer.parseInt(lineNrStr);
					
						List lines = null;
						try {
							lines = IOUtils.readLines(in);
						} catch (IOException e) {
							throw new BindException("Getting the original line failed",e);
						}
						if(lines!=null){
							line = (String)(lines.get(lineNr-1));
						}
					}
				}
			}
		}
		if(line!=null)
			line = StringUtils.trimToEmpty(line);
		return line;
	}
}
