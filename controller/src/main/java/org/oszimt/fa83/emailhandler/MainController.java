package org.oszimt.fa83.emailhandler;

import org.oszimt.fa83.pojo.ScrapeQuery;

import javax.mail.MessagingException;

public interface MainController {

    void startScraping(ScrapeQuery query) throws MessagingException;

    void sendEmail();


}
