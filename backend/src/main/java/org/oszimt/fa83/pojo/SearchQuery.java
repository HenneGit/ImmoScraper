package org.oszimt.fa83.pojo;

import org.oszimt.fa83.api.Entity;

import java.util.SplittableRandom;

public class SearchQuery implements Entity {

    private Comparable<?> pk;
    private String city;
    private Double priceFrom;
    private Double priceTo;



    @Override
    public Comparable<?> getPk() {
        return null;
    }
}
