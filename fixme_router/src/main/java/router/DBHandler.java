package router;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler
{
    public void closeDBConnection(Connection connection, Statement statement)
    {
        try
        {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3308", "root", "password");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return (conn);
    }

    public void createDB()
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = getConnection();
            statement = connection.createStatement();
            String createDB = "create database if not exists fixme";
            statement.executeUpdate(createDB);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            closeDBConnection(connection, statement);
        }
    }

    public void initDB()
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = getConnection();
            statement = connection.createStatement();

            String createHeroTable =
                    "CREATE TABLE if not exists `fixme`.`transactions` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , "+
                            "`brokerId` INT NOT NULL, " +
                            "`marketId` INT NOT NULL, " +
                            "`type` TEXT NOT NULL, " +
                            "`status` TEXT NOT NULL," +
                            "`instrument` TEXT, " +
                            "`quantity` INT, " +
                            "`price` DECIMAL);"
                    ;
            statement.executeUpdate(createHeroTable);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            closeDBConnection(connection, statement);
        }
    }
}
