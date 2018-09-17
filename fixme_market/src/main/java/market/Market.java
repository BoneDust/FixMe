package market;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Market
{
    static AsynchronousSocketChannel marketSocket;
    static ExecutorService threadPool;
    static int id;
    final int PORT = 5000;
    final static int TIME_OUT_DURATION = 3000;
    //ArrayList<Instrument> instruments = new ArrayList<>();

    public static void main (String [] args)
    {
        new Market().runMarket();
    }

    private void runMarket()
    {
        Thread currentThread;
        threadPool = Executors.newFixedThreadPool(2, Executors.defaultThreadFactory());

        try
        {
            currentThread = Thread.currentThread();
            marketSocket = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", PORT);
            marketSocket.connect(hostAddress,null, new MarketCompletionHandler());
            try
            {
                currentThread.join();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static void startReading()
    {
        MarketReadMessageTask readMessageTask = new MarketReadMessageTask();
        threadPool.execute(readMessageTask);
    }

    public static void startSending(String message)
    {
        MarketSendMessageTask sendMessageTask = new MarketSendMessageTask(message);
        threadPool.execute(sendMessageTask);
    }

    public static void ReadWriteNonBlockingTimeOut()
    {
        try
        {
            Thread.sleep(TIME_OUT_DURATION);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< MarketReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }
}
