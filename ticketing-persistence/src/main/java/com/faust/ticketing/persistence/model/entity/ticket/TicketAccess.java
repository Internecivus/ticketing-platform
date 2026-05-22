package com.faust.ticketing.persistence.model.entity.ticket;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public enum TicketAccess {
    OPEN(TicketStatus.UNASSIGNED, TicketStatus.ASSIGNED), CLOSED(TicketStatus.RESOLVED);

    @Getter
    private final List<TicketStatus> statuses;

    TicketAccess(@NonNull final TicketStatus... statuses) {
        this.statuses = Arrays.asList(statuses);
    }

    public static TicketAccess valueOf(final TicketStatus status) {
        return Arrays.stream(values()).filter(access -> access.statuses.contains(status)).findAny().orElse(null);
    }
}
