package com.faust.ticketing.core.exception;

import com.faust.ticketing.core.language.LanguageSelector;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@javax.ejb.ApplicationException(rollback = true)
public class ApplicationException extends EJBException {
    private static final long serialVersionUID = -4497885516183585260L;

    private LanguageSelector languageSelector;

    private final static ApplicationErrorCode defaultErrorCode = ApplicationErrorCode.NONE;
    private final static String EXCEPTION_MESSAGES_BUNDLE_NAME = "ExceptionMessages";
    private final static String DEV_MESSAGE_SUFFIX = "_DEV";

    public ApplicationException() {
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(null);
    }

    public ApplicationException(final ApplicationErrorCode code) {
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(code);
    }

    public ApplicationException(final ApplicationErrorCode code, final String additionalData) {
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(code);
        this.additionalData = additionalData;
    }

    public ApplicationException(final ApplicationErrorCode code, final String additionalData, final Exception ex) {
        super(ex);
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(code);
        this.additionalData = additionalData;
    }

    public ApplicationException(final String message) {
        super(message);
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(null);
    }

    public ApplicationException(final Exception ex) {
        super(ex);
        this.languageSelector = CDI.current().select(LanguageSelector.class).get();
        setCode(null);
    }

    @Getter
    private ApplicationErrorCode code;

    @Getter
    private String additionalData;

    @Getter
    private Instant timestamp;

    private void setCode(final ApplicationErrorCode code) {
        this.code = code != null ? code : defaultErrorCode;
    }

    private ResourceBundle exceptionMessages() {
        return ResourceBundle.getBundle(EXCEPTION_MESSAGES_BUNDLE_NAME, languageSelector.getSelectedLocale());
    }

    public String getMessage() {
        return code == null ? null : exceptionMessages().getString(getKeyName(code, additionalData));
    }

    public String getDevMessage() {
        return code == null ? null : exceptionMessages().getString(getKeyName(code, additionalData) + DEV_MESSAGE_SUFFIX);
    }

    private String getKeyName(final ApplicationErrorCode code, final String additionalData) {
        return code.name() + (additionalData == null ? "" : "_" + additionalData);
    }
}
