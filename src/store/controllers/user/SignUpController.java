package store.controllers.user;

import store.controllers.Controller;
import store.database.models.user.User;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.UserDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class SignUpController extends Controller implements Initializable
{
    @FXML
    TextField txtUsername, txtName, txtLastName, txtAddress, txtPhone, txtEmail;
    @FXML
    DatePicker dpDate;
    @FXML
    Button btnCard, btnPicture, btnAccept, btnCancel;
    @FXML
    PasswordField txtPassword;

    private Image profile = null;
    private File file = null;
    private AddCardController controller = null;
    private User user = null;
    private UserDAO userDAO = new UserDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnAccept.setOnAction(handler);
        btnCancel.setOnAction(handler);
        btnCard.setOnAction(handler);
        btnPicture.setOnAction(handler);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnAccept)
        {
            if (txtAddress.getText().trim().length() == 0 || txtPhone.getText().trim().length() == 0 ||
                txtEmail.getText().trim().length() == 0 || txtLastName.getText().trim().length() == 0 ||
                txtName.getText().trim().length() == 0 || txtUsername.getText().trim().length() == 0 ||
                dpDate.getValue() == null)
                alertMessage("You cannot leave any empty field", "Error", Alert.AlertType.ERROR, "Empty fields");
            else if (profile == null)
                alertMessage("Select a profile picture", "Error", Alert.AlertType.ERROR, "Please, select a profile picture");
            else
            {
                user = new User();
                if (AddCardController.card != null && user != null)
                {
                    user.setPassword(txtPassword.getText());
                    user.setPhone(txtPhone.getText().replaceAll("\\s+",""));
                    user.setPicture(profile);
                    user.setEmail(txtEmail.getText());
                    user.setCardNumber(AddCardController.card.getNumber());
                    user.setAdmin(false);
                    user.setBirthday(Date.valueOf(dpDate.getValue()));
                    user.setName(txtName.getText());
                    user.setLastName(txtLastName.getText());
                    user.setAddress(txtAddress.getText());
                    user.setImageFile(file);
                    user.setUsername(txtUsername.getText());

                    if (userDAO.insert(user))
                        alertMessage("You now have an account", "New user", Alert.AlertType.INFORMATION, "Congrats");
                    else
                        alertMessage("Error on the process", "Error", Alert.AlertType.ERROR, "Error");

                    closeStage();
                }
                else
                    alertMessage("You have to add a credit card", "Error", Alert.AlertType.ERROR, "Add a credit card");
            }
        }
        else if (event.getSource() == btnCancel)
            closeStage();
        else if (event.getSource() == btnPicture)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            configureFileChooser(fileChooser);
            file = fileChooser.showOpenDialog(new Stage());
            if (file != null)
                profile = new Image(file.toURI().toString());
        }
        else if (event.getSource() == btnCard)
        {
            controller = new FXMLLoader(getClass().getClassLoader().getResource("store/fxml/user/addCard.fxml")).getController();
            openNewWindow("Add Card", "store/fxml/user/addCard.fxml", 250, 500, controller);
        }
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
