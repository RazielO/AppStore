package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import store.controllers.Controller;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class FeaturingController extends Controller implements Initializable
{
    @FXML
    GridPane gridPane;

    private AppDAO appDAO = new AppDAO(MySQL.getConnection());

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     * Fills the screen with the featured apps
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
        fillApps(appDAO.findFeatured(), gridPane);
    }
}
