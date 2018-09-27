package market;

import market.models.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import router.*;

public class SaveTransactions
{

    private DBHandler dbHandler = new DBHandler();

    public void recordTransaction(Transaction transaction)
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = dbHandler.getConnection();
            statement = connection.createStatement();
            String insertTransaction = String.format("INSERT INTO `fixme`.`transactions` (`brokerId`, `marketId`, `type`," +
                            " `status`, `instrument`, `quantity`, `price`) " +
                            " VALUES (%d, %d, '%s', '%s', '%s', %d, %f);",
                    transaction.getBrokerId(),
                    transaction.getMarketId(),
                    transaction.getType(),
                    transaction.getStatus(),
                    transaction.getInstrument(),
                    transaction.getQuantity(),
                    transaction.getPrice()
            );
            statement.executeUpdate(insertTransaction);

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbHandler.closeDBConnection(connection, statement);
        }
    }
}
