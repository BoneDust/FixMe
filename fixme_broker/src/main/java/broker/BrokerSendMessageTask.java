package broker;

import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.Future;

public class BrokerSendMessageTask implements Runnable
{
    public void run()
    {
        Scanner stdin = new Scanner(System.in);
        String msg = "";
        try
        {
            while (true)
            {
                System.out.print("\nMessage to router: ");
                if (stdin.hasNextLine())
                    msg = stdin.nextLine();
                else
                    System.exit(0);
                byte[] bytes = msg.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                Future writing = Broker.brokerSocket.write(buffer);
              //  Broker.ReadWriteNonBlockingTimeOut();
                while (!writing.isDone());
                  //  System.out.println("\nMessage not sent. Send duration timed-out");
                //else
                {
                   new BrokerReadMessageTask().run();
                }
                buffer.clear();
            }
        }
        catch (Exception ex){  ex.printStackTrace();  }
    }
}
