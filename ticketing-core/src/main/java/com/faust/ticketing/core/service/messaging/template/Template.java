package com.faust.ticketing.core.service.messaging.template;

public enum Template {
    DEFAULT_MASTER("default"),
    DEV("dev"),
    PASSWORD_RESET_LINK("passwordResetLink"),
    PASSWORD_RESET_SUCCESS("passwordResetSuccess");

    private final String name;

    Template(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
