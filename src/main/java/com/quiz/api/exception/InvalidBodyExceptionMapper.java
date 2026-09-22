package com.quiz.api.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.spi.ReaderException;

@Provider
public class InvalidBodyExceptionMapper implements ExceptionMapper<ReaderException> {
    @Override
    public Response toResponse(ReaderException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiError("VALIDATION_ERROR", "Invalid JSON request body",
                        java.util.List.of())).build();
    }
}
