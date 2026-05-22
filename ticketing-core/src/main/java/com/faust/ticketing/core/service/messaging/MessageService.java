package com.faust.ticketing.core.service.messaging;

import com.faust.ticketing.core.service.messaging.dto.PasswordResetLinkMailDTO;
import com.faust.ticketing.core.service.messaging.dto.PasswordResetSuccessMailDTO;
import com.faust.ticketing.core.service.messaging.email.EmailTemplate;
import com.faust.ticketing.core.service.messaging.email.EmailService;
import com.faust.ticketing.core.service.messaging.template.Template;
import com.faust.ticketing.persistence.model.entity.ticket.Ticket;
import com.faust.ticketing.persistence.model.entity.user.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;

@Stateless
public class MessageService {
    @EJB
    private EmailService emailService;

    public void passwordResetLink(final User user, final char[] hash) throws Exception {
        emailService.send(new EmailTemplate(Set.of(user.getEmail()), Template.PASSWORD_RESET_LINK, new PasswordResetLinkMailDTO(hash)));
    }

    public void passwordResetSuccess(final User user) throws Exception {
        emailService.send(new EmailTemplate(Set.of(user.getEmail()), Template.PASSWORD_RESET_SUCCESS, new PasswordResetSuccessMailDTO()));
    }
}
