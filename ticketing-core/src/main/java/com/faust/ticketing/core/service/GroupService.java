package com.faust.ticketing.core.service;

import com.faust.ticketing.core.repository.GroupRepository;
import com.faust.ticketing.persistence.model.dto.client.GroupDTO;
import com.faust.ticketing.persistence.model.entity.user.Group;
import com.faust.ticketing.persistence.model.entity.user.Role;
import com.faust.ticketing.persistence.model.entity.user.RoleName;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class GroupService {
    @EJB
    private GroupRepository groupRepository;

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    public Set<GroupDTO> getAll() {
        return toDTO(groupRepository.findAll());
    }

    @PermitAll
    public GroupDTO toDTO(final Group entity) {
        final GroupDTO dto = new GroupDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    @PermitAll
    public Set<GroupDTO> toDTO(final Collection<Group> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @PermitAll
    public Set<Group> toEntity(final Collection<GroupDTO> dtoList) {
        return Optional.ofNullable(dtoList).orElse(Collections.emptySet())
                .stream().map(this::toEntity).collect(Collectors.toSet());
    }

    @PermitAll
    private Group toEntity(final GroupDTO dto) {
        final Group group = new Group();
        return updateEntity(group, dto);
    }

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    public Group updateEntity(final Group entity, final GroupDTO dto) {
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }
}
