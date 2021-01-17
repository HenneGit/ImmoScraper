package org.oszimt.fa83;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.email.EmailHandler;
import org.oszimt.fa83.email.EmailSupplier;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.CSVNotFoundException;
import org.oszimt.fa83.repository.ScrapeQueryRepositoryImpl;
import org.oszimt.fa83.repository.ScrapeResultRepositoryImpl;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;
import org.oszimt.fa83.pojo.ScrapeResultPojo;
import org.oszimt.fa83.scraper.Scraper;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * singleton controller class for organizing data and communication between backend and frontend. Hold current active
 * ScrapeQuery for central access.
 */
public class MainController {

    private final Scraper scraper = new Scraper();
    private final EmailHandler emailHandler = new EmailHandler();
    private static final MainController INSTANCE = new MainController();
    private final ScrapeQueryRepository repository = ScrapeQueryRepositoryImpl.getInstance();
    private final ScrapeResultRepositoryImpl resultRepository = ScrapeResultRepositoryImpl.getInstance();
    private ScrapeQuery activeQuery;

    private MainController() {
        //hide constructor.
    }

    public static MainController getInstance() {
        return INSTANCE;
    }

    /**
     * create scrapeQuery or if exists update it. Prevents serveral queries from having the same name.
     * @param query the query to create or to update.
     * @return the created or updated query.
     * @throws ValidationException thrown when a duplicate name was given.
     * @throws CSVNotFoundException when csv file was found.
     */
    public ScrapeQuery createScrapeQuery(ScrapeQuery query) throws ValidationException, CSVNotFoundException {
        if (!queryNameIsUnique(query)) {
            if (this.activeQuery != null && !query.getQueryName().equals(this.activeQuery.getQueryName())) {
                throw new ValidationException("Name des Auftrags schon vorhanden");
            } else {
                if (this.activeQuery != null && query.getQueryName().equals(Objects.requireNonNull(this.activeQuery.getQueryName()))) {
                    return repository.update(activeQuery.getPk(), query);
                }
            }
        }
        return repository.create(query);
    }


    public Collection<ScrapeQuery> getScrapeQueries() throws CSVNotFoundException {
        return repository.findAll();
    }

    public void removeQuery(Comparable<?> pk) {
        repository.remove(pk);
    }

    /**
     * write all repositories to file.
     * @throws CsvRequiredFieldEmptyException
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     */
    public void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        repository.write();
        resultRepository.write();
    }

    /**
     * start scraping with current active scrape query.
     * @return the results of scraping.
     * @throws IOException
     * @throws CSVNotFoundException
     */
    public List<ScrapeResultPojo> startScraping() throws IOException, CSVNotFoundException {
        List<ScrapeResultPojo> scrape = scraper.scrape(getActiveQuery());
        List<ScrapeResultPojo> newResults = new ArrayList<>();
        for (ScrapeResultPojo resultPojo : scrape){
            ScrapeResultPojo pojo = resultRepository.create(resultPojo);
            if (pojo != null){
                newResults.add(pojo);
            }

        }
        return newResults;
    }

    /**
     * send email.
     * @param body content of email.
     * @param subject subject of email 
     * @throws MessagingException
     */
    public void sendEmail(String body, String subject) throws MessagingException {
        emailHandler.createEmailMessage(EmailSupplier.getEmail(), "body", "subject");

    }

    public ScrapeQuery getActiveQuery() {
        return activeQuery;
    }

    public void setActiveQuery(ScrapeQuery activeQuery) {
        this.activeQuery = activeQuery;
    }

    private boolean queryNameIsUnique(ScrapeQuery s) throws CSVNotFoundException {
        return getScrapeQueries().stream().filter(q -> q.getQueryName().equals(s.getQueryName())).count() == 0;
    }
}
