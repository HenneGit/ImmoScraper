package org.oszimt.fa83.repository;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.pojo.EmailCredentials;
import org.oszimt.fa83.repository.api.EmailCredentialsRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EmailCredentialsRepositoryImpl implements EmailCredentialsRepository {

    private final String FILE = "email.csv";
    private final GenericFileWriter genericFileWriter = GenericFileWriter.getInstance();
    private static final EmailCredentialsRepository instance = new EmailCredentialsRepositoryImpl();

    private EmailCredentials emailCredentials;

    public static EmailCredentialsRepository getInstance(){
        return instance;
    }

    @Override
    public EmailCredentials getEmailCredentials() throws CSVNotFoundException {
        if (emailCredentials == null) {
            load();
        }
        return emailCredentials;
    }

    @Override
    public void setEmailCredentials(String email, String smtp, String password, int port) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        emailCredentials = new EmailCredentials(email, smtp, password, port);
        write();
    }

    @Override
    public void write() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        List<Entity> emails = null;
        if (emailCredentials != null) {
            emails = List.of(emailCredentials);
        } else {
            emails = Collections.EMPTY_LIST;
        }
        this.genericFileWriter.write(emails, FILE);
    }

    private void load() throws CSVNotFoundException {
        List<EmailCredentials> all = genericFileWriter.findAll(FILE, EmailCredentials.class).stream().map(e -> (EmailCredentials) e).collect(Collectors.toList());
        if (all.isEmpty()) {
            emailCredentials = null;
            return;
        }
        emailCredentials = all.get(0);
    }
}
