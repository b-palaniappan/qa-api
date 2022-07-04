package io.c12.bala.api.resource;

import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.service.UserService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestStreamElementType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RestStreamElementType(MediaType.APPLICATION_JSON)
@Slf4j
@RequestScoped
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Multi<UserDto> listAllUser(@RestQuery("pageIndex") int pageIndex, @RestQuery("pageSize") int pageSize) {
        return userService.getAllUsersPage(pageIndex, pageSize);
    }

    @GET
    @Path("/{id}")
    public Uni<UserDto> getUserById(@RestPath("id") String id) {
        return userService.findUserById(id);
    }

    @POST
    @ResponseStatus(201)
    public Uni<RestResponse> addUser(@Valid UserDto userDto) {
        return userService.addUser(userDto)
                .map(c -> RestResponse.ResponseBuilder.created(URI.create(String.format("/v1/users/%s", c.getId()))).entity(c).build());
    }

    @PUT
    @Path("/{id}")
    @ResponseStatus(200)
    public Uni<UserDto> updateUser(@RestPath("id") String id, @Valid UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @PATCH
    @Path("/{id}")
    @ResponseStatus(200)
    public Uni<UserDto> updatePartialUser(@RestPath("id") String id, UserDto userDto) {
        return userService.partialUpdateUser(id, userDto);
    }

    @DELETE
    @Path("/{id}")
    @ResponseStatus(204)
    public Uni<UserDto> deleteUser(@RestPath("id") String id) {
        return userService.deleteUser(id);
    }

}
