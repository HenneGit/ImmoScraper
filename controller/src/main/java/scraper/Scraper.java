package scraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.util.List;

public class Scraper {

    public Scraper() {
    }

    public void scrape(ScrapeQuery query) {

        //todo create object for srapce success
        String searchQuery = "berlin/berlin";
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://www.immobilienscout24.de/Suche/de/" + searchQuery + "/wohnung-mieten?enteredFrom=one_step_search";
            HtmlPage page = client.getPage(searchUrl);
            System.out.println(page.getPage());
            List<HtmlElement> elementsByAttribute = page.getDocumentElement().getElementsByAttribute("ul", "id", "resultListItems");
            for (HtmlElement element : elementsByAttribute) {
                System.out.println(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
