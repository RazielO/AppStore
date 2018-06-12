package store.database.models.dao.user;

import javafx.scene.image.Image;
import store.database.models.app.App;
import store.database.models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PurchasedDAO
{
    private Connection connection;

    /**
     * Constructor receives a connection to the database
     *
     * @param connection connection to a MySQL database
     */
    public PurchasedDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Inserts a purchase into the database
     *
     * @param app  App bought
     * @param user User who bought the app
     *
     * @return Boolean Returns whether or not the operation was completed
     *
     * @throws SQLException The user has already bought the app
     */
    public Boolean insert(App app, User user) throws SQLException
    {
        String query = "INSERT INTO purchases" +
                       "    (idapp, iduser, date)" +
                       "    VALUES (?, ?, ?)";

        Calendar cal = Calendar.getInstance();
        java.util.Date date = cal.getTime();
        Date today = new Date(date.getTime());

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, app.getId());
        statement.setLong(2, user.getId());
        statement.setDate(3, today);

        statement.execute();

        return true;
    }

    /**
     * Finds the apps a user has bought
     *
     * @param user User to search apps
     *
     * @return List Returns the apps the given user has bought
     */
    public List<App> findAllByUser(User user)
    {
        List<App> apps = new ArrayList<>();

        String query = "SELECT app.*" +
                       "     FROM purchases INNER JOIN app ON purchases.idapp = app.idapp" +
                       "     WHERE purchases.iduser = ?";

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
