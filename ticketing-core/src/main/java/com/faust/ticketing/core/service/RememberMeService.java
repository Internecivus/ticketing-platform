package com.faust.ticketing.core.service;

import com.faust.security.cryptography.Cryptography;
import com.faust.security.cryptography.CryptographyConfiguration;
import com.faust.security.exception.SecurityException;
import com.faust.security.utils.CharUtils;
import com.faust.security.utils.Generator;
import com.faust.ticketing.core.configuration.KeystoreManager;
import com.faust.ticketing.core.configuration.PasswordResetConfiguration;
import com.faust.ticketing.core.configuration.RememberMeConfiguration;
import com.faust.ticketing.core.exception.SystemException;
import com.faust.ticketing.core.repository.RememberMeTokenRepository;
import com.faust.ticketing.persistence.model.entity.user.PasswordResetToken;
import com.faust.ticketing.persistence.model.entity.user.RememberMeToken;
import com.faust.ticketing.persistence.model.entity.user.User;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@DenyAll
@Stateless
public class RememberMeService {
    @Inject
    private Logger logger;

    @EJB
    private RememberMeTokenRepository rememberMeTokenRepository;

    @EJB
    private RememberMeConfiguration rememberMeConfiguration;

    @PermitAll
    public char[] createToken(final User user) throws Exception {
        final char[] generatedPassword = Generator.generateSecureRandomUrlSafeCharArray(
                rememberMeConfiguration.TOKEN_BYTE_SIZE);
        final char[] hash = rememberMeConfiguration.getCryptography().hashPassword(generatedPassword.clone(),
                KeystoreManager.getDefaultPepper());

        final RememberMeToken token = new RememberMeToken();
        token.setHash(hash);
        token.setPepperAlias(KeystoreManager.getDefaultPepperAlias());
        token.setUser(user);

        rememberMeTokenRepository.create(token);

        final char[] tokenHash = createTokenHash(token.getId(), generatedPassword);

        logger.info("Created remember me token id {}", token.getId());
        return tokenHash;
    }

    private char[] createTokenHash(final Integer tokenId, final char[] generatedPassword) throws SecurityException {
        return CharUtils.base64EncodeAndDestroyCharsToChars(CharUtils.combineAndDestroy(
                CharUtils.base64EncodeAndDestroyCharsToChars(tokenId.toString().toCharArray()),
                new char[] { rememberMeConfiguration.TOKEN_DELIMITER },
                generatedPassword));
    }

    @PermitAll
    public void removeToken(final char[] providedToken) throws Exception {
        final Optional<RememberMeToken> optionalToken = findValidToken(providedToken);
        if (optionalToken.isEmpty()) {
            return;
        }
        final RememberMeToken token = optionalToken.get();

        token.setRemoved(Instant.now());
        rememberMeTokenRepository.update(token);

        logger.info("Removed remember me token id {}", token.getId());
    }

    @PermitAll
    public Optional<User> useToken(final char[] tokenHash) throws Exception {
        final Optional<RememberMeToken> optionalToken = findValidToken(tokenHash);
        if (optionalToken.isEmpty()) {
            return Optional.empty();
        }
        final RememberMeToken token = optionalToken.get();
        token.setUsed(Instant.now());
        rememberMeTokenRepository.update(token);

        logger.info("Used remember me token id {}", token.getId());

        return Optional.of(token.getUser());
    }

    private Optional<RememberMeToken> findValidToken(final char[] tokenHash) throws Exception {
        final char[] decodedTokenHash = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHash);
        final char[][] tokenHashParts = CharUtils.splitAndDestroy(decodedTokenHash, rememberMeConfiguration.TOKEN_DELIMITER);
        final char[] tokenIdStr = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHashParts[0]);
        final char[] hash = CharUtils.base64DecodeAndDestroyCharsToChars(tokenHashParts[1]);

        final Integer tokenId = Integer.valueOf(new String(tokenIdStr));
        final Optional<RememberMeToken> optionalToken = findUsableToken(tokenId);
        if (optionalToken.isEmpty()) {
            return optionalToken;
        }
        final RememberMeToken token = optionalToken.get();

        final boolean isValid = rememberMeConfiguration.getCryptography().validatePassword(hash,
                token.getHash().clone(), KeystoreManager.getPepper(token.getPepperAlias()));
        if (isValid) {
            logger.info("Successful validation for remember me token id {}", token.getId());
            return Optional.of(token);
        }
        else {
            logger.info("Provided token hash not valid for remember me token id {}", token.getId());
            return Optional.empty();
        }
    }

    private Optional<RememberMeToken> findUsableToken(final Integer tokenId) {
        final Optional<RememberMeToken> optionalToken = rememberMeTokenRepository.findById(tokenId);
        if (optionalToken.isEmpty()) {
            logger.info("No remember me token found for id {}", tokenId);
            return Optional.empty();
        }

        final RememberMeToken token = optionalToken.get();
        if (token.isRemoved()) {
            logger.info("Tried to use removed remember me token id {}", tokenId);
        }
        else if (token.canBeUsed()) {
            return optionalToken;
        }
        return Optional.empty();
    }
}
