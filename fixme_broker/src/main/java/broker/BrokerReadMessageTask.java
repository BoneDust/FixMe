package broker;

import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.util.concurrent.Future;

public class BrokerReadMessageTask implements Runnable
{
    public  void run()
    {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            Future reading = null;

            try
            {
                reading = Broker.brokerSocket.read(buffer);
            }
            catch (ReadPendingException ex)
            {
                if (reading != null)
                    reading.cancel(false);
            }
            Broker.ReadWriteNonBlockingTimeOut();
            if (!reading.isDone())
                System.out.println("Response not received. Response Duration timed-out");
            else
            {
                buffer.flip();
                String message = new String(buffer.array()).trim();
                BrokerReadHelper.processMessage(message);
                if (Broker.id == 0)
                    Broker.id = Integer.parseInt(message);
            }
    }
}
