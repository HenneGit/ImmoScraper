package org.oszimt.fa83.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import org.oszimt.fa83.util.SearchJob;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * class for managing frontend functions.
 */
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

    @FXML
    private CheckBox hasWBS;

    private SearchJob searchJob;
    private volatile boolean isScraping;
    private final MainController controller = MainController.getInstance();

    /**
     * fetches all scrape queries. Fills Choice and Comboboxes, Add Eventlisteners, Set field properties.
     */
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

    /**
     * logic for scraping. Sets and saves scrape query if not yet in repository. Sets email if not saved in query.
     * Starts the scraping action and sets a random time out to prevent bot detection by immoscout. If new results are found an email
     * is send.
     */
    @FXML
    private void startScraping() {
        try {
            controller.write();
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            callError(e);
        }

        if (isScraping) {
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
        addTextToTextArea("Scraping wird gestartet... ");
        searchJob = new SearchJob(this::search);
        searchJob.start();
    }

    public void search() {
        try {
            List<ScrapeResultPojo> results = controller.startScraping();
            if (results.size() > 0) {
                addTextToTextArea(results.size() + " Ergebnisse gefunden");
                addTextToTextArea("Sende email. ");
                controller.sendEmail(packResultLinks(results), "Hab was gefunden");
                addTextToTextArea("Email gesendet ");
                addTextToTextArea("Email wäre gesendet worden :(.");
                addTextToTextArea(packResultLinks(results));
            } else {
                addTextToTextArea("Nichts gefunden");
            }

        } catch (IOException | CSVNotFoundException | MessagingException e) {
            callError(e);
        }

    }

    private void sendEmail(){

        addTextToTextArea("sende email");
        try {
            controller.sendEmail("Test", "Test");
            addTextToTextArea("email gesendet");
        } catch (MessagingException e) {
            callError(e);
        }

    }

    /**
     * delete the current query.
     */
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

    /**
     * validates user input and saves query to repository.
     */
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

    /**
     * stop scraping action.
     */
    @FXML
    private void stopScraping() {
        searchJob.kill();
        addTextToTextArea("Suche gestoppt");

    }

    /**
     * validate and setup scrapequery.
     *
     * @return the created scrape query.
     */
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
                .hasWBS(hasWBS.isSelected())
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

    private void addTextToTextArea(String text) {
        String currentText = textArea.getText();
        currentText += getTime() + ": " + text;
        currentText += "\n";
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
            if (activeQuery.getHasWBS() != null) {
                hasWBS.setSelected(activeQuery.getHasWBS());
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

    private String getTime() {
        LocalTime now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String packResultLinks(List<ScrapeResultPojo> resultPojos) {
        StringBuilder builder = new StringBuilder();
        if (!resultPojos.isEmpty()) {
            for (ScrapeResultPojo pojo : resultPojos) {
                if (pojo != null) {
                    builder.append(pojo.getUrl());
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }
}
