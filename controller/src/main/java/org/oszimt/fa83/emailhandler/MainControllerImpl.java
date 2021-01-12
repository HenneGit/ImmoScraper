package org.oszimt.fa83.emailhandler;

import org.oszimt.fa83.pojo.ScrapeQuery;
import scraper.Scraper;

public class MainControllerImpl implements MainController {


    @Override
    public void startScraping(ScrapeQuery query) {
        Scraper scraper = new Scraper();
        scraper.scrape(query);
    }

    @Override
    public void sendEmail() {

    }
}
