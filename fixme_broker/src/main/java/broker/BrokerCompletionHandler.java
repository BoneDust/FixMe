package broker;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class BrokerCompletionHandler implements CompletionHandler<Void, AsynchronousSocketChannel>
{
    public void completed(Void att, AsynchronousSocketChannel ch)
    {
        Broker.startReading();
        try{Thread.sleep(2000);} catch (InterruptedException ex) {}
        Broker.startSending();
    }

    public void failed(Throwable e, AsynchronousSocketChannel ch)
    {
        System.out.println("connection failed");
        System.exit(0);
    }
}