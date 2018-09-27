package market;

import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.util.concurrent.Future;

public class MarketReadMessageTask implements Runnable
{
    boolean isFirstMessage;
    MarketReadHelper helper = new MarketReadHelper();

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
           Future reading = null;
            try
            {
                reading = Market.marketSocket.read(buffer);
            }
            catch (ReadPendingException ex)
            {
                if (reading != null)
                    reading.cancel(false);
                continue;
            }
            while (!reading.isDone());
            {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                System.out.println("Router response: " + msg);
                if (isFirstMessage)
                {
                    Market.id = Integer.parseInt(msg);
                    break;
                }
                else
                    helper.processMessage(msg);
            }
        }
    }
}
