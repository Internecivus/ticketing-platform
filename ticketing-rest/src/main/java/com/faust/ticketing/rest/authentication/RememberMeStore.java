package com.faust.ticketing.rest.authentication;

import com.faust.ticketing.core.exception.ApplicationException;
import com.faust.ticketing.core.exception.SystemException;
import com.faust.ticketing.core.service.RememberMeService;
import com.faust.ticketing.persistence.model.UserPrincipal;
import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.extern.log4j.Log4j2;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
@ApplicationScoped
public class RememberMeStore implements RememberMeIdentityStore {
    @EJB
    private RememberMeService rememberMeService;

    @Override
    public CredentialValidationResult validate(final RememberMeCredential credential) {
        try {
            final char[] token = credential.getToken().toCharArray();
            credential.clear();

            final Optional<User> optionalUser = rememberMeService.useToken(token);
            if (optionalUser.isPresent()) {
                final User user = optionalUser.get();
                return new CredentialValidationResult(new UserPrincipal(user), new HashSet<>(user.getRoleNamesAsString()));
            }
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        catch (final ApplicationException e) {
            log.throwing(e);
            return CredentialValidationResult.INVALID_RESULT;
        }
        catch (final Exception e) {
            log.throwing(e);
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    @Override
    public String generateLoginToken(final CallerPrincipal callerPrincipal, final Set<String> groups) {
        final User user = ((UserPrincipal) callerPrincipal).getUser();
        try {
            return new String(rememberMeService.createToken(user));
        }
        catch (final Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public void removeLoginToken(final String token) {
        try {
            rememberMeService.removeToken(token.toCharArray());
        }
        catch (final Exception e) {
            throw new SystemException(e);
        }
    }
}
