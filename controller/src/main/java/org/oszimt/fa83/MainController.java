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

    public void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        repository.write();
        resultRepository.write();
    }

    public List<ScrapeResultPojo> startScraping() throws IOException, CSVNotFoundException {
        List<ScrapeResultPojo> scrape = scraper.scrape(getActiveQuery());
        List<ScrapeResultPojo> newResults = new ArrayList<>();
        for (ScrapeResultPojo resultPojo : scrape){
            newResults.add(resultRepository.create(resultPojo));
        }
        return newResults;
    }

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
