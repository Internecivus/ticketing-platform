package com.faust.ticketing.core.repository;

import com.faust.ticketing.core.graph.GraphHelper;
import com.faust.ticketing.core.optional.Utility;
import com.faust.ticketing.persistence.model.entity.user.User;
import com.faust.ticketing.persistence.sql.NamedEntityGraph;
import com.faust.ticketing.persistence.sql.NamedQuery;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Stateless
public class UserRepository extends JPACrudRepository<User> {
    public Optional<User> findByUsernameOrEmail(final String usernameOrEmail) {
        return Utility.findOrEmpty(() -> em.createNamedQuery(NamedQuery.FindUserByUsernameOrEmail.getName(), User.class)
                .setParameter("usernameOrEmail", usernameOrEmail)
                .setHint(GraphHelper.FETCHGRAPH, em.getEntityGraph(NamedEntityGraph.UserWithAllAndGroupRoles.getName()))
                .getSingleResult());
    }

    public Optional<User> findByUsername(final String username) {
        return Utility.findOrEmpty(() -> em.createNamedQuery(NamedQuery.FindUserByUsername.getName(), User.class)
                .setParameter("username", username)
                .getSingleResult());
    }

    public Optional<User> findByEmail(final String email) {
        return Utility.findOrEmpty(() -> em.createNamedQuery(NamedQuery.FindUserByEmail.getName(), User.class)
                .setParameter("email", email)
                .getSingleResult());
    }
}
