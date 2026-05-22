package com.faust.ticketing.core.repository;

import com.faust.ticketing.persistence.model.entity.user.RememberMeToken;

import javax.ejb.Stateless;

@Stateless
public class RememberMeTokenRepository extends JPACrudRepository<RememberMeToken> {
}
