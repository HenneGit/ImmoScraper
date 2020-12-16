package org.oszimt.fa83.view;

import javafx.fxml.FXML;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;

public class QuerySetupView extends AbstractView{


    public void initialize() {
        System.out.println("QuerySetup");
    }




    @FXML
    private void switchToMain(){
        StageController.getInstance().setRoot(Layout.MAIN);
    }

}
