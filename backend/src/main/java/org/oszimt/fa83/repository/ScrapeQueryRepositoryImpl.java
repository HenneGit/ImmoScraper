package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;
import org.oszimt.fa83.util.IdCounter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScrapeQueryRepositoryImpl implements ScrapeQueryRepository {

    private Map<Comparable<?>, ScrapeQuery> repository = new HashMap<>();

    private static final ScrapeQueryRepository instance = new ScrapeQueryRepositoryImpl();

    private final ScrapeQueryFileWriter fileWriter = ScrapeQueryFileWriter.getInstance();

    private ScrapeQueryRepositoryImpl() {
    }

    public static ScrapeQueryRepository getInstance(){
        return instance;
    }

    @Override
    public void create(ScrapeQuery query) {
        String uuid = IdCounter.createId();
        if (query.getPk() == null){
            query.setPk(uuid);
        }
        repository.put(uuid, query);
    }

    @Override
    public ScrapeQuery update(ScrapeQuery query) throws CSVNotFoundException {
        if (this.repository.size() == 0){
            load();
        }
        ScrapeQuery scrapeQuery = this.repository.get(query.getPk());
        scrapeQuery.setEmail(query.getEmail());
        scrapeQuery.setQueryName(query.getQueryName());
        scrapeQuery.setCity(query.getCity());
        scrapeQuery.setRadius(query.getRadius());
        scrapeQuery.setRadius(query.getPriceTo());
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
        this.fileWriter.write(new ArrayList<>(this.repository.values()));
    }

    private void load() throws CSVNotFoundException {
            Collection<ScrapeQuery> all = fileWriter.findAll();
            all.forEach(q -> this.repository.put(q.getPk(), q));


    }

}
