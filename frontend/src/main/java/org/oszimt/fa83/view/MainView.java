package org.oszimt.fa83.view;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.pojo.ValidationError;
import org.oszimt.fa83.repository.ScrapeQueryFileWriter;
import org.oszimt.fa83.repository.ScrapeQueryRepositoryImpl;
import org.oszimt.fa83.repository.api.ScrapeQueryController;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView extends AbstractView {


    @FXML
    private Button setupQuery;

    @FXML
    private ComboBox<ScrapeQuery> queryComboBox;

    private final ScrapeQueryRepository repository = (ScrapeQueryRepository) ScrapeQueryRepositoryImpl.getInstance();


    public void initialize() throws ValidationError {

        ScrapeQueryFileWriter writer = ScrapeQueryFileWriter.getInstance();
        ScrapeQuery query = new ScrapeQuery.ScrapeQueryBuilder()
                .queryName("Berlin 1")
                .city("Berlin")
                .radius(15)
                .space(40)
                .priceTo(700D)
                .build();
        List<ScrapeQuery> queries = new ArrayList<>();
        queries.add(query);

        try {
            writer.write(queries);
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            callError(e);
        }

        ObservableList<ScrapeQuery> queryList = FXCollections.observableArrayList(repository.findAll());
        queryComboBox.itemsProperty().setValue(queryList);
        convertComboDisplayList();
    }


    @FXML
    private void switchToQuerySetup(){
        StageController.getInstance().setRoot(Layout.QUERY);
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
}
