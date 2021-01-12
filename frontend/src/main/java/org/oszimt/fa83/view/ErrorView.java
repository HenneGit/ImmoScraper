package org.oszimt.fa83.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ErrorView {


    @FXML
    private TextArea messageBox;
    private final Stage window = new Stage();

    public void initialize(){
        messageBox.setEditable(false);
        messageBox.setWrapText(true);
        messageBox.setEditable(false);
        messageBox.setMouseTransparent(false);
    }
    public void setMessage(final String msg){
        messageBox.setText(msg);
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

}
