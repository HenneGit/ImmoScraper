package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Repository;
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

    private static ScrapeQueryRepository instance = new ScrapeQueryRepositoryImpl();

    private ScrapeQueryFileWriter fileWriter = ScrapeQueryFileWriter.getInstance();

    private ScrapeQueryRepositoryImpl() {
        load();
    }

    public static Repository getInstance(){
        return instance;
    }

    @Override
    public Comparable<?> create(ScrapeQuery query) {
        Comparable<?> uuid = IdCounter.createId();
        repository.put(uuid, query);
        return uuid;
    }

    @Override
    public Collection<ScrapeQuery> findAll() {

        if (this.repository.size() == 0){
            load();
        }
        return this.repository.values();

    }

    @Override
    public ScrapeQuery findByPk(Comparable<?> pk) {
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

    private void load() {
        try {

            this.fileWriter.findAll().forEach(q -> this.repository.put(q.getPk(), q));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
