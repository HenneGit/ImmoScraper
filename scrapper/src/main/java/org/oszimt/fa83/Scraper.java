package org.oszimt.fa83;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;

import java.util.List;

public class Scraper {

    private static Logger log = Logger.getLogger(Scraper.class);

    public static void main(String[] args) {

        scrape();

    }

    public static void scrape() {

        String searchQuery = "berlin/berlin";

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://www.immobilienscout24.de/Suche/de/" + searchQuery + "/wohnung-mieten?enteredFrom=one_step_search";
            HtmlPage page = client.getPage(searchUrl);
            log.info(page.getPage());
            List<HtmlElement> elementsByAttribute = page.getDocumentElement().getElementsByAttribute("ul", "id", "resultListItems");
            for (HtmlElement element : elementsByAttribute) {
                log.info(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
