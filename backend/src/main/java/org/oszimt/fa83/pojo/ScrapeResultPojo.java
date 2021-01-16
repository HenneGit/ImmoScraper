package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;

public class ScrapeResultPojo implements Entity {

    private String url;

    @CsvBindByName(column = "pk")
    private String pk;

    @CsvBindByName(column = "title")
    private String title;

    /**
     * use for bean creation only.
     */
    public ScrapeResultPojo() {
        //necessary for CSVToBeanReader use builder
    }

    public ScrapeResultPojo(String url, String pk) {
        this.url = url;
        this.pk = pk;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @Override
    public String getPk() {
        return this.pk;
    }

}
