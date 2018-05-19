package store.database.models.dao.user;

import javafx.scene.image.Image;
import store.database.models.dao.MySQL;
import store.database.models.user.Card;
import store.database.models.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
    private Connection connection;
    private CardDAO cardDAO = new CardDAO(MySQL.getConnection());

    public UserDAO(Connection connection)
    {
        this.connection = connection;
    }

    public List<User> findAll()
    {
        List<User> users = new ArrayList<>();

        try
        {
            String query = "SELECT *" +
                           "    FROM user";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            User user;
            while (resultSet.next())
            {
                Image image = new Image(resultSet.getBlob("picture").getBinaryStream());

                user = new User();
                user.setAddress(resultSet.getString("address"));
                user.setAdmin(resultSet.getBoolean("admin"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setPicture(image);
                user.setId(resultSet.getLong("idUser"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getString("cardNumber"));

                users.add(user);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return users;
    }

    public Boolean delete(User user)
    {
        try
        {
            String query = "DELETE FROM user" +
                           "    WHERE iduser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, user.getId());
            statement.execute();

            Card card = new Card();
            card.setNumber(user.getCardNumber());
            cardDAO.delete(card);

            return Boolean.TRUE;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean updateWithPicture(User user)
    {
        try
        {
            String query = "UPDATE user" +
                           "    SET name = ?," +
                           "        lastname = ?," +
                           "        address = ?," +
                           "        phone = ?," +
                           "        birthday = ?," +
                           "        email = ?," +
                           "        admin = ?," +
                           "        cardnumber = ?," +
                           "        picture = ?" +
                           "    WHERE iduser = ?";


            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(user.getImageFile());

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.setString(4, user.getPhone());
            statement.setDate(5, user.getBirthday());
            statement.setString(6, user.getEmail());
            statement.setBoolean(7, user.getAdmin());
            statement.setString(8, user.getCardNumber());
            statement.setBinaryStream(9, stream);
            statement.setLong(10, user.getId());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean updateWithoutPicture(User user)
    {
        try
        {
            String query = "UPDATE user" +
                           "    SET name = ?," +
                           "        lastname = ?," +
                           "        address = ?," +
                           "        phone = ?," +
                           "        birthday = ?," +
                           "        email = ?," +
                           "        admin = ?," +
                           "        cardnumber = ?," +
                           "        password = ?" +
                           "    WHERE iduser = ?";


            PreparedStatement statement = connection.prepareStatement(query);


            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.setString(4, user.getPhone());
            statement.setDate(5, user.getBirthday());
            statement.setString(6, user.getEmail());
            statement.setBoolean(7, user.getAdmin());
            statement.setString(8, user.getCardNumber());
            statement.setString(9, user.getPassword());
            statement.setLong(10, user.getId());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public Boolean insert(User user)
    {
        try
        {
            String query = "INSERT INTO user" +
                           "    (name, lastname, address, phone, birthday, email, admin, cardnumber, picture, password, username) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);

            FileInputStream stream = new FileInputStream(user.getImageFile());

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.setString(4, user.getPhone());
            statement.setDate(5, user.getBirthday());
            statement.setString(6, user.getEmail());
            statement.setBoolean(7, user.getAdmin());
            statement.setString(8, user.getCardNumber());
            statement.setBinaryStream(9, stream);
            statement.setString(10, user.getPassword());
            statement.setString(11, user.getUsername());

            statement.execute();

            return Boolean.TRUE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public User findByUsername(String username) throws SQLException
    {
        User a = null;
        String query = "SELECT *" +
                       "    FROM user" +
                       "    WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next())
        {
            Image image = new Image(resultSet.getBlob("picture").getBinaryStream());

            a = new User();
            a.setAddress(resultSet.getString("address"));
            a.setAdmin(resultSet.getBoolean("admin"));
            a.setBirthday(resultSet.getDate("birthday"));
            a.setUsername(resultSet.getString("username"));
            a.setPassword(resultSet.getString("password"));
            a.setPhone(resultSet.getString("phone"));
            a.setPicture(image);
            a.setId(resultSet.getLong("idUser"));
            a.setName(resultSet.getString("name"));
            a.setLastName(resultSet.getString("lastName"));
            a.setEmail(resultSet.getString("email"));
            a.setCardNumber(resultSet.getString("cardNumber"));
        }

        return a;
    }

    public User findByEmail(String email) throws SQLException
    {
        User a = null;
        String query = "SELECT *" +
                       "    FROM user" +
                       "    WHERE email = '" + email + "'";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next())
        {
            Image image = new Image(resultSet.getBlob("picture").getBinaryStream());

            a = new User();
            a.setAddress(resultSet.getString("address"));
            a.setAdmin(resultSet.getBoolean("admin"));
            a.setBirthday(resultSet.getDate("birthday"));
            a.setUsername(resultSet.getString("username"));
            a.setPassword(resultSet.getString("password"));
            a.setPhone(resultSet.getString("phone"));
            a.setPicture(image);
            a.setId(resultSet.getLong("idUser"));
            a.setName(resultSet.getString("name"));
            a.setLastName(resultSet.getString("lastName"));
            a.setEmail(resultSet.getString("email"));
            a.setCardNumber(resultSet.getString("cardNumber"));
        }

        return a;
    }
}
