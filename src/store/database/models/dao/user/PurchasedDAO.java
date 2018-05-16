package store.database.models.dao.user;

import javafx.scene.image.Image;
import store.database.models.app.App;
import store.database.models.user.User;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PurchasedDAO
{
    private Connection connection;

    public PurchasedDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Boolean insert(App app, User user)
    {
        String query = "INSERT INTO purchases" +
                       "    (idApp, idUser, date)" +
                       "    VALUES (?, ?, ?)";
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Calendar cal = Calendar.getInstance();

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, app.getId());
            statement.setLong(2, user.getId());
            statement.setDate(3, Date.valueOf(dateFormat.format(cal)));

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public List<App> findAllByUser(User user)
    {
        List<App> apps = new ArrayList<>();

        String query = "SELECT app.*, publisher.name publisher\n" +
                       "  FROM purchases INNER JOIN app ON purchases.idApp = app.idApp\n" +
                       "                 INNER JOIN publishes ON app.idApp = publishes.idApp\n" +
                       "                 INNER JOIN publisher ON publishes.idPublisher = publisher.idPublisher\n" +
                       "  WHERE purchases.idUser = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, user.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Image image = new Image(resultSet.getBlob("logo").getBinaryStream());

                App app = new App();

                app.setId(resultSet.getLong("idApp"));
                app.setLogo(image);
                app.setPublisher(resultSet.getString("publisher"));
                app.setRating(resultSet.getDouble("rating"));
                app.setDownloads(resultSet.getLong("downloads"));
                app.setVersion(resultSet.getString("version"));
                app.setName(resultSet.getString("name"));

                apps.add(app);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }
}
