package router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class RouterReadMessageTask implements Runnable
{

    AsynchronousSocketChannel socket;

    public RouterReadMessageTask(AsynchronousSocketChannel socket)
    {
        this.socket = socket;
    }

    public  void run()
    {
        while (true)
        {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            Future result = socket.read(buffer);
            //todo timeout
            buffer.flip();
            String msg = new String(buffer.array()).trim();
            System.out.println("Message received: " + msg);
            if (msg.equals("bye"))
                    break;
        }
    }


}
