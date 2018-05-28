package store.controllers.category;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import store.controllers.Controller;
import store.database.models.category.Category;
import store.database.models.dao.category.CategoryDAO;
import store.database.models.dao.MySQL;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesController extends Controller implements Initializable
{
    @FXML
    VBox boxApps;

    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());

    /**
     * Called to initialize a controller after its root element has been
     * completely processed. Calls the fillCategories method
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
        fillCategories();
    }

    /**
     * Called to fill the VBox with the categories in the database
     */
    private void fillCategories()
    {
        List<Category> categories = categoryDAO.fetchAll();

        for(Category category : categories)
        {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setImage(category.getLogo());

            Label label = new Label(category.getName());
            label.setFont(new Font("Arial", 18));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);

            hBox.getChildren().addAll(imageView, label);
            hBox.setOnMouseClicked(handler);

            boxApps.getChildren().add(hBox);
        }
    }

    /**
     * Called when a category is pressed, it changes the scene
     * to the apps on that category
     */
    private EventHandler<MouseEvent> handler = event ->
    {
        Label label = (Label) ((HBox) event.getSource()).getChildren().get(1);

        CategoryAppsController.category = label.getText();
        changeScene("store/fxml/category/categoryApps.fxml");
    };
}
