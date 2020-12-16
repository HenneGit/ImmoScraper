package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.util.IdCounter;

/**
 * Pojo for setting up a search in immoscout.
 */
public class SearchQuery implements Entity {


    @CsvBindByName(column = "queryName")
    private String queryName;

    @CsvBindByName(column = "pk")
    private Comparable<?> pk;

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "priceFrom")
    private Double priceFrom;

    @CsvBindByName(column = "priceTo")
    private Double priceTo;

    public SearchQuery(){
        //necessary for CSVToBeanReader
    }

    private SearchQuery(SearchQueryBuilder builder) {
        this.pk = IdCounter.createId();
        this.city = builder.city;
        this.priceFrom = builder.priceFrom;
        this.priceTo = builder.priceTo;
        this.queryName = builder.queryName;
    }

    @Override
    public Comparable<?> getPk() {
        return null;
    }


    public void setPk(Comparable<?> pk) {
        this.pk = pk;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
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


    public static class SearchQueryBuilder{
        private String city;
        private Double priceFrom;
        private Double priceTo;
        private Comparable<?> pk;
        private String queryName;

        public SearchQueryBuilder city(String city){
            this.city = city;
            return this;
        }

        public SearchQueryBuilder queryName(String queryName){
            this.queryName = queryName;
            return this;
        }

        public SearchQueryBuilder priceFrom(Double priceFrom){
            this.priceFrom = priceFrom;
            return this;
        }

        public SearchQueryBuilder priceTo(Double priceTo){
            this.priceTo = priceTo;
            return this;
        }

        public SearchQuery build(){
            return new SearchQuery(this);
        }

    }
}
