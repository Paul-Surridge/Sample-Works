import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - java.util.concurrent.ExecutorService:     Provides convenient method of creating multiple threads without need to instantiate multiple thread objects.
    - Dynamically re-uses threads.
    - Allocate a thread pool with the ExecutorService.
    - If number of threads fixed then need to wait for a thread to become available.
    - .submit()     Runnable and Callable Interfaces.
    - .execute():   Runnable Interface Only.
    - Both methods adds new thread to pool.
*/
class Processor implements Runnable {
    @Override
    public void run() {
        for(int i = 0 ; i<10 ; i++) {
            System.out.println(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
public class App
{
    public static void main(String[] args)
    {
        java.util.concurrent.ExecutorService objExecutorService = Executors.newFixedThreadPool(5);
        
        for(int i = 0 ; i<5 ; i++)
            objExecutorService.submit(new Processor());
        
        objExecutorService.shutdown();
    }   
}
