package store.database.models.dao.user;

import store.database.models.user.Card;

import java.sql.*;

public class CardDAO
{
    private Connection connection;

    public CardDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Boolean delete(Card card)
    {
        String query = "DELETE FROM card" +
                       "    WHERE cardNumber = " + card.getNumber();
        try
        {
            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void insert(Card card)
    {
        String query = "INSERT INTO card (cardNumber, cvv, name, lastName, expirationDate)" +
                       "    VALUES (?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, card.getNumber());
            statement.setInt(2, card.getCvv());
            statement.setString(3, card.getName());
            statement.setString(4, card.getLastName());
            statement.setDate(5, card.getExpirationDate());

            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
