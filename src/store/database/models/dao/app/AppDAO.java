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
    private Connection connection;

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

            apps = retrieveApps(resultSet, apps);

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
            String query = "SELECT *" +
                           "    FROM app " +
                           "    ORDER BY rating, downloads" +
                           "    LIMIT 20";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app;
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

    public Boolean delete(App app)
    {
        String query;
        List<Long> ids = new ArrayList<>();
        PreparedStatement statement;

        try
        {
            query = "SELECT idScreenshot" +
                    "    FROM screenshot" +
                    "    WHERE idScreenshot IN (SELECT idScreenshot" +
                    "                               FROM appScreenshot" +
                    "                               WHERE idApp = ?)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, app.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                ids.add(resultSet.getLong("idScreenshot"));



            query = "DELETE FROM appScreenshot" +
                    "  WHERE idApp = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, app.getId());
            statement.execute();




            for (Long id : ids)
            {
                query = "DELETE FROM screenshot" +
                        "  WHERE idScreenshot = ?";

                statement = connection.prepareStatement(query);
                statement.setLong(1, id);
                statement.execute();
            }



            query = "DELETE FROM purchases" +
                    "  WHERE idApp = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, app.getId());
            statement.execute();


            query = "DELETE FROM comment" +
                    "  WHERE idApp = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, app.getId());
            statement.execute();



            query = "DELETE FROM appLanguage" +
                    "  WHERE idApp = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, app.getId());
            statement.execute();



            query = "DELETE FROM app" +
                    "  WHERE idApp = ?";
            statement = connection.prepareStatement(query);
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

    public Boolean update(App app)
    {
        try
        {
            String query = "UPDATE app" +
                           "    SET name = ?," +
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
                           "                          WHERE name = ?)," +
                           "        publisher = ?," +
                           "        featured = ?" +
                           "    WHERE idApp = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(app.getLogoFile());

            statement.setString(1, app.getName());
            statement.setString(2, app.getDescription());
            statement.setString(3, app.getVersion());
            statement.setBinaryStream(4, stream);
            statement.setDouble(5, app.getRating());
            statement.setDouble(6, app.getPrice());
            statement.setString(7, app.getSize());
            statement.setLong(8, app.getDownloads());
            statement.setString(9, app.getFeatures());
            statement.setString(10, app.getCompatibility());
            statement.setString(11, app.getCategory());
            statement.setString(12, app.getPublisher());
            statement.setBoolean(13, app.isFeatured());
            statement.setLong(14, app.getId());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean updateWithoutImage(App app)
    {
        try
        {
            String query = "UPDATE app" +
                           "    SET idApp = ?," +
                           "        name = ?," +
                           "        description = ?," +
                           "        version = ?," +
                           "        rating = ?," +
                           "        price = ?," +
                           "        size = ?," +
                           "        downloads = ?," +
                           "        features = ?," +
                           "        compatibility = ?," +
                           "        idCategory = (SELECT idCategory" +
                           "                          FROM category" +
                           "                          WHERE name = ?)," +
                           "         publisher = ?," +
                           "         featured = ?" +
                           "    WHERE idApp = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, app.getId());
            statement.setString(2, app.getName());
            statement.setString(3, app.getDescription());
            statement.setString(4, app.getVersion());
            statement.setDouble(5, app.getRating());
            statement.setDouble(6, app.getPrice());
            statement.setString(7, app.getSize());
            statement.setLong(8, app.getDownloads());
            statement.setString(9, app.getFeatures());
            statement.setString(10, app.getCompatibility());
            statement.setString(11, app.getCategory());
            statement.setString(12, app.getPublisher());
            statement.setBoolean(13, app.isFeatured());
            statement.setLong(14, app.getId());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (SQLException e)
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
                           "    (name, description, version, logo, rating, price, size, downloads, features, compatibility, idCategory, publisher, featured) " +
                           "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT idCategory" +
                           "                                            FROM category" +
                           "                                            WHERE name = ?), ?, ?)";

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
            statement.setString(12, app.getPublisher());
            statement.setBoolean(13, app.isFeatured());

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

            query = "SELECT a.name, comment, c.rating, commentDate, u.username, a.idApp, u.idUser" +
                    "    FROM comment c INNER JOIN user u ON c.idUser = u.idUser" +
                    "                   INNER JOIN app a ON a.idApp = c.idApp" +
                    "    WHERE a.name = '" + app.getName() + "'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Comment e = new Comment();
                e.setIdUser(resultSet.getLong("idUser"));
                e.setUsername(resultSet.getString("username"));
                e.setIdApp(resultSet.getLong("idApp"));
                e.setRating(resultSet.getDouble("rating"));
                e.setComment(resultSet.getString("comment"));
                e.setDate(resultSet.getDate("commentDate"));

                comments.add(e);
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
                a.setFeatured(resultSet.getBoolean("featured"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return a;
    }

    public List<App> findByCategory(int id)
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM app" +
                           "    WHERE idCategory = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            App app;
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

    public List<App> search(String name)
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM app" +
                           "    WHERE name LIKE '%" + name + "%'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            App app;
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

    public List<App> findFeatured()
    {
        List<App> apps = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM app" +
                           "    WHERE featured = TRUE";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            apps = retrieveApps(resultSet, apps);

            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return apps;
    }

    private List<App> retrieveApps(ResultSet resultSet, List<App> list)
    {
        App app;

        try
        {
            while (resultSet.next())
            {
                Image logo = new Image(resultSet.getBlob("logo").getBinaryStream());

                app = new App();
                app.setId(resultSet.getLong("idApp"));
                app.setLogo(logo);
                app.setPublisher(resultSet.getString("publisher"));
                app.setName(resultSet.getString("name"));
                app.setPrice(resultSet.getDouble("price"));
                app.setRating(resultSet.getDouble("rating"));

                list.add(app);
            }
            resultSet.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
