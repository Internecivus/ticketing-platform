package com.faust.ticketing.persistence.model.dto.client;

import com.faust.ticketing.persistence.model.entity.ticket.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TicketDTO extends AuditedEntityDTO {
    @EqualsAndHashCode.Include
    @Id
    private Integer id;
    private String content;
    private String title;
    private String resolved;
    private UserDTO assignedUser;
    private CategoryDTO category;
    private TicketPriority priority;
    private TicketStatus status;
}
