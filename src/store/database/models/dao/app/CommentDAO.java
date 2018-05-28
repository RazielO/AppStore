package store.database.models.dao.app;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import store.database.models.app.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO
{
    Connection connection;

    /**
     * Constructor receives a connection to the database
     *
     * @param connection connection to a MySQL database
     */
    public CommentDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Inserts the comment into the database
     *
     * @param comment Comment to insert into the database
     *
     * @return Boolean Returns whether or not the app was updated
     */
    public boolean insert(Comment comment)
    {
        String query = "INSERT INTO comment" +
                       " (idApp, idUser, comment, rating, commentDate)" +
                       "    VALUES (?, ?, ?, ?, ?)";

        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, comment.getIdApp());
            statement.setLong(2, comment.getIdUser());
            statement.setString(3, comment.getComment());
            statement.setDouble(4, comment.getRating());
            statement.setDate(5, comment.getDate());

            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns if a user has already commented the given app
     *
     * @param idApp id of the app to search
     * @param idUser id of the user to search
     *
     * @return boolean Returns if the user has commented the app
     */
    public boolean exists(Long idApp, Long idUser)
    {
        String query = "SELECT *" +
                       "  FROM comment" +
                       "  WHERE idApp = ? AND idUser = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, idApp);
            statement.setLong(2, idUser);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
