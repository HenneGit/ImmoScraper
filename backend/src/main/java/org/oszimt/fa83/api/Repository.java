package org.oszimt.fa83.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.repository.CSVNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public interface Repository<ENTITY extends Entity> {

    Comparable<?> create(ENTITY t);

    ENTITY update(ENTITY t) throws CSVNotFoundException;

    Collection<ENTITY> findAll() throws CSVNotFoundException;

    void remove(Comparable<?> pk);

    ENTITY findByPk(Comparable<?> pk) throws CSVNotFoundException;

    void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException;
}
