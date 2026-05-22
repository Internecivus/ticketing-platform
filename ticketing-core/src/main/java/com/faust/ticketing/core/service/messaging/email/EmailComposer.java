package com.faust.ticketing.core.service.messaging.email;

import com.faust.ticketing.core.configuration.ServerMode;
import com.faust.ticketing.core.language.LanguageSelector;
import com.faust.ticketing.core.service.messaging.dto.DevMailDTO;
import com.faust.ticketing.core.service.messaging.template.Template;
import com.faust.ticketing.core.service.messaging.template.TemplateService;
import freemarker.ext.beans.ResourceBundleModel;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class EmailComposer {
    @Inject
    private LanguageSelector languageSelector;

    private final static String EMAIL_MESSAGES_BUNDLE_NAME = "EmailMessages";
    private static final String DEV_EMAIL_TO = System.getProperty("email.dev.to");
    private static final String DEV_EMAIL_FROM = System.getProperty("email.dev.from");
    private static final String DEFAULT_SUBJECT_KEY = "default_subject";
    private static final String DEFAULT_FROM_KEY = "default_from";

    @PostConstruct
    public void init() {
        Objects.requireNonNull(DEV_EMAIL_TO);
        Objects.requireNonNull(DEV_EMAIL_FROM);
    }

    public Email compose(final EmailTemplate emailTemplate) {
        validateEmail(emailTemplate);
        final Email email = composeEmail(emailTemplate);

        if (ServerMode.isDevelopment()) {
            convertToTestEmail(email);
        }
        return email;
    }

    private void validateEmail(final EmailTemplate email) {
        if (CollectionUtils.isEmpty(email.getTo()) && CollectionUtils.isEmpty(email.getCc())
                && CollectionUtils.isEmpty(email.getBcc())) {
            throw new RuntimeException("No email receipients specified");
        }
    }

    private Email composeEmail(final EmailTemplate emailTemplate) {
        final Email email = new Email();
        email.setSubject(extractSubject(emailTemplate));
        email.setFrom(extractFrom(emailTemplate));
        email.setBcc(emailTemplate.getBcc());
        email.setCc(emailTemplate.getCc());
        email.setMessage(composeMessage(emailTemplate));
        return email;
    }

    private String composeMessage(final EmailTemplate emailTemplate) {
        final Map<String, Object> emailData = new HashMap<>();
        emailData.put("data", emailTemplate.getDto());
        emailData.put("labels", emailMessages());
        final String message = TemplateService.process(emailTemplate.getTemplate(), emailData);

        final Map<String, Object> masterData = new HashMap<>();
        masterData.put("message", message);
        masterData.put("subject", extractSubject(emailTemplate));

        return TemplateService.process(extractMasterTemplate(emailTemplate), masterData);
    }

    private String extractSubject(final EmailTemplate email) {
        return Objects.requireNonNullElse(
                emailMessages().getString(getMessageSubject(email.getTemplate().getName())), getDefaultSubject());
    }

    private String getMessageSubject(final String name) {
        return name + "_subject";
    }

    private String extractFrom(final EmailTemplate email) {
        return Objects.requireNonNullElse(email.getFrom(), getDefaultFrom());
    }

    private Template extractMasterTemplate(final EmailTemplate email) {
        return Objects.requireNonNullElse(email.getMasterTemplate(), Template.DEFAULT_MASTER);
    }

    private ResourceBundle emailMessages() {
        return ResourceBundle.getBundle(EMAIL_MESSAGES_BUNDLE_NAME, languageSelector.getSelectedLocale());
    }

    private String getDefaultFrom() {
        return emailMessages().getString(DEFAULT_FROM_KEY);
    }

    private String getDefaultSubject() {
        return emailMessages().getString(DEFAULT_SUBJECT_KEY);
    }

    private void convertToTestEmail(final Email email) {
        email.setTo(Set.of(DEV_EMAIL_TO));
        email.setCc(new HashSet<>());
        email.setBcc(new HashSet<>());
        email.setFrom(DEV_EMAIL_FROM);

        final DevMailDTO dto = new DevMailDTO(email.getTo(), email.getCc(), email.getBcc(), email.getFrom());
        final Map<String, Object> data = new HashMap<>();
        data.put("data", dto);
        final String testMessage = TemplateService.process(Template.DEV, data);

        email.setMessage(email.getMessage().concat(testMessage));
    }
}
