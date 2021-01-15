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
import org.oszimt.fa83.emailhandler.EmailSupplier;
import org.oszimt.fa83.emailhandler.MainController;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.util.QueryValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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




    private final MainController controller = MainController.getInstance();

    public void initialize() {
        fillRoomChoiceBox();
        updateCombobox();
        ObservableList<ScrapeQuery> queryList = FXCollections.observableArrayList(controller.getScrapeQueries());
        queryComboBox.itemsProperty().setValue(queryList);
        queryComboBox.valueProperty().addListener(new ChangeListener<ScrapeQuery>() {
            @Override
            public void changed(ObservableValue<? extends ScrapeQuery> observableValue, ScrapeQuery query, ScrapeQuery t1) {
                controller.setActiveQuery(t1);
                fillScrapeQueryFields();
            }
        });
        convertComboDisplayList();
        textArea.setEditable(false);

    }

    @FXML
    private void startScraping() {
        ScrapeQuery scrapeQuery = setUpScrapeQuery();
        if (StringUtils.isEmpty(email.getText())){
            try {
                QueryValidator.validateEmail(email.getText());
            } catch (ValidationException e) {
                callError(e);
                return;
            }
        }  else {
            EmailSupplier.getInstance().setEmail(email.getText());
        }
        try {
            controller.startScraping(scrapeQuery);
        } catch (Exception e) {
            callError(e);
        }

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
            if (scrapeQuery != null) {
                controller.createScrapeQuery(scrapeQuery);
                controller.write();
                controller.setActiveQuery(scrapeQuery);
                textArea.setText(queryName.getText() + " wurde gespeichert");
                updateCombobox();
            }
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException | ValidationException e) {
            callError(e);
        }
    }


    private ScrapeQuery setUpScrapeQuery(){
        QueryValidator validator = new QueryValidator(new StringBuilder());
        try {
            validator.validate(getAllTextFields());
        } catch (ValidationException e) {
            callError(e);
        }
        return new ScrapeQuery.ScrapeQueryBuilder()
                .queryName(queryName.getText())
                .city(city.getText())
                .radius(parseDouble(radius.getText()))
                .space(parseDouble(space.getText()))
                .priceTo(parseDouble(priceTo.getText()))
                .roomSize(rooms.getValue())
                .email(email.getText())
                .build();
    }

    private Double parseDouble(String doubleToParse){
        if (StringUtils.isEmpty(doubleToParse)){
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

    private void updateCombobox(){
        ObservableList<ScrapeQuery> queryList = FXCollections.observableArrayList(controller.getScrapeQueries());
        queryComboBox.itemsProperty().setValue(queryList);
        convertComboDisplayList();

        }

    private void fillScrapeQueryFields(){
        ScrapeQuery activeQuery = controller.getActiveQuery();
        if (activeQuery != null) {

            if (activeQuery.getRadius() != null){
                radius.setText(String.valueOf(activeQuery.getRadius().doubleValue()));
            }
            if (activeQuery.getSpace() != null){
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
            //todo set value from query.
            rooms.setValue(activeQuery.getRoomSize());
        }

    }
    private List<TextField> getAllTextFields(){
        List<TextField> allFields = new ArrayList<>();
        allFields.add(radius);
        allFields.add(space);
        allFields.add(email);
        allFields.add(queryName);
        allFields.add(priceTo);
        allFields.add(city);
        return allFields;
    }
}
