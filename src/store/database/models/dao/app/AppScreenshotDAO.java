package store.database.models.dao.app;

import store.database.models.app.App;
import store.database.models.app.Screenshot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppScreenshotDAO
{
    private Connection connection;

    public AppScreenshotDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Boolean insert(App app, Screenshot screenshot)
    {
        String query;

        query = "INSERT INTO screenshot" +
                "   (image)" +
                " VALUES (?)";

        try
        {
            PreparedStatement statement =  connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(screenshot.getFile());

            statement.setBinaryStream(1, stream);

            statement.execute();
        }
        catch (SQLException | FileNotFoundException e)
        {
            e.printStackTrace();
        }

        query = "INSERT INTO appscreenshot" +
                "    (idApp, idScreenshot)" +
                "     VALUES (?, (SELECT idScreenshot" +
                "                 FROM screenshot" +
                "                 WHERE idScreenshot IN (SELECT MAX(idScreenshot)" +
                "                                        FROM screenshot)))";

        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, app.getId());

            statement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
