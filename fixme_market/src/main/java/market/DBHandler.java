package market;

import market.models.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler
{
    private void closeDBConnection(Connection connection, Statement statement)
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

    private Connection getConnection()
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
                    "CREATE TABLE `fixme`.`transactions` (" +
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

    public void recordTransaction(Transaction transaction)
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = getConnection();
            statement = connection.createStatement();
            String insertHero = String.format("INSERT INTO `fixme`.`transactions` (`brokerId`, `marketId`, `type`," +
                            " `status`, `instrument`, `quantity`, `price`) " +
                            " VALUES (%d, %d, %s, %s, %s, %d, %f);",
                    transaction.getBrokerId(),
                    transaction.getMarketId(),
                    transaction.getType(),
                    transaction.getStatus(),
                    transaction.getInstrument(),
                    transaction.getQuantity(),
                    transaction.getPrice()
            );
            statement.executeUpdate(insertHero);

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
