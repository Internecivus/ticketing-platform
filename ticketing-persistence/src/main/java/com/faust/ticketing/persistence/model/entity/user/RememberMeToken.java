package com.faust.ticketing.persistence.model.entity.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RememberMeToken {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Size(min = 186, max = 186)
    private char[] hash;

    @NotNull
    private Instant generated;

    private Instant used;

    private Instant removed;

    @NotBlank
    @Size(max = 100)
    private String pepperAlias;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void setGenerated() {
        generated = Instant.now();
    }

    @Transient
    public boolean isRemoved() {
        return getRemoved() != null;
    }

    @Transient
    public boolean canBeUsed() {
        return !isRemoved();
    }
}
