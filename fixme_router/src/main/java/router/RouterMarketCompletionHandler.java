package router;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class RouterMarketCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object>
{
    private AsynchronousServerSocketChannel server;

    public RouterMarketCompletionHandler(AsynchronousServerSocketChannel server)
    {
        this.server = server;
    }

    public void completed(AsynchronousSocketChannel ch, Object att)
    {
        Router.handleMarketConnection(ch);
        server.accept(att, this);
    }

    public void failed(Throwable e, Object att)
    {
        System.out.println("connection failed");
        e.printStackTrace();
    }
}
