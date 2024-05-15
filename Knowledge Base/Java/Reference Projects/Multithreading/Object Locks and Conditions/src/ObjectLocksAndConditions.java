import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectLocksAndConditions {

    /*
        Locks
    
            - Can be extended
            - Can be made fair e.g. the longest waiting thread could be made to get the lock next
            - Can prevent thread starvation
            - Can check whether the lock is being held by a thread or not
            - Can obtain a lost of threads waiting on the lock
            - Must always remember/ensure that the .unlock() is called to prevent deadlock/hanging, the .unlocked must always be wrapped in Try/Catch/Finally
    
        Sync Blocks
    
            - Unfair by default, arbitrarily chose which thread once lock becomes available
            - Do not require Try/Catch block        
    */
    public static void main(String[] args)
    {
        Processor objProcessor = new Processor();
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){               
                try {
                    objProcessor.producer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObjectLocksAndConditions.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    objProcessor.consumer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObjectLocksAndConditions.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //t1 does indeed start prior to t2 - there is no ambiguity in that.
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ObjectLocksAndConditions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Finished: iCounter ");
    }   
}
