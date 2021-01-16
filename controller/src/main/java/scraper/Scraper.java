package scraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scraper {

    public Scraper() {
    }

    public List<ScrapeResultPojo> scrape(ScrapeQuery query) {

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

        List<String> resultIdList =  new ArrayList<>();
        List<ScrapeResultPojo> resultList =  new ArrayList<>();

        try {

            ScrapeResultsFileWriter fileWriter = ScrapeResultsFileWriter.getInstance();
            Collection<ScrapeResultPojo> fetchedIds = fileWriter.findAll();

           HtmlPage page = client.getPage(query.toUrl());
            System.out.println(page.getPage());
            List<HtmlElement> listCollection = page.getDocumentElement().getElementsByAttribute("ul", "id", "resultListItems");
            Iterable<HtmlElement> listElements = new ArrayList<HtmlElement>();
            for (HtmlElement list : listCollection) {
                for (HtmlElement element : list.getHtmlElementDescendants()) {
                    if (element.hasAttribute("data-id")) {
                        String oid = element.getAttribute("data-id");
                        if ((!resultIdList.contains(oid) && fetchedIds == null) || (!(fetchedIds == null) && !fetchedIds.contains(oid))) {
                            resultIdList.add(oid);
                        }
                    }
                }
            }
            for ( String oid : resultIdList ) {
                String url = "https://www.immobilienscout24.de/expose/" + oid;
                ScrapeResultPojo result = new ScrapeResultPojo(url, oid);
                resultList.add(result);
                System.out.println(result.getUrl());
            }
            fileWriter.write(resultList);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
    public static void main(String[] args){
        Scraper debug = new Scraper();
        ScrapeQuery query = new ScrapeQuery.ScrapeQueryBuilder().city("berlin/berlin").roomSize("2.0").priceTo(500.0).space(5.0).build();
        System.out.println(query.toUrl());
        debug.scrape(query);
    }

}
