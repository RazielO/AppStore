package store.database.models.dao.app;

import store.database.models.app.Publisher;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO
{
    private Connection connection;

    public PublisherDAO(Connection connection)
    {
        this.connection = connection;
    }

    public List<Publisher> findAll()
    {
        List<Publisher> categories = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM publisher";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Publisher category;

            while (resultSet.next())
            {
                Image image = new Image(resultSet.getBlob("logo").getBinaryStream());

                category = new Publisher(
                                         resultSet.getLong("idPublisher"),
                                         resultSet.getString("name"),
                                         resultSet.getString("description"),
                                         image
                                        );

                categories.add(category);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return categories;
    }

    public Boolean delete(Long id)
    {
        try
        {
            String query = "DELETE FROM publisher" +
                           "    WHERE idPublihser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.execute();
            return Boolean.TRUE;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean update(Publisher publisher)
    {
        try
        {
            String query = "UPDATE publisher" +
                           "    SET idPublisher = ?," +
                           "        name = ?," +
                           "        logo = ?," +
                           "        description = ?" +
                           "    WHERE idPublisher = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(String.valueOf(publisher.getLogo()));

            statement.setLong(1, publisher.getId());
            statement.setString(2, publisher.getName());
            statement.setBinaryStream(3, stream);
            statement.setString(4, publisher.getDescription());
            statement.setLong(5, publisher.getId());

            return Boolean.TRUE;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean insert(Publisher publisher)
    {
        try
        {
            String query = "INSERT INTO publisher" +
                           "    (idPublisher, name, logo, description)" +
                           "    VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(String.valueOf(publisher.getLogo()));

            statement.setLong(1, publisher.getId());
            statement.setString(2, publisher.getName());
            statement.setBinaryStream(3, stream);
            statement.setString(4, publisher.getDescription());

            return Boolean.TRUE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Publisher fetch(Publisher publisher)
    {
        Publisher a = null;
        try
        {
            String query = "SELECT *" +
                           "    FROM publisher" +
                           "    WHERE idPublisher = " + publisher.getId();

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);

            Image image = new Image(resultSet.getBlob("logo").getBinaryStream());

            a = new Publisher(
                            resultSet.getLong("idPublisher"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            image
                            );

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return a;
    }
}
