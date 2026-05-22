package com.faust.ticketing.core.configuration;

public enum ServerMode {
    DEV, TEST, PROD;

    public static ServerMode getCurrent() {
        if (ServerMode.isDevelopment()) {
            return DEV;
        }
        else {
            return PROD;
        }
    }

    public static boolean isDevelopment() {
        return !"true".equals(System.getenv("production"));
    }
}
