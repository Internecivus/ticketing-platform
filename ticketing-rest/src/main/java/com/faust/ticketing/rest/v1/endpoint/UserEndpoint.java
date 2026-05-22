package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.UserService;
import com.faust.ticketing.persistence.model.dto.client.SaveUserDTO;
import com.faust.ticketing.persistence.model.dto.client.UserDTO;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Cache(maxAge = 5)
@DenyAll
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    @EJB
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @PermitAll
    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    @Path("/current")
    @GET
    public Response getCurrentUser() {
        final UserDTO user = userService.getCurrentUser().orElse(null);
        return Response.ok(user).build();
    }

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    @Path("/current")
    @PUT
    public Response updateCurrentUser(final SaveUserDTO user) {
        final UserDTO updatedUser = userService.updateCurrent(user);
        return Response.ok(updatedUser).build();
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    @Path("/")
    @GET
    public Response getUsers() {
        final Set<UserDTO> users = userService.getAll();
        return Response.ok(users).build();
    }


    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    @Path("/{id}")
    @GET
    public Response getUser(@PathParam("id") final Integer userId) {
        final UserDTO user = userService.getById(userId);
        return Response.ok(user).build();
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    @Path("/{id}")
    @DELETE
    public Response deleteUser(@PathParam("id") final Integer userId) {
        userService.delete(userId);
        return Response.noContent().build();
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    @Path("/{id}")
    @PUT
    public Response updateUser(@PathParam("id") final Integer userId, final SaveUserDTO user) {
        final UserDTO updatedUser = userService.update(userId, user);
        return Response.ok(updatedUser).build();
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    @Path("/")
    @POST
    public Response createUser(final SaveUserDTO user) throws Exception {
        final UserDTO createdUser = userService.create(user);
        return Response.ok(createdUser).build();
    }
}
