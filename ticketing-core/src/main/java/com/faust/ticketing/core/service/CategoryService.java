package com.faust.ticketing.core.service;

import com.faust.ticketing.core.repository.CategoryRepository;
import com.faust.ticketing.persistence.model.dto.client.CategoryDTO;
import com.faust.ticketing.persistence.model.dto.client.SaveCategoryDTO;
import com.faust.ticketing.persistence.model.entity.ticket.Category;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import com.faust.ticketing.persistence.model.entity.user.User;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class CategoryService {
    @EJB
    private UserService userService;

    @EJB
    private CategoryRepository categoryRepository;

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public CategoryDTO getById(final Integer categoryId) {
        return toDTO(categoryRepository.findById(categoryId).orElseThrow());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Set<CategoryDTO> getAll() {
        return toSimpleDTO(categoryRepository.findAll());
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    public CategoryDTO create(final SaveCategoryDTO category) {
        return toDTO(categoryRepository.create(toEntity(category)));
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    public CategoryDTO update(final Integer categoryId, final SaveCategoryDTO updatedCategory) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow();
        updateEntity(category, updatedCategory);
        return toDTO(categoryRepository.update(category));
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    public void delete(final Integer categoryId) {
        categoryRepository.delete(categoryId);
    }

    @PermitAll
    public Category toEntity(final SaveCategoryDTO dto) {
        final Category entity = new Category();
        return updateEntity(entity, dto);
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Category updateEntity(final Category entity, final SaveCategoryDTO dto) {
        entity.setName(dto.getName());
        entity.setAssignedUsers(dto.getAssignedUserIds().stream().map(id -> {
            final User assignedUser = new User();
            assignedUser.setId(id);
            return assignedUser;
        }).collect(Collectors.toSet()));
        return entity;
    }

    @PermitAll
    public Set<CategoryDTO> toDTO(final Collection<Category> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @PermitAll
    public CategoryDTO toDTO(final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setAssignedUsers(userService.toSimpleDTO(category.getAssignedUsers()));
        AuditedEntityService.addToDTO(dto, category);
        return dto;
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Set<CategoryDTO> toSimpleDTO(final Collection<Category> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toSimpleDTO).collect(Collectors.toSet());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public CategoryDTO toSimpleDTO(final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
