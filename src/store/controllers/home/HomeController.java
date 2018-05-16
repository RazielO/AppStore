package store.controllers.home;

import store.controllers.Controller;
import store.database.models.app.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends Controller implements Initializable
{
    @FXML
    GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        fillApps(fillList(), gridPane);
    }

    public static List<App> fillList()
    {
        List<App> apps = new ArrayList<>();
        Long i;

        for (i = Long.valueOf(0); i < 100; i++)
        {
            Image image = new Image("store/controllers/resources/spotify.png");
            App app = new App();

            app.setId(i);
            app.setLogo(image);
            app.setName("Name");
            app.setPublisher("Publisher");
            app.setPrice(150.00);
            app.setRating(4.5);

            apps.add(app);
        }
        return apps;
    }
}
