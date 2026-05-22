package com.faust.ticketing.persistence.model.dto.client;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.swing.text.DateFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AuditedEntityDTO {
    private String createdUser;
    private String lastUpdateUser;
    private String createdDate;
    private String lastUpdatedDate;
}
