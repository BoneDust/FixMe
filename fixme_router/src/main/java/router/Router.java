package router;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router
{

    private static final int BROKER_PORT = 5000;
    private static final int MARKET_PORT = 5001;
    static final int TIME_OUT_DURATION = 3000;
    private static int currentBrokerId = 100000;
    private static int currentMarketId = 500000;
    private static AsynchronousServerSocketChannel marketServer;
    private static AsynchronousServerSocketChannel brokerServer;
    static Map<Integer, AsynchronousSocketChannel> brokers;
    static Map<Integer, AsynchronousSocketChannel> markets;
    private static ExecutorService threadPool;
    private static Thread currentThread;

    public static void main(String[] args)
    {
        new Router().runRouter();
    }

    private static void runRouter()
    {
        try
        {
            brokers = new HashMap<>();
            markets = new HashMap<>();
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
                System.out.println("Router idling interrupted");
                System.exit(0);
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
        threadPool.execute(new RouterSendMessageTask(marketSocket, Integer.toString(currentMarketId)));
        markets.put(currentMarketId, marketSocket);
        currentMarketId++;
    }

    public static void handleBrokerConnection(AsynchronousSocketChannel brokerSocket)
    {
        threadPool.execute(new RouterSendMessageTask(brokerSocket, Integer.toString(currentBrokerId)));
        threadPool.execute(new RouterReadMessageTask(brokerSocket, true));
        brokers.put(currentBrokerId, brokerSocket);
        currentBrokerId++;
    }

}
