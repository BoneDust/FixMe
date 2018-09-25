package market;

import market.models.Instrument;

public class MarketReadHelper
{
    private static String getTransactionType(String message)
    {
        String[] tagSplit = message.split("\\|");
        String transaction = tagSplit[2].split("=")[1];
        return (transaction);
    }

    private static String getBrokerId(String message)
    {
          String[] tags = message.split("\\|");
          String id = tags[0].split("=")[0];
          return (id);
    }

    public static void sendInstruments(String message)//Market=50000|Broker=1000000|Transaction=View|Instruments=bitcoin, 34, 12.0::ripple, 353, 12.55|
    {
        String response = "Market=" + Integer.toString(Market.id) + "|Broker=" + getBrokerId(message) +
                        "|Transaction=View|Instruments=";
        for (Instrument instrument : Market.instruments)
        {
            String instrumenString = instrument.getName() + ", " + Integer.toString(instrument.getQuantity()) + ", " +
                            instrument.getPrice() + "::";
            response += instrumenString;
        }
        response += "|";//todo add checksum;
        new MarketSendMessageTask(response).run();
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
