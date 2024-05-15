import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    static int iCounter;
    static AtomicInteger aiCounter = new AtomicInteger(0);
    
    public static void main(String[] args) {
        
        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                increment();
            }
        });
        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                increment();
            }
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //System.out.println(iCounter);
        System.out.println(aiCounter);
    }
    
    /*If 'synchronized' not used then the x2 threads cause unpredictable behaviour whereby iCounter comes out as any number between 10000 and 20000.
    public synchronized static void increment()
    {
        for(int i = 0 ; i<10000 ; i++)
            iCounter++;
    }*/
    
    //Alternatively if the counter is an AtomicInteger it uses low level synchronisation in order to ensure only x1 thread at a time can perform a function on it i.e.
    //only x1 thread may have its lock at any one time.
    
    //The AtomicInteger behaves as an atomic operation i.e. where it is so 'small' that it cannot be broken into smaller operations.
    //They are generally a convenience mechanism so do not need to use/worry about synchronisation.
    public static void increment()
    {
        for(int i = 0 ; i<10000 ; i++)
            aiCounter.getAndIncrement();
    }
}
