package com.faust.ticketing.core.service.messaging.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class TicketResolvedMailDTO {
    private final String userEmail;
    private final String ticketTitle;
    private final Instant stampResolved;
}
