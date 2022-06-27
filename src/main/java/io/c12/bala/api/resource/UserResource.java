package io.c12.bala.api.resource;

import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.service.UserService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestStreamElementType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RestStreamElementType(MediaType.APPLICATION_JSON)
@Slf4j
@RequestScoped
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Multi<UserDto> listAllUser(@QueryParam("pageIndex") int pageIndex, @QueryParam("pageSize") int pageSize) {
        return userService.getAllUsersPage(pageIndex, pageSize);
    }

    @GET
    @Path("/{id}")
    public Uni<UserDto> getUserById(@PathParam("id") String id) {
        return userService.findUserById(id);
    }

    @POST
    public Uni<Response> addUser(UserDto userDto) {
        return userService.addUser(userDto)
                .map(c -> Response.created(URI.create(String.format("/api/users/%s", c.getId()))).entity(c).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateUser(@PathParam("id") String id, UserDto userDto) {
        return userService.updateUser(id, userDto).map(u -> Response.ok(u).build());
    }

    @PATCH
    @Path("/{id}")
    public Uni<Response> updatePartialUser(@PathParam("id") String id, UserDto userDto) {
        return userService.partialUpdateUser(id, userDto).map(v -> Response.ok(v).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteUser(@PathParam("id") String id) {
        return userService.deleteUser(id).map(v -> Response.noContent().build());
    }

}
