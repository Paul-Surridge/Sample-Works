import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - Cyclic Barrier:

        - Similar to CountdownLatch however:

            - Reusable via .reset().
            - Each thread is halted at .await().
            - Until the last (5th) thread triggers .await().
            - Once the 'barrier' is reached (where all threads are complete)
            - The last thread runs the Runnable() supplied in its Constructor.

    - All x5 threads of course end/rejoined the application thread once their own respective run() is completed within their own respective Runnable() instances.
    - CyclicBarrier passed to each instance of Processor so that .await() can be called once each thread ends.
    - Usage:

            - x5 tasks need to run and complete before 6th is run.
            - For example download x5 items then run them...

    - NB: It is possible for code to run after the final 5th .await() is called once the CyclicBarrier own 'barrier' task has completed running.
*/

class Processor implements Runnable {
    private int             id;
    private CyclicBarrier   objCyclicBarrier;

    public Processor(int id, CyclicBarrier objCyclicBarrier)
    {
        this.id = id;
        this.objCyclicBarrier = objCyclicBarrier;
    }
    
    @Override
    public void run()
    {
        completeRole();
    }
    
    private void completeRole()
    {
        System.out.println("Thread ID: " + id + " begins...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Thread ID: " + id + " ends...");
        
        try {
            //All threads collect here...
            objCyclicBarrier.await();
            
            System.out.println("After .await() Thread ID: " + id);
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
}
public class App
{
    public static void main(String[] args)
    {
        ExecutorService     objExecutorService  = Executors.newFixedThreadPool(5);
        CyclicBarrier       objCyclicBarrier    = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("All threads completed");
            }
        });

        for(int i = 0 ; i<5 ; i++)
            objExecutorService.execute(new Processor(i+1, objCyclicBarrier));

        objExecutorService.shutdown(); 
    }
}
