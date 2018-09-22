package broker;

import static broker.Broker.TIME_OUT_DURATION;

public class BrokerHelper
{
    private static void displayMenu()
    {
        System.out.print("**********************************************\n" +
                            "*             Broker Message Menu            *\n" +
                            "**********************************************\n" +
                            "*                                            *\n" +
                            "*                                            *\n" +
                            "*  1. Request market list.                   *\n" +
                            "*  2. Request instrument list.               *\n" +
                            "*  3. Buy.                                   *\n" +
                            "*  4. Sell.                                  *\n" +
                            "*  5. Quit.                                  *\n" +
                            "*                                            *\n" +
                            "**********************************************\n" +
                            " Choice: ");

    }

    private static voi

    public static void ReadWriteNonBlockingTimeOut()
    {
        try
        {
            Thread.sleep(TIME_OUT_DURATION);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< BrokerReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }

}
