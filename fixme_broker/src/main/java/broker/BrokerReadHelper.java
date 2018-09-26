package broker;

import broker.models.Instrument;

public class BrokerReadHelper
{
    public static void processMessage(String message)
    {
        System.out.println("\nRouter response: " + message);

        if (message.contains("Markets"))  //Broker=100000|Markets=454545, 45454, 5554, 45454|
        {
            String[] tagSplit = message.split("\\|");
            String markets = tagSplit[1].split("=")[1];
            System.out.println("\nOnline Markets: " + markets);

        }
        else if (message.contains("Instruments"))//Market=50000|Broker=1000000|Transaction=View|Instruments=bitcoin, 34, 12.0::ripple, 353, 12.55|
        {
            String[] tagSplit = message.split("\\|");
            String instrumentsString = tagSplit[3].split("=")[1];
            String[] instuments = instrumentsString.split("::");
            System.out.println("\n\t\tInstruments");
            for(String instrument : instuments)
            {
                String[] items = instrument.split(",");
                System.out.println("Name: "+ items[0] + ", Quantity: " + items[1] + ", Price: R" + items[2]);
            }
        }
        else if (message.contains("Status=Executed"))//Market=500000|Broker=100000|Transaction=Buy|Instrument=ripple|Quantity=355|Price=425.34|Status=Executed|
        {
            String[] tagSplit = message.split("\\|");
            String transation = tagSplit[2].split("=")[1];
            if (transation.equals("Buy"))
            {
                String name = tagSplit[3].split("=")[1];
                int quantity = Integer.parseInt(tagSplit[4].split("=")[1]);
                double price = Double.parseDouble(tagSplit[5].split("=")[1].split("R")[1]);
                Broker.money -= price;
                Broker.instruments.add(new Instrument(name, quantity,price));
            }
            else
            {
                String name = tagSplit[3].split("=")[1];
                double price = Double.parseDouble(tagSplit[5].split("=")[1]);
                int itemIndex = 0;
                for (Instrument instrument :Broker.instruments)
                {
                    if (instrument.getName().equals(name))
                        break;
                    itemIndex++;
                }
                Broker.instruments.remove(itemIndex);
                Broker.money += price;
            }
        }
    }
}