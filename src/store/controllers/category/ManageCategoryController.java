package store.controllers.category;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.database.models.category.Category;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.AppDAO;
import store.database.models.dao.category.CategoryDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageCategoryController extends Controller implements Initializable
{
    @FXML
    ComboBox<String> cmbCategories;
    @FXML
    Button btnAdd, btnDelete;

    private CategoryDAO categoryDAO = new CategoryDAO(MySQL.getConnection());
    private AppDAO appDAO = new AppDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    private void init()
    {
        for (Category category : categoryDAO.fetchAll())
            cmbCategories.getItems().add(category.getName());

        btnAdd.setOnAction(handler);
        btnDelete.setOnAction(handler);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnAdd)
        {
            AddCategoryController controller = new AddCategoryController();
            openNewWindowShowAndWait("Add category", "store/fxml/category/addCategory.fxml", 200, 450, controller);

            init();
        }
        else if (event.getSource() == btnDelete)
        {
            Category category = categoryDAO.fetch(cmbCategories.getSelectionModel().getSelectedItem());
            if (appDAO.findByPublisher(Long.valueOf(category.getId())).size() == 0)
                alertMessage("This category has apps in it", "Error", Alert.AlertType.ERROR, "You cannot delete this category");
            else
            {
                Boolean result = categoryDAO.delete(category.getId());
                if (result)
                    closeStage();
                else
                    alertMessage("We couldn't delete that category", "Error", Alert.AlertType.ERROR, "Error");
            }
        }
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnAdd.getScene().getWindow();
        stage.close();
    }
}
