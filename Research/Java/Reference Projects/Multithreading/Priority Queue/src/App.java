import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - PriorityQueue is an unbounded concurrent queue.
    - PriorityQueue is a threadsafe/synchronized priority queuing mechanism.
    - It naturally sorts objects depending on how defined in the parameterised type compareTo().
    - NB: If try to take from empty queue then the thread halts at that .take().
*/

class Producer implements Runnable {
    private BlockingQueue<String> objPriorityQueue;

    public Producer(BlockingQueue<String> objPriorityQueue) {
        this.objPriorityQueue = objPriorityQueue;
    }

    @Override
    public void run() {
        try {
            objPriorityQueue.put("G");
            objPriorityQueue.put("A");
            objPriorityQueue.put("D");
            objPriorityQueue.put("C");
            objPriorityQueue.put("P");
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
class Consumer implements Runnable {
    private BlockingQueue<String> objPriorityQueue;

    public Consumer(BlockingQueue<String> objPriorityQueue) {
        this.objPriorityQueue = objPriorityQueue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(objPriorityQueue.take());
            System.out.println(objPriorityQueue.take());
            System.out.println(objPriorityQueue.take());
            System.out.println(objPriorityQueue.take());
            System.out.println(objPriorityQueue.take());
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public class App
{
    public static void main(String[] args)
    {
        BlockingQueue<String> objPriorityQueue = new PriorityBlockingQueue<>();
        
        new Thread(new Producer(objPriorityQueue)).start();
        new Thread(new Consumer(objPriorityQueue)).start();
    }
}
