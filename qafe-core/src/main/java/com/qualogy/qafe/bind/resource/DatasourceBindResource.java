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
package com.qualogy.qafe.bind.resource;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.io.FileLocation;

public abstract class DatasourceBindResource extends BindResource implements Validatable{

	public void validate() throws ValidationException {
		// TODO for generation this test is not fully working, because it does not take the current working directory into consideration
		// For now commented.
//		if(this.statementsFileUrl.toURI()==null){
//			throw new ValidationException("statements-file-url ["+this.statementsFileUrl+"] is not an existing resource");
//	    }
//
	}

	private static final long serialVersionUID = -7683603741777639055L;

	protected String dialect;
	protected FileLocation statementsFileUrl;
    protected Boolean proxyConnection=Boolean.FALSE;

    public Boolean isProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(Boolean proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

	public String getDialect() {
		return dialect;
	}

	public FileLocation getStatementsFileUrl() {
		return statementsFileUrl;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void setStatementsFileUrl(FileLocation statementsFileUrl) {
		this.statementsFileUrl = statementsFileUrl;
	}

}
