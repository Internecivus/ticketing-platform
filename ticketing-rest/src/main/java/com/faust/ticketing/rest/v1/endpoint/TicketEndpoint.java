package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.TicketService;
import com.faust.ticketing.persistence.model.dto.client.*;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Cache(maxAge = 5)
@DenyAll
@Path("/ticket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TicketEndpoint {
    @EJB
    private TicketService ticketService;

    @Context
    private UriInfo uriInfo;

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/")
    @GET
    public Response getTickets() {
        final Set<TicketDTO> tickets = ticketService.getAll();
        return Response.ok(tickets).build();
    }

    @RolesAllowed({RoleName.Constant.TICKET_ADMINISTRATION})
    @Path("/{id}")
    @GET
    public Response getTicket(@PathParam("id") final Integer ticketId) {
        final TicketDTO ticket = ticketService.getById(ticketId);
        return Response.ok(ticket).build();
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/currentUser")
    @GET
    public Response getTicketsForCurrentUser() {
        final Set<TicketDTO> tickets = ticketService.getAllForCurrentUser();
        return Response.ok(tickets).build();
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/{id}")
    @DELETE
    public Response deleteTicket(@PathParam("id") final Integer ticketId) {
        ticketService.delete(ticketId);
        return Response.noContent().build();
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/{id}")
    @PUT
    public Response updateTicket(@PathParam("id") final Integer ticketId, final SaveTicketDTO ticket) {
        final TicketDTO updatedTicket = ticketService.update(ticketId, ticket);
        return Response.ok(updatedTicket).build();
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/")
    @POST
    public Response createTicket(final SaveTicketDTO ticket) throws Exception {
        final TicketDTO createdTicket = ticketService.create(ticket);
        return Response.ok(createdTicket).build();
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    @Path("/{id}/resolve")
    @POST
    public Response resolveTicket(@PathParam("id") final Integer ticketId) {
        ticketService.resolve(ticketId);
        return Response.noContent().build();
    }
}
