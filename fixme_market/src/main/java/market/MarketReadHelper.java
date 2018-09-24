package market;

public class MarketReadHelper
{
    private static String getTransactionType(String message)
    {
        String[] tagSplit = message.split("\\|");
        String transaction = tagSplit[2].split("=")[1];
        return (transaction);
    }

    public static void sendInstruments(String message)
    {

    }

    private static void sendBuyTransaction(String message)
    {

    }

    private static void sendSellTransaction(String message)
    {

    }

    public static void processMessage(String message)
    {

    }
}
