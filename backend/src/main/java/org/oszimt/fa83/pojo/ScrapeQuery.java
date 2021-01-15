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
    private String roomSize;

    @CsvBindByName(column = "email")
    private String email;

    /**
     * use for bean creation only. To create new SrapeQuery object use {@link ScrapeQueryBuilder}.
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

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
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

    public static class ScrapeQueryBuilder {
        private String city;
        private Double priceTo;
        private String pk;
        private String queryName;
        private Double space;
        private Double radius;
        private String roomSize;
        private String email;

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

        public ScrapeQueryBuilder roomSize(String roomSize) {
            this.roomSize = roomSize;
            return this;
        }
        public ScrapeQueryBuilder email(String email) {
            this.email = email;
            return this;
        }


        public ScrapeQuery build() {
            ScrapeQuery query = new ScrapeQuery(this);
            return new ScrapeQuery(this);
        }

    }
}
