package org.oszimt.fa83.repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.api.GenericCSVWriter;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    Collection<ScrapeQuery> findAll() throws IOException {
        URL url = getClass().getClassLoader().getResource(FILE);
        if (url != null) {
            try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
                Collection<ScrapeQuery> beans = genericWriter.getBeans(reader, ScrapeQuery.class);
                return beans;
            }
        }
        return null;
    }

    /**
     * write all given search queries to file.
     *
     */
    public void write(List<ScrapeQuery> all) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        URL url = getClass().getClassLoader().getResource(FILE);

        try (OutputStream out = new FileOutputStream(url.getFile());
             Writer writer = new OutputStreamWriter(out,"UTF-8")) {
            genericWriter.write(all, writer);
        }
    }
}