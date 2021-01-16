package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;
import org.oszimt.fa83.util.IdCounter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ScrapeQueryRepositoryImpl implements ScrapeQueryRepository {

    private Map<Comparable<?>, ScrapeQuery> repository = new HashMap<>();

    private final String FILE = "queries.csv";

    private static final ScrapeQueryRepository instance = new ScrapeQueryRepositoryImpl();

    private final GenericFileWriter genericFileWriter = GenericFileWriter.getInstance();

    private ScrapeQueryRepositoryImpl() {
    }

    public static ScrapeQueryRepository getInstance(){
        return instance;
    }

    @Override
    public ScrapeQuery create(ScrapeQuery query) {
        String uuid = IdCounter.createId();
        if (query.getPk() == null){
            query.setPk(uuid);
        }
        repository.put(uuid, query);
        return repository.get(uuid);
    }

    @Override
    public ScrapeQuery update(Comparable<?> pk, ScrapeQuery query)  {
        ScrapeQuery scrapeQuery = this.repository.get(pk);
        scrapeQuery.setEmail(query.getEmail());
        scrapeQuery.setQueryName(query.getQueryName());
        scrapeQuery.setCity(query.getCity());
        scrapeQuery.setRadius(query.getRadius());
        scrapeQuery.setPriceTo(query.getPriceTo());
        scrapeQuery.setRoomSize(query.getRoomSize());
        scrapeQuery.setSpace(query.getSpace());
        return scrapeQuery;
    }

    @Override
    public Collection<ScrapeQuery> findAll() throws CSVNotFoundException {

        if (this.repository.size() == 0){
            load();
        }
        return this.repository.values();

    }

    @Override
    public ScrapeQuery findByPk(Comparable<?> pk) throws CSVNotFoundException {
        if (this.repository.size() == 0){
            load();
        }
       return this.repository.get(pk);
    }

    @Override
    public void remove(Comparable<?> pk) {
        this.repository.remove(pk);
    }
    @Override
    public void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        this.genericFileWriter.write(new ArrayList<>(this.repository.values()), FILE);
    }

    private void load() throws CSVNotFoundException {
        List<ScrapeQuery> all = genericFileWriter.findAll(FILE, ScrapeQuery.class).stream().map(e -> (ScrapeQuery) e).collect(Collectors.toList());
        all.forEach(q -> this.repository.put(q.getPk(), q));
    }

}
