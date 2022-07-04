package io.c12.bala.api.resource;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;

/**
 * Secure with JWT token. Only allow REST calls with valid JWT token.
 */
@Path("/s/users")
@RequestScoped
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
public class SecureUserResource {

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    @Claim(standard = Claims.full_name)
    String fullName;

    @GET
    @RolesAllowed({"User", "Admin"})
    public Map<String, String> getUsersSecure(@Context SecurityContext ctx) {
        log.info("{}", jsonWebToken);
        return Map.of("name", fullName);
    }
}
