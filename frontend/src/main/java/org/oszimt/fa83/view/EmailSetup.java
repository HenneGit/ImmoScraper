package org.oszimt.fa83.view;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.oszimt.fa83.MainController;
import org.oszimt.fa83.pojo.EmailCredentials;
import org.oszimt.fa83.repository.CSVNotFoundException;

import java.io.IOException;

public class EmailSetup extends AbstractView{


    @FXML
    private TextField email;

    @FXML
    private TextField smtp;

    @FXML
    private TextField password;

    @FXML
    private TextField port;

    private final Stage window = new Stage();

    private final MainController controller = MainController.getInstance();

    /**
     * set properties to popup.
     */
    public void initialize() {
        EmailCredentials emailCredentials = null;

        try {
            emailCredentials = controller.getEmailCredentials();
        } catch (CSVNotFoundException e) {
            try {
                controller.writeEmailCredentials();
            } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException ex) {
                //no op.
            }
        }
        if (emailCredentials != null) {
            email.setText(emailCredentials.getEmail());
            smtp.setText(emailCredentials.getSmtp());
            password.setText(emailCredentials.getPassword());
            port.setText(String.valueOf(emailCredentials.getPort()));
        }
    }

    public void initStage(final Parent root){
        window.setScene(new Scene(root));
        window.setTitle("ERROR");
        window.showAndWait();
    }


    @FXML
    public void close(){
        window.close();
    }


    @FXML
    public void saveEmailSettings(){
        String smtpText = smtp.getText();
        String passwordText = password.getText();
        String emailText = email.getText();
        int portNumber = Integer.parseInt(port.getText());
        try {
            controller.setEmailCredentials(emailText, smtpText, passwordText, portNumber);
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            callError(e);
        }
        window.close();
    }
}
