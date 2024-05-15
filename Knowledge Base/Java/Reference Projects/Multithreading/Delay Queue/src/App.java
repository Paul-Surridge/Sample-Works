import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - The elements can only be .take() once they have been on the queue for the duration defined within each element.
    - The head of the queue is always the one that expired furthest in the past.
    - If the next element at head of queue to be taken has not yet expired then execution is halted at the .take().

    - BlockingQueue<DelayedQueueElement>:

        - Generic Interface which accepts any object.
        - Interface variable is assigned a DelayQueue<> object (which implements the BlockingQueue<T> interface).
        - objDelayQueue:

            - Is passed x3 objects which extend Delayed.
            - The intended use being that any object can be passed, encompassed within a DelayQueue<> so long as it implemented Delay interface.
            - Thus ensuring that the required compareTo() and getDelay() are implemented for DelayQueue<> to use.

    - compareTo()

        - This is used by DelayQueue<> in order to sort its elements.
        - So that the one with the element which expired first is at the head of the queue when popped/taken.

    - DelayedQueueElement:

        - The necessary compareTo() and getDelay() are overridden.

    - All x3 elements are taken from DelayQueue<>
    - Can become an effective 'Stepper' (Simpl).
    - It is presumed that the DelayQueue<> is synchronized i.e. threadsafe.
*/

class DelayedQueueElement implements Delayed {
    
    private long    lDuration;
    private String  sMessage;

    public DelayedQueueElement(long lDuration, String sMessage) {
        this.lDuration = System.currentTimeMillis() + lDuration;
        this.sMessage = sMessage;
    }

    @Override
    public int compareTo(Delayed o) {
        
        if( lDuration < ((DelayedQueueElement) o).getDuration())  return -1;
        if( lDuration > ((DelayedQueueElement) o).getDuration())  return 1;
        return 0;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(lDuration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return sMessage;
    }

    public long getDuration() {
        return lDuration;
    }

    public void setDuration(long lDuration) {
        this.lDuration = lDuration;
    }

    public String getMessage() {
        return sMessage;
    }

    public void setMessage(String sMessage) {
        this.sMessage = sMessage;
    }
}
public class App
{
    public static void main(String[] args)
    {
        BlockingQueue<DelayedQueueElement> objDelayQueue = new DelayQueue<>();

        try {
            objDelayQueue.put(new DelayedQueueElement(1000, "Element 1"));
            objDelayQueue.put(new DelayedQueueElement(5000, "Element 2"));
            objDelayQueue.put(new DelayedQueueElement(4000, "Element 3"));
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*Remove the .sleep() for the DelayQueue<> to behave like a 'stepper' in Simpl Windows
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        while(!objDelayQueue.isEmpty())
        {
            try {
                //If the next element at head of queue to be taken has not yet expired then execution is halted here.
                System.out.println(objDelayQueue.take());
                System.out.println("Test");
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
