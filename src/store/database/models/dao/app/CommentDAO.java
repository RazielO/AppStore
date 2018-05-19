package store.database.models.dao.app;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import store.database.models.app.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO
{
    Connection connection;

    public CommentDAO(Connection connection)
    {
        this.connection = connection;
    }

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

    public List<Comment> findAll()
    {
        List<Comment> comments = new ArrayList<>();

        String query = "SELECT c.*, u.username username" +
                       "  FROM comment c INNER JOIN user u ON c.idUser = u.idUser";

        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next())
            {
                Comment e = new Comment();
                e.setComment(resultSet.getString("comment"));
                e.setDate(resultSet.getDate("commentDate"));
                e.setIdApp(resultSet.getLong("idApp"));
                e.setIdUser(resultSet.getLong("idUser"));
                e.setRating(resultSet.getDouble("rating"));
                e.setUsername(resultSet.getString("username"));

                comments.add(e);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return comments;
    }

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
