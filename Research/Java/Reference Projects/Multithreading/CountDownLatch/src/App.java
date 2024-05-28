import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - CountdownLatch:

        - .countDown():     Decrements the counter within the object. Counter defined when object created.
        - .await():         Execution is blocked at this point until countdown reaches zero, at which point resumes.

        - x5 Runnable Processor objects are immediately instantiated and placed on a single thread.
        - Each thread:

            - Decrements the CountdownLatch.
            - Rejoins the application thread.
            - Blocked at .await().

        - Once the CountdownLatch reaches zero and thread at .await() begins running.
        - One-Shot object, once count down complete the object can no longer be used (use Cyclic Barrier for multiple use).
        - NB:

            - It appears that .await() may also act as a .join() i.e. it only starts running once all x5 threads have rejoined.
            - Such that the CountdownLatch state if only evaluated upon .await() being executed (rather than immediately start running as soon as .countDown() is called.
            - Thread ID of 0 possibly may cause issues hence needing to be incremented +1 when being created.
*/

class Processor implements Runnable {
    private int id;
    private CountDownLatch objCountDownLatch;

    public Processor(int id, CountDownLatch objCountDownLatch) {
        this.id = id;
        this.objCountDownLatch = objCountDownLatch;
    }

    @Override
    public void run() {
        completeAssignedRole();
        objCountDownLatch.countDown();
        System.out.println("Countdown: " + id);
    }

    private void completeAssignedRole() {
        System.out.println("Thread with id: " + id + " is now running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

public class App
{
    public static void main(String[] args)
    {
        ExecutorService     objExecutorService  = Executors.newSingleThreadExecutor();
        CountDownLatch      objCountDownLatch   = new CountDownLatch(5);
        
        for(int i = 0 ; i<5 ; i++)
            objExecutorService.execute(new Processor(i+1, objCountDownLatch));

        try {
            objCountDownLatch.await();
            System.out.println("Await triggered");
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("All Threads Complete");
        
        objExecutorService.shutdown();
    }   
}
