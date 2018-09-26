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
          String id = tags[0].split("=")[1];
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

    private static void sendBuyTransaction(String message)//Market=500000|Broker=100000|Transaction=Buy|Instrument=ripple|Quantity=355|Price=425.34|Status=Executed|
    {
        String response, name, tagsplit[] = message.split("\\|");
        int quantity;
        double price;
        boolean approved = false;

        response = "Market=" + Market.id + "|" + tagsplit[0] + "|" + tagsplit[2] + "|" + tagsplit[3] + "|" +
                    tagsplit[4] + "|" + tagsplit[5];
        name = tagsplit[3].split("=")[1]; quantity = Integer.parseInt(tagsplit[4].split("=")[1]);
        price = Double.parseDouble(tagsplit[5].split("=")[1].split("R")[1]);
        for (Instrument instrument : Market.instruments)
        {
            if (instrument.getName().equals(name) && quantity <= instrument.getQuantity())
            {
                double total = quantity * instrument.getPrice();
                if (price >= total)
                {
                    Market.money += price;
                    instrument.setQuantity(instrument.getQuantity() - quantity);
                    approved = true;
                }
            }
        }
        if (approved)
            response += "|Status=Executed|";
        else
            response += "|Status=Rejected|";
        new MarketSendMessageTask(response).run();
    }

    private static void sendSellTransaction(String message)
    {
        String response, name, tagsplit[] = message.split("\\|");
        int quantity;
        double price;
        boolean approved = false;

        response = "Market=" + Market.id + "|" + tagsplit[0] + "|" + tagsplit[2] + "|" + tagsplit[3] + "|" +
                tagsplit[4] + "|" + tagsplit[5];
        name = tagsplit[3].split("=")[1]; quantity = Integer.parseInt(tagsplit[4].split("=")[1]);
        price = Double.parseDouble(tagsplit[5].split("=")[1].split("R")[1]);
        for (Instrument instrument : Market.instruments)
        {
            if (instrument.getName().equals(name))
            {
                double total = quantity * instrument.getPrice();
                if (price <= total)
                {
                    Market.money -= price;
                    instrument.setQuantity(instrument.getQuantity() + quantity);
                    approved = true;
                }
            }
        }
        if (approved)
            response += "|Status=Executed|";
        else
            response += "|Status=Rejected|";
        new MarketSendMessageTask(response).run();
    }

    public static void processMessage(String message)
    {
        String transaction = getTransactionType(message);
        if (transaction.equals("View"))
            sendInstruments(message);
        else if (transaction.equals("Buy"))
            sendBuyTransaction(message);
        else
            sendSellTransaction(message);
    }
}
