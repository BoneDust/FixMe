package market;

import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.util.concurrent.Future;

public class MarketReadMessageTask implements Runnable
{
    boolean isFirstMessage;

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
   141444040         Future reading = null;
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
                System.out.println("Response: " + msg);
                if (isFirstMessage)
                {
                    Market.id = Integer.parseInt(msg);
                    break;
                }
                else
                    MarketReadHelper.processMessage(msg);
            }
        }
    }
}
