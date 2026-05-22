package com.faust.ticketing.core.service;

import com.faust.ticketing.core.exception.ApplicationErrorCode;
import com.faust.ticketing.core.exception.BusinessException;
import com.faust.ticketing.core.repository.CategoryRepository;
import com.faust.ticketing.core.repository.TicketRepository;
import com.faust.ticketing.core.repository.UserRepository;
import com.faust.ticketing.core.service.messaging.MessageService;
import com.faust.ticketing.persistence.model.entity.ticket.Category;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import com.faust.ticketing.persistence.model.entity.user.User;

import javax.annotation.security.DenyAll;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class AutomaticAssignmentService {
    public final static boolean AUTOMATIC_ASSIGNMENT_ENABLED = true;
    public final static int AUTOMATIC_ASSIGNMENT_SCHEDULE_MINUTES = 30;

    @EJB
    private TicketRepository ticketRepository;

    @Schedule(hour = "*", minute = "*/" + AUTOMATIC_ASSIGNMENT_SCHEDULE_MINUTES, persistent = false)
    public void automaticAssignment() {
        if (!AUTOMATIC_ASSIGNMENT_ENABLED) {
            return;
        }

        final Set<Ticket> ticketsForAssignment = ticketRepository.findForAutomaticAssignment();

        if (ticketsForAssignment.isEmpty()) {
            return;
        }
        final HashMap<Category, Set<Ticket>> categoryTicketMap = createCategoryTicketMap(ticketsForAssignment);

        categoryTicketMap.keySet().forEach((category) -> {
            final List<Ticket> categoryTickets = new ArrayList<>(categoryTicketMap.get(category));
            final List<User> categoryUsers = new ArrayList<>(category.getAssignedUsers());

            if (categoryUsers.isEmpty() && !categoryTickets.isEmpty()) {
            } else if (categoryUsers.size() == 1) {
                categoryTickets.forEach((ticket -> {
                    assignUserToTicket((User) categoryUsers.toArray()[0], ticket);
                }));
            } else {
                categoryUsers.sort(Comparator.comparing(User::getWorkload));

                if (categoryUsers.size() < categoryTickets.size()) {
                    categoryTickets.sort(Comparator.comparing(Ticket::getPriority).reversed());
                }

                for (int i = 0, j = 0; i < categoryTickets.size(); i++) {
                    final Ticket ticket = categoryTickets.get(i);

                    if (j + 1 >= categoryUsers.size()) {
                        j = 0;
                    } else {
                        j++;
                    }
                    final User user = categoryUsers.get(j);
                    assignUserToTicket(user, ticket);
                }
            }
        });
    }

    private void assignUserToTicket(final User user, final Ticket ticket) {
        ticket.setAssignedUser(user);
        ticketRepository.update(ticket);
    }

    private HashMap<Category, Set<Ticket>> createCategoryTicketMap(final Set<Ticket> tickets) {
        final HashMap<Category, Set<Ticket>> categoryTicketMap = new HashMap<>();
        tickets.forEach((ticket -> {
            categoryTicketMap.putIfAbsent(ticket.getCategory(), new HashSet<>());
            final Set<Ticket> categoryTickets = categoryTicketMap.get(ticket.getCategory());
            categoryTickets.add(ticket);
        }));
        return categoryTicketMap;
    }
}
