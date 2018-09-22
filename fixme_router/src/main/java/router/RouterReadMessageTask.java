package router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ReadPendingException;
import java.util.concurrent.Future;

public class RouterReadMessageTask implements Runnable
{

    AsynchronousSocketChannel socket;
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
            int receiverId = RouterHelper.retrieveReceiverId(message);
            int senderId =  RouterHelper.retrieveSenderId(message);

            if (message.equals(""))
            {
                RouterHelper.endConnection(senderId, socket);
                break;
            }
            else
                System.out.println("Message received: " + message);

            if (!isBroker)
            {
                RouterHelper.sendToBroker(receiverId, message);
                break;
            }
            else
                RouterHelper.sendToMarket(receiverId, message);
        }
        while (true);
    }

}
