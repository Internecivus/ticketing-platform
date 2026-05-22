package com.faust.ticketing.core.service;

import com.faust.ticketing.core.DateFormatter;
import com.faust.ticketing.core.repository.TicketRepository;
import com.faust.ticketing.persistence.model.dto.client.*;
import com.faust.ticketing.persistence.model.entity.ticket.Category;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import com.faust.ticketing.persistence.model.entity.user.Group;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import com.faust.ticketing.persistence.model.entity.user.User;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@DenyAll
@Stateless
public class TicketService {
    @EJB
    private TicketRepository ticketRepository;

    @EJB
    private CategoryService categoryService;

    @EJB
    private UserService userService;

    @RolesAllowed({RoleName.Constant.TICKET_ADMINISTRATION})
    public TicketDTO getById(final Integer ticketId) {
        return toDTO(ticketRepository.findById(ticketId).orElseThrow());
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public Set<TicketDTO> getAll() {
        return toListDTO(ticketRepository.findAll());
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public Set<TicketDTO> getAllForCurrentUser() {
        return toListDTO(ticketRepository.findAllByUser(userService.getCurrentUser().orElseThrow().getId()));
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public TicketDTO create(final SaveTicketDTO ticketDto) throws Exception {
        final Ticket ticket = toEntity(ticketDto);
        final Ticket createdTicket = ticketRepository.create(ticket);
        return toDTO(createdTicket);
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public void delete(final Integer ticketId) {
        ticketRepository.delete(ticketId);
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public void resolve(final Integer ticketId) {
        final Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setResolved(Instant.now());
        ticketRepository.update(ticket);
    }

    @RolesAllowed(RoleName.Constant.TICKET_ADMINISTRATION)
    public TicketDTO update(final Integer ticketId, final SaveTicketDTO updatedTicket) {
        final Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        updateEntity(ticket, updatedTicket);
        return toDTO(ticketRepository.update(ticket));
    }

    @PermitAll
    public Set<TicketDTO> toDTO(final Collection<Ticket> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Set<TicketDTO> toListDTO(final Collection<Ticket> entityList) {
        return Optional.ofNullable(entityList).orElse(Collections.emptySet())
                .stream().map(this::toListDTO).collect(Collectors.toSet());
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public TicketDTO toListDTO(final Ticket ticket) {
        final TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setAssignedUser(userService.toSimpleDTO(ticket.getAssignedUser()));
        dto.setStatus(ticket.getStatus());
        dto.setCategory(categoryService.toSimpleDTO(ticket.getCategory()));
        dto.setPriority(ticket.getPriority());
        return dto;
    }

    @PermitAll
    public TicketDTO toDTO(final Ticket ticket) {
        final TicketDTO dto = new TicketDTO();
        dto.setContent(ticket.getContent());
        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setAssignedUser(userService.toDTO(ticket.getAssignedUser()));
        dto.setStatus(ticket.getStatus());
        dto.setCategory(categoryService.toDTO(ticket.getCategory()));
        dto.setPriority(ticket.getPriority());
        dto.setResolved(DateFormatter.shortFormat(ticket.getResolved()));
        AuditedEntityService.addToDTO(dto, ticket);
        return dto;
    }

    @PermitAll
    public Ticket toEntity(final SaveTicketDTO dto) {
        final Ticket entity = new Ticket();
        return updateEntity(entity, dto);
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    public Ticket updateEntity(final Ticket ticket, final SaveTicketDTO dto) {
        ticket.setContent(dto.getContent());
        ticket.setTitle(dto.getTitle());
        User assignedUser = null;
        if (dto.getAssignedUser() != null) {
            assignedUser = new User();
            assignedUser.setId(dto.getAssignedUser());
        }
        ticket.setAssignedUser(assignedUser);
        final Category category = new Category();
        category.setId(dto.getCategory());
        ticket.setCategory(category);
        ticket.setPriority(dto.getPriority());
        return ticket;
    }
}
