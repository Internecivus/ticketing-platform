package com.faust.ticketing.core.repository;

import com.faust.ticketing.core.graph.GraphHelper;
import com.faust.ticketing.core.optional.Utility;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import com.faust.ticketing.persistence.model.entity.user.User;
import com.faust.ticketing.persistence.sql.NamedEntityGraph;
import com.faust.ticketing.persistence.sql.NamedQuery;

import javax.ejb.Stateless;
import java.util.*;

@Stateless
public class TicketRepository extends JPACrudRepository<Ticket> {
    public Set<Ticket> findAll() {
        return new HashSet<>(em.createNamedQuery(NamedQuery.FindAllTickets.getName(), Ticket.class)
                .setHint(GraphHelper.FETCHGRAPH, em.getEntityGraph(NamedEntityGraph.TicketsWithAll.getName()))
                .getResultList());
    }

    public Set<Ticket> findForAutomaticAssignment() {
        return new HashSet<>(em.createNamedQuery(NamedQuery.FindAllTicketsUnassigned.getName(), Ticket.class)
                .setHint(GraphHelper.FETCHGRAPH, em.getEntityGraph(NamedEntityGraph.TicketsWithAllAndCategoryUsersAndTheirTickets.getName()))
                .getResultList());
    }

    public Set<Ticket> findAllByUser(final Integer userId) {
        return new HashSet<>(em.createNamedQuery(NamedQuery.FindTicketsByUser.getName(), Ticket.class)
                .setParameter("userId", userId)
                .setHint(GraphHelper.FETCHGRAPH, em.getEntityGraph(NamedEntityGraph.TicketsWithAll.getName()))
                .getResultList());
    }
}
