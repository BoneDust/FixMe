package market;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class MarketCompletionHandler implements CompletionHandler<Void, AsynchronousSocketChannel>
{
    public void completed(Void att, AsynchronousSocketChannel ch)
    {
        Market.startReading(true);
        try {Thread.sleep(2000);}catch (Exception ex){}
        Market.startReading(false);
    }

    public void failed(Throwable e, AsynchronousSocketChannel ch)
    {
        System.out.println("connection failed");
        e.printStackTrace();
        System.exit(0);
    }
}