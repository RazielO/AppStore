package store.controllers.category;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.database.models.category.Category;
import store.database.models.dao.MySQL;
import store.database.models.dao.category.CategoryDAO;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoryController extends Controller implements Initializable
{
    @FXML
    Button btnAdd, btnLogo, btnCancel;
    @FXML
    TextField txtName;

    private File file;
    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());
    private Image logo;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
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
        btnCancel.setOnAction(handler);
        btnAdd.setOnAction(handler);
        btnLogo.setOnAction(handler);
    }

    /**
     * Called when a button is pressed.
     * It cancels, adds the logo and adds the category.
     */
    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnCancel)
            closeStage();
        else if (event.getSource() == btnLogo)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            configureFileChooser(fileChooser);
            file = fileChooser.showOpenDialog(new Stage());
            if (file != null)
                logo = new Image(file.toURI().toString());
        }
        else if (event.getSource() == btnAdd)
        {
            if (file == null)
                alertMessage("Select a logo first", "Error", Alert.AlertType.ERROR, "Error");
            else if (txtName.getText().isEmpty())
                alertMessage("Please, fill all the fields", "Error", Alert.AlertType.ERROR, "Empty fields");
            else
            {
                Category category = new Category();
                category.setLogo(logo);
                category.setName(txtName.getText());
                category.setFile(file);

                categoryDAO.insert(category);

                closeStage();
            }
        }
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
