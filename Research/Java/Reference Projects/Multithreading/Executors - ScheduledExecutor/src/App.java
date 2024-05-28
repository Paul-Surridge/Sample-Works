import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Process implements Runnable
{
    private static int iID;
    
    @Override
    public void run() {
        System.out.println("Process " + iID + " run");
        iID++;
    }
}
public class App
{
    public static void main(String[] args)
    {
        ScheduledExecutorService objExecutorService = Executors.newScheduledThreadPool(1);

        objExecutorService.scheduleAtFixedRate(new Process(), 1, 3, TimeUnit.SECONDS);
        
        //The use of scheduled executor services is a lot more convenient, easy to use than trying to create timers, timer tasks etc...
    }
}
