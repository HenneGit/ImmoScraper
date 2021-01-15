package org.oszimt.fa83.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.util.Collection;

public interface Repository<ENTITY extends Entity> {

    Comparable<?> create(ENTITY t);

    ENTITY update(ENTITY t);

    Collection<ENTITY> findAll();

    void remove(Comparable<?> pk);

    ENTITY findByPk(Comparable<?> pk);

    void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException;
}
