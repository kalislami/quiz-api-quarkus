package com.quiz.api.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ApiError.FieldError> errors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String path = violation.getPropertyPath().toString();
                    String field = path.substring(path.lastIndexOf('.') + 1);
                    return new ApiError.FieldError(field, violation.getMessage());
                }).toList();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiError("VALIDATION_ERROR", "Invalid request", errors)).build();
    }
}
