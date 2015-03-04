package com.canon.tsz662.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.canon.tsz662.rest.exceptions.BadRequestAttemptException;

/**
 * BadRequestAttemptException時に400を返す。
 * @HTTP 400 BadRequestAttemptException発生
 * @author tsz662
 *
 */
@Provider
public class BadRequestAttemptExceptionMapper implements ExceptionMapper<BadRequestAttemptException> {

	@Override
	public Response toResponse(BadRequestAttemptException exception) {
		return Response.status(Status.BAD_REQUEST).entity("Exception Mapper invoked!").build();
	}
}
