package com.faust.ticketing.core.service;

import com.faust.ticketing.core.DateFormatter;
import com.faust.ticketing.core.repository.CredentialsRepository;
import com.faust.ticketing.core.repository.UserRepository;
import com.faust.ticketing.persistence.model.dto.client.SaveUserDTO;
import com.faust.ticketing.persistence.model.dto.client.UserDTO;
import com.faust.ticketing.persistence.model.entity.user.*;

import javax.annotation.Resource;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class UserService {
    @EJB
    private GroupService groupService;

    @EJB
    private UserRepository userRepository;

    @EJB
    private RoleService roleService;

    @EJB
    private CredentialsService credentialsService;

    @EJB
    private CredentialsRepository credentialsRepository;

    @Resource
    private EJBContext ejbContext;

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    public String getCurrentUsername() {
        return ejbContext.getCallerPrincipal().getName();
    }

    @PermitAll
    public Optional<UserDTO> getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername()).map(this::toDTO);
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public UserDTO create(final SaveUserDTO userDTO) throws Exception {
        final User user = toEntity(userDTO);
        final User createdUser = userRepository.create(user);
        credentialsService.createRandomCredentials(createdUser);
        return toDTO(createdUser);
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public void delete(final Integer userId) {
        credentialsRepository.delete(credentialsRepository.findByUser(userId).orElseThrow().getId());
        userRepository.delete(userId);
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public UserDTO getById(final Integer userId) {
        return toDTO(userRepository.findById(userId).orElseThrow());
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public Set<UserDTO> getAll() {
        return toSimpleDTO(userRepository.findAll());
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public UserDTO update(final Integer userId, final SaveUserDTO updatedUser) {
        final User user = userRepository.findById(userId).orElseThrow();
        updateEntity(user, updatedUser);
        return toDTO(userRepository.update(user));
    }

    @RolesAllowed(RoleName.Constant.BASIC_FUNCTIONALITY)
    public UserDTO updateCurrent(final SaveUserDTO updatedUser) {
        final User user = userRepository.findByUsername(getCurrentUsername()).orElseThrow();
        updateEntity(user, updatedUser);
        return toDTO(userRepository.update(user));
    }

    @PermitAll
    public User toEntity(final SaveUserDTO dto) {
        final User entity = new User();
        return updateEntity(entity, dto);
    }

    @PermitAll
    public Set<UserDTO> toDTO(final Collection<User> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Set<UserDTO> toSimpleDTO(final Collection<User> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toSimpleDTO).collect(Collectors.toSet());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public UserDTO toSimpleDTO(final User user) {
        if (user == null) {
            return null;
        }
        final UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @PermitAll
    public UserDTO toDTO(final User user) {
        if (user == null) {
            return null;
        }
        final UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setWorkload(user.getWorkload());
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());

        dto.setGroups(groupService.toDTO(user.getGroups()));
        dto.setRoles(roleService.toDTO(user.getRoles()));

        AuditedEntityService.addToDTO(dto, user);
        return dto;
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public User updateEntity(final User user, final SaveUserDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setGroups(dto.getGroups().stream().map(id -> {
            final Group group = new Group();
            group.setId(id);
            return group;
        }).collect(Collectors.toSet()));
        return user;
    }
}
