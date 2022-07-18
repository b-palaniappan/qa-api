package io.c12.bala.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

/**
 * Global Exception Mapper for handling exceptions.
 */
@Slf4j
public class ExceptionMappers {

    @ServerExceptionMapper
    public Uni<RestResponse<ApiError>> mapUserNotFoundException(UserNotFoundException ex) {
        return Uni.createFrom().item(RestResponse.status(Response.Status.NOT_FOUND,
                new ApiError(Response.Status.NOT_FOUND, "User not found", ex)));
    }

    @ServerExceptionMapper
    public Uni<RestResponse<ApiError>> mapConstraintViolationException(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(Response.Status.BAD_REQUEST, "Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return Uni.createFrom().item(RestResponse.status(Response.Status.BAD_REQUEST, apiError));
    }

    @ServerExceptionMapper
    public Uni<RestResponse<ApiError>> mapInvalidFormatException(InvalidFormatException ex) {
        ApiError apiError = new ApiError(Response.Status.BAD_REQUEST, "Validation error");
        log.warn("Exception in request data ", ex);
        return Uni.createFrom().item(RestResponse.status(Response.Status.BAD_REQUEST, apiError));
    }
}
