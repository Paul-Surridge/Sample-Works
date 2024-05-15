import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectLocks {

    public static int   iCounter    = 0;
    public static Lock  objLock     = new ReentrantLock();
    
    public static void increment()
    {       
        /*
            - Opposed to using the implicit lock built into every object.
            - Use an explicit Lock object to 'lock' and 'unlock'.
            - One of the benefits of Lock objects is that the control of teh locking mechanism can be called anywhere within the scope of the code (as opposed to a fix sync block).
        
        NB:
            - To prevent any possible deadlock or hanging thread.
            - Place unlock within a Try/Catch so that the .unlock is always called even upon an exception being thrown.
        */
        
        objLock.lock();
        
        try {
            for(int i = 0 ; i<1000 ; i++)
                iCounter++;
        }finally {
            objLock.unlock();
        }
    }
    
    public static void main(String[] args)
    {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){               
                increment();
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                increment();
            }
        });
        
        //t1 does indeed start prior to t2 - there is no ambiguity in that.
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ObjectLocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Finished: iCounter " + iCounter);
    }
}
