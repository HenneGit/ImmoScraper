package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @CsvBindByName(column = "roomSize")
    private Double roomSize;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "hasWBS")
    private Boolean hasWBS;

    @CsvBindByName(column = "districts")
    private String district;


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
        this.email = builder.email;
        this.roomSize = builder.roomSize;
        this.hasWBS = builder.hasWBS;
        this.district = builder.district;
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
        if (this.district.contains("/")) {
            url += this.district;
        }

        if (this.hasWBS) {
            url += "/sozialwohnung-mieten?";
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
        url += "pricetype=rentpermonth&";
        if (this.getDistrict().contains(";")) {
            String replaced = this.district.replace(";", ",");

            url += "geocodes=" + replaced.substring(0, replaced.length() - 1);
        }
        url +="&enteredFrom=result_list";
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

    public Boolean getHasWBS() {
        return hasWBS;
    }

    public void setHasWBS(Boolean hasWBS) {
        this.hasWBS = hasWBS;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    private String parseCity() {
        String toLowerCase = city.toLowerCase();
        return toLowerCase + "/" + toLowerCase;
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
        private Double roomSize;
        private String email;
        private boolean hasWBS;
        private String district;

        public ScrapeQueryBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ScrapeQueryBuilder queryName(String queryName) {
            this.queryName = queryName;
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

        public ScrapeQueryBuilder district(String district) {
            this.district = district;
            return this;
        }

        public ScrapeQuery build() {
            return new ScrapeQuery(this);
        }

    }
}
