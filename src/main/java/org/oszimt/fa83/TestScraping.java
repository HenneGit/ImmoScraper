package org.oszimt.fa83;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URLEncoder;

@Path("/hello")
public class TestScraping {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public HtmlPage hello() {

        String searchQuery = "Berlin" ;

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://www.immobilienscout24.de/Suche/radius/wohnung-mieten?centerofsearchaddress=Berlin;;;1276003001;"+ searchQuery + ";&numberofrooms=2.0-&price=-1200.0&livingspace=50.0-&geocoordinates=52.51051;13.43068;10.0&enteredFrom=one_step_search";
            HtmlPage page = client.getPage(searchUrl);
            return page;
        }catch(Exception e){
            e.printStackTrace();
        }
    return null;
    }
}