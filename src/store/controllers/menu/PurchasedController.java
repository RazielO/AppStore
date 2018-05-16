package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import store.controllers.Controller;
import store.controllers.home.HomeController;
import store.database.models.app.App;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.PurchasedDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PurchasedController extends Controller implements Initializable
{
    @FXML
    VBox vBox;

    private PurchasedDAO purchasedDAO = new PurchasedDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        List<App> apps = purchasedDAO.findAllByUser(MenuController.user);

        if (apps.size() != 0)
            fillPurchased(apps, vBox);
        else
        {
            Label label = new Label("Oops!\nLooks like you haven't bought anything");
            label.setFont(new Font("Arial", 18));
            label.setTextAlignment(TextAlignment.CENTER);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(label);
        }
    }
}
