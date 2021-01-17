package org.oszimt.fa83.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.repository.CSVNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

/**
 * generic interface vor repositories.
 * @param <ENTITY>
 */
public interface Repository<ENTITY extends Entity> {

    /**
     * create new entity.
     * @param t the entity to create.
     * @return the created entity.
     * @throws CSVNotFoundException thrown if csv of filewriter is not found.
     */
    ENTITY create(ENTITY t) throws CSVNotFoundException;

    /**
     * find all entities in repositor
     * @return all entities.
     * @throws CSVNotFoundException thrown if csv of filewriter is not found.
     */
    Collection<ENTITY> findAll() throws CSVNotFoundException;

    /**
     * find enitity by primary key
     * @param pk primary key of the entity to find.
     * @return the found entity.
     * @throws CSVNotFoundException thrown if csv of filewriter is not found.
     */
    ENTITY findByPk(Comparable<?> pk) throws CSVNotFoundException;

    /**
     * write repository to file.
     * @throws CsvRequiredFieldEmptyException
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     */
    void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException;
}
