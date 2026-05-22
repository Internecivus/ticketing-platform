package com.faust.ticketing.core.service;

import com.faust.ticketing.core.configuration.CredentialsConfiguration;
import com.faust.ticketing.core.configuration.KeystoreManager;
import com.faust.ticketing.core.exception.SystemException;
import com.faust.ticketing.core.repository.CredentialsRepository;
import com.faust.ticketing.core.repository.UserRepository;
import com.faust.ticketing.persistence.model.dto.client.LoginCredentialDTO;
import com.faust.ticketing.persistence.model.entity.user.Credentials;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import com.faust.ticketing.persistence.model.entity.user.User;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;

@Stateless
public class CredentialsService {
    @EJB
    private CredentialsRepository credentialsRepository;

    @EJB
    private UserRepository userRepository;

    @EJB
    private CredentialsConfiguration credentialsConfiguration;

    @Inject
    private Logger logger;

    @PermitAll
    public Optional<User> validateUsernamePassword(final LoginCredentialDTO loginCredentials) throws Exception {
        final Optional<User> optionalUser = userRepository.findByUsernameOrEmail(loginCredentials.getUsernameOrEmail());
        if (optionalUser.isEmpty()) {
            credentialsConfiguration.getCryptography().fauxValidatePassword();
            logger.info("No user found for username/email {}", loginCredentials.getUsernameOrEmail());
            return Optional.empty();
        }

        final User user = optionalUser.get();
        final Credentials credentials = credentialsRepository.findByUser(user.getId()).orElseThrow();

        final byte[] pepper = KeystoreManager.getPepper(credentials.getPepperAlias());

        final boolean isValid = credentialsConfiguration.getCryptography().validatePassword(
                loginCredentials.getPassword().toCharArray(), credentials.getPassword().clone(), pepper);

        if (isValid) {
            logger.info("Successful password validation for user id {}", user.getId());
            return optionalUser;
        }
        logger.info("Provided password not valid for user id {}", user.getId());
        return Optional.empty();
    }

    @RolesAllowed(RoleName.Constant.USER_ADMINISTRATION)
    public void createRandomCredentials(final User user) throws Exception {
        //final String randomPassword = Generator.generateSecureRandomBase64String(securityConfiguration.DEFAULT_PASSWORD_SIZE);
        final Credentials credentials = new Credentials();
        final byte[] pepper = KeystoreManager.getDefaultPepper();

        //credentials.setPassword(passwordCryptography.withPepper(pepper).hashPassword(randomPassword));
        credentials.setPepperAlias(KeystoreManager.getDefaultPepperAlias());
        credentials.setUser(user);
        credentialsRepository.create(credentials);
    }

    @PermitAll
    public void createCredentials(final User user, final String password) throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setPassword(credentialsConfiguration.getCryptography().hashPassword(password.toCharArray(), KeystoreManager.getDefaultPepper()));
        credentials.setPepperAlias(KeystoreManager.getDefaultPepperAlias());
        credentials.setUser(user);
        credentialsRepository.create(credentials);
    }

    @PermitAll
    public void updatePassword(final User user, final String password) throws Exception {
        final Credentials credentials = credentialsRepository.findByUser(user.getId()).orElseThrow();
        credentials.setPassword(credentialsConfiguration.getCryptography().hashPassword(password.toCharArray(), KeystoreManager.getDefaultPepper()));
        credentialsRepository.update(credentials);
    }
}
