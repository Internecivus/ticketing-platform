package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.GroupService;
import com.faust.ticketing.persistence.model.dto.client.GroupDTO;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;
@Cache(maxAge = 5)
@DenyAll
@Path("/group")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupEndpoint {
    @EJB
    private GroupService groupService;

    @Context
    private UriInfo uriInfo;

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    @Path("/")
    @GET
    public Response getGroups() {
        final Set<GroupDTO> groups = groupService.getAll();
        return Response.ok(groups).build();
    }

}
