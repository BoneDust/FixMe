package broker;

import broker.models.Instrument;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class
Broker
{
    static AsynchronousSocketChannel brokerSocket;
    static ExecutorService threadPool;
    static int id = 0;
    final int PORT = 5000;
    final static int TIME_OUT_DURATION = 5000;
    static ArrayList<Instrument> instruments;
    static double money = 1000.00;
    
    public static void main (String [] args)
    {
        new Broker().runBroker();
    }

    private void runBroker()
    {
        Thread currentThread;
        threadPool = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        try
        {
            currentThread = Thread.currentThread();
            brokerSocket = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", PORT);
            brokerSocket.connect(hostAddress,null, new BrokerCompletionHandler());
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
        threadPool.execute(new BrokerReadMessageTask());
    }

    public static void startSending()
    {
        threadPool.execute(new BrokerSendMessageTask());
    }

    public static void ReadWriteNonBlockingTimeOut()
    {
        try
        {
            Thread.sleep(TIME_OUT_DURATION);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< BrokerReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }


}