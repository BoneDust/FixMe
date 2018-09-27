package market;

import market.models.Instrument;
import market.models.Transaction;
import router.ChecksumHelper;

public class MarketReadHelper
{
    SaveTransactions saveTransaction = new SaveTransactions();

    private String getTransactionType(String message)
    {
        String[] tagSplit = message.split("\\|");
        String transaction = tagSplit[2].split("=")[1];
        return (transaction);
    }

    private String getBrokerId(String message)
    {
          String[] tags = message.split("\\|");
          String id = tags[0].split("=")[1];
          return (id);
    }

    public void sendInstruments(String message)
    {
        String response = "Market=" + Integer.toString(Market.id) + "|Broker=" + getBrokerId(message) +
                        "|Transaction=View|Instruments=";
        for (Instrument instrument : Market.instruments)
        {
            String instrumenString = instrument.getName() + ", " + Integer.toString(instrument.getQuantity()) + ", " +
                            instrument.getPrice() + "::";
            response += instrumenString;
        }
        response += "|";
        response += "Checksum="  + ChecksumHelper.generateChecksum(response) + "|";
        saveTransaction.recordTransaction(new Transaction(Integer.parseInt(getBrokerId(message)), Market.id, "View",
                                            "Executed", null,null, null));//todo might need to put this on a thread
        new MarketSendMessageTask(response).run();
    }

    private void sendBuyTransaction(String message)
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

        response += "Checksum="  + ChecksumHelper.generateChecksum(response) + "|";
        saveTransaction.recordTransaction(new Transaction(Integer.parseInt(getBrokerId(message)), Market.id, "Buy",
                (approved? "Executed" : "Rejected"), name, quantity, price));
        new MarketSendMessageTask(response).run();
    }

    private void sendSellTransaction(String message)
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

        response += "Checksum="  + ChecksumHelper.generateChecksum(response) + "|";

        saveTransaction.recordTransaction(new Transaction(Integer.parseInt(getBrokerId(message)), Market.id, "Sell",
                (approved? "Executed" : "Rejected"), name, quantity, price));
        new MarketSendMessageTask(response).run();
    }

    public void processMessage(String message)
    {
        if (!message.equals(""))
        {
            String transaction = getTransactionType(message);
            if (transaction.equals("View"))
                sendInstruments(message);
            else if (transaction.equals("Buy"))
                sendBuyTransaction(message);
            else
                sendSellTransaction(message);
        }
        else
        {
            System.out.println("\nRouter is  went offline");
            System.exit(0);
        }
    }
}
