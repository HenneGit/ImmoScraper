package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.util.IdCounter;

/**
 * Pojo for setting up a search in immoscout.
 */
public class ScrapeQuery implements Entity {


    @CsvBindByName(column = "queryName")
    private String queryName;

    @CsvBindByName(column = "pk")
    private String pk;

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "priceTo")
    private Double priceTo;

    @CsvBindByName(column = "space")
    private Double space;

    @CsvBindByName(column = "radius")
    private Double radius;

    @CsvBindByName(column = "roomSize")
    private Double roomSize;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "hasWBS")
    private Boolean hasWBS;

    /**
     * use for bean creation only. To create new ScrapeQuery object use {@link ScrapeQueryBuilder}.
     */
    public ScrapeQuery() {
        //necessary for CSVToBeanReader use builder
    }

    private ScrapeQuery(ScrapeQueryBuilder builder) {
        this.pk = builder.pk;
        this.city = builder.city;
        this.priceTo = builder.priceTo;
        this.queryName = builder.queryName;
        this.space = builder.space;
        this.radius = builder.radius;
        this.email = builder.email;
        this.roomSize = builder.roomSize;
        this.hasWBS = builder.hasWBS;
    }

    /**
     * creates an url from entity properties. Used for scraping immoscout results.
     *
     * @return new search url with entity properties.
     */
    public String toUrl() {
        String url = "https://www.immobilienscout24.de/Suche/de/";
        if (this.city == null) {
            return "";
        }
        url += parseCity();
        if (this.hasWBS) {
            url += "sozialwohnung-mieten?";
        } else {
            url += "/wohnung-mieten?";
        }
        if (!(this.roomSize == null)) {
            url += "numberofrooms=" + this.roomSize + "-&";
        }
        if (!(this.priceTo == null)) {
            url += "price=-" + this.priceTo + "&";
        }
        if (!(this.space == null)) {
            url += "livingspace=" + this.space + "-&";
        }
        url += "pricetype=rentpermonth&enteredFrom=one_step_search";
        return url;
    }

    @Override
    public String getPk() {
        return pk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Double getSpace() {
        return space;
    }

    public void setSpace(Double space) {
        this.space = space;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Boolean getHasWBS() {
        return hasWBS;
    }

    public void setHasWBS(Boolean hasWBS) {
        this.hasWBS = hasWBS;
    }

    private String parseCity() {
        String toLowerCase = city.toLowerCase();
        return toLowerCase + "/" + toLowerCase + "/friedrichshain-kreuzberg";
    }

    /**
     * builder class for ScrapeQueries.
     */
    public static class ScrapeQueryBuilder {
        private String city;
        private Double priceTo;
        private String pk;
        private String queryName;
        private Double space;
        private Double radius;
        private Double roomSize;
        private String email;
        private boolean hasWBS;

        public ScrapeQueryBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ScrapeQueryBuilder queryName(String queryName) {
            this.queryName = queryName;
            return this;
        }

        public ScrapeQueryBuilder radius(Double radius) {
            this.radius = radius;
            return this;
        }

        public ScrapeQueryBuilder priceTo(Double priceTo) {
            this.priceTo = priceTo;
            return this;
        }

        public ScrapeQueryBuilder space(Double space) {
            this.space = space;
            return this;
        }

        public ScrapeQueryBuilder pk(String pk) {
            this.pk = pk;
            return this;
        }

        public ScrapeQueryBuilder roomSize(Double roomSize) {
            this.roomSize = roomSize;
            return this;
        }

        public ScrapeQueryBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ScrapeQueryBuilder hasWBS(Boolean hasWBS) {
            this.hasWBS = hasWBS;
            return this;
        }


        public ScrapeQuery build() {
            return new ScrapeQuery(this);
        }

    }
}
