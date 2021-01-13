package org.oszimt.fa83.emailhandler;

public class ValidationException extends Exception{

    private final String reason;

    public ValidationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
