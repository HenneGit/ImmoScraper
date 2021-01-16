package scraper;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.pojo.ScrapeQuery;

public class ScrapeResultPojo implements Entity {

    private String url;

    @CsvBindByName(column = "pk")
    private String oid;

    /**
     * use for bean creation only.
     */
    public ScrapeResultPojo() {
        //necessary for CSVToBeanReader use builder
    }

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
