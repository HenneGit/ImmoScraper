package org.oszimt.fa83.repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.FileUtils;
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
        File file = new File(FILE);
        System.out.println(file.getAbsolutePath());
        URL url;
        if (!file.exists()){
            url = getClass().getClassLoader().getResource(FILE);
        } else {
            url = file.toURI().toURL();
        }
        if (url != null) {
            try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
                return genericWriter.getBeans(reader, ScrapeQuery.class);
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
        if (url != null) {
            File dest = new File("");
            FileUtils.copyURLToFile(url, dest);
            try (OutputStream out = new FileOutputStream(url.getFile());
                 Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
                genericWriter.write(all, writer);
            }
        }
    }
}