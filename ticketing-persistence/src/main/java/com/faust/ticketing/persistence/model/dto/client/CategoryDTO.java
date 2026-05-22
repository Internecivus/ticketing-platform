package com.faust.ticketing.persistence.model.dto.client;

import com.faust.ticketing.persistence.model.entity.AuditedEntity;
import com.faust.ticketing.persistence.model.entity.ticket.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CategoryDTO extends AuditedEntityDTO {
    @EqualsAndHashCode.Include
    @Id
    private Integer id;
    private String name;
    private Set<UserDTO> assignedUsers = new HashSet<>();
}
