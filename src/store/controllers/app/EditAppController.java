package store.controllers.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.database.models.app.App;
import store.database.models.app.Language;
import store.database.models.category.Category;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.app.AppLanguageDAO;
import store.database.models.dao.app.LanguageDAO;
import store.database.models.dao.category.CategoryDAO;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditAppController extends Controller implements Initializable
{
    @FXML
    ImageView image;
    @FXML
    TextField txtName, txtPublisher, txtPrice, txtSize, txtVersion, txtCompatibility, txtFeature1, txtFeature2,
              txtFeature3;
    @FXML
    ComboBox<String> cmbCategory, cmbLanguages;
    @FXML
    TextArea txtDescription;
    @FXML
    Button btnAddLanguage, btnCancel, btnSave, btnRemoveLanguage, btnLogo;
    @FXML
    Label lblLanguages;

    public static App app;

    private Image newLogo;
    private File fileLogo;
    private AppDAO appDAO = new AppDAO(MySQL.getConnection());
    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());
    private LanguageDAO languageDAO = new LanguageDAO(MySQL.getConnection());
    private AppLanguageDAO appLanguageDAO = new AppLanguageDAO(MySQL.getConnection());

    /*
    TODO: Add and delete screenshots, if possible
     */

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initValues();
    }

    private void initCategory()
    {
        for (Category category : categoryDAO.fetchAll())
            cmbCategory.getItems().add(category.getName());

        cmbCategory.getSelectionModel().select(app.getCategory());
    }

    private void initValues()
    {
        initCategory();
        initLanguages();

        image.setImage(app.getLogo());
        txtCompatibility.setText(app.getCompatibility());
        txtDescription.setText(app.getDescription());
        txtName.setText(app.getName());
        txtPrice.setText(String.valueOf(app.getPrice()));
        txtSize.setText(app.getSize());
        txtVersion.setText(app.getVersion());
        txtPublisher.setText(app.getPublisher());

        String[] fs = app.getFeatures().split("\n");

        txtFeature1.setText(fs[0]);
        txtFeature2.setText(fs[1]);
        txtFeature3.setText(fs[2]);

        btnAddLanguage.setOnAction(handler);
        btnRemoveLanguage.setOnAction(handler);
        btnCancel.setOnAction(handler);
        btnSave.setOnAction(handler);
    }

    private void initLanguages()
    {
        for (Language language : languageDAO.fetchAll())
            cmbLanguages.getItems().add(language.getName());

        String s = "";
        for (Language language : app.getLanguages())
            s = s + language.getName() + "\n";

        lblLanguages.setText(s);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnCancel)
        {
            AppController.appName = app.getName();
            changeScene("store/fxml/home/app.fxml");
        }
        else if (event.getSource() == btnAddLanguage)
        {
            String temp = cmbLanguages.getSelectionModel().getSelectedItem();
            Language language = languageDAO.search(temp);

            if (language != null)
            {
                List<Language> list = app.getLanguages();
                list.add(language);
                app.setLanguages(list);

                appLanguageDAO.insert(app, language);

                lblLanguages.setText(lblLanguages.getText() + language.getName() + "\n");
            }
        }
        else if (event.getSource() == btnRemoveLanguage)
        {
            String temp = cmbLanguages.getSelectionModel().getSelectedItem();
            Language language = languageDAO.search(temp);
            List<Language> list = app.getLanguages();
            boolean flag = false;

            if (language != null)
                for (Language l : app.getLanguages())
                    if (l.getId().equals(language.getId()))
                        flag = true;

            if (flag && list.size() != 0)
            {
                list.remove(language);
                app.setLanguages(list);

                appLanguageDAO.delete(app, language);

                String aux = "";
                for (String s : lblLanguages.getText().split("\n"))
                    if (!s.equals(temp))
                        aux = aux + s + "\n";

                lblLanguages.setText(aux);
            }
        }
        else if (event.getSource() == btnLogo)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileLogo = fileChooser.showOpenDialog(new Stage());
            if (fileLogo != null)
            {
                newLogo = new Image(fileLogo.toURI().toString());
                image.setImage(newLogo);
            }
        }
        else if (event.getSource() == btnSave)
        {
            boolean result;

            if (txtFeature3.getText().isEmpty() || txtFeature2.getText().isEmpty() || txtFeature1.getText().isEmpty() ||
                txtPublisher.getText().isEmpty() || txtVersion.getText().isEmpty() || txtSize.getText().isEmpty() ||
                txtPrice.getText().isEmpty() || txtName.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                txtCompatibility.getText().isEmpty() || lblLanguages.getText().isEmpty() ||
                cmbCategory.getSelectionModel().getSelectedItem().isEmpty())
                alertMessage("Do not leave any empty field", "Error", Alert.AlertType.ERROR, "Empty fields");
            else
            {
                app.setName(txtName.getText());
                app.setPrice(Double.valueOf(txtPrice.getText()));
                app.setPublisher(txtPublisher.getText());
                app.setVersion(txtVersion.getText());
                app.setCategory(cmbCategory.getSelectionModel().getSelectedItem());
                app.setCompatibility(txtCompatibility.getText());
                app.setDescription(txtDescription.getText());
                app.setSize(txtSize.getText());
                app.setFeatures(txtFeature1.getText() + "\n" + txtFeature2.getText() + "\n" + txtFeature3.getText());

                if (fileLogo != null)
                {
                    app.setLogo(newLogo);
                    app.setLogoFile(fileLogo);
                    result = appDAO.update(app);
                }
                else
                    result = appDAO.updateWithoutImage(app);

                if (result)
                {
                    alertMessage("This app was updated correctly", "Cool", Alert.AlertType.INFORMATION, "App updated");
                    AppController.appName = app.getName();
                    changeScene("store/fxml/app/app.fxml");
                }
            }
        }
    };
}
