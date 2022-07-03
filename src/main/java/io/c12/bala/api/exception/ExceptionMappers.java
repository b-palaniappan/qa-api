package io.c12.bala.api.exception;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.core.Response;

/**
 * Global Exception Mapper for handling exceptions.
 */
public class ExceptionMappers {

    @ServerExceptionMapper
    public Uni<RestResponse<ApiError>> mapUserNotFoundException(UserNotFoundException ex) {
        return Uni.createFrom().item(RestResponse.status(Response.Status.NOT_FOUND,
                new ApiError(Response.Status.NOT_FOUND, ex.getMessage(), ex)));
    }
}
