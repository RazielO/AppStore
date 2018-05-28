package store.database.models.dao.app;

import store.database.models.app.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LanguageDAO
{
    private Connection connection;

    /**
     * Constructor receives a connection to the database
     *
     * @param connection connection to a MySQL database
     */
    public LanguageDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Selects all the languages in the database
     *
     * @return List Returns all the languages in the database
     */
    public List<Language> fetchAll()
    {
        String query = "SELECT *" +
                       "    FROM language" +
                       "    ORDER BY name";

        List<Language> list = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                list.add(new Language(resultSet.getString("idLanguage"), resultSet.getString("name")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Searches a language
     *
     * @param name Name of the language to search
     *
     * @return Language Returns the language with the given name
     */
    public Language search(String name)
    {
        String query = "SELECT *" +
                       "    FROM language" +
                       "    WHERE name = ?";
        Language language = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                language = new Language(resultSet.getString("idLanguage"), resultSet.getString("name"));

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return language;
    }
}
