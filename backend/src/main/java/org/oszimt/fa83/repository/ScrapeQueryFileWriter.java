package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.api.GenericCSVWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class ScrapeQueryFileWriter {

    private final String FILE = "queries.csv";
    private static ScrapeQueryFileWriter instance = new ScrapeQueryFileWriter();
    private GenericCSVWriter<ScrapeQuery> genericWriter = new GenericCSVWriter<>();

    private ScrapeQueryFileWriter() {
    }

    public static ScrapeQueryFileWriter getInstance() {
        return instance;
    }

    /**
     * loads all search queries from file.
     *
     * @return collection of all search queries saved in file.
     */
    Collection<ScrapeQuery> findAll() throws CSVNotFoundException {

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FILE))) {
            return genericWriter.getBeans(reader, ScrapeQuery.class);
        } catch (Exception e){
            throw new CSVNotFoundException();
        }
    }

    /**
     * write all given search queries to file.
     */
    public void write(List<ScrapeQuery> all) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (FileOutputStream out = new FileOutputStream(FILE)) {
            final Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            genericWriter.write(all, writer);
            writer.close();
        }
    }
}