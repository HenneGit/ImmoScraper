package org.oszimt.fa83;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * main entry class for calling frontend layout.
 */
public class ApplicationMain extends Application {


    public static void main(final String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController.getInstance().init(primaryStage);
    }
}
