package store.database.models.dao.app;

import store.database.models.app.App;
import store.database.models.app.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppLanguageDAO
{
    private Connection connection;

    /**
     * Constructor receives a connection to the database
     *
     * @param connection connection to a MySQL database
     */
    public AppLanguageDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Inserts a language to the app
     *
     * @param app App to add the language
     * @param language Language to add to the app
     *
     * @return Boolean Returns whether or not the operation was completed
     */
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


    /**
     * Deletes a language from the app
     *
     * @param app App to delete the language
     * @param language Language to delete to the app
     *
     * @return boolean Returns whether or not the operation was completed
     */
    public boolean delete(App app, Language language)
    {
        String query = "DELETE FROM appLanguage" +
                       "    WHERE idApp = ? AND idLanguage = ?";

        try
        {
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
        return false;
    }
}
