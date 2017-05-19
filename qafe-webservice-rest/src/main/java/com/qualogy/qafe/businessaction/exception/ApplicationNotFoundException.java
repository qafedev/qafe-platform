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
package com.qualogy.qafe.businessaction.exception;

/**
 * Thrown whenever it is expected an application must be found.
 * 
 * @author sdahlberg
 * 
 */
public final class ApplicationNotFoundException extends BusinessActionResourceException {

    private static final long serialVersionUID = 930593172903138796L;

    public ApplicationNotFoundException(final String message) {
        super(message);
    }
}
