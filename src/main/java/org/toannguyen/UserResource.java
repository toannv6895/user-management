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
    @Inject
    DynamoDbService dynamoDbService;

    @POST
    public Response createUser(@Valid CreateUser user) {
        var resultUser = userService.createUser(user);
        dynamoDbService.add(new User(resultUser));
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{username}")
    public Response editUser(@Valid EditUser user, @PathParam("username") String username) {
        userService.editUser(user, username);
        var updateUser = new User(user);
        updateUser.setUsername(username);
        dynamoDbService.update(updateUser);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        userService.deleteUser(username);
        dynamoDbService.delete(username);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        var user = dynamoDbService.get(username);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }

    @GET
    public Response getUsers() {
        var users = dynamoDbService.getUsers();
        return Response.ok(users).build();
    }
}
