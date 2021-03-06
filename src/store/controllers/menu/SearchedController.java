package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.MySQL;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchedController extends Controller implements Initializable
{
    @FXML
    GridPane gridPane;

    private static String search;
    private AppDAO appDAO = new AppDAO(MySQL.getConnection());

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     * Fills the screen with the apps with similar names to the one
     * searched.
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        List<App> apps = appDAO.search(search);
        if (apps.size() > 0)
            fillApps(apps, gridPane);
        else
        {
            Label label = new Label("Sorry\nWe couldn't found anything");
            label.setFont(new Font("Arial", 30));
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);

            gridPane.setAlignment(Pos.CENTER);

            gridPane.add(label, 0, 0, 5, 5);
        }
    }

    /**
     * Called to set the string searched
     *
     * @param search String searched
     */
    static void setSearch(String search)
    {
        SearchedController.search = search;
    }
}
