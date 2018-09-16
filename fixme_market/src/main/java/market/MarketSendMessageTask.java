package market;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class MarketSendMessageTask implements Runnable
{
    private String message;

    public MarketSendMessageTask(String message)
    {
        this.message = message;
    }

    public void run()
    {
        try
        {
            System.out.print("\nMessage to router: " + message);
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Future writing = Market.marketSocket.write(buffer);
            Market.ReadWriteNonBlockingTimeOut();
            if (!writing.isDone())
                System.out.println("\nMessage not sent. Send duration timed-out");
            buffer.clear();
        }
        catch (Exception ex){  ex.printStackTrace();  }
    }
}
