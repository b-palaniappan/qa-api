package io.c12.bala.api.resource;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.db.entity.User;
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
import java.time.Instant;
import java.util.Objects;

@Path("/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RestStreamElementType(MediaType.APPLICATION_JSON)
@Slf4j
@RequestScoped
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Multi<UserDto> listAllUser(@QueryParam("pageIndex") int pageIndex, @QueryParam("pageSize") int pageSize) {
        return userService.getAllUsersPage(pageIndex, pageSize);
    }

    @GET
    @Path("/{id}")
    public Uni<User> getUserById(@PathParam("id") String id) {
        return User.findById(id);
    }

    @POST
    public Uni<Response> addUser(User user) {
        user.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 25);
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        return User.persist(user)
                .flatMap(i -> User.findById(user.id))
                .map(c -> Response.created(URI.create(String.format("/api/users/%s", ((User) c).id))).entity(c).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateUser(@PathParam("id") String id, User user) {
        Uni<User> uniUpdateUser = User.findById(id);
        return uniUpdateUser.onItem().transform(updateUser -> {
            updateUser.firstName = user.firstName;
            updateUser.lastName = user.lastName;
            updateUser.email = user.email;
            updateUser.updatedAt = Instant.now();
            return updateUser;
        }).call(updateUser -> updateUser.persistOrUpdate()).map(v -> Response.ok(v).build());
    }

    @PATCH
    @Path("/{id}")
    public Uni<Response> updatePartialUser(@PathParam("id") String id, User user) {
        Uni<User> uniUpdateUser = User.findById(id);
        return uniUpdateUser.onItem().transform(u -> {
            if (Objects.nonNull(user.firstName)) {
                u.firstName = user.firstName;
            }
            if (Objects.nonNull(user.lastName)) {
                u.lastName = user.lastName;
            }
            if (Objects.nonNull(user.email)) {
                u.email = user.email;
            }
            if (Objects.nonNull(user.status)) {
                u.status = user.status;
            }
            u.updatedAt = Instant.now();
            return u;
        }).call(u -> u.persistOrUpdate()).map(v -> Response.ok(v).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteUser(@PathParam("id") String id) {
        Uni<User> uniDeleteUser = User.findById(id);
        return uniDeleteUser.onItem().transform(u -> {
            u.deletedAt = Instant.now();
            return u;
        }).call(u -> u.persistOrUpdate()).map(v -> Response.noContent().build());
    }

}
