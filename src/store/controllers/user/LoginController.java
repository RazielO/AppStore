package store.controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.controllers.menu.MenuController;
import store.database.models.dao.MySQL;
import store.database.models.dao.user.UserDAO;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController extends Controller implements Initializable
{
    @FXML
    Button btnLogin, btnSignUp;
    @FXML
    TextField txtUser;
    @FXML
    PasswordField txtPassword;

    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        userDAO = new UserDAO(MySQL.getConnection());
        btnSignUp.setOnAction(handler);
        btnLogin.setOnAction(handler);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnLogin)
        {
            if (txtUser.getText().isEmpty() || txtPassword.getText().isEmpty())
                alertMessage("Fill all the fields", "Error", Alert.AlertType.ERROR, "Empty fields");
            else
            {
                String user = txtUser.getText();

                Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+");
                Matcher matcher = pattern.matcher(user);
                userDAO = new UserDAO(MySQL.getConnection());

                try
                {
                    if (matcher.matches())
                        MenuController.user = userDAO.findByEmail(user);
                    else
                        MenuController.user = userDAO.findByUsername(user);

                    if (!MenuController.user.getPassword().equals(txtPassword.getText()))
                    {
                        alertMessage("Wrong password", "Wrong password", Alert.AlertType.ERROR, "Wrong password");
                        MenuController.user = null;
                    }
                    else
                    {
                        alertMessage("You are now logged in", "Login", Alert.AlertType.INFORMATION, "Successful login");
                        closeStage();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    alertMessage(e.getMessage(), "Invalid user", Alert.AlertType.ERROR, "Invalid user");
                }
            }
        }
        else if (event.getSource() == btnSignUp)
        {
            openNewWindow("Sign Up", "store/fxml/user/signUp.fxml", 600, 500, false);
            closeStage();
        }
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }
}
