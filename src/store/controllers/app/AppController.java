package store.controllers.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import store.controllers.Controller;
import store.controllers.menu.MenuController;
import store.database.models.app.App;
import store.database.models.app.Comment;
import store.database.models.app.Language;
import store.database.models.app.Screenshot;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.user.PurchasedDAO;
import store.database.models.dao.user.UserDAO;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable
{
    @FXML
    ImageView image;
    @FXML
    Label lblName, lblPublisher, lblCategory, lblDownloads, lblPrice, lblSize, lblVersion, lblCompatibility,
            lblLanguages, lblFeatures;
    @FXML
    Button btnBuy, btnEdit, btnDelete, btnFeature;
    @FXML
    TextArea txtDescription;
    @FXML
    HBox hboxScreenshot;
    @FXML
    VBox vboxComments;
    @FXML
    Rating rating;


    public static String appName;

    private AppDAO appDAO = new AppDAO(MySQL.getConnection());
    private App app;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed. Calls the init method.
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
        init();
    }

    /**
     * Fills all the elements on the app window, also gives the handler to the buttons.
     * Makes visible or invisible some buttons for admin utilities.
     */
    private void init()
    {
        app = new App();
        app.setName(AppController.appName);
        app = appDAO.fetch(app);
        btnEdit.setOnAction(handler);
        btnDelete.setOnAction(handler);
        btnBuy.setOnAction(handlerBuy);
        btnFeature.setOnAction(handler);

        if (MenuController.user.getId() != null && MenuController.user.getAdmin())
        {
            btnFeature.setText(app.isFeatured() ? "Remove feature" : "Feature");

            btnEdit.setVisible(true);
            btnDelete.setVisible(true);
            btnFeature.setVisible(true);
        }

        if (app != null)
        {
            image.setImage(app.getLogo());

            lblName.setText(app.getName());
            lblCategory.setText("Category: " + app.getCategory());
            lblCompatibility.setText("Compatibility: " + app.getCompatibility());
            lblDownloads.setText(String.valueOf(app.getDownloads()) + " downloads");
            lblFeatures.setText(app.getFeatures());
            lblPublisher.setText(app.getPublisher());
            lblSize.setText("Size: " + app.getSize());
            lblVersion.setText("Version: " + app.getVersion());
            txtDescription.setText(app.getDescription());
            rating.setRating(app.getRating());

            if (app.getPrice() != 0)
                lblPrice.setText("$" + app.getPrice());
            else
                lblPrice.setText("Free");

            StringBuilder text = new StringBuilder();
            for (Language language : app.getLanguages())
                text.append(language.getName()).append("\n");

            lblLanguages.setText(text.toString());

            for (Screenshot screenshot : app.getScreenshots())
            {
                ImageView temp = new ImageView();
                temp.setImage(screenshot.getScreenshot());
                temp.setFitHeight(350);
                temp.setPreserveRatio(true);
                hboxScreenshot.getChildren().add(temp);
            }

            for (Comment comment : app.getComments())
            {
                HBox hBox = new HBox();
                hBox.setSpacing(20);

                VBox vBox = new VBox();
                vBox.setSpacing(10);
                vBox.setAlignment(Pos.CENTER);

                Label lblUser = new Label(comment.getUsername());

                Rating rating = new Rating();
                rating.setOrientation(Orientation.HORIZONTAL);
                rating.setDisable(true);
                rating.setPartialRating(true);
                rating.setRating(comment.getRating());

                Label lblDate = new Label(String.valueOf(comment.getDate()));

                vBox.getChildren().addAll(lblUser, rating, lblDate);

                TextArea txtComment = new TextArea(comment.getComment());
                txtComment.setEditable(false);

                hBox.getChildren().addAll(vBox, txtComment);

                vboxComments.getChildren().add(hBox);
            }
        }
    }

    /**
     * Called when a button is pressed, this changes the scene to edit, deletes the app or
     * features or unfeatures the app
     */
    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnEdit)
        {
            EditAppController.app = this.app;
            changeScene("store/fxml/app/editApp.fxml");
        }
        else if (event.getSource() == btnDelete)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("¿Do you want to delete this app?");
            alert.setContentText("Confirm that you want to delete this app");
            alert.setTitle("Delete app");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK)
            {
                String temp = app.getName();

                boolean result = appDAO.delete(this.app);

                if (result)
                {
                    changeScene("store/fxml/home/home.fxml");
                    alertMessage("It was deleted", "Info", Alert.AlertType.INFORMATION, temp + " was deleted");
                }
            }
        }
        else if (event.getSource() == btnFeature)
        {
            app.setFeatured(!app.isFeatured());
            appDAO.updateWithoutImage(app);
            btnFeature.setText(app.isFeatured() ? "Remove feature" : "Feature");
        }
    };

    /**
     * Called when the buy button is pressed.
     * Adds the transaction to the database
     */
    protected EventHandler<ActionEvent> handlerBuy = event ->
    {
        if (MenuController.user.getId() == null)
            alertMessage("You have to login first to buy an app", "Error", Alert.AlertType.ERROR, "Login first");
        else
        {
            AppDAO appDAO = new AppDAO(MySQL.getConnection());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("¿Do you want to buy this app?");
            alert.setContentText("Confirm that you want this app");
            alert.getDialogPane().getStylesheets().add("store/resources/css/JMetroLightTheme.css");
            alert.setTitle("Buy this app");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK)
            {
                Label label = (Label) ((Node) event.getSource()).getParent().getChildrenUnmodifiable().get(2);

                PurchasedDAO purchasedDAO = new PurchasedDAO(MySQL.getConnection());
                UserDAO userDAO = new UserDAO(MySQL.getConnection());

                App app = new App();
                app.setName(label.getText());
                app = appDAO.fetch(app);

                try
                {
                    purchasedDAO.insert(app, userDAO.findByEmail(MenuController.user.getEmail()));
                    alertMessage("Thanks for buying this app", "New app", Alert.AlertType.INFORMATION, "Congrats! you own this app");
                    app.setDownloads(app.getDownloads() + 1);
                    appDAO.updateWithoutImage(app);
                }
                catch (SQLException e)
                {
                    if (e.toString().contains("Duplicate entry"))
                        alertMessage("You already have this app", "Error", Alert.AlertType.ERROR, "You cannot buy this twice");
                    else
                        e.printStackTrace();
                }
            }
        }
    };
}
