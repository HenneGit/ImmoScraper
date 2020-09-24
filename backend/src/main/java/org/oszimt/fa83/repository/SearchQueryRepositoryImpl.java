package org.oszimt.fa83.repository;

import org.oszimt.fa83.api.SearchQueryRepository;
import org.oszimt.fa83.pojo.SearchQuery;
import org.oszimt.fa83.util.IdCounter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SearchQueryRepositoryImpl implements SearchQueryRepository<SearchQuery> {

    private Map<Comparable<?>, SearchQuery> repository = new HashMap<>();

    private SearchQueryFileWriter fileWriter;


    public SearchQueryRepositoryImpl(SearchQueryFileWriter fileWriter) {
        this.fileWriter = fileWriter;
        load();
    }

    @Override
    public Comparable<?> create(SearchQuery query) {
        Comparable<?> uuid = IdCounter.createId();
        repository.put(uuid, query);
        return uuid;
    }

    @Override
    public Collection<SearchQuery> findAll() {

        if (this.repository.size() == 0){
            load();
        }
        return this.repository.values();

    }

    @Override
    public SearchQuery findByPk(Comparable<?> pk) {
       return this.repository.get(pk);
    }

    @Override
    public void remove(Comparable<?> pk) {
        this.repository.remove(pk);
    }

    public void write() {
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
