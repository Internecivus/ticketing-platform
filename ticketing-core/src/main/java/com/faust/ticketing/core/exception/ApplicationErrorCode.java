package com.faust.ticketing.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApplicationErrorCode {
    NONE("000_0000"),
    USER_ALREADY_ASSIGNED_TO_CATEGORY("CAT_0001"),
    USER_NOT_FOUND("USR_0001"),
    PASSWORD_RESET_EXPIRED("PRT_0001"),
    PASSWORD_RESET_USED("PRT_0002"),
    PASSWORD_RESET_INVALID("PRT_0003"),
    PASSWORD_CHANGED_TO_CURRENT("PSW_0001"),
    VALIDATION_GENERIC("VAL_0001"),
    VALIDATION_CONSTRAINT("VAL_0002"),
    PASSWORD_REPEAT_DOES_NOT_MATCH("PWD_0002"),
    PASSWORD_RESET_ERROR("PWR_0001");

    @Getter
    private String name;

    public static ApplicationErrorCode valueOfSQLState(final String sqlStateCode) {
        switch (sqlStateCode) {
            case "23505":
                return VALIDATION_CONSTRAINT;
            default:
                throw new IllegalArgumentException("SQLState code of " + sqlStateCode + " not supported");
        }
    }
}
