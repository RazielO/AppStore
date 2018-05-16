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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import store.controllers.menu.MenuController;
import store.database.models.app.App;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.MySQL;
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

        for (i = 0; i < (apps.size() / 5); i++)
            for (j = 0; j < 5; j++)
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

                Label lblPrice = new Label("$" + app.getPrice());
                lblPrice.setFont(new Font("Arial", 18));

                Button btnBuy = new Button("Buy");
                btnBuy.setFont(new Font("Arial", 18));
                btnBuy.setOnAction(handler);

                vBox.getChildren().addAll(imageView, lblId, lblName, lblPublisher, rating, lblPrice, btnBuy);

                vBox.setOnMouseClicked(event -> changeScene("store/fxml/app/app.fxml"));

                gridPane.add(vBox, j, i);
                count = count + 1;
            }
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (MenuController.user.getId() == null)
            alertMessage("You have to login first to buy an app", "Error", Alert.AlertType.ERROR, "Login first");
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("¿Do you want to buy this app?");
            alert.setContentText("Confirm that you want this app");
            alert.setTitle("Buy this app");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK)
            {
                Label label = (Label) ((Node) event.getSource()).getParent().getChildrenUnmodifiable().get(1);

                PurchasedDAO purchasedDAO = new PurchasedDAO(MySQL.getConnection());
                AppDAO appDAO = new AppDAO(MySQL.getConnection());
                UserDAO userDAO = new UserDAO(MySQL.getConnection());

                App app = new App();
                app.setId(Long.valueOf(label.getText()));

                try
                {
                    purchasedDAO.insert(appDAO.fetch(app), userDAO.findByEmail(MenuController.user.getEmail()));

                    alertMessage("Thanks for buying this app", "New app", Alert.AlertType.INFORMATION, "Congrats! you own this app");
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    protected void fillPurchased(List<App> apps, VBox vBox)
    {
        for (App app : apps)
        {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            imageView.setImage(new Image("store/controllers/resources/spotify.png"));

            Label label1 = new Label(app.getName());
            Label label2 = new Label(app.getPublisher());
            Label label3 = new Label("Downloads");
            Label label4 = new Label("Version");

            Rating rating = new Rating();
            rating.setOrientation(Orientation.HORIZONTAL);
            rating.setPartialRating(true);
            rating.setRating(app.getRating());
            rating.setUpdateOnHover(false);
            rating.setMax(5);
            rating.setRating(app.getRating());
            rating.setDisable(true);

            Label label5 = new Label(String.valueOf(app.getId()));

            Button button = new Button("Rate it");

            hBox.getChildren().addAll(imageView, label1, label2, label3, label4, rating, label5, button);

            vBox.getChildren().add(hBox);
        }
    }

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
