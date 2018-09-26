package market;

import market.models.Instrument;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        instruments = new ArrayList<>();
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

    public static void ReadWriteNonBlockingTimeOut(Future future)
    {
        long startTime = Calendar.getInstance().getTimeInMillis();
        while (true)
        {
            long execTime = Calendar.getInstance().getTimeInMillis();
            long elapse = execTime - startTime;
            if (elapse >= TIME_OUT_DURATION || future.isDone())
                break;
        }
    }

    private static void generateInstruments()
    {
        String[] names = {"IMACS","EMACS","DMACS"};
        Random random = new Random();
        for (String name : names)
        {
            int quantity = random.nextInt(21);
            double price = random.nextDouble() * 250;
            price = (double)Math.round(price * 100d) / 100;
            instruments.add(new Instrument(name, quantity, price));
        }
    }
}
