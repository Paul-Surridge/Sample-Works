import java.util.logging.Level;
import java.util.logging.Logger;

class Worker implements Runnable
{
    /*
        - The volatile keyword is in order to ensure that the bTerminated variable is accessible from both the main application thread and the child
          thread i.e. it will not be placed in the child threads local cache.
    
        - The 'volatile' boolean flag is the recommended method of stopping a thread rather that the Thread.stop() method due to inherent instabilities i.e. possibly
          random allocation of the thread state in the local cache, becomes unavailable and does not stop when called.
    */
    private volatile boolean bTerminated;
    
    @Override
    public void run() {
        
        while(!bTerminated)
        {
            System.out.println("Worker Thread is running...");
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean getTerminated()
    {
        return bTerminated;
    }
    
    public void setTerminated(boolean bState)
    {
        bTerminated = bState;
    }
}
public class App
{
    public static void main(String[] args)
    {
        Worker objWorker = new Worker();
        Thread objThread = new Thread(objWorker);
        
        objThread.start();
                
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        objWorker.setTerminated(true);
        
        System.out.println("Worker Thread has stopped.");
    }
}
