package broker;

import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.util.Scanner;
import java.util.concurrent.Future;

public class BrokerSendMessageTask implements Runnable
{
    public void run()
    {
        Scanner stdin = new Scanner(System.in);
        String message = "";

        while (true)
        {
            message = BrokerSendHelper.retrieveFixMessage(stdin);
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Future writing = null;
            try
            {
                writing = Broker.brokerSocket.read(buffer);
            }
            catch (ReadPendingException ex)
            {
                if (writing != null)
                    writing.cancel(false);
            }
            Broker.ReadWriteNonBlockingTimeOut();
            if (!writing.isDone())
                System.out.println("\nMessage not sent. Send duration timed-out");
            else
                new BrokerReadMessageTask().run();
            buffer.clear();
        }
    }
}
