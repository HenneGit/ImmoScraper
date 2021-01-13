package org.oszimt.fa83.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.oszimt.fa83.StageController;
import org.oszimt.fa83.definition.Layout;
import org.oszimt.fa83.emailhandler.EmailSupplier;
import org.oszimt.fa83.emailhandler.MainController;
import org.oszimt.fa83.emailhandler.ValidationException;
import org.oszimt.fa83.pojo.ScrapeQuery;
import org.oszimt.fa83.repository.ScrapeQueryRepositoryImpl;
import org.oszimt.fa83.repository.api.ScrapeQueryRepository;

import javax.mail.MessagingException;
import java.lang.management.ManagementFactory;

public class QuerySetupView extends AbstractView {


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
        StringBuilder builder = new StringBuilder();
        Double priceToParsed = parseDouble(priceTo.getText(), "Preis bis", builder);
        String cityText = city.getText();
        Double radiusParsed = parseDouble(radius.getText(), "Radius", builder);
        Double spaceParsed = parseDouble(space.getText(), "Radius", builder);
        String queryNameText = queryName.getText();

        if (priceToParsed == null || radiusParsed == null || spaceParsed == null) {
            callError(new ValidationException("Eingabefehler", builder.toString()));
            return;
        }
        ScrapeQuery query = new ScrapeQuery.ScrapeQueryBuilder()
                .queryName(queryNameText)
                .city(cityText)
                .radius(radiusParsed)
                .space(spaceParsed)
                .priceTo(priceToParsed)
                .build();

        try {
            MainController.getInstance().startScraping(query);
        } catch (Exception e) {
            callError(e);
        }
        //todo set rooms from enum
        //rooms.getValue();

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

}
