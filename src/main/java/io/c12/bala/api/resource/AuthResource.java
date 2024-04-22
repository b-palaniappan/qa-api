package io.c12.bala.api.resource;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/a")
@RequestScoped
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @POST
    @Path("/register")
    public Map<String, String> registerUser() {
        // Register user with credentials

        return Map.of("msg", "hello");
    }
}
