package router;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router
{

    static final int BROKER_PORT = 5000;
    static final int MARKET_PORT = 5001;
    static final int TIME_OUT_DURATION = 3000;
    static AsynchronousServerSocketChannel marketServer;
    static AsynchronousServerSocketChannel brokerServer;
    static ExecutorService threadPool;
    static Thread currentThread;

    public static void main(String[] args)
    {
        new Router().runRouter();
    }

    private static void runRouter()
    {
        try
        {
            threadPool  = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
            marketServer = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("localhost", MARKET_PORT));
            brokerServer = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("localhost", BROKER_PORT));;
            currentThread = Thread.currentThread();
            marketServer.accept(null, new RouterMarketCompletionHandler(marketServer));
            brokerServer.accept(null, new RouterBrokerCompletionHandler(brokerServer));
            try
            {
                currentThread.join();
            }
            catch (InterruptedException ex)
            {
            }
        }
        catch (Exception ex)
        {
            System.out.println("Router IOException occured\n\n");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public static void handleMarketConnection(AsynchronousSocketChannel marketSocket)
    {

    }

    public static void handleBrokerConnection(AsynchronousSocketChannel brokerSocket)
    {

    }

}
