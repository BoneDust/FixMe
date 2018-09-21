package router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class RouterSendMessageTask implements Runnable
{

    AsynchronousSocketChannel socket;
    String message;
    boolean isBroker;

    public RouterSendMessageTask(AsynchronousSocketChannel socket, boolean isBroker ,String message)
    {
        this.socket = socket;
        this.message  = message;
        this.isBroker = isBroker;
    }

    public  void run()
    {
        byte[] bytes = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        Future writing = socket.write(buffer);
        //Router.RouterReadWriteNonBlockingTimeOut(writing);
        while  (!writing.isDone());
        /*{
            writing.cancel(true);
            System.out.println("couldn't write in time.");
        }
        else
        {
           // if (!isBroker)
             //   Router.startReading(socket);todo
        }*/
    }
}
