package com.faust.ticketing.core.configuration;

import com.faust.security.utils.Generator;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public final class KeystoreManager {
    private static KeyStore keyStore;
    private final static char[] KEYSTORE_PRIVATE_KEY = System.getProperty("keystore.private_key").toCharArray();
    private final static String KEYSTORE_FOLDER = System.getProperty("jboss.server.config.dir");
    private final static String KEYSTORE_NAME = System.getProperty("keystore.name");
    private final static int PEPPER_BYTE_SIZE = 32;
    private final static char[] PEPPERS_PRIVATE_KEY = System.getProperty("keystore.peppers.private_key").toCharArray();
    private final static String DEFAULT_PEPPER_ALIAS = System.getProperty("keystore.peppers.default.alias");

    static void initKeystore() {
        if (keyStore != null) {
            return;
        }
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            final File keystoreFile = new File(KEYSTORE_FOLDER + File.separator + KEYSTORE_NAME);
            if (!keystoreFile.exists()) {
                keyStore.load(null, KEYSTORE_PRIVATE_KEY);
                try (final FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                    createDefaultPepper();
                    keyStore.store(fos, KEYSTORE_PRIVATE_KEY);
                }
            }
            else {
                try (final FileInputStream fis = new FileInputStream(keystoreFile)) {
                    keyStore.load(fis, KEYSTORE_PRIVATE_KEY);
                }
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Error initiating the keystore", e);
        }
    }

    private static void createDefaultPepper() {
        try {
            final byte[] pepper = Generator.generateSecureRandomBytes(PEPPER_BYTE_SIZE * Byte.BYTES);
            final KeyStore.SecretKeyEntry secret =  new KeyStore.SecretKeyEntry(new SecretKeySpec(pepper, "PBKDF2WithHmacSHA1"));
            final KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(PEPPERS_PRIVATE_KEY);
            keyStore.setEntry(DEFAULT_PEPPER_ALIAS, secret, password);
        }
        catch (final Exception e) {
            throw new RuntimeException("Error creating the default pepper", e);
        }
    }

    public static byte[] getPepper(final String pepperAlias) throws Exception {
        return keyStore.getKey(pepperAlias, PEPPERS_PRIVATE_KEY).getEncoded();
    }

    public static byte[] getDefaultPepper() throws Exception {
        return getPepper(DEFAULT_PEPPER_ALIAS);
    }

    public static String getDefaultPepperAlias() {
        return DEFAULT_PEPPER_ALIAS;
    }
}
