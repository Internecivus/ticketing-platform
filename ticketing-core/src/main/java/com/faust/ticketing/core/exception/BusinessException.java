package com.faust.ticketing.core.exception;

import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BusinessException extends ApplicationException {
    private static final long serialVersionUID = 9087153945838279361L;

    public BusinessException(final ApplicationErrorCode code) {
        super(code);
    }

    public BusinessException(final ApplicationErrorCode code, final String additionalData) {
        super(code, additionalData);
    }

    public BusinessException(final ApplicationErrorCode code, final String additionalData, final Exception ex) {
        super(code, additionalData, ex);
    }

    public static BusinessException valueOf(final PersistenceException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof ConstraintViolationException) {
            final ConstraintViolationException constraintException = (ConstraintViolationException) cause;
            final Throwable causeOfCause = constraintException.getCause();
            if (causeOfCause instanceof SQLException) {
                final SQLException sqlException = (SQLException) causeOfCause;

                return new BusinessException(
                        ApplicationErrorCode.valueOfSQLState(sqlException.getSQLState()),
                        constraintException.getConstraintName(), e);
            }
        }
        return new BusinessException(ApplicationErrorCode.VALIDATION_GENERIC);
    }
}
