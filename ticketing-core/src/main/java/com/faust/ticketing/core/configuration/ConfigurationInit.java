package com.faust.ticketing.core.configuration;

import com.faust.security.utils.Generator;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Objects;

@Singleton
@Startup
public class ConfigurationInit {
    @PostConstruct
    public void init() {
        KeystoreManager.initKeystore();
    }
}
