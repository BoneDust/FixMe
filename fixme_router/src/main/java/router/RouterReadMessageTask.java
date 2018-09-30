package router;

import com.sun.imageio.spi.RAFImageOutputStreamSpi;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ReadPendingException;
import java.rmi.MarshalException;
import java.util.Iterator;
import java.util.concurrent.Future;

public class RouterReadMessageTask implements Runnable
{

    AsynchronousSocketChannel socket;
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    boolean isBroker;

    public RouterReadMessageTask(AsynchronousSocketChannel socket, boolean isBroker)
    {
        this.socket = socket;
        this.isBroker = isBroker;
    }

    public  void run()
    {
        do
        {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            Future reading = null;
            try
            {
                reading  = socket.read(buffer);
            }
            catch (ReadPendingException ex)
            {
                if (reading != null)
                    reading.cancel(false);
                continue;
            }

            while (!reading.isDone());
            buffer.flip();
            String message = new String(buffer.array()).trim();
            int senderId;

            if (message.equals(""))
            {
                RouterHelper.endConnection(socket, isBroker);
                break;
            }
            else
            {
                senderId = RouterHelper.retrieveSenderId(message);
                if (isBroker)
                    System.out.println(ANSI_GREEN + message + ANSI_RESET);
                else
                    System.out.println(ANSI_YELLOW + message + ANSI_RESET);
            }
            if (ChecksumHelper.isValidChecksum(message))
            {
                if (!isBroker)
                {
                    int receiverId = RouterHelper.retrieveReceiverId(message);
                    if (Router.brokers.keySet().contains(receiverId))
                        RouterHelper.sendToBroker(receiverId, message);
                    break;
                }
                else
                {
                    if (message.contains("All"))
                        RouterHelper.sendMarketList(senderId, socket);
                    else
                    {
                        int receiverId = RouterHelper.retrieveReceiverId(message);
                        if (Router.markets.keySet().contains(receiverId))
                        {
                            RouterHelper.sendToMarket(receiverId, message);
                            if (!Router.markets.keySet().contains(receiverId))
                            {
                                message = "Market=" + receiverId + "|Broker=" + senderId + "|Status=Offline|";
                                RouterHelper.sendToBroker(senderId, message);
                            }
                        }
                        else
                        {
                            message += "Status=Rejected|";
                            RouterHelper.sendToBroker(senderId, message);
                        }
                    }
                }
            }
            else
                System.out.println("\nResponse: Invalid checksum");
        }
        while (true);
    }

}
