package market;

import market.models.Instrument;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Market
{
    static AsynchronousSocketChannel marketSocket;
    static ExecutorService threadPool;
    static int id = 0;
    final int PORT = 5001;
    final static int TIME_OUT_DURATION = 3000;
    static ArrayList<Instrument> instruments;
    static double money = 1000.0;

    public static void main (String [] args)
    {
        new Market().runMarket();
    }

    private void runMarket()
    {
        Thread currentThread;
        generateInstruments();
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


    public static void startReading(boolean isFirstMessage)
    {
        threadPool.execute(new MarketReadMessageTask(isFirstMessage));
    }

    public static void startSending(String message)
    {
        threadPool.execute(new MarketSendMessageTask(message));
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

    private static void generateInstruments()
    {
        String[] names = {"IMACs","EMACs","DMACs"};
        Random random = new Random();
        for (String name : names)
        {
            int quantity = random.nextInt(21);
            double price = random.nextDouble() * 250;
            instruments.add(new Instrument(name, quantity, price));
        }
    }
}
