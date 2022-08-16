package org.oszimt.fa83.exception;

public class NoEmailCredentialsSet extends Exception{

    private static final String NO_CREDENTIALS = "Bitte Email Informationen angeben.";

    @Override
    public String getMessage() {
        return NO_CREDENTIALS;
    }
}
