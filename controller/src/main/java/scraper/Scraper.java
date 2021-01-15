package scraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.util.List;

public class Scraper {

    public Scraper() {
    }

    public ScrapeResultPojo scrape(ScrapeQuery query) {

        //todo create object for srapce success
        String searchQuery = "berlin/berlin";
        WebClient client = new WebClient(BrowserVersion.FIREFOX);
        client.addRequestHeader("Mozilla/5.0",  "(compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
        client.setJavaScriptTimeout(100000);
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setPopupBlockerEnabled(false);
        client.getOptions().setGeolocationEnabled(true);
        client.getOptions().setDoNotTrackEnabled(false);
        client.getOptions().setActiveXNative(true);
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setWebSocketEnabled(true);
        client.getOptions().setCssEnabled(true);
        client.getOptions().setJavaScriptEnabled(true);
        client.getCookieManager().setCookiesEnabled(true);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        try {
            String searchUrl = "https://www.immobilienscout24.de/Suche/de/" + searchQuery + "/wohnung-mieten?enteredFrom=one_step_search";
            HtmlPage page = client.getPage("https://www.immobilienscout24.de/Suche/de/berlin/berlin/wohnung-mieten?price=-500.0&livingspace=50.0-&pricetype=rentpermonth&enteredFrom=one_step_search");
            System.out.println(page.getPage());
            List<HtmlElement> elementsByAttribute = page.getDocumentElement().getElementsByAttribute("ul", "id", "resultListItems");
            for (HtmlElement element : elementsByAttribute) {
                System.out.println(element);
            }
        client.getOptions().setThrowExceptionOnScriptError(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ScrapeResultPojo(null,null);
    }


}
