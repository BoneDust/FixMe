package Broker;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class BrokerCompletionHandler implements CompletionHandler<Void, AsynchronousSocketChannel>
{
    public void completed(Void att, AsynchronousSocketChannel ch)
    {
        Broker.startReading();
        Broker.startSending();
    }

    public void failed(Throwable e, AsynchronousSocketChannel ch)
    {
        System.out.println("connection failed");
        e.printStackTrace();
        System.exit(0);
    }
}