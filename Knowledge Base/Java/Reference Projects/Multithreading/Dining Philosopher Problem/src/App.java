import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    private static int findRightChopstick(int iChopstick)
    {
        if(iChopstick == 0) return 4;
        else                return (iChopstick-1);
    }
    public static void main(String[] args) throws InterruptedException
    {
        ExecutorService objExecutorService  = null;
        Philosopher[]   Philosophers        = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
        Chopstick[]     Chopsticks          = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

        try {
            
            for(int i = 0 ; i<Constants.NUMBER_OF_CHOPSTICKS ; i++)
                Chopsticks[i] = new Chopstick(i);
            
            objExecutorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);
            
            for(int i=0 ; i<Constants.NUMBER_OF_PHILOSOPHERS ; i++) {
                Philosophers[i] = new Philosopher(i, Chopsticks[i], Chopsticks[findRightChopstick(i)]);
                objExecutorService.execute(Philosophers[i]);
            }
            
            Thread.sleep(Constants.SIMULATION_DURATION);
            
            for(Philosopher p : Philosophers)
                p.setFull(true);
        }
        finally
        {
            objExecutorService.shutdown();
            
            while(!objExecutorService.isTerminated())
                Thread.sleep(1000);
            
            for(Philosopher p : Philosophers)
                System.out.println(p + " eats " + p.getEatCounter());
        }
    }
}
