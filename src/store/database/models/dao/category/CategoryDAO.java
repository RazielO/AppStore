package store.database.models.dao.category;

import store.database.models.category.Category;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO
{
    private Connection connection;

    public CategoryDAO(Connection connection)
    {
        this.connection = connection;
    }

    public List<Category> fetchAll()
    {
        List<Category> categories = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM category";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Category category;

            while (resultSet.next())
            {
                Image image = new Image(resultSet.getBlob("logo").getBinaryStream());

                category = new Category(
                                        resultSet.getInt("idCategory"),
                                        resultSet.getString("name"),
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

    public Boolean delete(Integer id)
    {
        try
        {
            String query = "DELETE FROM category" +
                           "    WHERE idCategory = ?";
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

    public Boolean update(Category category)
    {
        try
        {
            String query = "UPDATE category" +
                           "    SET name = ?," +
                           "        logo = ?" +
                           "    WHERE idCategory = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(category.getFile());

            statement.setString(1, category.getName());
            statement.setBinaryStream(2, stream);
            statement.setInt(3, category.getId());

            return Boolean.TRUE;
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean insert(Category category)
    {
        try
        {
            String query = "INSERT INTO category" +
                           "    (idCategory, name, logo, game)" +
                           "    VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(category.getFile());

            statement.setString(1, category.getName());
            statement.setBinaryStream(2, stream);

            return Boolean.TRUE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Category fetch(String name)
    {
        Category a = null;
        try
        {
            String query = "SELECT *" +
                           "    FROM category" +
                           "    WHERE name = " + name;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);

            Image image = new Image(resultSet.getBlob("logo").getBinaryStream());

            a = new Category(
                             resultSet.getInt("idCategory"),
                             resultSet.getString("name"),
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
