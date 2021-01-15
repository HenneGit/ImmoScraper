package org.oszimt.fa83;

public class ValidationException extends Exception {

    public String reason;

    public ValidationException(String reason){
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
