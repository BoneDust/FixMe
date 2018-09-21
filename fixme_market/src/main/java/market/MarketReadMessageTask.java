package market;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class MarketReadMessageTask implements Runnable
{
    boolean isFirstMessage;
//todo cancel the future object when timing out.
    public MarketReadMessageTask(boolean isFirstMessage)
    {
        this.isFirstMessage = isFirstMessage;
    }

    public  void run()
    {
        while (true)
        {
            System.out.println("\nWaiting for incoming message ...");
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            Future reading = Market.marketSocket.read(buffer);
          //  Market.ReadWriteNonBlockingTimeOut();
            while (!reading.isDone());
            //    System.out.println("Response not received. Response Duration timed-out");
            //else
            {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                System.out.println("Response: " + msg);
                if (isFirstMessage)
                {
                    Market.id = Integer.parseInt(msg);
                    break;
                }
                else
                    Market.startSending("market sees you");
            }
        }
    }
}
