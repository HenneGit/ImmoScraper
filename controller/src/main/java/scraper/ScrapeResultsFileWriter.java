package scraper;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.api.GenericCSVWriter;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class ScrapeResultsFileWriter {
    private final String FILE = "results.csv";
    private static ScrapeResultsFileWriter instance = new ScrapeResultsFileWriter();
    private GenericCSVWriter<ScrapeResultPojo> genericWriter = new GenericCSVWriter<>();

    private ScrapeResultsFileWriter() {
    }

    public static ScrapeResultsFileWriter getInstance() {
        return instance;
    }

    /**
     * loads all scraper results from file.
     *
     * @return collection of all search queries saved in file.
     */
    Collection<ScrapeResultPojo> findAll() throws IOException {
        URL url = getClass().getClassLoader().getResource(FILE);
        if (url != null) {
            try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
                Collection<ScrapeResultPojo> beans = genericWriter.getBeans(reader, ScrapeResultPojo.class);
                return beans;
            }
        }
        return null;
    }

    /**
     * write all given search queries to file.
     *
     */
    public void write(List<ScrapeResultPojo> all) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        URL url = getClass().getClassLoader().getResource(FILE);

        try (OutputStream out = new FileOutputStream(url.getFile());
             Writer writer = new OutputStreamWriter(out,"UTF-8")) {
            genericWriter.write(all, writer);
        }
    }
}
