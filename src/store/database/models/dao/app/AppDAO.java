package store.database.models.dao.app;

import store.database.models.app.App;
import store.database.models.app.Comment;
import store.database.models.app.Language;
import store.database.models.app.Screenshot;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppDAO
{
    Connection connection;

    public AppDAO(Connection connection)
    {
        this.connection = connection;
    }

    public List<App> findAll()
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM app";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            App app = null;
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App(
                        resultSet.getLong("idApp"),
                        logo,
                        resultSet.getString("name"),
                        resultSet.getString("publisher"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("rating")
                );

                apps.add(app);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }

    public List<App> findTop()
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT a.idApp, a.name, p.name publisher" +
                           "    FROM app a INNER JOIN publishes pb ON a.idApp = pb.idApp" +
                           "               INNER JOIN publisher p ON p.idPublisher = pb.idPublisher" +
                           "    ORDER BY a.rating" +
                           "    LIMIT 100";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app = null;
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App(
                        resultSet.getLong("id"),
                        logo,
                        resultSet.getString("name"),
                        resultSet.getString("publisher"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("rating")
                );

                apps.add(app);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }

    public Boolean delete(Long id)
    {
        try
        {
            String query = "DELETE FROM app" +
                           "    WHERE idApp = ?";
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

    public Boolean update(App app)
    {
        try
        {
            String query = "UPDATE app" +
                           "    SET idApp = ?," +
                           "        name = ?," +
                           "        description = ?," +
                           "        version = ?," +
                           "        logo = ?," +
                           "        rating = ?," +
                           "        price = ?," +
                           "        size = ?," +
                           "        downloads = ?," +
                           "        features = ?," +
                           "        compatibility = ?," +
                           "        idCategory = (SELECT idCategory" +
                           "                          FROM category" +
                           "                          WHERE name = ?)" +
                           "    WHERE idApp = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(String.valueOf(app.getLogo()));

            statement.setLong(1, app.getId());
            statement.setString(2, app.getName());
            statement.setString(3, app.getDescription());
            statement.setString(4, app.getVersion());
            statement.setBinaryStream(5, stream);
            statement.setDouble(6, app.getRating());
            statement.setDouble(7, app.getPrice());
            statement.setString(8, app.getSize());
            statement.setLong(9, app.getDownloads());
            statement.setString(10, app.getFeatures());
            statement.setString(11, app.getCompatibility());
            statement.setString(12, app.getCategory());
            statement.setLong(13, app.getId());

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

    public Boolean insert(App app)
    {
        try
        {
            String query = "INSERT INTO app" +
                           "    (name, description, version, logo, rating, price, size, downloads, features, compatibility, idCategory) " +
                           "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT idCategory" +
                           "                                            FROM category" +
                           "                                            WHERE name = ?))";

            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(app.getLogoFile());

            statement.setString(1, app.getName());
            statement.setString(2, app.getDescription());
            statement.setString(3, app.getVersion());
            statement.setBlob(4, stream);
            statement.setDouble(5, app.getRating());
            statement.setDouble(6, app.getPrice());
            statement.setString(7, app.getSize());
            statement.setLong(8, app.getDownloads());
            statement.setString(9, app.getFeatures());
            statement.setString(10, app.getCompatibility());
            statement.setString(11, app.getCategory());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public App fetch(App app)
    {
        App a = null;
        String query;
        PreparedStatement statement;
        ResultSet resultSet;

        List<Screenshot> screenshots = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<Language> languages = new ArrayList<>();

        try
        {
            query = "SELECT s.*" +
                    "   FROM screenshot s INNER JOIN appscreenshot aps ON s.idScreenshot = aps.idScreenshot" +
                    "                     INNER JOIN app a ON a.idApp = aps.idApp" +
                    "   WHERE a.name = '" + app.getName() + "'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Image screenshot = new Image(resultSet.getBlob("image").getBinaryStream());
                screenshots.add(new Screenshot(resultSet.getLong("idScreenshot"), screenshot));
            }

            query = "SELECT l.*" +
                    "   FROM language l INNER JOIN applanguage al ON l.idLanguage = al.idLanguage" +
                    "                   INNER JOIN app a ON a.idApp = al.idApp" +
                    "   WHERE a.name = '" + app.getName() + "'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                languages.add(new Language(resultSet.getString("idLanguage"), resultSet.getString("name")));
            }

            query = "SELECT a.name, comment, c.rating, commentDate" +
                    "    FROM comment c INNER JOIN user u ON c.idUser = u.idUser" +
                    "                   INNER JOIN app a ON a.idApp = c.idApp" +
                    "    WHERE a.name = '" + app.getName() + "'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                comments.add(new Comment(resultSet.getString("comment"), resultSet.getString("name"),
                        resultSet.getDouble("rating"), resultSet.getDate("dateComment")));
            }


            query = "SELECT a.*, c.name category" +
                    "   FROM app a INNER JOIN category c ON a.idCategory = c.idCategory" +
                    "   WHERE a.name = '" + app.getName() + "'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);

            if (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                a = new App();
                a.setId(resultSet.getLong("idApp"));
                a.setCategory(resultSet.getString("category"));
                a.setName(resultSet.getString("name"));
                a.setComments(comments);
                a.setCompatibility(resultSet.getString("compatibility"));
                a.setDescription(resultSet.getString("description"));
                a.setDownloads(resultSet.getLong("downloads"));
                a.setFeatures(resultSet.getString("features"));
                a.setLanguages(languages);
                a.setLogo(logo);
                a.setPrice(resultSet.getDouble("price"));
                a.setPublisher(resultSet.getString("publisher"));
                a.setRating(resultSet.getDouble("rating"));
                a.setScreenshots(screenshots);
                a.setSize(resultSet.getString("size"));
                a.setVersion(resultSet.getString("version"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return a;
    }

    public List<App> findByCategory(Integer idCategory)
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT a.idApp, a.name, p.name publisher" +
                           "    FROM app a INNER JOIN publishes pb ON a.idApp = pb.idApp" +
                           "               INNER JOIN publisher p ON p.idPublisher = pb.idPublisher" +
                           "    WHERE a.idCategory = " + idCategory;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app = null;
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App(
                        resultSet.getLong("id"),
                        logo,
                        resultSet.getString("name"),
                        resultSet.getString("publisher"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("rating")
                );

                apps.add(app);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }

    public List<App> findByPublisher(Long idPublisher)
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT a.idApp, a.name, p.name publisher" +
                           "    FROM app a INNER JOIN publishes pb ON a.idApp = pb.idApp" +
                           "               INNER JOIN publisher p ON p.idPublisher = pb.idPublisher" +
                           "    WHERE p.idPublisher = " + idPublisher;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app = null;
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App(
                        resultSet.getLong("id"),
                        logo,
                        resultSet.getString("name"),
                        resultSet.getString("publisher"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("rating")
                );

                apps.add(app);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }

    public List<App> search(String name)
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT a.idApp, a.name, p.name publisher" +
                           "    FROM app a INNER JOIN publishes pb ON a.idApp = pb.idApp" +
                           "               INNER JOIN publisher p ON p.idPublisher = pb.idPublisher" +
                           "    WHERE a.name LIKE '%" + name + "%'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app;
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App(
                        resultSet.getLong("id"),
                        logo,
                        resultSet.getString("name"),
                        resultSet.getString("publisher"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("rating")
                );

                apps.add(app);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }
}
