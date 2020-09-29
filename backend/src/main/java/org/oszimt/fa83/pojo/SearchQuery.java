package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;

/**
 * Pojo for setting up a search in immoscout.
 */
public class SearchQuery implements Entity {

    @CsvBindByName(column = "pk")
    private Comparable<?> pk;

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "priceFrom")
    private Double priceFrom;

    @CsvBindByName(column = "priceTo")
    private Double priceTo;

    public SearchQuery(Comparable<?> pk, String city, Double priceFrom, Double priceTo) {
        this.pk = pk;
        this.city = city;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public SearchQuery(String pk, String city, String priceFrom, String priceTo) {
        this.pk = pk;
        this.city = city;
        this.priceFrom = Double.parseDouble(priceFrom);
        this.priceTo = Double.parseDouble(priceTo);
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
}
