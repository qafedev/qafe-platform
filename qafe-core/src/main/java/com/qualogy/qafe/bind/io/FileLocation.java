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
package com.qualogy.qafe.bind.io;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.orm.jibx.BindException;

public class FileLocation implements PostProcessing, Serializable {

	public final static String SCHEME_HTTP = "http";
	public final static String SCHEME_FILE = "file";
	//public final static String SCHEME_FTP = "ftp";

	public final static String[] SUPPORTED_PROTOCOLS = {SCHEME_FILE, SCHEME_HTTP};

	public final static String COMMON_SCHEME_DELIM = "://";

	protected String location;

	private String root;

	private URI uri;


	private FileLocation() {
		super();
	}

	public FileLocation(String location) {
		this();
		this.location = location;
	}

	public FileLocation(String root, String location) {
		this();
		this.location = location;
		setRoot(root);
	}

	public FileLocation(URI uri) {
		this.uri = uri;
	}

	public String getLocation() {
		return location;
	}

	/**
	 * this class takes the root argument if not empty
	 * and adds it to this.path
	 *
	 * protocol http is supported
	 *
	 * @param root
	 * @return
	 */
	// CHECKSTYLE.OFF: CyclomaticComplexity
	public URI toURI(){
		if(this.uri==null){
			String path = location;

			if(!StringUtils.isEmpty(root) && !root.endsWith("/") && !root.endsWith("\\"))//add File.separator @ end
				root += File.separator;

			path = ((root!=null)?root:"") + ((location!=null)?location:"");

			if (File.separatorChar == '\\') {
		        path = path.replace('\\', '/');
			}

			if(path.startsWith(SCHEME_HTTP + COMMON_SCHEME_DELIM)){
				try {
					URL url = new URL(path);
					this.uri = url.toURI();
				} catch (MalformedURLException e) {
					throw new BindException(e);
				} catch (URISyntaxException e) {
					throw new BindException(e);
				}
			}if(path.startsWith(SCHEME_FILE)){
				try {
					uri = new URI(path);
				} catch (URISyntaxException e) {
					throw new BindException(e);
				}
			}else{
				File file = StringUtils.isEmpty(root)? new File(((location!=null)?location:"")) : new File(root, ((location!=null)?location:""));
				if(file.exists()){
					this.uri = file.toURI();
				}
			}
		}
		return this.uri;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	public static URI toURI(String location){
		return new FileLocation(location).toURI();
	}

	public boolean isEmpty(){
		return (this.getLocation()==null);
	}

	/**
	 * root string will be added if this location is not null and
	 * does not start with file or http protocol
	 * @param root
	 */
	public void setRoot(String root){
		boolean setRoot = true;
		if(this.getLocation()!=null){
			for (int i = 0; i < SUPPORTED_PROTOCOLS.length; i++) {
				if(this.getLocation().startsWith(SUPPORTED_PROTOCOLS[i])){
					setRoot = false;
					break;
				}
			}

		}
		if(setRoot)
			this.root = root;
	}

	public String getRoot(){
		return root;
	}

	/**
	 * String representation of the uri or location if uri is not set
	 */
	public String toString(){
		return uri!=null ? uri.toString() : ((toURI()!=null)? toURI().toString():location);
	}

	public void performPostProcessing() {
		postset(null);
	}

	public void postset(IUnmarshallingContext context) {
		if(context!=null)
			setRoot(context.getDocumentName());
	}
}
