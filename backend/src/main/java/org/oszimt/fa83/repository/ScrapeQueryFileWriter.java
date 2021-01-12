package org.oszimt.fa83.repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ScrapeQueryFileWriter {

    private final String FILE = "queries.csv";//korrigieren!
    private Path path;
    private static ScrapeQueryFileWriter instance = new ScrapeQueryFileWriter();

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
    Collection<ScrapeQuery> findAll() throws URISyntaxException {


        try {
            path = Paths.get(Paths.get(new File("").getPath()).toRealPath() + "/resources");
            if (!Files.exists(path)) {
                path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("")).getPath());
            }
            if (!Files.exists(path)) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<ScrapeQuery> csvToBean = new CsvToBeanBuilder<ScrapeQuery>(reader)
                    .withType(ScrapeQuery.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * write all given search queries to file.
     *
     * @param queries list of {@link ScrapeQuery}.
     */
    public void write(List<ScrapeQuery> queries) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        path = Paths.get(Paths.get(new File("").getPath()).toRealPath() + "/resources");
        if (!Files.exists(path)) {
            path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(FILE)).getPath());

            try (Writer writer = Files.newBufferedWriter(path)) {

                StatefulBeanToCsv<ScrapeQuery> csvWriter = new StatefulBeanToCsvBuilder<ScrapeQuery>(writer)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                        .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                        .withOrderedResults(false)
                        .build();

                csvWriter.write(queries);

            }

        }
    }
}