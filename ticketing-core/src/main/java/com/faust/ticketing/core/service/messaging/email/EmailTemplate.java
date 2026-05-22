package com.faust.ticketing.core.service.messaging.email;

import com.faust.ticketing.core.service.messaging.template.Template;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.*;

@Data
public class EmailTemplate extends Email {
    private static final long serialVersionUID = 5250638164335649128L;

    @NonNull
    private Template template;
    private Template masterTemplate;
    private Object dto;

    public EmailTemplate(final Set<String> to, final Template template, final Object dto) {
        super.setTo(to);
        this.template = template;
        this.dto = dto;
        Objects.requireNonNull(template);
    }
}
