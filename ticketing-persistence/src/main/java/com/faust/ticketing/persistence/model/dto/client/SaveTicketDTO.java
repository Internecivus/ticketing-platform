package com.faust.ticketing.persistence.model.dto.client;

import com.faust.ticketing.persistence.model.entity.ticket.TicketPriority;
import com.faust.ticketing.persistence.model.entity.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SaveTicketDTO {
    private String content;
    private String title;
    private Integer assignedUser;
    private Integer category;
    private TicketPriority priority;
}
