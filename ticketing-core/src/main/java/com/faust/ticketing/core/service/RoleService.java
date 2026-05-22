package com.faust.ticketing.core.service;

import com.faust.ticketing.persistence.model.dto.client.RoleDTO;
import com.faust.ticketing.persistence.model.dto.client.UserDTO;
import com.faust.ticketing.persistence.model.entity.user.Role;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import com.faust.ticketing.persistence.model.entity.user.User;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class RoleService {
    @PermitAll
    public Set<RoleDTO> toDTO(final Collection<Role> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @PermitAll
    public RoleDTO toDTO(final Role role) {
        final RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName().getName());
        return dto;
    }
}
