package broker;

import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.util.Scanner;
import java.util.concurrent.Future;

public class BrokerSendMessageTask implements Runnable
{
    BrokerSendHelper sendHelper = new BrokerSendHelper();
    public void run()
    {
        Scanner stdin = new Scanner(System.in);
        String message = "";

        while (true)
        {
            message = sendHelper.retrieveFixMessage(stdin);
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Future writing = null;
            try
            {
                writing = Broker.brokerSocket.write(buffer);
            }
            catch (WritePendingException ex)
            {
                if (writing != null)
                    writing.cancel(false);
            }
            Broker.ReadWriteNonBlockingTimeOut(writing, true);
            if (!writing.isDone())
            {
                writing.cancel(false);
                System.out.println("\nMessage not sent. Send duration timed-out");
            }
            else
                new BrokerReadMessageTask().run();
            buffer.clear();
        }
    }
}
