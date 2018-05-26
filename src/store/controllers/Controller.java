package store.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import store.controllers.app.AppController;
import store.controllers.menu.MenuController;
import store.controllers.menu.RateAppController;
import store.database.models.app.App;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.app.CommentDAO;
import store.database.models.dao.user.PurchasedDAO;
import store.database.models.dao.user.UserDAO;
import store.main.Main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Controller
{
    protected void changeScene(String fxml)
    {
        BorderPane pane;
        try
        {
            pane = new BorderPane();

            pane.setCenter(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(fxml))));
            pane.setTop(Main.getTopContent());

            Stage stage = store.main.Main.getPrimaryStage();
            stage.setMaximized(true);
            stage.getScene().setRoot(pane);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void openNewWindow(String title, String fxml, Integer height, Integer width, Object controller)
    {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));

        try
        {
            Parent parent = loader.load();
            loader.setController(controller);
            Scene scene = new Scene(parent, width, height);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void alertMessage(String message, String title, Alert.AlertType type, String header)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    protected void fillApps(List<App> apps, GridPane gridPane)
    {
        int i, j, count = 0;

        for (i = 0; i <= (apps.size() / 5); i++)
            for (j = 0; j < 5; j++)
                if ((i * 5) + j >= apps.size())
                    break;
                else
                {
                    App app = apps.get(count);

                    VBox vBox = new VBox();
                    vBox.setAlignment(Pos.CENTER);
                    vBox.setPrefHeight(350);
                    vBox.setPrefWidth(250);
                    vBox.setSpacing(5);

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(200);
                    imageView.setFitWidth(200);
                    imageView.setImage(app.getLogo());

                    Label lblId = new Label(String.valueOf(app.getId()));
                    lblId.setFont(new Font("Arial", 1));
                    lblId.setVisible(false);

                    Label lblName = new Label(app.getName());
                    lblName.setFont(new Font("Arial Black", 24));

                    Label lblPublisher = new Label(app.getPublisher());
                    lblPublisher.setFont(new Font("Arial", 20));

                    Label lblRating = new Label(String.valueOf(app.getRating()));
                    lblRating.setFont(new Font("Arial", 18));

                    Rating rating = new Rating();
                    rating.setOrientation(Orientation.HORIZONTAL);
                    rating.setPartialRating(true);
                    rating.setRating(app.getRating());
                    rating.setUpdateOnHover(false);
                    rating.setMax(5);
                    rating.setDisable(true);

                    Label lblPrice = new Label();
                    lblPrice.setFont(new Font("Arial", 18));

                    if (app.getPrice() != 0)
                        lblPrice.setText("$" + app.getPrice());
                    else
                        lblPrice.setText("Free");

                    Button btnBuy = new Button("Buy");
                    btnBuy.setFont(new Font("Arial", 18));
                    btnBuy.setOnAction(handlerBuy);

                    vBox.getChildren().addAll(imageView, lblId, lblName, lblPublisher, rating, lblPrice, btnBuy);

                    vBox.setOnMouseClicked(event ->
                    {
                        AppController.appName = lblName.getText();
                        changeScene("store/fxml/app/app.fxml");
                    });

                    gridPane.add(vBox, j, i);
                    count = count + 1;
                }
    }

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

    protected void fillPurchased(List<App> apps, VBox vBox, boolean visible)
    {
        for (App app : apps)
        {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            imageView.setImage(app.getLogo());

            Label label1 = new Label(app.getName());
            Label label2 = new Label(app.getPublisher());
            Label label3 = new Label(String.valueOf(app.getDownloads()));
            Label label4 = new Label(app.getVersion());

            Rating rating = new Rating();
            rating.setOrientation(Orientation.HORIZONTAL);
            rating.setPartialRating(true);
            rating.setRating(app.getRating());
            rating.setUpdateOnHover(false);
            rating.setRating(app.getRating());
            rating.setDisable(true);

            Label label5 = new Label(String.valueOf(app.getId()));
            label5.setVisible(false);

            Button button = new Button("Rate it");
            button.setOnAction(handlerRate);
            button.setVisible(visible);

            hBox.getChildren().addAll(imageView, label1, label2, label3, label4, rating, label5, button);

            vBox.getChildren().add(hBox);
        }
    }

    private EventHandler<ActionEvent> handlerRate = event ->
    {
        CommentDAO commentDAO = new CommentDAO(MySQL.getConnection());

        Label temp = (Label) (((Node) event.getSource()).getParent().getChildrenUnmodifiable().get(1));
        RateAppController.appName = temp.getText();

        temp = (Label) (((Node) event.getSource()).getParent().getChildrenUnmodifiable().get(6));
        RateAppController.idApp = Long.parseLong(temp.getText());

        if (commentDAO.exists(Long.parseLong(temp.getText()), MenuController.user.getId()))
            alertMessage("You cannot comment twice on the same app", "Error", Alert.AlertType.ERROR, "You already had commented this app");
        else
        {
            RateAppController controller = new RateAppController();
            openNewWindowShowAndWait("Rate this app", "store/fxml/menu/rateApp.fxml", 250, 750, controller);
        }
    };

    protected void configureFileChooser(final FileChooser fileChooser)
    {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
                                       );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
                                                );
    }

    protected void openNewWindowShowAndWait(String title, String fxml, Integer height, Integer width, Object controller)
    {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));

        try
        {
            Parent parent = loader.load();
            loader.setController(controller);
            Scene scene = new Scene(parent, width, height);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
