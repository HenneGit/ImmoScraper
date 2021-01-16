package org.oszimt.fa83.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.repository.CSVNotFoundException;

import java.io.IOException;
import java.util.Collection;

public interface Repository<ENTITY extends Entity> {

    void create(ENTITY t);

    Collection<ENTITY> findAll() throws CSVNotFoundException;

    ENTITY findByPk(Comparable<?> pk) throws CSVNotFoundException;

    void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException;
}
