package com.faust.ticketing.persistence.model.entity.ticket;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedEntityGraph(name = "Ticket.withAll", includeAllAttributes = true)
@NamedEntityGraph(
        name = "Ticket.withAllAndCategoryUsersAndTheirTickets",
        includeAllAttributes = true,
        attributeNodes = {
                @NamedAttributeNode(value = "category", subgraph = "Category.assignedUsers")
        },
        subgraphs = {
                @NamedSubgraph(
                    name = "Category.assignedUsers",
                    attributeNodes = @NamedAttributeNode(value = "assignedUsers", subgraph = "User.assignedTickets")),
                @NamedSubgraph(
                    name = "User.assignedTickets",
                    attributeNodes = @NamedAttributeNode(value = "assignedTickets"))
})
public class Ticket extends AuditedEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 32000)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private Instant resolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", referencedColumnName = "id")
    private User assignedUser;

    @NotBlank
    @Size(max = 240)
    @Column(unique = true)
    private String title;

    @Enumerated(value = EnumType.ORDINAL)
    private TicketPriority priority = TicketPriority.LOW;

    @Transient
    public TicketStatus getStatus() {
        if (resolved != null) {
            return TicketStatus.RESOLVED;
        }
        else if (assignedUser != null) {
            return TicketStatus.ASSIGNED;
        }
        else {
            return TicketStatus.UNASSIGNED;
        }
    }

    @Transient
    public TicketAccess getAccess() {
        return TicketAccess.valueOf(getStatus());
    }
}
