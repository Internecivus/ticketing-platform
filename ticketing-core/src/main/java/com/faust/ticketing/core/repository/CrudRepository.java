package com.faust.ticketing.core.repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T> {
    T create(final T entity);

    T update(final T entity);

    void delete(final Object id);

    Optional<T> findById(final Object id);

    Collection<T> findAll();

    Collection<T> findByField(final FieldValuesPair fieldValuesPair);

    Collection<T> findByFields(final FieldValuePair... fieldValuePairs);
}
