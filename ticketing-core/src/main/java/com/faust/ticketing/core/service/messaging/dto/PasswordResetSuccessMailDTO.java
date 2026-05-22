package com.faust.ticketing.core.service.messaging.dto;

import com.faust.ticketing.core.configuration.ServerConfiguration;
import lombok.Data;

import java.text.MessageFormat;

@Data
public class PasswordResetSuccessMailDTO {
    private final String loginLink;

    public PasswordResetSuccessMailDTO() {
        this.loginLink = MessageFormat.format("{0}/login", ServerConfiguration.DOMAIN_NAME);
    }
}
