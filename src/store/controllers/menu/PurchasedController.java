package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import store.controllers.Controller;
import store.controllers.home.HomeController;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.PurchasedDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class PurchasedController extends Controller implements Initializable
{
    @FXML
    VBox vBox;

    PurchasedDAO purchasedDAO = new PurchasedDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        fillPurchased(HomeController.fillList(), vBox);
    }
}
