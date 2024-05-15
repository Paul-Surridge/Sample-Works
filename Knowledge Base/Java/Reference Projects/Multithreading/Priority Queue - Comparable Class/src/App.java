import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - PriorityQueue is an unbounded concurrent queue.
    - PriorityQueue is a threadsafe/synchronized priority queuing mechanism.
    - It naturally sorts objects depending on how defined in the paramatersed type compareTo().
    - NB: If try to take from empty queue then the thread halts at that .take().
*/
class Producer implements Runnable {
    private BlockingQueue<Person> objPriorityQueue;

    public Producer(BlockingQueue<Person> objPriorityQueue) {
        this.objPriorityQueue = objPriorityQueue;
    }

    @Override
    public void run() {
        try {
            objPriorityQueue.put(new Person(28, "Paul"));
            objPriorityQueue.put(new Person(18, "Rob"));
            objPriorityQueue.put(new Person(54, "Noel"));
            objPriorityQueue.put(new Person(10, "Bronwyn"));
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
class Person implements Comparable<Person> {

    private int     iAge;
    private String  sName;

    public Person(int iAge, String sName) {
        this.iAge = iAge;
        this.sName = sName;
    }

    public String getName() {
        return sName;
    }

    @Override
    public int compareTo(Person o) {
        return sName.compareTo(o.getName());
    }
    @Override
    public String toString() {
        return sName;
    }
}
class Consumer implements Runnable {
    private BlockingQueue<Person> objPriorityQueue;

    public Consumer(BlockingQueue<Person> objPriorityQueue) {
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
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public class App {

    public static void main(String[] args)
    {
        BlockingQueue<Person> objPriorityQueue = new PriorityBlockingQueue<>();
        
        new Thread(new Producer(objPriorityQueue)).start();
        new Thread(new Consumer(objPriorityQueue)).start();
    }
}
