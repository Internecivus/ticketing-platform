package com.faust.ticketing.persistence.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum NamedEntityGraph {
    CategoriesWithAssignedUsers("Category.withAssignedUsers"),
    UserWithAllAndGroupRoles("User.withAllAndGroupRoles"),
    TicketsWithAll("Ticket.withAll"),
    TicketsWithAllAndCategoryUsersAndTheirTickets("Ticket.withAllAndCategoryUsersAndTheirTickets");

    @Getter
    private final String name;
}
