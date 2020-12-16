package org.oszimt.fa83.api;

import java.util.Collection;

public interface Repository<ENTITY extends Entity> {

    Comparable<?> create(ENTITY t);

    Collection<ENTITY> findAll();

    void remove(Comparable<?> pk);

    ENTITY findByPk(Comparable<?> pk);
}
