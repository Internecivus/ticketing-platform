package com.faust.ticketing.persistence.model.dto.client;

import com.faust.ticketing.persistence.model.entity.user.Role;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDTO extends AuditedEntityDTO {
    @EqualsAndHashCode.Include
    @Id
    private Integer id;
    private String email;
    private String username;
    private Set<GroupDTO> groups = new HashSet<>();
    private Set<RoleDTO> roles = new HashSet<>();
    private Integer workload;
}
