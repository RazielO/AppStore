package store.controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import store.controllers.Controller;
import store.controllers.menu.MenuController;
import store.controllers.menu.PurchasedController;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.UserDAO;
import store.database.models.user.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageUsersController extends Controller implements Initializable
{
    @FXML
    VBox vBox;

    private UserDAO userDAO = new UserDAO(MySQL.getConnection());

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     * Calls the init method
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
     * Fills the screen with all the users in the database
     */
    private void init()
    {
        List<User> users = userDAO.findAll();

        for (User user : users)
        {
            if (!user.getId().equals(MenuController.user.getId()))
            {
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);

                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER);

                Font usernameFont = new Font("Arial black", 18);
                Font elseFont = new Font("Arial", 14);

                ImageView imageView = new ImageView(user.getPicture());
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                VBox vBoxTemp = new VBox();
                vBoxTemp.setSpacing(15);
                Label lblUsername = new Label(user.getUsername());
                lblUsername.setFont(usernameFont);
                Label lblName = new Label(user.getName() + " " + user.getLastName());
                lblName.setFont(elseFont);

                vBoxTemp.getChildren().addAll(lblUsername, lblName);

                Label lblEmail = new Label(user.getEmail());
                lblEmail.setFont(elseFont);

                VBox vBoxTemp2 = new VBox();
                vBoxTemp2.setSpacing(15);

                Button btnAdmin = new Button();
                btnAdmin.setPrefWidth(150);
                btnAdmin.setFont(elseFont);
                btnAdmin.setOnAction(admin);

                if (user.getAdmin())
                    btnAdmin.setText("Make regular user");
                else
                    btnAdmin.setText("Make admin user");

                Button btnDelete = new Button("Delete user");
                btnDelete.setFont(elseFont);
                btnDelete.setOnAction(delete);
                btnDelete.setPrefWidth(150);

                Button btnApps = new Button("Apps owned");
                btnApps.setFont(elseFont);
                btnApps.setOnAction(owned);
                btnApps.setPrefWidth(150);

                vBoxTemp2.getChildren().addAll(btnAdmin, btnDelete);

                hBox.getChildren().addAll(imageView, vBoxTemp, lblEmail, vBoxTemp2, btnApps);
                v.getChildren().add(hBox);
                vBox.getChildren().add(v);
            }
        }
    }

    /**
     * Called when the delete button is pressed.
     * Asks for confirmation and deletes the user from the database.
     */
    private EventHandler<ActionEvent> delete = event ->
    {
        Label label = (Label) ((Node) event.getSource()).getParent().getParent().getChildrenUnmodifiable().get(2);
        try
        {
            User user = userDAO.findByEmail(label.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete user");
            alert.setHeaderText("Are you sure you want to delete this user?");
            alert.setContentText("Confirm that you want to delete this user");

            Optional<ButtonType> confirmation = alert.showAndWait();

            if(confirmation.isPresent() && confirmation.get() == ButtonType.OK)
            {
                userDAO.delete(user);
                changeScene("store/fxml/user/manageUsers.fxml");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    };

    /**
     * Called when the admin button is pressed.
     * Makes a user an admin or a regular user
     */
    private EventHandler<ActionEvent> admin = event ->
    {
        Label label = (Label) ((Node) event.getSource()).getParent().getParent().getChildrenUnmodifiable().get(2);

        try
        {
            User user = userDAO.findByEmail(label.getText());

            if (user.getAdmin())
            {
                user.setAdmin(false);
                userDAO.updateWithoutPicture(user);
            }
            else
            {
                user.setAdmin(true);
                userDAO.updateWithoutPicture(user);
            }
            changeScene("store/fxml/user/manageUsers.fxml");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    };

    /**
     * Called when the owned button is pressed
     * Changes the scene to the window with all the apps which that user has bought
     */
    private EventHandler<ActionEvent> owned = event ->
    {
        Label label = (Label) ((Node)event.getSource()).getParent().getChildrenUnmodifiable().get(2);
        try
        {
            PurchasedController.user = userDAO.findByEmail(label.getText());
            changeScene("store/fxml/menu/purchased.fxml");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    };
}
