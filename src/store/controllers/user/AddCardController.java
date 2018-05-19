package store.controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import store.controllers.Controller;
import store.database.models.user.Card;
import store.database.models.dao.user.CardDAO;
import store.database.models.dao.MySQL;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCardController extends Controller implements Initializable
{
    @FXML
    TextField txtNumber, txtCvv, txtName, txtLastName;
    @FXML
    ComboBox cmbMonth, cmbYear;
    @FXML
    Button btnAccept, btnCancel;

    static Card card;
    private CardDAO cardDAO = new CardDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        fillComboBoxes();
        btnAccept.setOnAction(handler);
        btnCancel.setOnAction(handler);
    }

    private EventHandler<ActionEvent> handler = event ->
    {
        if (event.getSource() == btnAccept)
        {
            Pattern cvvPattern = Pattern.compile("([0-9]{3,4})");
            Pattern numberPattern = Pattern.compile("([0-9]{16,19})");
            Matcher cvvMatcher = cvvPattern.matcher(txtCvv.getText().replaceAll("\\s+",""));
            Matcher numberMatcher = numberPattern.matcher(txtNumber.getText().replaceAll("\\s+",""));


            if (txtLastName.getText().trim().length() == 0 || txtName.getText().length() == 0 ||
                txtCvv.getText().length() < 3 || txtNumber.getText().trim().length() < 19 ||
                !cvvMatcher.matches() || !numberMatcher.matches())
            {
                alertMessage("Incomplete or invalid fields", "Error", Alert.AlertType.ERROR, "Please check all the fields");
            }
            else
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, Integer.parseInt((String) cmbMonth.getSelectionModel().getSelectedItem()));
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.YEAR, Integer.parseInt((String) cmbYear.getSelectionModel().getSelectedItem()));

                Date date = new Date(calendar.getTimeInMillis());
                if (date.after(new Date(Calendar.getInstance().getTimeInMillis())))
                {
                    card = new Card(txtNumber.getText(), txtName.getText(), txtLastName.getText(),
                            Integer.parseInt(txtCvv.getText()), date);
                    try
                    {
                        cardDAO.insert(card);
                    }
                    catch (SQLException e)
                    {
                        alertMessage("Someone else already has that card number", "Error", Alert.AlertType.ERROR, "Card not valid");
                    }
                    closeStage();
                }
                else
                    alertMessage("Credit card already expired", "Error", Alert.AlertType.ERROR, "Expired card");
            }
        }
        else if (event.getSource() == btnCancel)
            closeStage();
    };

    private void closeStage()
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void fillComboBoxes()
    {
        Integer aux;
        for (aux = 2018; aux <= 2028; aux++)
            cmbYear.getItems().add(String.valueOf(aux));
        for (aux = 1; aux <= 12; aux++)
            cmbMonth.getItems().add(String.valueOf(aux));
    }
}
