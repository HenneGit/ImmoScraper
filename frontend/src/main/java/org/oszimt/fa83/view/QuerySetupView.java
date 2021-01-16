package org.oszimt.fa83.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.oszimt.fa83.ValidationException;
import org.oszimt.fa83.definition.RoomSize;
import org.oszimt.fa83.email.EmailSupplier;
import org.oszimt.fa83.MainController;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.pojo.ScrapeResultPojo;
import org.oszimt.fa83.repository.CSVNotFoundException;
import org.oszimt.fa83.util.QueryValidator;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class QuerySetupView extends AbstractView {


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

    @FXML
    private ComboBox<ScrapeQuery> queryComboBox;

    private boolean isScraping;
    private final MainController controller = MainController.getInstance();

    public void initialize() {
        try {
            Collection<ScrapeQuery> scrapeQueries = controller.getScrapeQueries();
            ObservableList<ScrapeQuery> queryList = FXCollections.observableArrayList(scrapeQueries);
            queryComboBox.itemsProperty().setValue(queryList);
        } catch (Exception e) {
            try {
                if (e instanceof CSVNotFoundException) {
                    controller.write();
                }
            } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException csvRequiredFieldEmptyException) {
                callError(e);
            }
        }
        fillRoomChoiceBox();
        updateCombobox();
        convertComboDisplayList();
        queryComboBox.valueProperty().addListener(new ChangeListener<ScrapeQuery>() {
            @Override
            public void changed(ObservableValue<? extends ScrapeQuery> observableValue, ScrapeQuery query, ScrapeQuery t1) {
                controller.setActiveQuery(t1);
                fillScrapeQueryFields();
            }
        });
        textArea.setEditable(false);
    }

    @FXML
    private void startScraping() {
        if (isScraping){
            return;
        }
        createQuery();
        clearTextArea();
        if (StringUtils.isEmpty(email.getText())) {
            try {
                QueryValidator.validateEmail(email.getText());
            } catch (ValidationException e) {
                callError(e);
                return;
            }
        } else {
            EmailSupplier.getInstance().setEmail(email.getText());
        }
        try {
            isScraping = true;
            int randomTimeout = 0;
            while (isScraping){
                addTextToTextArea("Scraping wird ausgeführt... ");
                TimeUnit.MINUTES.sleep(randomTimeout);
                List<ScrapeResultPojo> results = controller.startScraping();
                if (results.size() > 0){
                    controller.write();
                    addTextToTextArea(results.size() + " Ergebnisse gefunden");
                    addTextToTextArea("Sende email. ");
                    controller.sendEmail(packResultLinks(results), "Hab was gefunden");
                    addTextToTextArea("Email gesendet ");
                    addTextToTextArea(packResultLinks(results));
                }
                Random random = new Random();
                randomTimeout = random.nextInt(1 + 5) + 1;
                addTextToTextArea("Schlafe für " + randomTimeout + " Minuten");
            }

        } catch (Exception e) {
            isScraping = false;
            callError(e);
        }

    }

    @FXML
    private void removeQuery() {
        controller.removeQuery(controller.getActiveQuery().getPk());
        try {
            controller.write();
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            callError(e);
        }
        updateCombobox();
        addTextToTextArea(queryName.getText() + " wurde gelöscht");
    }

    @FXML
    private void createQuery() {
            try {
                ScrapeQuery scrapeQuery = setUpScrapeQuery();
                if (scrapeQuery != null) {
                    ScrapeQuery newScrapeQuery = controller.createScrapeQuery(scrapeQuery);
                    controller.write();
                    controller.setActiveQuery(newScrapeQuery);
                    addTextToTextArea(queryName.getText() + " wurde gespeichert");
                    updateCombobox();
                }
            } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException | ValidationException | CSVNotFoundException e) {
                callError(e);
            }
    }

    @FXML
    private void stopScraping(){
        addTextToTextArea("Stoppe...");
        isScraping = false;
    }

    private ScrapeQuery setUpScrapeQuery() {
        QueryValidator validator = new QueryValidator(new StringBuilder());
        try {
            validator.validate(getAllTextFields());
        } catch (ValidationException e) {
            callError(e);
            return null;
        }
        return new ScrapeQuery.ScrapeQueryBuilder()
                .queryName(queryName.getText())
                .city(city.getText())
                .radius(parseDouble(radius.getText()))
                .space(parseDouble(space.getText()))
                .priceTo(parseDouble(priceTo.getText()))
                .roomSize(RoomSize.getRoomSizeByDescription(rooms.getValue()))
                .email(email.getText())
                .build();

    }

    private Double parseDouble(String doubleToParse) {
        if (StringUtils.isEmpty(doubleToParse)) {
            return null;
        } else {
            return Double.parseDouble(doubleToParse);
        }

    }

    private void fillRoomChoiceBox() {
        List<String> collect = Arrays.stream(RoomSize.values()).map(RoomSize::getDescription).collect(Collectors.toList());
        rooms.getItems().addAll(collect);
    }

    private void convertComboDisplayList() {
        queryComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ScrapeQuery product) {
                return product.getQueryName();
            }

            @Override
            public ScrapeQuery fromString(final String string) {
                return queryComboBox.getItems().stream().filter(query -> query.getQueryName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void updateCombobox() {
        ObservableList<ScrapeQuery> queryList = null;
        try {
            queryList = FXCollections.observableArrayList(controller.getScrapeQueries());
        } catch (CSVNotFoundException e) {
            callError(e);
        }
        queryComboBox.itemsProperty().setValue(queryList);
        convertComboDisplayList();

    }

    private void addTextToTextArea(String text){
        String currentText = textArea.getText();
        currentText += "\n";
        currentText += getTime() + ": " + text;
        textArea.setText(currentText);
    }

    private void fillScrapeQueryFields() {
        ScrapeQuery activeQuery = controller.getActiveQuery();
        if (activeQuery != null) {

            if (activeQuery.getRadius() != null) {
                radius.setText(String.valueOf(activeQuery.getRadius().doubleValue()));
            }
            if (activeQuery.getSpace() != null) {
                space.setText(String.valueOf(activeQuery.getSpace().doubleValue()));
            }
            if (activeQuery.getPriceTo() != null) {
                priceTo.setText(String.valueOf(activeQuery.getPriceTo().doubleValue()));
            }
            if (activeQuery.getEmail() != null) {
                email.setText(activeQuery.getEmail());
            }
            queryName.setText(activeQuery.getQueryName());
            city.setText(Objects.requireNonNull(activeQuery.getCity()));
            rooms.getSelectionModel().select(RoomSize.getRoomSizeString(activeQuery.getRoomSize()));
        }

    }

    private void clearTextArea() {
        textArea.setText("");
    }

    private List<TextField> getAllTextFields() {
        List<TextField> allFields = new ArrayList<>();
        allFields.add(radius);
        allFields.add(space);
        allFields.add(email);
        allFields.add(queryName);
        allFields.add(priceTo);
        allFields.add(city);
        return allFields;
    }

    private String getTime(){
        LocalTime now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String packResultLinks(List<ScrapeResultPojo> resultPojos) {
        StringBuilder builder = new StringBuilder();
        resultPojos.forEach(pojo -> {
            builder.append(pojo.getUrl());
            builder.append("\n");
        });
        return builder.toString();
    }
}
