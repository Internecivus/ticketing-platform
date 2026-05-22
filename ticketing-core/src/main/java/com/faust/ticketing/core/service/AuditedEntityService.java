package com.faust.ticketing.core.service;

import com.faust.ticketing.core.DateFormatter;
import com.faust.ticketing.persistence.model.dto.client.AuditedEntityDTO;
import com.faust.ticketing.persistence.model.entity.AuditedEntity;

public class AuditedEntityService {
    public static void addToDTO(final AuditedEntityDTO dto, final AuditedEntity auditedEntity) {
        dto.setLastUpdatedDate(DateFormatter.shortFormat(auditedEntity.getLastUpdated()));
        dto.setCreatedDate(DateFormatter.shortFormat(auditedEntity.getCreated()));
        dto.setCreatedUser(auditedEntity.getCreatedUser() != null
                ? auditedEntity.getCreatedUser().getUsername() : null);
        dto.setLastUpdateUser(auditedEntity.getLastUpdateUser() != null
                ? auditedEntity.getLastUpdateUser().getUsername() : null);
    }
}
