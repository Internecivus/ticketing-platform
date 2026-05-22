package com.faust.ticketing.core.repository;

import com.faust.ticketing.persistence.model.entity.user.PasswordResetToken;
import com.faust.ticketing.persistence.model.entity.user.User;
import com.faust.ticketing.persistence.sql.NamedQuery;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PasswordResetTokenRepository extends JPACrudRepository<PasswordResetToken> {
    public List<PasswordResetToken> findByUser(final User user) {
        return em.createNamedQuery(NamedQuery.FindPasswordResetTokensByUser.getName(), PasswordResetToken.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }
}

