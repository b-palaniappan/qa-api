package io.c12.bala.api.resource;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.c12.bala.db.entity.User;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestStreamElementType;

import javax.ws.rs.Consumes;
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
public class UserResource {

    @GET
    public Multi<User> listAllUser(@QueryParam("pageIndex") int pageIndex, @QueryParam("pageSize") int pageSize) {
        return User.findAll().page(Page.of(pageIndex, pageSize)).stream();
    }

    @GET
    @Path("/{id}")
    public Uni<User> getUserById(@PathParam("id")String id) {
        return User.findById(id);
    }

    @POST
    public Uni<Response> addUser(User user) {
        user.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 25);
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        return User.persist(user)
                .flatMap(i -> User.findById(user.id))
                .map(c -> Response.created(URI.create(String.format("/api/users/%s", ((User)c).id))).entity(c).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<User> updateUser(@PathParam("id")String id, User user) {
        Uni<User> uniUpdateUser = User.findById(id);
        return uniUpdateUser.onItem().transform(updateUser -> {
            updateUser.firstName = user.firstName;
            updateUser.lastName = user.lastName;
            updateUser.email = user.email;
            updateUser.updatedAt = Instant.now();
            return updateUser;
        }).call(updateUser -> updateUser.persistOrUpdate());
    }

    @PATCH
    @Path("/{id}")
    public Uni<User> updatePartialUser(@PathParam("id")String id, User user) {
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
        }).call(u -> u.persistOrUpdate());
    }

}
