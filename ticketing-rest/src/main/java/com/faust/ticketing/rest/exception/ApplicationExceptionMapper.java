package com.faust.ticketing.rest.exception;

import com.faust.ticketing.core.configuration.ServerMode;
import com.faust.ticketing.core.exception.*;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Log4j2
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {
    @Override
    public Response toResponse(final ApplicationException e) {
        Response.Status status;

        if (e instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
        }
        else if (e instanceof NotAuthorizedException) {
            status = Response.Status.UNAUTHORIZED;
        }
        else if (e instanceof SystemException) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        else if (e instanceof BusinessException) {
            status = Response.Status.BAD_REQUEST;
        }
        else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        final RestErrorMessage message = new RestErrorMessage(e.getTimestamp(), status.getStatusCode(),
                e.getCode().getName(), e.getMessage(), e.getDevMessage());

        log.throwing(e);

        return Response.status(status).entity(message).build();
    }
}
