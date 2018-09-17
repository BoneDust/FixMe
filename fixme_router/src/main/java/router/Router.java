package router;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router
{

    static final int BROKER_PORT = 5000;
    static final int MARKET_PORT = 5001;
    static final int TIME_OUT_DURATION = 3000;
    static AsynchronousServerSocketChannel marketServer;
    static AsynchronousServerSocketChannel brokerServer;
    static ArrayList<AsynchronousSocketChannel> brokers;
    static ArrayList<AsynchronousSocketChannel> markets;
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
            brokers = new ArrayList<>();
            markets = new ArrayList<>();
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

    public static void startReading(AsynchronousSocketChannel socket)
    {
        RouterReadMessageTask readMessageTask = new RouterReadMessageTask(socket, false);
        threadPool.execute(readMessageTask);
    }

    public static void startSending(AsynchronousSocketChannel socket)
    {
        RouterSendMessageTask sendMessageTask = new RouterSendMessageTask(socket, true, "hello");
        threadPool.execute(sendMessageTask);
    }

    public static void handleMarketConnection(AsynchronousSocketChannel marketSocket)
    {
        RouterSendMessageTask sendMessageTask = new RouterSendMessageTask(marketSocket, false,"welcome market");
        threadPool.execute(sendMessageTask);
    }

    public static void handleBrokerConnection(AsynchronousSocketChannel brokerSocket)
    {
        RouterSendMessageTask sendMessageTask = new RouterSendMessageTask(brokerSocket, true, "welcome broker");
        RouterReadMessageTask readMessageTask = new RouterReadMessageTask(brokerSocket, true);
        threadPool.execute(sendMessageTask);
        threadPool.execute(readMessageTask);
    }

    public static void RouterReadWriteNonBlockingTimeOut()
    {
        try
        {
            Thread.sleep(TIME_OUT_DURATION);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< RouterReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }

}
