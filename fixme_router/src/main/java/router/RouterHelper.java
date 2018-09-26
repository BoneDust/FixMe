package router;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

public class RouterHelper
{

    public static void sendMarketList(int senderId, AsynchronousSocketChannel socket)
    {
        String allMarkets =" ";
        Iterator iterator = Router.markets.keySet().iterator();
        if (iterator.hasNext())
        {
            while (iterator.hasNext())
            {
                allMarkets += iterator.next();
                if (iterator.hasNext())
                    allMarkets += ", ";
            }
        }
        else
            allMarkets = "NULL";
        String response = "Broker=" + senderId +"|Markets=" + allMarkets + "|";
        new RouterSendMessageTask(socket, response).run();
    }

    private static int retrieveSocketId(AsynchronousSocketChannel socket, Map<Integer, AsynchronousSocketChannel> map)
    {
        for (int id : map.keySet())
        {
            if (map.get(id).equals(socket))
                return(id);
        }
        return (0);
    }

    public static void endConnection(AsynchronousSocketChannel socket, boolean isBroker)
    {
        int socketId;
        if (isBroker)
            socketId = retrieveSocketId(socket, Router.brokers);
        else
            socketId = retrieveSocketId(socket, Router.markets);
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        if (isBroker)
        {
            Router.brokers.remove(socketId, socket);
            System.out.println("Broker " + socketId + " logged out.");
        }
        else
        {
            Router.markets.remove(socketId, socket);
            System.out.println("Market " + socketId + " logged out.");
        }
    }

    public static int retrieveReceiverId(String message)
    {
        String[] split = message.split("\\|");
        return (Integer.parseInt(split[1].split("=")[1]));
    }
    public static int retrieveSenderId(String message)
    {
        String[] split = message.split("\\|");
        return (Integer.parseInt(split[0].split("=")[1]));
    }

    public static void sendToMarket(int id, String message)
    {
        new RouterSendMessageTask(Router.markets.get(id), message).run();
        new RouterReadMessageTask(Router.markets.get(id), false).run();
    }

    public  static void sendToBroker(int id , String message)
    {
        new RouterSendMessageTask(Router.brokers.get(id), message).run();
    }

    public static void RouterReadWriteNonBlockingTimeOut(Future future)
    {
        long startTime = Calendar.getInstance().getTimeInMillis();
        while (true)
        {
            long execTime = Calendar.getInstance().getTimeInMillis();
            long elapse = execTime - startTime;
            if (elapse >= Router.TIME_OUT_DURATION || future.isDone())
                break;
        }
    }
}
