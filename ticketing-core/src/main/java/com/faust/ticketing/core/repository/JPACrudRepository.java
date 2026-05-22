package com.faust.ticketing.core.repository;

import com.faust.ticketing.core.optional.Utility;
import com.faust.ticketing.persistence.DataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.internal.typetools.TypeResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.*;

public abstract class JPACrudRepository<T> implements CrudRepository<T> {

    @PersistenceContext(name = DataSource.Ticketing)
    protected EntityManager em;

    @SuppressWarnings("unchecked")
    private final Class<T> entityClass = (Class<T>) TypeResolver.resolveRawArgument(CrudRepository.class, getClass()); // Magic.

    @Override
    public T create(final T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T update(final T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(final Object id) {
        em.remove(em.find(entityClass, id));
    }

    @Override
    public Optional<T> findById(final Object id) {
        return Utility.findOrEmpty(id, () -> em.find(entityClass, id));
    }

    @Override
    public Set<T> findAll() {
        return findByFields(null);
    }

//    @Override
//    public Set<T> findByField(final FieldValuePair fieldValuePair) {
//        return findByFields(fieldValuePair);
//    }

    @Override
    public Collection<T> findByField(final FieldValuesPair fieldValuesPair) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(entityClass);
        final Root<T> root = query.from(entityClass);

        query.select(root);
        query.where(root.get(fieldValuesPair.getFieldName()).in(fieldValuesPair.getFieldValues()));
        return em.createQuery(query).getResultList();
    }

    @Override
    public Set<T> findByFields(final FieldValuePair... fieldValuePairs) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(entityClass);
        final Root<T> root = query.from(entityClass);

        query.select(root);

        if (!ArrayUtils.isEmpty(fieldValuePairs)) {
            final List<Predicate> predicates = new ArrayList<>();
            for (final FieldValuePair fieldValuePair : fieldValuePairs) {
                predicates.add(builder.equal(root.get(fieldValuePair.getFieldName()), fieldValuePair.getFieldValue()));
            }
            query.where(predicates.toArray(new Predicate[] {}));
        }
        return new HashSet<>(em.createQuery(query).getResultList());
    }

}
