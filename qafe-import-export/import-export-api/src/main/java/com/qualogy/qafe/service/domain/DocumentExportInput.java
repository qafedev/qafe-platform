/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.service.domain;

import java.util.List;
import java.util.Map;

public class DocumentExportInput {
	
	private List<Map<String,Object>> data;
	private List<String> columnSequence;
	private String configurationLocation ="src/main/resources";
	private String header =null;
	private boolean generateColumnHeader= false;

	public List<String> getColumnSequence() {
		return columnSequence;
	}
	public void setColumnSequence(List<String> columnSequence) {
		this.columnSequence = columnSequence;
	}
	public boolean isGenerateColumnHeader() {
		return generateColumnHeader;
	}
	public void setGenerateColumnHeader(boolean generateColumnHeader) {
		this.generateColumnHeader = generateColumnHeader;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getConfigurationLocation() {
		return configurationLocation;
	}
	public void setConfigurationLocation(String configurationLocation) {
		this.configurationLocation = configurationLocation;
	}
	public List<Map<String, Object>> getData() {
		
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public String getExportCode() {
		return exportCode;
	}
	public void setExportCode(String exportCode) {
		this.exportCode = exportCode;
	}
	public Boolean getIncludeHeader() {
		return includeHeader;
	}
	public void setIncludeHeader(Boolean includeHeader) {
		this.includeHeader = includeHeader;
	}
	private String exportCode ;
	private Boolean includeHeader;
}
