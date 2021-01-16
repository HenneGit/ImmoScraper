package org.oszimt.fa83.scraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.pojo.ScrapeResultPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public Scraper() {
    }


    public List<ScrapeResultPojo> scrape(ScrapeQuery query) throws IOException {

        WebClient client = new WebClient(BrowserVersion.FIREFOX);
        client.addRequestHeader("Mozilla/5.0", "(compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
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
        List<String> resultIdList = new ArrayList<>();
        List<ScrapeResultPojo> resultList = new ArrayList<>();

            HtmlPage page = client.getPage(query.toUrl());
            List<HtmlElement> listCollection = page.getDocumentElement().getElementsByAttribute("ul", "id", "resultListItems");
            for (HtmlElement list : listCollection) {
                for (HtmlElement element : list.getHtmlElementDescendants()) {
                    if (element.hasAttribute("data-id")) {
                        String oid = element.getAttribute("data-id");
                            resultIdList.add(oid);
                    }
                }
            }
            for (String oid : resultIdList) {
                String url = "https://www.immobilienscout24.de/expose/" + oid;
                ScrapeResultPojo result = new ScrapeResultPojo(url, oid);
                resultList.add(result);
            }
            return resultList;
    }
}
