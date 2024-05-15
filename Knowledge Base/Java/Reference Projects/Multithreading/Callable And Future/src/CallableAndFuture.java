import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - Callable() returns a String.
    - Call() within Callable is automatically run once submitted to ExecutorService.
    - Allowing for thread to return a string to the Main thread.
    - The return string:

        - Is placed into Future<> object.
        - Then extracted via get().

    - The first x2 threads are run.
    - The remaining x3 threads need to wait for the first x2 before running.
    - The final 5th thread is run once initial x4 are finished.

    - Shutdown()

        - Prevents any further threads being submitted to the ExecutorService
        - Will shut down once all threads once finished.
        - Will not interrupt any running threads.

    - NB: Thread ID of 0 possibly may cause issues hence needing to be incremented +1 when being created.
*/
class Processor implements Callable<String> {

    private int id;

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return "id: " + id;
    }  
}
public class CallableAndFuture {

    public static void main(String[] args)
    {
        ExecutorService objExecutorService  = Executors.newFixedThreadPool(2);
        List<Future<String>> list           = new ArrayList<>();
        
        for(int i=0 ; i<5 ; i++) {
            Future<String> objFuture = objExecutorService.submit(new Processor(i+1));
            list.add(objFuture);
        }
        
        for(Future<String> future : list)
        {
            try {
                System.out.println(future.get());
            } catch (InterruptedException ex) {
                Logger.getLogger(CallableAndFuture.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(CallableAndFuture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        objExecutorService.shutdown();
    }
}
