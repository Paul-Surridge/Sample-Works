import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - Illustrates the use of wait() notify() in order to implement dual work streams within the same object.
    - Ideal when x2 (or more) streams which regularly get held up.

    In General:
    
        - No x2 synchronized blocks may be entered at same time on same object.
        - When locked:
    
            - t1:   Enters own sync block
            - t2:   Encountering sync block 2:
            - t2:   Execution is automatically halted just outside sync block 2.
            - t1:   Upon leave the block 1 or itself is wait(), object lock is lifted.
            - t2:   Automatically started and enters sync block 2.

        - Only way to release lock:

            1. Exit sync block.
            2. Call wait().
        
    Available Methods
    
        - this.wait()       Suspends the current thread and releases lock on object, waits for notify()
        - this.notify()     Wakes any other threads which are currently waiting.
                            But does not necessarily make them start running, only 'lets them know' that they can run once lock is available to them.
                            If notify() is called and other threads are also in monitor i.e. also in other Sync blocks

                                Those other threads will not run until this thread releases monitor, regardless if notify() has been called.
                                Notify() can be considered as telling other suspended threads in monitor to 'turn engines on' but only able to release hand brake once lock released.
    
        - Thread.sleep()    Does not release lock if in sync block.
        - Thread.yield()    Do not use - debugging only (if ever)
*/

class Controller
{
    int     iValue;
    boolean bValueSet, bRunning;
    
    Controller()
    {
        iValue      = 0;
        bValueSet   = false;
        bRunning    = true;
    }
    
    synchronized void put(int iValue)
    {
        while(bValueSet)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        System.out.println("Put: " + iValue);
        this.iValue     = iValue;
        bValueSet       = true;
        notify();
        
        bRunning = (iValue < 10);
    }
    
    synchronized int get()
    {
        while(!bValueSet)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        System.out.println("Get: " + iValue);
        bValueSet       = false;
        notify();
        
        bRunning = (iValue < 10);
        
        return iValue;
    }
    
    boolean isRunning(){
        return bRunning;
    }
}
public class WaitAndNotify {

    public static void main(String[] args)
    {
        Controller objController = new Controller();
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                int i = 0;
                    
                while(objController.isRunning()){
                    objController.put(i++);
                }
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                while(objController.isRunning()){
                    objController.get();
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
            Logger.getLogger(WaitAndNotify.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Finished");
    }    
}
