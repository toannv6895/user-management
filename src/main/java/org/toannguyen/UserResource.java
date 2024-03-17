package org.toannguyen;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @POST
    public Response createUser(@Valid CreateUser user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{username}")
    public Response editUser(@Valid EditUser user, @PathParam("username") String username) {
        userService.editUser(user, username);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        userService.deleteUser(username);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        var user = userService.getUser(username);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }
}
