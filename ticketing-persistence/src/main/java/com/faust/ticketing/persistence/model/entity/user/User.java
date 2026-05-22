package com.faust.ticketing.persistence.model.entity.user;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedEntityGraph(name = "User.withAllAndGroupRoles",
        attributeNodes = { @NamedAttributeNode(value = "groups", subgraph = "Group.roles") },
        subgraphs = { @NamedSubgraph(name = "Group.roles", attributeNodes = @NamedAttributeNode(value = "roles")) })
public class User extends AuditedEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 5, max = 120)
    @Column(unique = true)
    private String username;

    @Email
    @NotBlank
    @Size(min = 5, max = 120)
    @Column(unique = true)
    private String email;

    @NotEmpty
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_user_group",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"))
    private Set<Group> groups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", referencedColumnName = "id")
    private Set<Ticket> assignedTickets = new HashSet<>();

    @Transient
    private Integer workload;

    public Integer getWorkload() {
        int workload = 0;
        for (Ticket assignedTicket : assignedTickets) {
            workload += assignedTicket.getPriority().ordinal() + 1;
        }
        return workload;
    }

    public Set<Role> getRoles() {
        return groups.stream().flatMap(group -> group.getRoles().stream()).collect(Collectors.toSet());
    }

    public Set<RoleName> getRolesNames() {
        return groups.stream().flatMap(group -> group.getRoles().stream()).collect(Collectors.toSet())
                .stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Transient
    public Set<String> getRoleNamesAsString() {
        return getRoles().stream().map(role -> role.getName().getName()).collect(Collectors.toSet());
    }
}
