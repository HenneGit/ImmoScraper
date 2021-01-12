package org.oszimt.fa83.emailhandler;

import org.oszimt.fa83.pojo.ScrapeQuery;

public interface MainController {

    void startScraping(ScrapeQuery query);

    void sendEmail();


}
