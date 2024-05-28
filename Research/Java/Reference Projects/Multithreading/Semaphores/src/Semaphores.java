import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

enum Downloader {
    
    INSTANCE;
    
    private Semaphore objSemaphore = new Semaphore(3, true);
    /*
        3:      Number of Permits
        true:   Ensure fairness i.e. that the longest waiting thread obtains lock once current thread releases lock
    */
    
    public void downloadData() {
        
        try {
            objSemaphore.acquire();     //The current thread acquires the next available permit
            download();
        } catch (InterruptedException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            objSemaphore.release();     //The current thread releases the permit ready to be used by another thread
        }
    }
    
    private void download() {
        System.out.println("Downloading data from the web...");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

public class Semaphores {

    /*
        - objExecutorService is created
        - It instantly spins off x12 threads all trying to download data immediately.
        - However objSemaphore restricts only x3 at a time may proceed.
        - The other x9 threads are waiting at acquire().
        - Once each of the x3 initial thread completes and calls .release().
        - The next thread is allowed to proceed until all x12 thread have completed.
    
        NB: ENUM are more threadsafe than a traditional class/method and confirm to the Singleton design pattern (to be researched)
    */
    public static void main(String[] args)
    {
    
        ExecutorService objExecutorService = Executors.newCachedThreadPool();
        /*
            - Very Useful
            - Enable/allow the instantiation of multiple threads without the need to manually create each other.
        */
        
        for(int i =  0 ; i<12 ; i++) {
            objExecutorService.execute(new Runnable() {
                public void run() {
                    Downloader.INSTANCE.downloadData();
                }
            });
        }
        
        /*NB:
        
            - The below is automatically run as the first output to console (rather than the Downloads)
            - ExecutorService can be thought of as 'Waits()' in Simpl+
        */
        System.out.println("Finished");
    }    
}
