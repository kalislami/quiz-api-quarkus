package com.quiz.api.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {
    @Override
    public Response toResponse(ApiException exception) {
        ApiError body = "VALIDATION_ERROR".equals(exception.code)
                ? new ApiError(exception.code, "Invalid request",
                    java.util.List.of(new ApiError.FieldError("answer", exception.getMessage())))
                : ApiError.of(exception.code, exception.getMessage());
        return Response.status(exception.status)
                .entity(body).build();
    }
}
