package com.faust.ticketing.core.service.messaging.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DevMailDTO {
    private final Set<String> to;
    private final Set<String> cc;
    private final Set<String> bcc;
    private final String from;
}
