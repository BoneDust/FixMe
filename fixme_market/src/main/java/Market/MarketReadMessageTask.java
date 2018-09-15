package Market;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class MarketReadMessageTask implements Runnable
{
    public  void run()
    {
        while (true)
        {
            System.out.println("\nWaiting for response ...");
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            Future reading = Market.marketSocket.read(buffer);
            Market.ReadWriteNonBlockingTimeOut();
            if (!reading.isDone())
                System.out.println("Response not received. Response Duration timed-out");
            else
            {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                System.out.println("\nResponse: " + msg);
                if (msg.equals("bye"))
                    break;
            }
        }
    }
}
