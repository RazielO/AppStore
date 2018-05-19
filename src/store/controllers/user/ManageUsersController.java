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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    private void init()
    {
        List<User> users = userDAO.findAll();

        for (User user : users)
        {
            if (user.getId() != MenuController.user.getId())
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

                Button btnDelte = new Button("Delete user");
                btnDelte.setFont(elseFont);
                btnDelte.setOnAction(delete);
                btnDelte.setPrefWidth(150);

                vBoxTemp2.getChildren().addAll(btnAdmin, btnDelte);

                hBox.getChildren().addAll(imageView, vBoxTemp, lblEmail, vBoxTemp2);
                v.getChildren().add(hBox);
                vBox.getChildren().add(v);
            }
        }
    }

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

            if(confirmation.get() == ButtonType.OK)
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
}
