package router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
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
            Future reading = socket.read(buffer);
            Router.RouterReadWriteNonBlockingTimeOut();
           while (!reading.isDone());
            //   System.out.println("couldn't read in time.");
            //else
            {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                if (msg.equals(""))
                    break;
                else
                    System.out.println("Message received: " + msg);
            }
            if (!isBroker)
                break;
            else
                Router.startSending(socket);
        }
        while (true);
    }

}
