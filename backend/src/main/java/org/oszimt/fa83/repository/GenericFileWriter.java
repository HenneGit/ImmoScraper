package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * class for accessing and writing files.
 */
public class GenericFileWriter {

    private static GenericFileWriter instance = new GenericFileWriter();
    private GenericCSVWriter<Entity> genericWriter = new GenericCSVWriter<>();



    private GenericFileWriter() {
    }

    public static GenericFileWriter getInstance() {
        return instance;
    }

    /**
     * loads all search queries from file.
     *
     * @return collection of all search queries saved in file.
     */
    Collection<? extends Entity> findAll(String fileName, Class<? extends Entity> clazz) throws CSVNotFoundException {

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName))) {
            return genericWriter.getBeans(reader, clazz);
        } catch (Exception e){
            throw new CSVNotFoundException();
        }
    }

    /**
     * write all given search queries to file.
     */
    public void write(List<Entity> all, String fileName) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            final Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            genericWriter.write(all, writer);
            writer.close();
        }
    }
}