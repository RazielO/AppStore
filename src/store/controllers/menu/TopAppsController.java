package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TopAppsController extends Controller implements Initializable
{
    @FXML
    GridPane gridPane;

    private AppDAO appDAO = new AppDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        List<App> apps = appDAO.findTop();

        fillApps(apps, gridPane);
    }
}
