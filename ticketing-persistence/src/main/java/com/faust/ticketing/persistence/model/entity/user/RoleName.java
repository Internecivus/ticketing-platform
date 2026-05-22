package com.faust.ticketing.persistence.model.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoleName {
    BASIC_FUNCTIONALITY(Constant.BASIC_FUNCTIONALITY),
    CATEGORY_ADMINISTRATION(Constant.CATEGORY_ADMINISTRATION),
    USER_ADMINISTRATION(Constant.USER_ADMINISTRATION),
    CATEGORY_USER_ASSIGNMENT(Constant.CATEGORY_USER_ASSIGNMENT),
    TICKET_ADMINISTRATION(Constant.TICKET_ADMINISTRATION);

    @Getter
    private String name;

    public static class Constant {
        public final static String BASIC_FUNCTIONALITY = "BASIC_FUNCTIONALITY";
        public final static String CATEGORY_ADMINISTRATION = "CATEGORY_ADMINISTRATION";
        public final static String USER_ADMINISTRATION = "USER_ADMINISTRATION";
        public final static String CATEGORY_USER_ASSIGNMENT = "CATEGORY_USER_ASSIGNMENT";
        public final static String TICKET_ADMINISTRATION = "TICKET_ADMINISTRATION";
    }
}
