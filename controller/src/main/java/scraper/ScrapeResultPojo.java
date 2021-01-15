package scraper;

import org.oszimt.fa83.api.Entity;

public class ScrapeResultPojo implements Entity {

    String url;
    String oid;


    public ScrapeResultPojo(String url, String oid) {
        this.url = url;
        this.oid = oid;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String getPk() {
        return this.oid;
    }
}
