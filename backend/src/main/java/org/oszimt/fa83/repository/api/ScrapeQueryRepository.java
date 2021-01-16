package org.oszimt.fa83.repository.api;

import org.oszimt.fa83.api.Repository;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.CSVNotFoundException;

public interface ScrapeQueryRepository extends Repository<ScrapeQuery> {

    ScrapeQuery update(Comparable<?> pk, ScrapeQuery query);

    void remove(Comparable<?> pk);


}
