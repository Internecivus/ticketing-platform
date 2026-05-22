package com.faust.ticketing.persistence.model.entity;

import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@MappedSuperclass
public abstract class AuditedEntity {
    @NotNull
    private Instant created;

    private Instant lastUpdated;

    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdUser;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User lastUpdateUser;

    private Instant deleted;

    @PrePersist
    public void setCreated() {
        this.created = Instant.now();
    }

    @PreUpdate
    public void setLastUpdated() {
        this.lastUpdated = Instant.now();
    }
}
