package org.oszimt.fa83;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Scene1Controller {

    private Scene scene2;
    private MainView main;

    @FXML
    private Button btn1;

    public void setMain(MainView main){this.main = main;}
    public void setScene2(Scene scene2){this.scene2 = scene2;}
    // this method is called by clicking the button
    @FXML
    private void switchScene(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if(event.getSource()==btn1){
            stage = (Stage) btn1.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("scene2.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        }
    }
}