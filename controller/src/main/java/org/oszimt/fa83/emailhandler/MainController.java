package org.oszimt.fa83.emailhandler;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.ScrapeQueryRepositoryImpl;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;
import scraper.ScrapeResultPojo;
import scraper.Scraper;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class MainController  {

    private final Scraper scraper = new Scraper();
    private final EmailHandler emailHandler = new EmailHandler();
    private static final MainController INSTANCE = new MainController();
    private final ScrapeQueryRepository repository = (ScrapeQueryRepository) ScrapeQueryRepositoryImpl.getInstance();
    private ScrapeQuery activeQuery;

    private MainController(){
        //hide constructor.
    }

    public static MainController getInstance(){
        return INSTANCE;
    }

    public void createScrapeQuery(ScrapeQuery query) throws ValidationException {
        if (!queryNameIsUnique(query)){
            throw new ValidationException("Fehler", "Name des Auftrags schon vorhanden");
        } else {
            repository.create(query);

        }
    }

    public Collection<ScrapeQuery> getScrapeQueries(){
        return repository.findAll();
    }

    public void removeQuery(Comparable<?> pk){
        repository.remove(pk);
    }

    public void write() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        repository.write();
    }



    public void startScraping(ScrapeQuery query) throws Exception {
        //logic for sending a new result
        ScrapeResultPojo scrape = scraper.scrape(query);
        emailHandler.createEmailMessage(EmailSupplier.getInstance().getEmail(), scrape.getBody(), scrape.getBody());

    }

    public ScrapeQuery getActiveQuery() {
        return activeQuery;
    }

    public void setActiveQuery(ScrapeQuery activeQuery) {
        this.activeQuery = activeQuery;
    }

    private boolean queryNameIsUnique(ScrapeQuery s){
        return getScrapeQueries().stream().filter(q -> q.getQueryName().equals(s.getQueryName())).collect(Collectors.toList()).size() == 0;
    }
}
