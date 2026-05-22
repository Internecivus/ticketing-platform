package com.faust.ticketing.core.optional;

import javax.persistence.NoResultException;
import java.util.Optional;

public class Utility {
    public static <T> Optional<T> findOrEmpty(final DaoRetriever<T> retriever) {
        try {
            return Optional.of(retriever.retrieve());
        }
        catch (final NoResultException ignore) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> findOrEmpty(final Object id, final DaoRetriever<T> retriever) {
        if (id == null) {
            return Optional.empty();
        }
        return findOrEmpty(retriever);
    }
}
