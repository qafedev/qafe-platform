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
package com.qualogy.qafe.jaxrs.exception;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.qualogy.qafe.businessaction.exception.BusinessActionResourceException;

/**
 * Maps the main business action resource exception to the proper RESTful response.
 * 
 * @author sdahlberg
 * 
 */
@Provider
public final class BusinessActionResourceExceptionMapper implements
        ExceptionMapper<BusinessActionResourceException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(final BusinessActionResourceException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage())
            .type(MediaType.APPLICATION_JSON).build();
    }

}
