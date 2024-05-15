import java.util.concurrent.Exchanger;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    - Decide which object type to pass/exchange between threads.
    - Include the type within the declaration of the Exchange<>
    - Pass the reference to the Exchange to both threads (or as many threads as required).

    - t1:       Upon encountering .exchange().
    - t1:       If no other thread already entered the Exchange via its own .exchange() then current thread halts at this position (it sits/waits within the Exchange).
    - t1:       If another thread is already halted/waiting in Exchange and then objects are exchanged.
    - t1/t2:    Both threads resume.
*/

class Thread_1 implements Runnable {
    
    private int                 iCounter = 1;
    private Exchanger<Integer>  objExchanger;

    public Thread_1(Exchanger<Integer> objExchanger) {
        this.objExchanger = objExchanger;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread_1.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                iCounter = objExchanger.exchange(iCounter);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread_1.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Thread 1 - iCounter: " + iCounter);
        }
    }   
}
class Thread_2 implements Runnable {
    
    private int                 iCounter = 0;
    private Exchanger<Integer>  objExchanger;

    public Thread_2(Exchanger<Integer> objExchanger) {
        this.objExchanger = objExchanger;
    }

    @Override
    public void run() {
        while(true)
        {
            try {               
                iCounter = objExchanger.exchange(iCounter);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Thread 2 - iCounter: " + iCounter);
        }
    }   
}
public class App {

    public static void main(String[] args)
    {
        Exchanger<Integer> objExchanger = new Exchanger<>();
        
        new Thread(new Thread_1(objExchanger)).start();
        new Thread(new Thread_2(objExchanger)).start();
    }
}
