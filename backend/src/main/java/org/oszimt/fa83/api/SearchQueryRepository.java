package org.oszimt.fa83.api;

import org.oszimt.fa83.pojo.SearchQuery;

import java.util.Collection;

public interface SearchQueryRepository<T> {

    Comparable<?> create(T t);

    Collection<T> findAll();

    void remove(Comparable<?> pk);

    T findByPk(Comparable<?> pk);
}
