package org.oszimt.fa83;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageController {


    private static final StageController INSTANCE = new StageController();
    private static final Layout DEFAULT_LAYOUT = Layout.MAIN;
    private Scene scene;
    private Stage stage;
    private FXMLLoader loader;

    public static StageController getInstance(){
        return INSTANCE;
    }

    public void init(final Stage window) throws IOException {

        scene = new Scene(loadFXML(DEFAULT_LAYOUT.getFileName()));
        this.stage = window;
        window.setTitle("ImmoScraper");
        window.setScene(scene);
        window.show();
    }

    public Parent loadFXML(final String fxml) throws IOException{
        final FXMLLoader fxmlLoader = new FXMLLoader()
        loader = fxmlLoader;
        return fxmlLoader.load();
    }

    public void setRoot(final Layout layout){
        try {
            scene.setRoot(loadFXML(layout.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callErrorLayout(final Throwable exception){
        Parent root = null;
        try {
            root = loadFXML(Layout.ERROR.getFileName());
        } catch (IOException e) {
            System.exit(1);
        }

        //load error view and set error.


    }

}
