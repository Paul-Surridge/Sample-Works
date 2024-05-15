import java.util.logging.Level;
import java.util.logging.Logger;

public class Synchronisation {

    private static int iCounter = 0;
    
    public static synchronized void incrementCounter()
    {
        iCounter++;
    }
    public static void process()
    {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                for(int i = 0 ; i<100 ; i++)
                    incrementCounter();
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                for(int i = 0 ; i<100 ; i++)
                    incrementCounter();
            }
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Synchronisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(iCounter);
    }
    public static void main(String[] args)
    {
        process();
    }   
}
