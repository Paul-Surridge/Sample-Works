import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - BlockingQueue<Integer> objBlockingQueue = new ArrayBlockingQueue<>(10);

        - Declares a BlockingQueue for integers of size 10.
        - Maximum of x10 items in queue at any one time.
        - Of course references to other objects allowable.

    - If attempting to put something on full queue:         That thread is halted at .put().
    - If attempting to take something from empty queue:     That thread is halted at .take().

    - Threadsafe.
    - Synchronized therefore no need to manually called wait() notify() etc...
    - The 'Blocking' is reference to the blocking nature of the queue such that threads are halted until there is something to either .put() or .take().
*/
class Producer implements Runnable {

    private BlockingQueue<Integer> objBlockingQueue;

    public Producer(BlockingQueue<Integer> objBlockingQueue) {
        this.objBlockingQueue = objBlockingQueue;
    }
    
    public void run() {
        int iCounter = 0;
        
        while(true) {
            try {
                objBlockingQueue.put(iCounter);
                System.out.println("Producer: Putting item on queue: " + iCounter);
                iCounter++;
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
class Consumer implements Runnable {

    private BlockingQueue<Integer> objBlockingQueue;

    public Consumer(BlockingQueue<Integer> objBlockingQueue) {
        this.objBlockingQueue = objBlockingQueue;
    }
    
    public void run() {
        while(true) {
            try {
                System.out.println("Consumer: Taking item from queue: " + objBlockingQueue.take());
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
public class App
{
    public static void main(String[] args)
    {
        BlockingQueue<Integer> objBlockingQueue = new ArrayBlockingQueue<>(10);
        
        Producer objProducer = new Producer(objBlockingQueue);
        Consumer objConsumer = new Consumer(objBlockingQueue);
        
        new Thread(objProducer).start();
        new Thread(objConsumer).start();
    }
}
