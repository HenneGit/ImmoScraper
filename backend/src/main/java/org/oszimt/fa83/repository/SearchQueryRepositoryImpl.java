package org.oszimt.fa83.repository;

import org.oszimt.fa83.api.SearchQueryRepository;
import org.oszimt.fa83.pojo.SearchQuery;
import org.oszimt.fa83.util.IdCounter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SearchQueryRepositoryImpl implements SearchQueryRepository<SearchQuery> {

    private Map<Comparable<?>, SearchQuery> repository = new HashMap<>();


    @Override
    public Comparable<?> create(SearchQuery query) {
        Comparable uuid = IdCounter.createId();
        repository.put(uuid, query);
        return uuid;
    }

    @Override
    public Collection findAll() {
        return repository.values();
    }

    @Override
    public SearchQuery findByPk(Comparable<?> pk) {
       return repository.get(pk);
    }

    @Override
    public void remove(Comparable<?> pk) {
        repository.remove(pk);
    }
}
