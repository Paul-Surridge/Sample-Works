import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class Task implements Runnable
{
    private int iID;
    
    public Task(int iID) {
        this.iID = iID;
    }

    @Override
    public void run() {
        
        long lDuration;
        
        System.out.println("Task Running - ID: " + iID + " with Thread Name: " + Thread.currentThread().getName());
        
        lDuration = (long) (Math.random() * 5);
        
        try {
            //Alternate method of making a thread sleep
            TimeUnit.SECONDS.sleep(lDuration);
        } catch (InterruptedException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public class App
{
    public static void main(String[] args)
    {
        //The interface ExecutorService is declared and uses the static factory method newSingleThreadExecutor() within Executors to create
        //and assign a SingleThreadExecutor reference to the interface.
        ExecutorService objExecutorService = Executors.newSingleThreadExecutor();
        
        //The calls to .execute() cause the x5 tasks to be queued one after the other i.e. all x5 do not spin off and run concurrently at same time,
        //the SingleThreadExecutor only moves onto the next one once the last one has finished.
        
        //Execution does not wait/halt at .execute() for each thread to finish, all x5 are 'loaded' and queued within the SingleThreadExecutor
        //and all run separately to the main application thread.
        for(int i = 0 ; i<5 ; i++)
            objExecutorService.execute(new Task(i));
        
        //It will be noted that the below is sent to the console immediately once all x5 tasks have been loaded (and not once all x5 threads have finished)
        System.out.println("For loop complete");
        
        //The manual shutdown is required in order to ensure all threads associated are shutdown.
        objExecutorService.shutdown();
        
        //As shown this is a lot more convenient, easy to use and less prone to error than trying to manually use wait(), notify() etc...
    }
}
