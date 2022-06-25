package io.c12.bala.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.c12.bala.db.entity.User;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Instant;

@Path("/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public Multi<User> listAllUser() {
        return User.streamAll();
    }

    @POST
    public Uni<Response> addUser(User user) {
        user.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 25);
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        return User.persistOrUpdate(user)
                .flatMap(i -> User.findById(user.id))
                .map(c -> Response.created(URI.create(String.format("/api/users/%s", ((User)c).id))).entity(c).build());
    }
}
