package org.oszimt.fa83.email;

/**
 * singleton class for central access to current email
 */
public class EmailSupplier {

    private static String email;
    private static final EmailSupplier INSTANCE = new EmailSupplier();

    private EmailSupplier(){
        //hide constructor
    }

    public static EmailSupplier getInstance(){
        return INSTANCE;
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
