package org.oszimt.fa83;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.oszimt.fa83.pojo.SearchQuery;
import org.oszimt.fa83.repository.SearchQueryRepositoryImpl;

public class MainView extends Application {



    private SearchQueryRepositoryImpl sqRepo = new SearchQueryRepositoryImpl();
    //Primary Stage
    Stage window;
    //Two Scenes
    Scene scene1, scene2;
    // The panes are associated with the respective .fxml files
    private Pane pane1;
    private Pane pane2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Set window as primary stage
        window = primaryStage;

        //load .fxml files and their controllers
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainView.class.getResource("scene1.fxml"));
        pane1 = loader.load();

        Scene1Controller controller1 = loader.getController();

        loader = new FXMLLoader();
        loader.setLocation(MainView.class.getResource("scene2.fxml"));
        pane2 = loader.load();
        Scene2Controller controller2 = loader.getController();



        // The scenes are based on what has been loaded from the .fxml files
        Scene scene1 = new Scene(pane1);
        Scene scene2 = new Scene(pane2);

        Button startButton = new Button("startButton");

        ObservableList<SearchQuery> sqListBox = FXCollections.observableArrayList();

        sqListBox.addAll(sqRepo.findAll());

        ComboBox comboBox = new ComboBox(sqListBox);
        // Pass reference the each scenes controller
/*        controller1.setScene2(scene2);
        controller1.setMain(this);
        controller2.setScene1(scene1);
        controller2.setMain(this);*/

        //Display scene 1 at first
        window.setScene(scene1);
        window.setTitle("Scene!");
        window.show();
    }

    private void startSQScene(Stage primaryStage){




    }
}
