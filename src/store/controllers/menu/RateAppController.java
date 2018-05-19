package store.controllers.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import store.controllers.Controller;
import store.database.models.app.Comment;
import store.database.models.dao.MySQL;
import store.database.models.dao.app.CommentDAO;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

public class RateAppController extends Controller implements Initializable
{
    @FXML
    Label lblName;
    @FXML
    Rating rating;
    @FXML
    Button btnAccept, btnCancel;
    @FXML
    TextArea txtComment;

    private CommentDAO commentDAO = new CommentDAO(MySQL.getConnection());

    public static String appName;
    public static Long idApp;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lblName.setText(appName);

        btnCancel.setOnAction(event -> closeStage());
        btnAccept.setOnAction(handler);
    }

    EventHandler<ActionEvent> handler = event ->
    {
        if (!txtComment.getText().isEmpty())
        {
            Comment comment = new Comment(txtComment.getText(), MenuController.user.getUsername(), rating.getRating(),
                    new Date(Calendar.getInstance().getTime().getTime()));
            comment.setIdApp(idApp);
            comment.setIdUser(MenuController.user.getId());


            commentDAO.insert(comment);

            closeStage();
        }
        else
            alertMessage("You cannot comment null", "Error", Alert.AlertType.ERROR, "Empty comment field");
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
