package com.faust.ticketing.persistence.model;

import com.faust.ticketing.persistence.model.entity.user.User;
import lombok.Getter;

import javax.security.enterprise.CallerPrincipal;

public class UserPrincipal extends CallerPrincipal {
    @Getter
    private final User user;

    public UserPrincipal(final User user) {
        super(user.getUsername());
        this.user = user;
    }
}
