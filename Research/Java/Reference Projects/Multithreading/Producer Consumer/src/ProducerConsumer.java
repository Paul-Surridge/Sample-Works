import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumer
{
    static Processor objProcessor = new Processor();
    
    public static void main(String[] args)
    {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    objProcessor.producer();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    objProcessor.consumer();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        //t1 does indeed start prior to t2 - there is no ambiguity in that.
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Finished");
    }
}
