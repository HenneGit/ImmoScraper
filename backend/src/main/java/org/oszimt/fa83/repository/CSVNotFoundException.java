package org.oszimt.fa83.repository;

/**
 * thrown id csv wasn't found.
 */
public class CSVNotFoundException extends Exception{
    private final String message;

    public CSVNotFoundException() {
        this.message = "queries.csv not found. New one will be created with first save";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

