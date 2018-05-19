package store.controllers.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import store.controllers.Controller;
import store.controllers.category.ManageCategoryController;
import store.controllers.user.LoginController;
import store.database.models.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends Controller implements Initializable
{
    @FXML
    Button btnCategories, btnTopApps, btnFeatured, btnPurchased, btnUser, btnSearch, btnHome;
    @FXML
    TextField txtSearchBar;
    @FXML
    MenuButton btnMenu;

    private MenuItem itmLogout, itmAddApp, itmUsers, itmCategories;

    public static User user = new User();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    public void init()
    {
        btnUser.setOnAction(buttonHandler);
        btnHome.setOnAction(event -> changeScene("store/fxml/home/home.fxml"));
        btnCategories.setOnAction(event -> changeScene("store/fxml/category/categories.fxml"));
        btnTopApps.setOnAction(buttonHandler);
        btnPurchased.setOnAction(buttonHandler);
        btnMenu.setOnAction(buttonHandler);
        btnSearch.setOnAction(buttonHandler);

        itmLogout = new MenuItem("Logout");
        itmAddApp = new MenuItem("Add app");
        itmUsers = new MenuItem("Manage users");
        itmCategories = new MenuItem("Manage categories");

        itmAddApp.setOnAction(itemHandler);
        itmLogout.setOnAction(itemHandler);
        itmUsers.setOnAction(itemHandler);
        itmCategories.setOnAction(itemHandler);

        if (user.getId() != null)
        {
            btnMenu.setVisible(true);
            if (user.getAdmin())
                btnMenu.getItems().addAll(itmAddApp, itmCategories, itmUsers, itmLogout);
            else
                btnMenu.getItems().add(itmLogout);

            ImageView imageView = new ImageView(user.getPicture());
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);

            btnUser.setGraphic(imageView);
            btnUser.setText("");
        }
        else
            btnMenu.setVisible(false);
    }

    private EventHandler<ActionEvent> buttonHandler = event ->
    {
        if (event.getSource() == btnUser)
        {
            if (user.getId() == null)
            {
                LoginController controller = new LoginController();
                openNewWindowShowAndWait("Login", "store/fxml/user/login.fxml", 200, 500, controller);

                init();
            }
            else
                changeScene("store/fxml/user/user.fxml");
        }
        else if (event.getSource() == btnTopApps)
        {
            System.out.println("top apps");
        }
        else if (event.getSource() == btnPurchased)
            if (user.getId() == null)
                alertMessage("You have to login first to see your purchased apps", "Error", Alert.AlertType.ERROR, "Error");
            else
                changeScene("store/fxml/menu/purchased.fxml");
        else if (event.getSource() == btnMenu)
            alertMessage("btnMenu", "btnMenu", Alert.AlertType.INFORMATION, "btnMenu");
        else if (event.getSource() == btnSearch)
        {
            SearchedController.setSearch(txtSearchBar.getText());
            changeScene("store/fxml/menu/searched.fxml");
        }
    };

    private EventHandler<ActionEvent> itemHandler = event ->
    {
        if (event.getSource() == itmLogout)
        {
            if (user.getId() != null)
            {
                user = new User();
                btnUser.setText("User");
                btnUser.setGraphic(null);
                changeScene("store/fxml/home/home.fxml");
                alertMessage("You are now logged out", "Logout", Alert.AlertType.INFORMATION, "Successful logout");
            }
            else
                alertMessage("You have to login first to logout", "Error", Alert.AlertType.ERROR, "Error");
        }
        else
        {
            if (user.getId() != null && user.getAdmin())
            {
                if (event.getSource() == itmAddApp)
                    changeScene("store/fxml/app/addApp.fxml");
                else if (event.getSource() == itmCategories)
                {
                    ManageCategoryController controller = new ManageCategoryController();
                    openNewWindow("Manage categories", "store/fxml/category/manageCategory.fxml", 200, 450, controller);
                }
            }
            else if (user.getId() != null && !user.getAdmin())
                alertMessage("You have to be an admin to perform this action", "Error", Alert.AlertType.ERROR, "You cannot access this");
            else if (user.getId() == null)
                alertMessage("You have to be logged in and be an admin to perform this action", "Error", Alert.AlertType.ERROR, "Login first");
        }
    };
}
