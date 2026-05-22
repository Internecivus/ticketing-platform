package com.faust.ticketing.core.service.messaging.dto;

import com.faust.ticketing.core.configuration.ServerConfiguration;
import lombok.Data;

import javax.ws.rs.core.Application;
import java.text.MessageFormat;

@Data
public class PasswordResetLinkMailDTO {
    private final String resetLink;

    public PasswordResetLinkMailDTO(final char[] hash) {
        this.resetLink = MessageFormat.format(
                "{0}/changePassword?token={1}", ServerConfiguration.DOMAIN_NAME, new String(hash));
    }
}
