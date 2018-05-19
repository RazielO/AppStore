package store.controllers.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.app.Comment;
import store.database.models.app.Language;
import store.database.models.app.Screenshot;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable
{
    @FXML
    ImageView image;
    @FXML
    Label lblName, lblPublisher, lblCategory, lblDownloads, lblPrice, lblSize, lblVersion, lblCompatibility,
            lblLanguages, lblFeatures;
    @FXML
    Button btnBuy;
    @FXML
    TextArea txtDescription;
    @FXML
    HBox hboxScreenshot;
    @FXML
    VBox vboxComments;


    public static String appName;

    private AppDAO appDAO = new AppDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    private void init()
    {
        App app = new App();
        app.setName(AppController.appName);
        app = appDAO.fetch(app);

        if (app != null)
        {
            image.setImage(app.getLogo());

            lblName.setText(app.getName());
            lblCategory.setText(app.getCategory());
            lblCompatibility.setText(app.getCompatibility());
            lblDownloads.setText(String.valueOf(app.getDownloads()));
            lblFeatures.setText(app.getFeatures());
            lblPrice.setText("$" + app.getPrice());
            lblPublisher.setText(app.getPublisher());
            lblSize.setText(app.getSize());
            lblVersion.setText(app.getVersion());
            txtDescription.setText(app.getDescription());

            String text = "";
            for (Language language : app.getLanguages())
                text = text + language.getName() + "\n";

            lblLanguages.setText(text);

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
            }
        }
    }
}
