import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchronisationBlocks {

    private static int     iCounter1 = 0;
    private static int     iCounter2 = 0;
    
    private static Object objLock1 = new Object();
    private static Object objLock2 = new Object();
    
    /*
        - How to run >1 threads within the same class.
        - Each class has only x1 Monitor/Lock, therefore:
        
            1. Not possible to have x2 Synchronised methods run concurrently within the same object.
            2. Could not have x2 separate threads run x2 Synchronised methods on same object.
    
    //Synchronised block used as opposed to a Synchronised method whereby passing in the local class reference:

        public static void add(){
            synchronized (SynchronisationBlocks.class){
                iCounter1++;
            }
        }

    */
    
    //Synchronise/lock the objects rather than the actual encapsulating class
    public static void add1()
    {
        synchronized (objLock1){
            for(int i = 0 ; i< 1000 ; i++)
                iCounter1++;
        }
    }
    
    public static void add2()
    {
        synchronized (objLock2){
            for(int i = 0 ; i< 1000 ; i++)
                iCounter2++;
        }
    }
    
    public static void main(String[] args)
    {
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                add1();
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                add2();
            }
        });
    
        t1.start();
        t2.start();
    
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SynchronisationBlocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Thread 1: " + iCounter1 + " Thread 2: " + iCounter2);
    }
}
