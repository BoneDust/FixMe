package Broker;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Broker
{
    static AsynchronousSocketChannel brokerSocket;
    static ExecutorService threadPool;
    static int id;
    final int PORT = 5000;
    final static int TIMEOUT = 3000;
    
    public static void main (String [] args)
    {
        new Broker().runBroker();
    }

    private void runBroker()
    {
        Thread currentThread;
        threadPool = Executors.newFixedThreadPool(2, Executors.defaultThreadFactory());

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
        BrokerReadMessageTask readMessageTask = new BrokerReadMessageTask();
        threadPool.execute(readMessageTask);
    }

    public static void startSending()
    {
        BrokerSendMessageTask sendMessageTask = new BrokerSendMessageTask();
        threadPool.execute(sendMessageTask);
    }

    public static void ReadWriteNonBlockingTimeOut()
    {
        try
        {
            Broker.class.wait(TIMEOUT);
        }
        catch (InterruptedException ex)
        {
            System.out.println("\n\t<< BrokerReadWriteNonBlockingException >> \n");
            ex.printStackTrace();
        }
    }
}