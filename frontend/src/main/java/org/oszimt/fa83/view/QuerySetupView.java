package org.oszimt.fa83.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.definition.RoomSize;
import org.oszimt.fa83.emailhandler.EmailSupplier;
import org.oszimt.fa83.emailhandler.MainController;
import org.oszimt.fa83.emailhandler.ValidationException;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuerySetupView extends AbstractView {

    ObservableList<String> roomsList = FXCollections.observableArrayList();

    @FXML
    private TextField priceTo;

    @FXML
    private TextField city;

    @FXML
    private TextField email;

    @FXML
    private TextField radius;

    @FXML
    private TextField space;

    @FXML
    private TextField queryName;

    @FXML
    private ChoiceBox<String> rooms;

    @FXML
    private TextArea textArea;

    private final MainController controller = MainController.getInstance();

    public void initialize() {
        fillChoiceBox();
        ScrapeQuery activeQuery = controller.getActiveQuery();
        if (activeQuery != null) {
            city.setText(activeQuery.getCity());
            radius.setText(activeQuery.getRadius().toString());
            space.setText(activeQuery.getSpace().toString());
            queryName.setText(activeQuery.getQueryName());
            priceTo.setText(activeQuery.getPriceTo().toString());
            //todo set value from query.
            rooms.setValue(activeQuery.getRoomSize());
        }
    }

    @FXML
    private void startScraping() {
        ScrapeQuery scrapeQuery = setUpScrapeQuery();
        EmailSupplier.getInstance().setEmail(email.getText());
        try {
            controller.startScraping(scrapeQuery);
        } catch (Exception e) {
            callError(e);
        }

    }

    @FXML
    private void switchToMain() {
        StageController.getInstance().setRoot(Layout.MAIN);

    }

    @FXML
    private void removeQuery(){
        controller.removeQuery(controller.getActiveQuery().getPk());
        try {
            controller.write();
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            callError(e);
        }
        textArea.setText(queryName.getText() + " wurde gelöscht");
    }

    @FXML
    private void createQuery(){

        try {
            ScrapeQuery scrapeQuery = setUpScrapeQuery();
            controller.createScrapeQuery(scrapeQuery);
            controller.write();
            controller.setActiveQuery(scrapeQuery);
            textArea.setText(queryName.getText() + " wurde gespeichert");

        } catch (ValidationException | CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            callError(e);
        }
    }
    private Double parseDouble(String toBeParsed, String field, StringBuilder builder) {
        Double parsed = null;
        try {
            if (toBeParsed != null) {
                parsed = Double.parseDouble(priceTo.getText());
            }
        } catch (Exception e) {
            builder.append(field).append(" muss eine Nummer enthalten");
            builder.append("\n");
            return null;
        }
        return parsed;
    }

    private ScrapeQuery setUpScrapeQuery(){
        StringBuilder builder = new StringBuilder();
        Double priceToParsed = parseDouble(priceTo.getText(), "Preis bis", builder);
        String cityText = city.getText();
        Double radiusParsed = parseDouble(radius.getText(), "Radius", builder);
        Double spaceParsed = parseDouble(space.getText(), "Radius", builder);
        String queryNameText = queryName.getText();
        String roomSize = rooms.getValue();

        if (priceToParsed == null || radiusParsed == null || spaceParsed == null) {
            callError(new ValidationException("Eingabefehler", builder.toString()));
            return null;
        }

        return new ScrapeQuery.ScrapeQueryBuilder()
                .queryName(queryNameText)
                .city(cityText)
                .radius(radiusParsed)
                .space(spaceParsed)
                .priceTo(priceToParsed)
                .roomSize(roomSize)
                .build();
    }

    private void fillChoiceBox () {
        List<String> collect = Arrays.stream(RoomSize.values()).map(RoomSize::getDescription).collect(Collectors.toList());
        rooms.getItems().addAll(collect);
    }

}
