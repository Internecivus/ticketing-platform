package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.PasswordResetService;
import com.faust.ticketing.persistence.model.dto.client.CreatePasswordResetDTO;
import com.faust.ticketing.persistence.model.dto.client.UsePasswordResetDTO;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@NoCache
@PermitAll
@Path("/passwordReset")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PasswordResetEndpoint {
    @EJB
    private PasswordResetService passwordResetService;

    @Path("/")
    @POST
    public Response createPasswordReset(final CreatePasswordResetDTO dto) throws Exception {
        passwordResetService.createAndSendToken(dto);
        return Response.accepted().build();
    }

    @Path("/use")
    @POST
    public Response usePasswordReset(final UsePasswordResetDTO dto) throws Exception {
        passwordResetService.useToken(dto);
        return Response.noContent().build();
    }
}
