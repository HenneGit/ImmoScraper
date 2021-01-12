package org.oszimt.fa83.emailhandler;

import org.oszimt.fa83.pojo.ScrapeQuery;
import scraper.ScrapeResultPojo;
import scraper.Scraper;

import javax.mail.MessagingException;

public class MainControllerImpl implements MainController {

    private Scraper scraper = new Scraper();
    private EmailHandler emailHandler = new EmailHandler();
    private String email;

    public MainControllerImpl(String email) {
        this.email = email;
    }

    @Override
    public void startScraping(ScrapeQuery query) throws MessagingException {
        //logic for sending a new result
        ScrapeResultPojo scrape = scraper.scrape(query);
        emailHandler.createEmailMessage(this.email, scrape.getBody(), scrape.getBody());

    }

    @Override
    public void sendEmail() {

    }
}
