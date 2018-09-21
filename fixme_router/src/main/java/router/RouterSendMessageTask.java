package router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.WritePendingException;
import java.util.concurrent.Future;

public class RouterSendMessageTask implements Runnable
{

    AsynchronousSocketChannel socket;
    String message;

    public RouterSendMessageTask(AsynchronousSocketChannel socket, String message)
    {
        this.socket = socket;
        this.message  = message;
    }

    public  void run()
    {
        byte[] bytes = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        Future writing = null;
        try
        {
            writing = socket.write(buffer);
        }
        catch (WritePendingException ex)
        {
            if (writing != null)
                writing.cancel(true);
        }
        RouterHelper.RouterReadWriteNonBlockingTimeOut(Router.TIME_OUT_DURATION);
        if (!writing.isDone())
        {
            writing.cancel(true);
            System.out.println("couldn't write in time.");
        }
    }
}
