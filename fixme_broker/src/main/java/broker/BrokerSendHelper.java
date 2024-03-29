package broker;

import broker.models.Instrument;
import router.*;
import java.util.Scanner;


public class BrokerSendHelper
{
    ChecksumHelper checksumHelper;
    private String displayMenu(Scanner stdin)
    {
        String choice =  "";
        do
        {
            System.out.print("\n**********************************************\n" +
                            "*             Broker Message Menu            *\n" +
                            "**********************************************\n" +
                            "*                                            *\n" +
                            "*                                            *\n" +
                            "*  1. Request market list.                   *\n" +
                            "*  2. Request instrument list.               *\n" +
                            "*  3. View Inventory.                        *\n" +
                            "*  4. Buy.                                   *\n" +
                            "*  5. Sell.                                  *\n" +
                            "*  6. Logout.                                *\n" +
                            "*                                            *\n" +
                            "**********************************************\n" +
                            " Choice: ");
            if (stdin.hasNextLine())
                choice = stdin.nextLine();
            else
                System.exit(0);
        }
        while (!(choice.equals("1") || choice.equals("2") || choice.equals("3") ||
                choice.equals("4") || choice.equals("5") || choice.equals("6")));
        return (choice);
    }

    private String selectMarket(Scanner stdin)
    {
        String choice = "0";
        do
        {
            System.out.print("\n*********************************\n" +
                            "*         Select a market       *\n" +
                            "*********************************\n");
            System.out.print("Market ID: ");
            if (stdin.hasNextLine())
                choice = stdin.nextLine();
            else
                System.exit(0);
        }
        while(!choice.matches("^\\d+$"));
        return (choice);
    }

    private String verifyBuyingPower(String name, String quantity, String price)
    {
        if (Double.parseDouble(price) <= Broker.money)
            return ("Instrument=" + name +"|Quantity=" + quantity + "|Price=R" + price + "|");
        else
            return  ("Instrument=null|Quantity=null|Price=null|");
    }

    private String verifySellingPower(String name, String quantity, String price)
    {
        boolean foundInstrument = false;
        for (Instrument instrument : Broker.instruments)
        {
            if (instrument.getName().equals(name) && Integer.parseInt(quantity) <= instrument.getQuantity())
            {
                foundInstrument = true;
                break;
            }
        }
        if (foundInstrument)
            return ("Instrument=" + name +"|Quantity=" + quantity + "|Price=R" + price + "|");
        else
            return  ("Instrument=null|Quantity=null|Price=null|");
    }

    private String selectInstrument(Scanner stdin, boolean isBuying)
    {
        String instrumentInfo = "", name = "", quantity = "", price = "";

        System.out.print("\n*********************************\n" +
                "*      Select an instrument     *\n" +
                "*********************************\n");

        System.out.print("Name: ");
        if (stdin.hasNextLine())
            name = stdin.nextLine();
        else
            System.exit(0);

        do
        {
            System.out.print("\nQuantity: ");
            if (stdin.hasNextLine())
                quantity = stdin.nextLine();
            else
                System.exit(0);
        }
        while (!quantity.matches("^\\d+$"));

        do
        {
            System.out.print("\nPrice: ");
            if (stdin.hasNextLine())
                price = stdin.nextLine();
            else
                System.exit(0);
        }
        while (!price.matches("^\\d+(\\.\\d{1,2})?$"));

        if (isBuying)
            instrumentInfo = verifyBuyingPower(name, quantity, price);
        else
            instrumentInfo = verifySellingPower(name, quantity, price);

        return (instrumentInfo);
    }

    private void viewInstruments()
    {
        int index = 0;
        System.out.println("\n***********************************\n" +
                                "*            Inventory            *\n" +
                                "***********************************\n" +
                                "\n\tInstruments:");
        for (Instrument instru : Broker.instruments)
            System.out.println("  " + ++index + ". Name: " + instru.getName() + ", Quantity: " +instru.getQuantity() +
                                ", Price: R" + instru.getPrice());
        System.out.println("\nAccount Balance: R" + Broker.money);
    }

    private String processMenuSelection(Scanner stdin)
    {
        String selected = displayMenu(stdin);
        String message = "BrokerID=" + Integer.toString(Broker.id);

        switch (selected)
        {
            case "1":
                    message += "|Markets=All|";
                    break;

            case "2":
                    message += "|Market=" + selectMarket(stdin) + "|Transaction=View|";
                    break;

            case "3":
                    message = "null";
                    viewInstruments();
                    break;

            case "4":
                    message +="|Market=" + selectMarket(stdin) + "|Transaction=Buy|" + selectInstrument(stdin, true);
                    break;

            case "5":
                    message +="|Market=" + selectMarket(stdin) + "|Transaction=Sell|" + selectInstrument(stdin, false);
                    break;

            case "6":
                    System.exit(0);
                    break;

             default:
                    System.exit(0);
                    break;
        }
        return (message);
    }

    public  String retrieveFixMessage(Scanner stdin)
    {
        String message = "";
        do
        {
            message = processMenuSelection(stdin);
        }
        while (message.equals("null"));

        try
        {
            checksumHelper = new ChecksumHelper();
            message += "Checksum=" + checksumHelper.generateChecksum(message) + "|";
        }
        catch (Exception ex)//todo need to fix this ClassNotFoundException
        {
            System.out.println("\nRouter is offline");
            System.exit(0);
        }
        System.out.println("\nSending : " + message);
        return message;
    }
}