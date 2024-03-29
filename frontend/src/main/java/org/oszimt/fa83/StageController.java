package org.oszimt.fa83;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.exception.NoEmailCredentialsSet;
import org.oszimt.fa83.repository.CSVNotFoundException;
import org.oszimt.fa83.view.EmailSetup;
import org.oszimt.fa83.view.ErrorView;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * controller for frontend stages.
 */
public class StageController {


    private static final StageController INSTANCE = new StageController();
    private static final Layout DEFAULT_LAYOUT = Layout.QUERY;
    private Scene scene;
    private Stage stage;
    private FXMLLoader loader;
    private MainController controller = MainController.getInstance();

    public static StageController getInstance(){
        return INSTANCE;
    }

    /**
     * setup stage
     * @param window window to setup stage to.
     * @throws IOException
     */
    public void init(final Stage window) throws IOException {

        scene = new Scene(loadFXML(DEFAULT_LAYOUT.getFileName()));
        this.stage = window;
        stage.setResizable(false);
        stage.setMinHeight(320D);
        stage.setMinWidth(660D);
        stage.setOnCloseRequest(event -> {
            try {
                controller.write();
                controller.writeEmailCredentials();
            } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
                callErrorLayout(e);
            }
        });

        window.setTitle("ImmoScraper");
        window.setScene(scene);
        window.show();
    }

    public Parent loadFXML(final String fxml) throws IOException{
        final FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain.class.getResource(fxml));
        loader = fxmlLoader;
        return fxmlLoader.load();
    }

    /**
     * deal with exception and set contents to error layout.
     * @param exception the exception to display in popup.
     */
    public void callErrorLayout(final Exception exception){
        Parent root = null;
        try {
            root = loadFXML(Layout.ERROR.getFileName());
        } catch (IOException e) {
            System.exit(1);
        }
        ErrorView view = loader.getController();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        if (exception instanceof ValidationException){
            ValidationException ex = (ValidationException) exception;
            view.setMessage(ex.getReason());
            view.initStage(root);
            return;
        }
        if (exception instanceof CSVNotFoundException){
            CSVNotFoundException ex = (CSVNotFoundException) exception;
            view.setMessage(ex.getMessage());
            view.initStage(root);
            return;
        }
        if (exception instanceof NoEmailCredentialsSet) {
            NoEmailCredentialsSet ex = (NoEmailCredentialsSet) exception;
            view.setMessage(ex.getMessage());
            view.initStage(root);
            return;

        } else {
            exception.printStackTrace(pw);
            view.setMessage(writer.toString());
        }
        view.initStage(root);
    }

    public void callEmailSetup() {
        Parent root = null;
        try {
            root = loadFXML(Layout.EMAIL.getFileName());
        } catch (IOException e) {
            System.exit(1);
        }
        EmailSetup view = loader.getController();
        view.initStage(root);

    }

}
