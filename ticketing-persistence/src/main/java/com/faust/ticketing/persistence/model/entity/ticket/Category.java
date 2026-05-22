package com.faust.ticketing.persistence.model.entity.ticket;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedEntityGraph(name = "Category.withAssignedUsers", attributeNodes = {
        @NamedAttributeNode(value = "assignedUsers")
})
public class Category extends AuditedEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 150)
    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_assigned_user",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> assignedUsers = new HashSet<>();

    public void addAssignedUser(final User user) {
        assignedUsers.add(user);
    }

    public void removeAssignedUser(final User user) {
        assignedUsers.remove(user);
    }
}
