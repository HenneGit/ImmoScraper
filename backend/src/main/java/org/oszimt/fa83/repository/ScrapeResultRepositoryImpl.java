package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Repository;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.pojo.ScrapeResultPojo;
import org.oszimt.fa83.util.IdCounter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for {@link ScrapeResultPojo}.
 */
public class ScrapeResultRepositoryImpl implements Repository<ScrapeResultPojo> {

    private Map<Comparable<?>, ScrapeResultPojo> repository = new HashMap<>();
    private static final String FILE_NAME = "results.csv";
    private final GenericFileWriter genericFileWriter = GenericFileWriter.getInstance();
    private static final ScrapeResultRepositoryImpl INSTANCE = new ScrapeResultRepositoryImpl();


    private ScrapeResultRepositoryImpl() {
    }

    public static ScrapeResultRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ScrapeResultPojo create(ScrapeResultPojo resultPojo) throws CSVNotFoundException {
        if (this.repository.size() == 0) {
            load();
        }
        String uuid = IdCounter.createId();
        if (!checkIfExists(resultPojo)) {
            if (resultPojo.getPk() == null) {
                resultPojo.setPk(uuid);
            }
            repository.put(uuid, resultPojo);
            return resultPojo;
        }
        return null;
    }

    @Override
    public Collection<ScrapeResultPojo> findAll() throws CSVNotFoundException {

        if (this.repository.size() == 0) {
            load();
        }
        return this.repository.values();
    }

    @Override
    public void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        this.genericFileWriter.write(new ArrayList<>(this.repository.values()), FILE_NAME);

    }

    @Override
    public ScrapeResultPojo findByPk(Comparable<?> pk) throws CSVNotFoundException {
        if (this.repository.size() == 0) {
            load();
        }
        return repository.get(pk);
    }

    /**
     * load all Scrape result from file.
     * @throws CSVNotFoundException
     */
    private void load() throws CSVNotFoundException {
        List<ScrapeResultPojo> all = genericFileWriter.findAll(FILE_NAME, ScrapeQuery.class).stream().map(e -> (ScrapeResultPojo) e).collect(Collectors.toList());
        all.forEach(q -> this.repository.put(q.getPk(), q));
    }

    /**
     * checks if a result was scraped before.
     * @param resultPojo the resultPojo to check.
     * @return returns true if result exits.
     */
    private boolean checkIfExists(ScrapeResultPojo resultPojo) {
        if (!this.repository.values().isEmpty()) {
            for (ScrapeResultPojo pojo : this.repository.values()) {
                if (pojo.getPk().equals(resultPojo.getPk())) {
                    return true;
                }
            }
        }
        return false;
    }
}
