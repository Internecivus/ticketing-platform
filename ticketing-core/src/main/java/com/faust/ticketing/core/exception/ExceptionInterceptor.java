package com.faust.ticketing.core.exception;

import com.faust.ticketing.core.configuration.ServerMode;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.Serializable;

@Throws
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class ExceptionInterceptor implements Serializable {
    private static final long serialVersionUID = 2533242119824555332L;

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object checkForExceptions(final InvocationContext invocationContext) {
        try {
            return invocationContext.proceed();
        }
        catch (final Throwable e) {
            handleThrowsException(e, invocationContext);

            if (e instanceof EntityNotFoundException || e instanceof NoResultException) {
                throw new NotFoundException();
            }
            else if (e instanceof PersistenceException) {
                throw BusinessException.valueOf((PersistenceException) e);
            }
            // SystemException at this point means something is wrong in the Core Module itself, so any details are withheld.
            else if (e instanceof SystemException) {
                logger.throwing(e);
                throw new SystemException();
            }
            // BusinessExceptions and other application exceptions have necessary information so we rethrow them.
            else if (e instanceof ApplicationException) {
                logger.throwing(e);
                throw (ApplicationException) e;
            }
            // We don't want to propagate any other exceptions, as this could be a security issue.
            else {
                logger.throwing(e);
                throw new SystemException();
            }
        }
    }

    private void handleThrowsException(final Throwable e, final InvocationContext invocationContext) {
        final Throws throwsAnnotation = invocationContext.getMethod().getAnnotation(Throws.class);
        if (throwsAnnotation != null && !throwsAnnotation.errorCode().equals(ApplicationErrorCode.NONE)) {
            logger.throwing(e);
            throw new BusinessException(throwsAnnotation.errorCode());
        }
    }
}
