package market;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class MarketReadMessageTask implements Runnable
{
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
                Market.startSending("\nmarket sees you");
                String msg = new String(buffer.array()).trim();
                System.out.println("\nResponse: " + msg);
                if (msg.equals("bye"))
                    break;
            }
        }
    }

    private void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
