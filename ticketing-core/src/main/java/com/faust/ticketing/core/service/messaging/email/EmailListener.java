package com.faust.ticketing.core.service.messaging.email;

import com.faust.ticketing.core.configuration.ServerConfiguration;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = EmailListener.JMS_QUEUE_EMAIL_JNDI),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class EmailListener implements MessageListener {
    @Resource(mappedName = MAIL_SESSION_JNDI)
    private Session mailSession;

    static final String JMS_QUEUE_EMAIL_JNDI = "java:/jms/queue/Ticketing";
    private static final String CONTENT_TYPE = "text/html;charset=\"" + ServerConfiguration.CHARSET_NAME + "\"";
    private static final String MAIL_SESSION_JNDI = "java:/mail/Ticketing";

    @Override
    public void onMessage(final Message message) {
        try {
            final Email email = (Email) ((ObjectMessage) message).getObject();
            final MimeMessage mimeMessage = createMessage(email);
            Transport.send(mimeMessage);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage createMessage(final Email email) throws MessagingException {
        final MimeMessage mimeMessage = new MimeMessage(mailSession);

        mimeMessage.setFrom(new InternetAddress(email.getFrom()));
        mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, convertStringToAddress(email.getTo()));
        mimeMessage.setRecipients(javax.mail.Message.RecipientType.CC, convertStringToAddress(email.getCc()));
        mimeMessage.setRecipients(javax.mail.Message.RecipientType.BCC, convertStringToAddress(email.getBcc()));

        mimeMessage.setSubject(email.getSubject());
        mimeMessage.setContent(email.getMessage(), CONTENT_TYPE);
        mimeMessage.setHeader("Content-Type", CONTENT_TYPE);

        return mimeMessage;
    }

    private Address[] convertStringToAddress(final Set<String> emails) {
        final InternetAddress[] addresses = new InternetAddress[emails.size()];
        int i = 0;
        for (final String email : emails) {
            try {
                addresses[i++] = new InternetAddress(email);
            } catch (final AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return addresses;
    }
}
