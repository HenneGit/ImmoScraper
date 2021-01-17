package org.oszimt.fa83.repository.api;

import org.oszimt.fa83.api.Repository;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.CSVNotFoundException;

/**
 * extended interface for managing ScrapeQueries.
 */
public interface ScrapeQueryRepository extends Repository<ScrapeQuery> {

    /**
     * update query
     * @param pk the pk of the query to update.
     * @param query query wiht update information.
     * @return the updated query.
     */
    ScrapeQuery update(Comparable<?> pk, ScrapeQuery query);

    /**
     * delete a scrape queries.
     * @param pk the pk of the query to delete.
     */
    void remove(Comparable<?> pk);


}
