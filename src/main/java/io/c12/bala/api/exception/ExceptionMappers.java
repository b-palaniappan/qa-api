package io.c12.bala.api.exception;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.core.Response;
import java.util.Map;

public class ExceptionMappers {

    @ServerExceptionMapper
    public Uni<RestResponse<Map<String, String>>> mapException(UserNotFoundException ex) {
        // TODO: create Error entity bean
        return Uni.createFrom().item(RestResponse.status(Response.Status.NOT_FOUND, Map.of("errorMessage", "User Not Found for id: " + ex.id)));
    }
}
