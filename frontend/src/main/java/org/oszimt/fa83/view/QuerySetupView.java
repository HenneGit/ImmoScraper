package org.oszimt.fa83.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.emailhandler.MainController;
import org.oszimt.fa83.emailhandler.ValidationException;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.io.IOException;

public class QuerySetupView extends AbstractView {

    ObservableList<String> roomsList = FXCollections.observableArrayList();

    @FXML
    private TextField priceTo;

    @FXML
    private TextField city;

    @FXML
    private TextField radius;

    @FXML
    private TextField space;

    @FXML
    private TextField queryName;

    @FXML
    private ChoiceBox rooms;

    private MainController controller = MainController.getInstance();

    public void initialize() {
        //todo fill listbox and fields with query.
        fillChoiceBox();
        ScrapeQuery activeQuery = controller.getActiveQuery();
        if (activeQuery != null) {
            city.setText(activeQuery.getCity());
            radius.setText(activeQuery.getRadius().toString());
            space.setText(activeQuery.getSpace().toString());
            queryName.setText(activeQuery.getQueryName());
            priceTo.setText(activeQuery.getPriceTo().toString());
        }
    }


    @FXML
    private void startScraping() {
        ScrapeQuery scrapeQuery = setUpScrapeQuery();
        try {
            controller.startScraping(scrapeQuery);
        } catch (Exception e) {
            callError(e);
        }
        //todo set rooms from enum
        rooms.getValue();

    }

    @FXML
    private void switchToMain() {
        StageController.getInstance().setRoot(Layout.MAIN);

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

        if (priceToParsed == null || radiusParsed == null || spaceParsed == null) {
            callError(new ValidationException("Eingabefehler", builder.toString()));
            return null;
        }
        ScrapeQuery query = new ScrapeQuery.ScrapeQueryBuilder()
                .queryName(queryNameText)
                .city(cityText)
                .radius(radiusParsed)
                .space(spaceParsed)
                .priceTo(priceToParsed)
                .build();
        try {
            controller.createScrapeQuery(query);
            controller.write();
        } catch (ValidationException e) {
            callError(e);
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
        return query;
    }

    private void fillChoiceBox () {
        String egal ="egal";
        String eins ="ab ein Zimmer";
        String zwei ="ab zwei Zimmer";
        String drei ="ab drei Zimmer";
        String vier ="ab vier Zimmer";
        String fünf ="ab fünf Zimmer";
        String sechs ="ab sechs Zimmer";
        roomsList.addAll(egal,eins,zwei,drei,vier,fünf,sechs);
        rooms.getItems().addAll(roomsList);
    }

}
