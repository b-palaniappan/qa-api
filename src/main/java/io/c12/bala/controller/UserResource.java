package io.c12.bala.controller;

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
        return User.persistOrUpdate(user)
                .flatMap(i -> User.findById(user.id))
                .map(c -> Response.created(URI.create(String.format("/api/users/%s", ((User)c).id))).entity(c).build());
    }
}
