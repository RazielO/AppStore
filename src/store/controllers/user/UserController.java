package store.controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.controllers.menu.MenuController;
import store.database.models.user.User;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.UserDAO;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController extends Controller implements Initializable
{
    @FXML
    TextField txtName, txtLastName, txtPhone, txtAddress, txtEmail;
    @FXML
    Button btnAccept, btnCancel, btnEdit, btnChangeImage;
    @FXML
    ImageView imgProfilePhoto;
    @FXML
    Label lblUsername;
    @FXML
    DatePicker dpBirthday;
    @FXML
    PasswordField txtPassword;
    @FXML
    HBox hBox;

    File file;
    Image profile;
    UserDAO userDAO = new UserDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();

        btnAccept.setOnAction(handler);
        btnChangeImage.setOnAction(handler);
        btnCancel.setOnAction(handler);
        btnEdit.setOnAction(handler);
    }

    private void init()
    {
        lblUsername.setText(MenuController.user.getUsername());
        txtAddress.setText(MenuController.user.getAddress());
        txtLastName.setText(MenuController.user.getLastName());
        txtEmail.setText(MenuController.user.getEmail());
        txtName.setText(MenuController.user.getName());
        txtPhone.setText(MenuController.user.getPhone());
        dpBirthday.setValue(MenuController.user.getBirthday().toLocalDate());
        imgProfilePhoto.setImage(MenuController.user.getPicture());
        txtPassword.setText(MenuController.user.getPassword());
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnEdit)
        {
            btnEdit.setVisible(false);
            btnCancel.setVisible(true);
            btnAccept.setVisible(true);
            btnChangeImage.setVisible(true);

            txtName.setEditable(true);
            txtAddress.setEditable(true);
            txtEmail.setEditable(true);
            txtLastName.setEditable(true);
            txtPhone.setEditable(true);
            dpBirthday.setEditable(true);
            txtPassword.setEditable(true);
            hBox.setVisible(true);
        }
        else if (event.getSource() == btnAccept)
        {
            User user = new User();
            User user1 = null;
            try
            {
                user1 = userDAO.findByUsername(lblUsername.getText());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try
            {
                user.setId(Objects.requireNonNull(user1).getId());
                user.setAddress(txtAddress.getText());
                user.setLastName(txtLastName.getText());
                user.setName(txtName.getText());
                user.setAdmin(user1.getAdmin());
                user.setBirthday(Date.valueOf(dpBirthday.getValue()));
                user.setCardNumber(user1.getCardNumber());
                user.setEmail(txtEmail.getText());
                user.setPhone(txtPhone.getText());
                user.setPassword(txtPassword.getText());
                user.setPicture(imgProfilePhoto.getImage());
                user.setUsername(lblUsername.getText());

                userDAO.updateWithoutPicture(user);
                MenuController.user = user;

                btnEdit.setVisible(true);
                btnCancel.setVisible(false);
                btnAccept.setVisible(false);
                btnChangeImage.setVisible(false);

                txtName.setEditable(false);
                txtAddress.setEditable(false);
                txtEmail.setEditable(false);
                txtLastName.setEditable(false);
                txtPhone.setEditable(false);
                dpBirthday.setEditable(false);
                hBox.setVisible(false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                alertMessage("Do not leave empty fields", "Error", Alert.AlertType.ERROR, "Empty fields");
            }
        }
        else if (event.getSource() == btnCancel)
        {
            btnEdit.setVisible(true);
            btnCancel.setVisible(false);
            btnAccept.setVisible(false);
            btnChangeImage.setVisible(false);

            txtName.setEditable(false);
            txtAddress.setEditable(false);
            txtEmail.setEditable(false);
            txtLastName.setEditable(false);
            txtPhone.setEditable(false);
            dpBirthday.setEditable(false);
            hBox.setVisible(false);
        }
        else if (event.getSource() == btnChangeImage)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            file = fileChooser.showOpenDialog(new Stage());
            if (file != null)
            {
                profile = new Image(file.toURI().toString());
                imgProfilePhoto.setImage(profile);
                try
                {
                    User usr = userDAO.findByUsername(lblUsername.getText());
                    usr.setImageFile(file);
                    usr.setPicture(profile);
                    userDAO.updateWithPicture(usr);
                    MenuController.user = usr;
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        init();
    };
}
