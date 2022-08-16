package org.oszimt.fa83.pojo;

import com.opencsv.bean.CsvBindByName;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.util.IdCounter;

public class EmailCredentials implements Entity {

    @CsvBindByName(column = "email")
    private String email;
    @CsvBindByName(column = "smtp")
    private String smtp;
    @CsvBindByName(column = "password")
    private String password;
    @CsvBindByName(column = "pk")
    private String pk;
    @CsvBindByName(column = "port")
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EmailCredentials(String email, String smtp, String password, int port) {
        this.email = email;
        this.smtp = smtp;
        this.password = password;
        this.pk = IdCounter.createId();
        this.port = port;
    }

    public EmailCredentials() {
    }

    public String getEmail() {
        return email;
    }

    public String getSmtp() {
        return smtp;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @Override
    public String getPk() {
        return pk;
    }

}
