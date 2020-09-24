package org.oszimt.fa83;

import org.oszimt.fa83.pojo.SearchQuery;
import org.oszimt.fa83.repository.SearchQueryFileWriter;
import org.oszimt.fa83.repository.SearchQueryRepositoryImpl;
import org.oszimt.fa83.util.IdCounter;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        SearchQueryFileWriter fileWriter = new SearchQueryFileWriter();
        SearchQueryRepositoryImpl repository = new SearchQueryRepositoryImpl(fileWriter);

        SearchQuery query = new SearchQuery(IdCounter.createId(), "Berlin", 50D, 700D);
        SearchQuery query1 = new SearchQuery(IdCounter.createId(), "Dortmund", 150D, 2700D);
        SearchQuery query2 = new SearchQuery(IdCounter.createId(), "Hamburg", 250D, 1700D);
        repository.create(query);
        repository.create(query2);
        repository.create(query1);

        repository.write();

        Collection<SearchQuery> all = repository.findAll();
        all.forEach(q-> System.out.println(q.getCity()));
    }

}
