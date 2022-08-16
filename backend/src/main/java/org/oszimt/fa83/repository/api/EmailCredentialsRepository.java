package org.oszimt.fa83.repository.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.EmailCredentials;
import org.oszimt.fa83.repository.CSVNotFoundException;

import java.io.IOException;

public interface EmailCredentialsRepository {

    EmailCredentials getEmailCredentials() throws CSVNotFoundException;

    void setEmailCredentials(String email, String smtp, String password, int port) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;

    void write() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;
}
