package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.UserService;
import com.faust.ticketing.persistence.model.dto.client.LoginCredentialDTO;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@NoCache
@Path("/authentication")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationEndpoint {
    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @PermitAll
    @POST
    @Path("/login")
    public Response login(final LoginCredentialDTO credentialDTO) {
        final UsernamePasswordCredential credential = new UsernamePasswordCredential(credentialDTO.getUsernameOrEmail(), credentialDTO.getPassword());
        final AuthenticationParameters authenticationParameters = new AuthenticationParameters();
        authenticationParameters.setCredential(credential);

        securityContext.authenticate(request, response, authenticationParameters);

        return Response.noContent().build();
    }

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    @POST
    @Path("/logout")
    public Response logout() throws ServletException {
        request.logout();
        request.getSession().invalidate();
        return Response.noContent().build();
    }
}
