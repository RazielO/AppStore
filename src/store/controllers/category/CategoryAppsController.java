package store.controllers.category;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.category.Category;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.category.CategoryDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryAppsController extends Controller implements Initializable
{
    @FXML
    GridPane gridPane;

    public static String category;

    private AppDAO appDAO = new AppDAO(MySQL.getConnection());
    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Category c = categoryDAO.fetch(category);

        List<App> apps = appDAO.findByCategory(c.getId());

        fillApps(apps, gridPane);
    }
}
