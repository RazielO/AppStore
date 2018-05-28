package store.database.models.dao.user;

import store.database.models.user.Card;

import java.sql.*;

public class CardDAO
{
    private Connection connection;

    /**
     * Constructor receives a connection to the database
     *
     * @param connection connection to a MySQL database
     */
    public CardDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Deletes a credit card from the database
     *
     * @param card Card to delete
     *
     * @return Returns whether or not the operation was completed
     */
    public Boolean delete(Card card)
    {
        String query = "DELETE FROM card" +
                       "    WHERE cardNumber = '" + card.getNumber() + "'";
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


    /**
     * Inserts into the database a credit card
     *
     * @param card Card to insert
     *
     * @throws SQLException The card already is in the database
     */
    public void insert(Card card) throws SQLException
    {
        String query = "INSERT INTO card (cardNumber, cvv, name, lastName, expirationDate)" +
                       "    VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, card.getNumber());
            statement.setInt(2, card.getCvv());
            statement.setString(3, card.getName());
            statement.setString(4, card.getLastName());
            statement.setDate(5, card.getExpirationDate());

            statement.execute();
    }
}
