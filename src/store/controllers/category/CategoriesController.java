package store.controllers.category;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import store.controllers.Controller;
import store.database.models.dao.category.CategoryDAO;
import store.database.models.dao.MySQL;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriesController extends Controller implements Initializable
{
    @FXML
    VBox boxGames, boxApps;

    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        fillCategories();
    }

    void fillCategories()
    {
        int i;
        for(i = 0; i < 20; i++)
        {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setImage(new Image("store/controllers/resources/flappy.png"));

            Label label = new Label("Game " + (i + 1));
            label.setFont(new Font("Arial", 18));

            hBox.getChildren().addAll(imageView, label);

            boxGames.getChildren().add(hBox);
        }

        for(i = 0; i < 20; i++)
        {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setImage(new Image("store/controllers/resources/evernote.png"));

            Label label = new Label("App " + (i + 1));
            label.setFont(new Font("Arial", 18));

            hBox.getChildren().addAll(imageView, label);

            boxApps.getChildren().add(hBox);
        }
    }
}
