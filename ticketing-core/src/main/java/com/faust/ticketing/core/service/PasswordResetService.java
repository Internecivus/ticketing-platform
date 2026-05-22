package com.faust.ticketing.core.service;

import com.faust.security.exception.SecurityException;
import com.faust.security.utils.CharUtils;
import com.faust.security.utils.Generator;
import com.faust.ticketing.core.configuration.KeystoreManager;
import com.faust.ticketing.core.configuration.PasswordResetConfiguration;
import com.faust.ticketing.core.exception.ApplicationErrorCode;
import com.faust.ticketing.core.exception.BusinessException;
import com.faust.ticketing.core.exception.Throws;
import com.faust.ticketing.core.repository.PasswordResetTokenRepository;
import com.faust.ticketing.core.repository.UserRepository;
import com.faust.ticketing.core.service.messaging.MessageService;
import com.faust.ticketing.persistence.model.dto.client.CreatePasswordResetDTO;
import com.faust.ticketing.persistence.model.dto.client.UsePasswordResetDTO;
import com.faust.ticketing.persistence.model.entity.user.PasswordResetToken;
import com.faust.ticketing.persistence.model.entity.user.User;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@DenyAll
@Stateless
public class PasswordResetService {
    @EJB
    private UserRepository userRepository;

    @EJB
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @EJB
    private PasswordResetConfiguration passwordResetConfiguration;

    @EJB
    private CredentialsService credentialsService;

    @EJB
    private MessageService messageService;

    @Inject
    private Logger logger;

    @PermitAll
    @Asynchronous
    public Future<Void> createAndSendToken(final CreatePasswordResetDTO dto) throws Exception {
        final User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        final char[] generatedPassword = Generator.generateSecureRandomUrlSafeCharArray(
                PasswordResetConfiguration.PASSWORD_CHAR_SIZE);
        final char[] hash = passwordResetConfiguration.getCryptography().hashPassword(
                generatedPassword.clone(), KeystoreManager.getDefaultPepper());

        final PasswordResetToken token = new PasswordResetToken();
        token.setExpiration(Instant.now().plus(PasswordResetConfiguration.resetPasswordDuration));
        token.setHash(hash);
        token.setPepperAlias(KeystoreManager.getDefaultPepperAlias());
        token.setUser(user);

        expireAllTokensForUser(user);
        passwordResetTokenRepository.create(token);

        final char[] tokenHash = createTokenHash(token.getId(), generatedPassword);
        messageService.passwordResetLink(user, tokenHash);

        logger.info("Created ResetPassword token id {}", token.getId());
        return new AsyncResult<>(null);
    }

    private void expireAllTokensForUser(final User user) {
        final List<PasswordResetToken> tokens = passwordResetTokenRepository.findByUser(user);
        for (final PasswordResetToken token : tokens) {
            if (token.canBeUsed()) {
                token.setRemoved(Instant.now());
                passwordResetTokenRepository.update(token);
            }
        }
    }

    private char[] createTokenHash(final Integer tokenId, final char[] generatedPassword) throws SecurityException {
        return CharUtils.base64EncodeAndDestroyCharsToChars(CharUtils.combineAndDestroy(
                CharUtils.base64EncodeAndDestroyCharsToChars(tokenId.toString().toCharArray()),
                new char[]{ PasswordResetConfiguration.TOKEN_DELIMITER },
                CharUtils.base64EncodeAndDestroyCharsToChars(generatedPassword)));
    }

    @PermitAll
    @Throws(errorCode = ApplicationErrorCode.PASSWORD_RESET_ERROR)
    public void useToken(final UsePasswordResetDTO dto) throws Exception {
        final PasswordResetToken token = findValidToken(dto.getTokenHash()).orElseThrow();
        credentialsService.updatePassword(token.getUser(), dto.getNewPassword());
        token.setUsed(Instant.now());
        passwordResetTokenRepository.update(token);
        messageService.passwordResetSuccess(token.getUser());

        logger.info("Used password reset token id {}", token.getId());
    }

    private Optional<PasswordResetToken> findValidToken(final String tokenHash) throws Exception {
        final char[] decodedTokenHash = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHash.toCharArray());
        final char[][] tokenHashParts = CharUtils.splitAndDestroy(
                decodedTokenHash, PasswordResetConfiguration.TOKEN_DELIMITER);
        final char[] tokenIdStr = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHashParts[0]);
        final char[] hash = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHashParts[1]);

        final Integer tokenId = Integer.valueOf(new String(tokenIdStr));
        final Optional<PasswordResetToken> optionalToken = findUsableToken(tokenId);
        if (optionalToken.isEmpty()) {
            return optionalToken;
        }
        final PasswordResetToken token = optionalToken.get();

        final boolean isValid = passwordResetConfiguration.getCryptography().validatePassword(hash,
                token.getHash().clone(), KeystoreManager.getPepper(token.getPepperAlias()));
        if (isValid) {
            return Optional.of(token);
        }
        else {
            logger.info("Provided token hash not valid for password reset token id {}", token.getId());
            return Optional.empty();
        }
    }

    private Optional<PasswordResetToken> findUsableToken(final Integer tokenId) {
        final Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findById(tokenId);
        if (optionalToken.isEmpty()) {
            logger.info("No password reset token found for id {}", tokenId);
            return Optional.empty();
        }

        final PasswordResetToken token = optionalToken.get();
        if (token.hasExpired()) {
            logger.info("Tried to use expired password reset token id {}", tokenId);
        }
        else if (token.isRemoved()) {
            logger.info("Tried to use removed password reset token id {}", tokenId);
        }
        else if (token.isUsed()) {
            logger.info("Tried to use already used password reset token id {}", tokenId);
        }
        else if (token.canBeUsed()) {
            return optionalToken;
        }
        return Optional.empty();
    }
}
