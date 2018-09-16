package broker;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class BrokerReadMessageTask implements Runnable
{
    public  void run()
    {
            System.out.println("\nWaiting for response ...");
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            Future reading = Broker.brokerSocket.read(buffer);
            Broker.ReadWriteNonBlockingTimeOut();
            if (!reading.isDone())
                System.out.println("Response not received. Response Duration timed-out");
            else
            {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                System.out.println("\nResponse: " + msg);
            }
    }
}
