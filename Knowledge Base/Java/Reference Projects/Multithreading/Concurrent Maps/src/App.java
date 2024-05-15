import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - ConcurrentHashMap is a threadsafe Hash Map.
    - get():

        - If the Key is not recognised then return null.
        - Returns the value associated with the key supplied as argument within .get().

    - Typical characteristic of Hash Map is that retrieval time is same for all elements.

    NB:
        - Anything which is synchronized will always run slower.
        - Should only be used when necessary.
        - Do not use if only running a single thread.
*/

class Producer implements Runnable {
    
    private ConcurrentMap<String, Integer> objConcurrentMap;

    public Producer(ConcurrentMap<String, Integer> objConcurrentMap) {
        this.objConcurrentMap = objConcurrentMap;
    }

    @Override
    public void run() {
        try {
            objConcurrentMap.put("B", 1);
            Thread.sleep(1000);
            objConcurrentMap.put("H", 2);
            Thread.sleep(1000);
            objConcurrentMap.put("F", 3);
            objConcurrentMap.put("A", 4);
            Thread.sleep(1000);
            objConcurrentMap.put("E", 5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
class Consumer implements Runnable {
    
    private ConcurrentMap<String, Integer> objConcurrentMap;

    public Consumer(ConcurrentMap<String, Integer> objConcurrentMap) {
        this.objConcurrentMap = objConcurrentMap;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(objConcurrentMap.get("F"));
        System.out.println(objConcurrentMap.get("H"));
        System.out.println(objConcurrentMap.get("B"));
        System.out.println(objConcurrentMap.get("E"));
        System.out.println(objConcurrentMap.get("A"));   
    }   
}
public class App {

    public static void main(String[] args)
    {
        ConcurrentMap<String, Integer> objConcurrentMap = new ConcurrentHashMap<>();
        
        new Thread(new Producer(objConcurrentMap)).start();
        new Thread(new Consumer(objConcurrentMap)).start();
    }
}
