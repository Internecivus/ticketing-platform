package com.faust.ticketing.rest.authentication;

import com.faust.ticketing.core.exception.ApplicationException;
import com.faust.ticketing.core.service.CredentialsService;
import com.faust.ticketing.persistence.model.UserPrincipal;
import com.faust.ticketing.persistence.model.dto.client.LoginCredentialDTO;
import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;
import java.util.Optional;

@Log4j2
@ApplicationScoped
public class CredentialStore implements IdentityStore {
    @Inject
    private CredentialsService credentialsService;

    @Override
    public CredentialValidationResult validate(final Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            return validate((UsernamePasswordCredential) credential);
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }

    private CredentialValidationResult validate(final UsernamePasswordCredential credential) {
        try {
            final LoginCredentialDTO credentialDTO = new LoginCredentialDTO(credential.getCaller(), credential.getPasswordAsString());
            credential.clearCredential();

            final Optional<User> optionalUser = credentialsService.validateUsernamePassword(credentialDTO);
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
}
