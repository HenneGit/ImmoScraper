package org.oszimt.fa83.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.pojo.SearchQuery;
import org.oszimt.fa83.repository.SearchQueryFileWriter;
import org.oszimt.fa83.repository.SearchQueryRepositoryImpl;
import org.oszimt.fa83.repository.api.SearchQueryRepository;

import java.util.ArrayList;
import java.util.List;

public class MainView extends AbstractView {


    @FXML
    private Button setupQuery;

    @FXML
    private ComboBox<SearchQuery> queryComboBox;

    private SearchQueryRepository repository = (SearchQueryRepository) SearchQueryRepositoryImpl.getInstance();


    public void initialize(){

        SearchQueryFileWriter writer = SearchQueryFileWriter.getInstance();
        SearchQuery query = new SearchQuery.SearchQueryBuilder()
                .queryName("Berlin 1")
                .city("Berlin")
                .priceFrom(4D)
                .priceTo(200D).build();
        List<SearchQuery> queries = new ArrayList<>();
        queries.add(query);
        writer.write(queries);
        ObservableList<SearchQuery> queryList = FXCollections.observableArrayList(repository.findAll());
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
            public String toString(SearchQuery product) {
                return product.getQueryName();
            }
            @Override
            public SearchQuery fromString(final String string) {
                return queryComboBox.getItems().stream().filter(query -> query.getQueryName().equals(string)).findFirst().orElse(null);
            }
        });
    }
}
