package store.database.models.dao.app;

import store.database.models.app.App;
import store.database.models.app.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppLanguageDAO
{
    private Connection connection;

    public AppLanguageDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Boolean insert(App app, Language language)
    {
        try
        {
            String query = "INSERT INTO appLanguage" +
                           "    (idApp, idLanguage)" +
                           " VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, app.getId());
            statement.setString(2, language.getId());

            statement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return true;
    }
}
