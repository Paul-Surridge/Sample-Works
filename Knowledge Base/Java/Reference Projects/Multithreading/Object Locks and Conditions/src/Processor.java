import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Processor {
    
    private Lock        objLock         = new ReentrantLock();
    private Condition   objCondition    = objLock.newCondition();
    
    /*
        - In order to implement the same functionality as the wait() and notify()
        - Associate the Lock object with a Condition object
        - Call .await() and .signal() accordingly
    
       Similarly:
    
        - t2:   Unable to enter objLock until t1 has .await()
        - t2:   .signal() causes t1 to finish
    
        - If .lock() is called multiple times, then .unlock() has to be called the same number of times
        - Subject to testing
    */
    public void producer() throws InterruptedException {
        
        objLock.lock();
        
        System.out.println("Producer Method - Stage 1");
        
        Thread.sleep(1000);
        
        objCondition.await();
        
        System.out.println("Producer Method - Stage 2");
    }
    
    public void consumer() throws InterruptedException {
        
        objLock.lock();
        
        System.out.println("Consumer Method - Stage 1");
        
        Thread.sleep(5000);
        
        System.out.println("Consumer Method - Stage 2");
        
        objCondition.signal();
        
        objLock.unlock();
    }
}
