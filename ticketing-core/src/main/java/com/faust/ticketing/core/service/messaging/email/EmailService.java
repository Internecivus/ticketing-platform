package com.faust.ticketing.core.service.messaging.email;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.jms.Queue;

@Stateless
public class EmailService {
    @Resource(name = "java:jboss/DefaultJMSConnectionFactory")
    private QueueConnectionFactory connectionFactory;

    @Resource(name = EmailListener.JMS_QUEUE_EMAIL_JNDI)
    private Queue queue;

    @EJB
    private EmailComposer emailComposer;

    public void send(final EmailTemplate emailTemplate) throws Exception {
        final QueueConnection connection = connectionFactory.createQueueConnection();
        final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();

        final Email email = emailComposer.compose(emailTemplate);
        final ObjectMessage message = session.createObjectMessage(email);
        final QueueSender sender = session.createSender(queue);

        sender.send(message);

        session.close();
        connection.close();
    }
}
