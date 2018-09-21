package router;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class RouterHelper
{
    public static void endConnection(int socketId, AsynchronousSocketChannel socket)
    {
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        if (Router.brokers.remove(socketId, socket))
            System.out.println("Broker " + socketId + " logged out.");
        if (Router.markets.remove(socketId, socket))
            System.out.println("Market " + socketId + " logged out.");
    }

    public static int retrieveReceiverId(String message)
    {
        return (0);
    }
    public static int retrieveSenderId(String message)
    {
        return (0);
    }

    public static void sendToMarket(int id, String message)
    {

    }

    public  static void sendToBroker(int id , String message)
    {
        new RouterSendMessageTask(Router.brokers.get(id), message).run();
    }

    public static void RouterReadWriteNonBlockingTimeOut(int duration)
    {
        try
        {
            Thread.sleep(duration);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< RouterReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }
}
