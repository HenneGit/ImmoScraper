package org.oszimt.fa83;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationMain extends Application {




    public static void main(final String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController.getInstance().init(primaryStage);
    }
}
