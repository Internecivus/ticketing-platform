package com.faust.ticketing.persistence.model;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import com.faust.ticketing.persistence.model.entity.user.User;
import org.hibernate.Session;
import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

public class CurrentUserValueEntityListener {
    @Inject
    private SecurityContext securityContext;

    @PreUpdate
    public void handlePreUpdate(final Object entity) {
        if (entity instanceof AuditedEntity) {
            final AuditedEntity auditedEntity = (AuditedEntity) entity;

            final Principal callerPrincipal = securityContext.getCallerPrincipal();
            if (callerPrincipal != null) {
                final User user = ((UserPrincipal) callerPrincipal).getUser();
                auditedEntity.setLastUpdateUser(user);
            }
        }
    }

    @PrePersist
    public void handlePrePersist(final Object entity) {
        if (entity instanceof AuditedEntity) {
            final AuditedEntity auditedEntity = (AuditedEntity) entity;
            auditedEntity.setCreatedUser(((UserPrincipal) securityContext.getCallerPrincipal()).getUser());
        }
    }
}
