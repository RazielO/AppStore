package store.database.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL
{
    private static Connection conn = null;
    private static String hostname = "localhost";
    private static String dbname = "appStore";
    private static String dbuser = "root";
    private static String dbpass = "mysql";

    /**
     * Makes a connection to the database
     */
    public static void Connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + dbname, dbuser, dbpass);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the connection to the database
     *
     * @return Connection Connection to the database
     */
    public static Connection getConnection()
    {
        if (conn == null)
            Connect();
        return conn;
    }

    /**
     * Disconnects from the database
     */
    public static void Disconnect()
    {
        try
        {
            conn.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
