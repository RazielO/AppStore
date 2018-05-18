package store.controllers.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.category.Category;
import store.database.models.app.Language;
import store.database.models.app.Screenshot;
import store.database.models.dao.*;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.app.AppLanguageDAO;
import store.database.models.dao.app.AppScreenshotDAO;
import store.database.models.dao.app.LanguageDAO;
import store.database.models.dao.category.CategoryDAO;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddAppController extends Controller implements Initializable
{
    @FXML
    TextField txtName, txtCompatibility, txtFeature1, txtFeature2, txtFeature3, txtVersion, txtSize,
            txtPrice, txtRating, txtDownloads, txtPublisher;
    @FXML
    TextArea txtDescription;
    @FXML
    ComboBox cmbCategory, cmbLanguages;
    @FXML
    Button btnPublish, btnCancel, btnLogo, btnScreenshots, btnLanguage;
    @FXML
    Label lblLanguages;

    private Image logo;
    private File file;
    private AppDAO appDAO = new AppDAO(MySQL.getConnection());
    private FileChooser fileChooser = new FileChooser();
    private List<File> list;
    private List<Screenshot> screenshots;
    private List<Language> appLanguages;
    private AppScreenshotDAO appScreenshotDAO = new AppScreenshotDAO(MySQL.getConnection());
    private AppLanguageDAO appLanguageDAO = new AppLanguageDAO(MySQL.getConnection());
    private LanguageDAO languageDAO = new LanguageDAO(MySQL.getConnection());
    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        appLanguages = new ArrayList<>();
        screenshots = new ArrayList<>();

        for (Language language : languageDAO.fetchAll())
            cmbLanguages.getItems().add(language.getName());

        for (Category category : categoryDAO.fetchAll())
            cmbCategory.getItems().add(category.getName());

        btnCancel.setOnAction(handler);
        btnPublish.setOnAction(handler);
        btnScreenshots.setOnAction(handler);
        btnLogo.setOnAction(handler);
        btnLanguage.setOnAction(handler);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnScreenshots)
        {
            fileChooser.setTitle("Select all the screenshots");
            screenshots = new ArrayList<>();
            configureFileChooser(fileChooser);
            list = fileChooser.showOpenMultipleDialog(new Stage());
            if (list != null)
                for (File file : list)
                {
                    Image image = new Image(file.toURI().toString());
                    Screenshot e = new Screenshot();
                    e.setScreenshot(image);
                    e.setFile(file);
                    screenshots.add(e);
                }
        }
        else if (event.getSource() == btnLogo)
        {
            fileChooser.setTitle("Open Resource File");
            configureFileChooser(fileChooser);
            file = fileChooser.showOpenDialog(new Stage());
            if (file != null)
                logo = new Image(file.toURI().toString());
        }
        else if (event.getSource() == btnPublish)
        {
            App app;

            if (file == null)
                alertMessage("Select a logo for this app", "Error", Alert.AlertType.ERROR, "Logo missing");
            else if (list.size() == 0)
                alertMessage("Select at least one screenshot", "Error", Alert.AlertType.ERROR, "Missing screenshot");
            else
            {
                if (txtFeature3.getText().isEmpty() || txtFeature2.getText().isEmpty() || txtFeature1.getText().isEmpty() ||
                    txtName.getText().isEmpty() || txtCompatibility.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                    txtSize.getText().isEmpty() || txtVersion.getText().isEmpty() || txtPublisher.getText().isEmpty() ||
                    txtPrice.getText().isEmpty() || lblLanguages.getText().isEmpty() || cmbCategory.getSelectionModel().isEmpty())
                    alertMessage("Please, fill all the fields", "Error", Alert.AlertType.ERROR, "Empty fields");
                else
                {
                    String features = txtFeature1.getText() + "\n" + txtFeature2.getText() + "\n" + txtFeature3.getText();

                    app = new App();

                    app.setVersion(txtVersion.getText());
                    app.setName(txtName.getText());
                    app.setDownloads(Long.parseLong(txtDownloads.getText()));
                    app.setPrice(Double.parseDouble(txtPrice.getText()));
                    app.setCategory(String.valueOf(cmbCategory.getSelectionModel().getSelectedItem()));
                    app.setCompatibility(txtCompatibility.getText());
                    app.setSize(txtSize.getText());
                    app.setDescription(txtDescription.getText());
                    app.setRating(Double.valueOf(txtRating.getText()));
                    app.setPublisher(txtPublisher.getText());
                    app.setLogoFile(file);
                    app.setScreenshotsFiles(list);
                    app.setLogo(logo);
                    app.setFeatures(features);

                    if (appDAO.insert(app))
                    {

                        app = appDAO.fetch(app);

                        System.out.println(app);

                        for (Screenshot screenshot : screenshots)
                        {
                            appScreenshotDAO.insert(app, screenshot);
                        }

                        for (Language language : appLanguages)
                        {
                            appLanguageDAO.insert(app, language);
                        }

                        alertMessage("Thanks for publishing this app", "Publish", Alert.AlertType.INFORMATION, "Your app is published now");
                        changeScene("store/fxml/home/home.fxml");
                    }
                }
            }
        }
        else if (event.getSource() == btnCancel)
            changeScene("store/fxml/home/home.fxml");
        else if (event.getSource() == btnLanguage)
        {
            if (cmbCategory.getSelectionModel().getSelectedItem() != null)
            {
                lblLanguages.setText(lblLanguages.getText() + "\n" + cmbLanguages.getSelectionModel().getSelectedItem());
                appLanguages.add(languageDAO.search(String.valueOf(cmbLanguages.getSelectionModel().getSelectedItem())));
            }
        }
    };
}
