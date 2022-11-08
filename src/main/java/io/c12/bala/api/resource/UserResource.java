package io.c12.bala.api.resource;

import io.c12.bala.api.exception.ApiError;
import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.service.UserService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
    @Operation(summary = "Get list of all users", description = "Get list of all users paginated by pageIndex and pageSize")
    @APIResponse(responseCode = "200", description = "List of paginated users list")
    public Multi<UserDto> listAllUser(@Parameter(name = "pageIndex", description = "Page Index or Page number for pagination") @RestQuery("pageIndex") int pageIndex,
                                      @Parameter(name = "pageSize", description = "Page Size for pagination") @RestQuery("pageSize") int pageSize) {
        return userService.getAllUsersPage(pageIndex, pageSize);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by Id", description = "Get one user by unique user id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Get user by id successfully", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserDto.class))}),
            @APIResponse(responseCode = "404", description = "User not found for requested id", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = ApiError.class))})
    })
    public Uni<UserDto> getUserById(@RestPath("id") String id) {
        return userService.findUserById(id);
    }

    @POST
    @ResponseStatus(201)
    @Operation(summary = "Create a user", description = "Create or add a new user to system")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "User added successfully", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserDto.class))},
                    headers = @Header(name = "Location", description = "partial url of the user created", schema = @Schema(type = SchemaType.STRING))),
            @APIResponse(responseCode = "400", description = "Request payload validation failed", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = ApiError.class))})
    })
    public Uni<RestResponse> addUser(@RequestBody(name = "user information", description = "User information to be added", required = true) @Valid UserDto userDto) {
        return userService.addUser(userDto)
                .map(c -> RestResponse.ResponseBuilder.created(URI.create(String.format("/v1/users/%s", c.getId()))).entity(c).build());
    }

    @PUT
    @Path("/{id}")
    @ResponseStatus(200)
    @Operation(summary = "Update user", description = "Update whole user by user id.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Updated user successfully", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserDto.class))}),
        @APIResponse(responseCode = "400", description = "Request payload validation failed", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = ApiError.class))})
    })
    public Uni<UserDto> updateUser(@RestPath("id") String id, @Valid UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @PATCH
    @Path("/{id}")
    @ResponseStatus(200)
    @Operation(summary = "Patch user", description = "Update or or more param of user by User id")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Updated user successfully", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserDto.class))}),
        @APIResponse(responseCode = "400", description = "Request payload validation failed", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = ApiError.class))})
    })
    public Uni<UserDto> updatePartialUser(@RestPath("id") String id, UserDto userDto) {
        return userService.partialUpdateUser(id, userDto);
    }

    @DELETE
    @Path("/{id}")
    @ResponseStatus(204)
    @Operation(summary = "Delete a user", description = "Delete a user by user id")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Delete user successfully", content = {@Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserDto.class))})
    })
    public Uni<UserDto> deleteUser(@RestPath("id") String id) {
        return userService.deleteUser(id);
    }

}
