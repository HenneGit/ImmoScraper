package org.oszimt.fa83;

import javafx.fxml.FXML;
import javafx.scene.*;

public class Scene1Controller {

    private Scene scene2;
    private MainView main;

    public void setMain(MainView main){this.main = main;}
    public void setScene2(Scene scene2){this.scene2 = scene2;}
    // this method is called by clicking the button
    @FXML
    public void switchScene(){main.setScene(scene2);}
}