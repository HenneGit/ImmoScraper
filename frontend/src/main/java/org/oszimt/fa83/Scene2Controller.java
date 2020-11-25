package org.oszimt.fa83;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Scene2Controller {

    private Scene scene1;
    private MainView main;

    @FXML
    private Button backButton;

    public void setMain(MainView main) {
        this.main = main;
    }

    public void setScene1(Scene scene1) {
        this.scene1 = scene1;
    }

    // this method is called by clicking the button
    @FXML
    private void switchBack(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == backButton) {
            stage = (Stage) backButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("scene1.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
