import java.util.ArrayList;
import java.util.List;

/*
    - Class gives a good demonstration of how to run x2 threads sequentially in a controlled manner.

        t1:     Enters producer sync block.
        t2:     Waits outside consumer sync block.
        t1:     Within loop adds x5 items to list, whilst calling notify() but does not affect any other thread directly (just 're-lets them know that they can run if have lock).
        t1:     Once condition met, t1 is suspended via wait() and releases lock.
        t2:     Enters consumer sync block.
        t2:     Removes items from list.
        t2:     Repeatedly calls notify() as it removes items from list.
                    However t1 cannot run given that t2 has lock.
                    Once t2 releases lock will t1 then run.
                    Therefore t1 was notified but because it was in a sync block it was unable to progress and left 'hanging' i.e. engine going but with hand brake on.
        t2:     Once condition met, t2 is suspended via wait() and releases lock.
        t1:     Repeats.
*/
public class Processor {
    
    private List<Integer> list = new ArrayList<>();
    
    private final int LIMIT     = 5;
    private final int BOTTOM    = 0;
    private final Object objLock = new Object();
    private int iValue = 0;

    void producer() throws InterruptedException
    {
        synchronized (objLock) {
        
            while(true) {
                
                if(list.size() == LIMIT) {
                    System.out.println("Waiting to remove items from the list");
                    objLock.wait();
                }
                else {
                    System.out.println("Adding item to list: " + iValue);
                    list.add(iValue);
                    iValue++;
                    objLock.notify();
                }
                
                Thread.sleep(500);
            }
        }
    }
    void consumer() throws InterruptedException
    {
        synchronized (objLock) {
        
            while(true) {
                
                if(list.size() == BOTTOM) {
                    System.out.println("Waiting to add items to the list");
                    objLock.wait();
                }
                else {
                    System.out.println("Removing item from list: " + list.remove(--iValue));
                    objLock.notify();
                }
                
                Thread.sleep(500);
            }
        }
    }
}
