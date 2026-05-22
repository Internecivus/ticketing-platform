package com.faust.ticketing.core.repository;

import com.faust.ticketing.core.optional.Utility;
import com.faust.ticketing.persistence.model.entity.user.Credentials;
import com.faust.ticketing.persistence.sql.NamedQuery;

import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class CredentialsRepository extends JPACrudRepository<Credentials> {
    public Optional<Credentials> findByUser(final Integer userId) {
        return Utility.findOrEmpty(() -> em.createNamedQuery(NamedQuery.FindCredentialsByUser.getName(), Credentials.class)
                .setParameter("userId", userId)
                .getSingleResult());
    }
}
