package store.controllers.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.PurchasedDAO;
import store.database.models.user.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PurchasedController extends Controller implements Initializable
{
    @FXML
    VBox vBox;

    private PurchasedDAO purchasedDAO = new PurchasedDAO(MySQL.getConnection());
    public static User user;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     * Fills the screen with the apps a user has bought.
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
        List<App> apps = purchasedDAO.findAllByUser(user);

        if (apps.size() != 0 && PurchasedController.user == MenuController.user)
            fillPurchased(apps, vBox, true);
        else if (apps.size() != 0 && PurchasedController.user != MenuController.user)
            fillPurchased(apps, vBox, false);
        else
        {
            Label label = new Label("Oops!\nThere's nothing here");
            label.setFont(new Font("Arial", 18));
            label.setTextAlignment(TextAlignment.CENTER);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(label);
        }
    }
}
