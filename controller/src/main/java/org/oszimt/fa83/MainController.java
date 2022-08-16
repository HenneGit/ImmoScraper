package org.oszimt.fa83;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.definition.District;
import org.oszimt.fa83.email.EmailHandler;
import org.oszimt.fa83.email.EmailSupplier;
import org.oszimt.fa83.exception.NoEmailCredentialsSet;
import org.oszimt.fa83.pojo.EmailCredentials;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.CSVNotFoundException;
import org.oszimt.fa83.repository.EmailCredentialsRepositoryImpl;
import org.oszimt.fa83.repository.ScrapeQueryRepositoryImpl;
import org.oszimt.fa83.repository.ScrapeResultRepositoryImpl;
import org.oszimt.fa83.repository.api.EmailCredentialsRepository;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;
import org.oszimt.fa83.pojo.ScrapeResultPojo;
import org.oszimt.fa83.scraper.Scraper;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

/**
 * singleton controller class for organizing data and communication between backend and frontend. Hold current active
 * ScrapeQuery for central access.
 */
public class MainController {

    private final Scraper scraper = new Scraper();
    private static final MainController INSTANCE = new MainController();
    private final ScrapeQueryRepository repository = ScrapeQueryRepositoryImpl.getInstance();
    private final ScrapeResultRepositoryImpl resultRepository = ScrapeResultRepositoryImpl.getInstance();
    private final EmailCredentialsRepository emailCredentialsRepository = EmailCredentialsRepositoryImpl.getInstance();
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
                    Collection<ScrapeQuery> all = repository.findAll();

                }
            }
        }
        return repository.create(query);
    }


    public Collection<ScrapeQuery> getScrapeQueries() throws CSVNotFoundException {
        return repository.findAll();
    }

    public boolean checkEntry(int zahl1, int zahl2) {
        if (zahl1 > zahl2) {
            return true;

        }
        return false;
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

    public void writeEmailCredentials() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        emailCredentialsRepository.write();

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
    public void sendEmail(String body, String subject) throws MessagingException, CSVNotFoundException, NoEmailCredentialsSet {
        EmailCredentials emailCredentials = emailCredentialsRepository.getEmailCredentials();
        if (emailCredentials == null) {
            throw new NoEmailCredentialsSet();
        }

        EmailHandler handler = new EmailHandler(emailCredentials);
        handler.createEmailMessage(EmailSupplier.getEmail(), body , subject);

    }

    public void setEmailCredentials(String email, String smtp, String password, int portNumber) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        emailCredentialsRepository.setEmailCredentials(email, smtp, password, portNumber);
    }

    public EmailCredentials getEmailCredentials() throws CSVNotFoundException {
        return emailCredentialsRepository.getEmailCredentials();
    }

    public ScrapeQuery getActiveQuery() {
        return activeQuery;
    }

    public void setActiveQuery(ScrapeQuery activeQuery) {
        this.activeQuery = activeQuery;
    }

    private boolean queryNameIsUnique(ScrapeQuery s) throws CSVNotFoundException {
        return getScrapeQueries().stream().noneMatch(q -> q.getQueryName().equals(s.getQueryName()));
    }

}
