package market;

import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
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
            System.out.println("\nMessage to router:  " + message);
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Future writing = null;
            try
            {
                Market.marketSocket.write(buffer);
            }
            catch (WritePendingException ex)
            {
                if (writing != null)
                    writing.cancel(false);
            }
            Market.ReadWriteNonBlockingTimeOut();
            if  (!writing.isDone())
            {
                writing.cancel(false);
                System.out.println("\nMessage not sent. Send duration timed-out");
            }
            buffer.clear();
        }
        catch (Exception ex){  ex.printStackTrace();  }
    }
}
