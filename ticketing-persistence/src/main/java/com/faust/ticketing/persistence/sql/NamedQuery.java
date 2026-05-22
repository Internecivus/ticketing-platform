package com.faust.ticketing.persistence.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum NamedQuery {
    FindAllCategories("Category.findAll"),
    FindUserByUsername("User.findByUsername"),
    FindUserByUsernameOrEmail("User.findByUsernameOrEmail"),
    FindCredentialsByUser("Credentials.findByUser"),
    FindAllTickets("Ticket.findAll"),
    FindAllTicketsUnassigned("Ticket.findAllUnassigned"),
    FindUserByEmail("User.findByEmail"),
    FindTicketsByUser("Ticket.findByUser"),
    FindPasswordResetTokensByUser("PasswordResetToken.findByUser");

    @Getter
    private final String name;
}
