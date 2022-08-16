package org.oszimt.fa83.view;

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
import org.controlsfx.control.CheckComboBox;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.ValidationException;
import org.oszimt.fa83.definition.District;
import org.oszimt.fa83.definition.RoomSize;
import org.oszimt.fa83.email.EmailSupplier;
import org.oszimt.fa83.MainController;
import org.oszimt.fa83.exception.NoEmailCredentialsSet;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.pojo.ScrapeResultPojo;
import org.oszimt.fa83.repository.CSVNotFoundException;
import org.oszimt.fa83.util.QueryValidator;
import org.oszimt.fa83.util.RandomTimeoutSupplier;
import org.oszimt.fa83.util.SearchJob;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private TextField space;

    @FXML
    private TextField queryName;

    @FXML
    private ComboBox<RoomSize> rooms;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<ScrapeQuery> queryComboBox;

    @FXML
    private CheckComboBox<District> districts;

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

            ObservableList<District> allDistricts = FXCollections.observableArrayList(Arrays.asList(District.values()));
            districts.getItems().addAll(allDistricts);

            ObservableList<RoomSize> allRoomSizes = FXCollections.observableArrayList(Arrays.asList(RoomSize.values()));
            rooms.itemsProperty().setValue(allRoomSizes);


        } catch (Exception e) {
            try {
                if (e instanceof CSVNotFoundException) {
                    controller.write();
                    initialize();
                }
            } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException csvRequiredFieldEmptyException) {
                callError(e);
            }
        }
        updateCombobox();
        convertScrapeQueryComboBox();
        convertDistrictComboBox();
        convertRoomsComboBox();
        queryComboBox.valueProperty().addListener((observableValue, query, t1) -> {
            controller.setActiveQuery(t1);
            fillScrapeQueryFields();
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
                addTextToTextArea(packResultLinks(results));
                long random = RandomTimeoutSupplier.getCurrentTimeout();
                addTextToTextArea("Schlafe für " + random / 1000 + " Sekunden");

            } else {
                addTextToTextArea("Nichts gefunden");
            }

        } catch (IOException | CSVNotFoundException | MessagingException | NoEmailCredentialsSet e) {
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

    @FXML
    private void openEmailSettings() {
        StageController.getInstance().callEmailSetup();

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
                .space(parseDouble(space.getText()))
                .priceTo(parseDouble(priceTo.getText()))
                .roomSize(rooms.getValue().getSize())
                .email(email.getText())
                .district(getZipCodes())
                .hasWBS(hasWBS.isSelected())
                .build();
    }

    private String getZipCodes() {
        ObservableList<Integer> checkedIndices = districts.getCheckModel().getCheckedIndices();
        if (checkedIndices.size() > 1) {
            List<District> selectedDistricts = new ArrayList<>();
            for (Integer integer : checkedIndices) {
                selectedDistricts.add(districts.getItems().get(integer.intValue()));
            }

            List<Long> zipCodes = selectedDistricts.stream().map(District::getZipCode).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            zipCodes.forEach(zipCode -> {
                sb.append(zipCode);
                sb.append(";");
            });
            return sb.toString();
        } else {
            return districts.getItems().get(checkedIndices.get(0)).getLocationURL();
        }

    }


    private Double parseDouble(String doubleToParse) {
        if (StringUtils.isEmpty(doubleToParse)) {
            return null;
        } else {
            return Double.parseDouble(doubleToParse);
        }
    }

    private void convertScrapeQueryComboBox() {
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

    private void convertRoomsComboBox() {
        rooms.setConverter(new StringConverter<>() {
            @Override
            public String toString(RoomSize size) {
                return size.getDescription();
            }

            @Override
            public RoomSize fromString(final String string) {
                return rooms.getItems().stream().filter(rooms -> rooms.getDescription().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void convertDistrictComboBox() {
        districts.setConverter(new StringConverter<>() {
            @Override
            public String toString(District size) {
                return size.getDistrict();
            }

            @Override
            public District fromString(final String string) {
                return districts.getItems().stream().filter(district -> district.getDistrict().equals(string)).findFirst().orElse(null);
            }
        });
    }


    private void updateCombobox() {
        resetDistricts();
        ObservableList<ScrapeQuery> queryList = null;
        try {
            queryList = FXCollections.observableArrayList(controller.getScrapeQueries());
        } catch (CSVNotFoundException e) {
            callError(e);
        }
        queryComboBox.itemsProperty().setValue(queryList);
        convertScrapeQueryComboBox();

    }

    private void addTextToTextArea(String text) {
        String currentText = textArea.getText();
        currentText += getTime() + ": " + text;
        currentText += "\n";
        textArea.setText(currentText);
    }

    private void fillScrapeQueryFields() {
        resetDistricts();
        ScrapeQuery activeQuery = controller.getActiveQuery();
        if (activeQuery != null) {

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
            rooms.getSelectionModel().select(RoomSize.getRoomSizeByDouble(activeQuery.getRoomSize()));
            if (activeQuery.getDistrict() != null ){
                List<Integer> checkedIndices = getCheckedIndices(activeQuery.getDistrict());
                checkedIndices.forEach(i -> districts.getCheckModel().check(i));
            }
        }

    }


    private void clearTextArea() {
        textArea.setText("");
    }

    private List<TextField> getAllTextFields() {
        List<TextField> allFields = new ArrayList<>();
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


    private List<Integer> getCheckedIndices(String district) {
        List<Integer> indices = new ArrayList<>();
        if (district.contains("/")) {
            for (District dis : districts.getItems()) {
                if (dis.getLocationURL().equals(district)) {
                    indices.add(districts.getItems().indexOf(dis));
                }
            }
        }
        if (district.contains(";")) {
            List<Long> zipCodes = Arrays.stream(district.split(";"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            districts.getItems().forEach(d -> {
                if (zipCodes.contains(d.getZipCode())) {
                    indices.add(districts.getItems().indexOf(d));
                }
            });
        }
        return indices;

    }

    private void resetDistricts(){
        for (int i = 0; i < districts.getItems().size(); i++) {
            districts.getCheckModel().clearCheck(i);
        }
    }

}
