import java.util.logging.Level;
import java.util.logging.Logger;

public class Counter extends Thread
{
    //Volatile ensures that the variable is stored in memory (RAM) rather than in any potential local Cache associated with the core that the thread is running on.
    private volatile String sName;
    
    Counter(String sName)
    {
        this.sName = sName;
    }
    
    @Override
    public void run()
    {
        for(int i = 0 ; i<10 ; i++){
            System.out.println(sName + ": " + i);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Counter.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
    }
}
